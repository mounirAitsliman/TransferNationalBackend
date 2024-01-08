package org.national.transfer.serve.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.serve.service.dto.MailOTPCollection;
import org.national.transfer.serve.service.dto.RecapDto;
import org.national.transfer.serve.service.exception.NotFoundException;
import org.national.transfer.serve.service.proxy.ClientProspectServiceProxy;
import org.national.transfer.serve.service.proxy.EmailServiceProxy;
import org.national.transfer.serve.service.proxy.EmissionServiceProxy;
import org.national.transfer.serve.service.proxy.WalletServiceProxy;
import org.national.transfer.serve.service.utils.Constants;
import org.national.transfer.serve.service.utils.LoggingUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@CommonsLog
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class WalletServeService {

    private final MongoTemplate mongoTemplate;
    private final EmailServiceProxy emailServiceProxy;
    private final WalletServiceProxy walletServiceProxy;
    private final ClientProspectServiceProxy clientProspectServiceProxy;
    private final EmissionServiceProxy emissionServiceProxy;
    private final ObjectMapper mapper;
    private final String COLLECTION_NAME = "mailOTPCollection";
    private Map<String, Object> transfer = new HashMap<>();
    private Map<String, Object> wallet = new HashMap<>();

    public RecapDto getTransfer(String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        transfer = emissionServiceProxy.getTransfer(reference);
        log.debug(LoggingUtils.getEndMessage(transfer));
        RecapDto recap = mapper.convertValue(transfer, RecapDto.class);
        return recap;
    }

    public Map<String, Object> getWallet(String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        wallet = walletServiceProxy.getWallet(identityNumber);
        log.debug(LoggingUtils.getEndMessage(wallet));
        sendOTP((String) wallet.get("identityNumber"));
        return wallet;
    }

    public Map<String, Object> addWallet(Map<String, Object> newWallet) {
        log.info(LoggingUtils.getStartMessage(newWallet));
        wallet = walletServiceProxy.addWallet(newWallet);
        log.debug(LoggingUtils.getEndMessage(wallet));
        sendOTP((String) wallet.get("identityNumber"));
        return wallet;
    }

    public void checkOTP(String otp, String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber, otp));
        Map<String, Object> otpObj = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(identityNumber)), Map.class, COLLECTION_NAME);

        if (CollectionUtils.isEmpty(otpObj)) {
            throw new NotFoundException(Constants.EX_OTP_NOT_FOUND);
        }
        if (!otp.equals(otpObj.get("OTP"))) {
            throw new NotFoundException(Constants.EX_OTP_INCORRECT);
        }
        remove(identityNumber, COLLECTION_NAME);
    }

    public Map validateTransaction(String canalId) {
        walletServiceProxy.addAmountToWallet((Double) transfer.get("amount"), String.valueOf(wallet.get(Constants.IDENTITY_NUMBER)));
        String reference = String.valueOf(transfer.get("_id"));
        emissionServiceProxy.updateTransferStatus(Constants.SERVED, reference);
        transfer.put(Constants.CANAL_ID, canalId);
        sendEmailNotification();
        return transfer;
    }

    private String sendOTP(String identityNumber) {
        log.info(LoggingUtils.getMessage(identityNumber));
        String otp = generateOTP();
        MailOTPCollection mail = new MailOTPCollection(identityNumber, otp);
        mongoTemplate.insert(mail, COLLECTION_NAME);
        Map<String, String> mailOtp = new HashMap();
        mailOtp.put("email", (String) wallet.get("email"));
        mailOtp.put("clientFullName", wallet.get("firstName") + " " + wallet.get("lastName"));
        log.info(LoggingUtils.getMessage(mailOtp));
        emailServiceProxy.sendOTP(mailOtp, otp);
        return "OTP sent";
    }

    private String generateOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private void remove(String id, String collectionName) {
        removeByKeyValue("_id", id, collectionName);
    }

    private void removeByKeyValue(String key, Object value, String collectionName) {
        log.debug(LoggingUtils.getMessage(key, value));
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, collectionName);
    }

    private void sendEmailNotification() {
        if (transfer.get("withNotification").equals(true)) {
            Map<String, String> notificationDetails = new HashMap<>();
            notificationDetails.put("clientEmail", (String) transfer.get("clientEmail"));
            notificationDetails.put("clientFullName", (String) transfer.get("clientFullName"));
            notificationDetails.put("beneficiaryFullName", transfer.get("beneficiaryFirstName") + " " + transfer.get("beneficiaryLastName"));
            notificationDetails.put("status", Constants.SERVED);
            emailServiceProxy.sendStatusNotification(notificationDetails);
        }
    }
}
