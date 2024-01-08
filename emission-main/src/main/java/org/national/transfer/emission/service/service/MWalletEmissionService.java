package org.national.transfer.emission.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.emission.service.collection.MailOTPCollection;
import org.national.transfer.emission.service.collection.TransferHistoric;
import org.national.transfer.emission.service.collection.Transfers;
import org.national.transfer.emission.service.dto.BeneficiaryInfoMailDto;
import org.national.transfer.emission.service.dto.ClientInfoMailDto;
import org.national.transfer.emission.service.dto.KYC;
import org.national.transfer.emission.service.exception.AmountHighException;
import org.national.transfer.emission.service.exception.NotFoundException;
import org.national.transfer.emission.service.proxy.ClientProspectServiceProxy;
import org.national.transfer.emission.service.proxy.EmailServiceProxy;
import org.national.transfer.emission.service.proxy.WalletServiceProxy;
import org.national.transfer.emission.service.utils.Constants;
import org.national.transfer.emission.service.utils.LoggingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@CommonsLog
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class MWalletEmissionService {

    private final WalletServiceProxy walletServiceProxy;
    private final ClientProspectServiceProxy clientProspectServiceProxy;
    private final EmailServiceProxy emailServiceProxy;
    private final ObjectMapper mapper;
    private final MongoTemplate mongoTemplate;
    private String COLLECTION_NAME = "mailOTPCollection";
    private String COLLECTION_TRANSACTION = "transfers";
    private String COLLECTION_HISTORIC = "transferHistoric";
    @Value("${max.amount.per.transfer.wallet}")
    private String maxAmount;
    private Map<String, Object> transfer = new HashMap();

    private final static String createTransactionReference(long len) {
        if (len > 18)
            throw new IllegalStateException("To many digits");
        long tLen = (long) Math.pow(10, len - 1) * 9;

        long number = (long) (Math.random() * tLen) + (long) Math.pow(10, len - 1) * 1;

        String tVal = number + "";
        if (tVal.length() != len) {
            throw new IllegalStateException("The random number '" + tVal + "' is not '" + len + "' digits");
        }
        log.info(LoggingUtils.getMessage(tVal));
        return tVal;
    }

    public KYC getKYC(String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        Map result = clientProspectServiceProxy.getProspect(identityNumber);
        KYC kyc = mapper.convertValue(result, KYC.class);
        transfer.put(Constants.IDENTITY_NUMBER, identityNumber);
        transfer.put(Constants.CLIENT_FULL_NAME, kyc.getLastname() + " " + kyc.getFirstname());
        transfer.put(Constants.CLIENT_EMAIL, kyc.getEmail());
        log.info(LoggingUtils.getMessage(kyc));
        return kyc;
    }

    public Set getAllBeneficiariesByIdentityNumber(String identityNumber) {
        Set beneficiaries = clientProspectServiceProxy.getAllBeneficiariesByProspectClientId(identityNumber);
        log.info(LoggingUtils.getMessage(beneficiaries));
        if (!CollectionUtils.isEmpty(beneficiaries)) {
            return beneficiaries;
        }
        return null;
    }

    public void addBeneficiary(Map<String, String> beneficiaryCoordinates) {
        log.info(LoggingUtils.getStartMessage(beneficiaryCoordinates));
        transfer.put(Constants.BENEFICIARY_EMAIL, beneficiaryCoordinates.get(Constants.BENEFICIARY_EMAIL));
        transfer.put(Constants.BENEFICIARY_FIRSTNAME, beneficiaryCoordinates.get(Constants.BENEFICIARY_FIRSTNAME));
        transfer.put(Constants.BENEFICIARY_LASTNAME, beneficiaryCoordinates.get(Constants.BENEFICIARY_LASTNAME));
        clientProspectServiceProxy.addBeneficiary((String) transfer.get(Constants.IDENTITY_NUMBER), beneficiaryCoordinates);
    }

    public void checkAmount(String identityNumber, Double amount) {
        log.info(LoggingUtils.getStartMessage(identityNumber, amount));
        Double walletAmount = walletServiceProxy.getWalletAmount(identityNumber);
        transfer.put(Constants.IDENTITY_NUMBER, identityNumber);
        if (amount > Double.parseDouble(maxAmount) || amount > walletAmount) {
            throw new AmountHighException(Constants.EX_AMOUNT_IS_HIGH);
        }
    }

    public void amountTransferAfterFee(String feeMode, Double amount, Double totalTransferAmount,
                                       Double feeTransferAmount, Boolean withNotification) {
        log.info(LoggingUtils.getStartMessage(feeMode, amount, totalTransferAmount, feeTransferAmount, withNotification));
        transfer.put(Constants.TOTAL_OPERATION_AMOUNT, totalTransferAmount);
        transfer.put(Constants.FEE_AMOUNT, feeTransferAmount);
        transfer.put(Constants.WITH_NOTIFICATION, withNotification);

        if (feeMode.equals(Constants.ON_PUBLISHER)) {
            transfer.put(Constants.TRANSFER_AMOUNT, amount);
        }
        if (feeMode.equals(Constants.ON_SUBSCRIBER)) {
            transfer.put(Constants.TRANSFER_AMOUNT, amount - feeTransferAmount);
        }
        sendOTP((String) transfer.get(Constants.IDENTITY_NUMBER));
    }

    private String sendOTP(String identityNumber) {
        log.info(LoggingUtils.getMessage(identityNumber));
        String otp = generateOTP();
        MailOTPCollection mail = new MailOTPCollection(identityNumber, otp);
        mongoTemplate.insert(mail, COLLECTION_NAME);
        Map<String, String> mailOtp = new HashMap();
        mailOtp.put("email", (String) transfer.get(Constants.CLIENT_EMAIL));
        mailOtp.put("clientFullName", (String) transfer.get(Constants.CLIENT_FULL_NAME));
        log.info(LoggingUtils.getMessage(mailOtp));
        emailServiceProxy.sendOTP(mailOtp, otp);
        return "OTP sent";
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

    public Transfers validateTransaction(String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        LocalDate creationTime = LocalDate.now();
        LocalDate validityTime = LocalDate.now().plusDays(7);
        DateTimeFormatter dfor = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String ref = "837" + createTransactionReference(10L);
        String pinCode = generatePinCode();
        List<Transfers> t = findIBykeyAndValue(identityNumber, COLLECTION_TRANSACTION);
        Transfers transfers = new Transfers();

        if (!CollectionUtils.isEmpty(t)) {
            transfers.setFirstTransaction(false);
            transfers.setTotalTransactionAmountForOneYear(t.get(t.size() - 1).getTotalTransactionAmountForOneYear() + (Double) transfer.get(Constants.TOTAL_OPERATION_AMOUNT));
        }

        if (CollectionUtils.isEmpty(t)) {
            transfers.setFirstTransaction(true);
            transfers.setTotalTransactionAmountForOneYear((Double) transfer.get(Constants.TOTAL_OPERATION_AMOUNT));
            transfers.setDateForOneYear(creationTime.format(dfor));
        }
        transfers.setReference(ref);
        transfers.setPinCode(pinCode);
        transfers.setBeneficiaryEmail((String) transfer.get(Constants.BENEFICIARY_EMAIL));
        transfers.setBeneficiaryFirstName((String) transfer.get(Constants.BENEFICIARY_FIRSTNAME));
        transfers.setBeneficiaryLastName((String) transfer.get(Constants.BENEFICIARY_LASTNAME));
        transfers.setClientFullName((String) transfer.get(Constants.CLIENT_FULL_NAME));
        transfers.setClientIdentityNumber(identityNumber);
        transfers.setClientEmail(String.valueOf(transfer.get(Constants.CLIENT_EMAIL)));
        transfers.setAmount((Double) transfer.get(Constants.TRANSFER_AMOUNT));
        transfers.setTotalAmount((Double) transfer.get(Constants.TOTAL_OPERATION_AMOUNT));
        transfers.setFeeAmount((Double) transfer.get(Constants.FEE_AMOUNT));
        transfers.setWithNotification((Boolean) transfer.get(Constants.WITH_NOTIFICATION));
        transfers.setStatus(Constants.TO_SERVE);
        transfers.setEmissionType(Constants.M_WALLET_EMISSION_TYPE);
        transfers.setCreationDate(creationTime.format(dfor));
        transfers.setValidityDate(validityTime.format(dfor));
        mongoTemplate.save(transfers, COLLECTION_TRANSACTION);

        TransferHistoric historic = mapper.convertValue(transfers, TransferHistoric.class);

        mongoTemplate.save(historic, COLLECTION_HISTORIC);

        walletServiceProxy.updateWalletAmount((Double) transfer.get(Constants.TOTAL_OPERATION_AMOUNT), identityNumber);
        sendEmails((Boolean) transfer.get(Constants.WITH_NOTIFICATION), ref, pinCode);
        log.debug(LoggingUtils.getEndMessage(transfers));
        return transfers;
    }

    public List<TransferHistoric> getHistoricByIdentityNumber(String identityNumber) {
        Query query = Query.query(Criteria.where("clientIdentityNumber").is(String.valueOf(identityNumber)));
        log.debug(query.toString());
        List<TransferHistoric> outputs = mongoTemplate.find(query, TransferHistoric.class, COLLECTION_HISTORIC);
        log.debug(outputs);
        return outputs;
    }

    private void remove(String id, String collectionName) {
        removeByKeyValue("_id", id, collectionName);
    }

    private void removeByKeyValue(String key, Object value, String collectionName) {
        log.debug(LoggingUtils.getMessage(key, value));
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, collectionName);
    }

    private List<Transfers> findIBykeyAndValue(Object value, String collectionName) {
        log.info(LoggingUtils.getMessage(value));
        Query query = Query.query(Criteria.where("clientIdentityNumber").is(String.valueOf(value)));
        log.debug(query.toString());
        List<Transfers> outputs = mongoTemplate.find(query, Transfers.class, collectionName);
        log.debug(outputs);
        return outputs;
    }

    private String generatePinCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        log.debug(LoggingUtils.getEndMessage(number));
        return String.format("%04d", number);
    }

    private String generateOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private void sendEmails(Boolean withNotification, String reference, String pinCode) {
        if (withNotification.equals(true)) {
            ClientInfoMailDto clientInfo = new ClientInfoMailDto();
            clientInfo.setClientEmail((String) transfer.get(Constants.CLIENT_EMAIL));
            clientInfo.setClientFullName((String) transfer.get(Constants.CLIENT_FULL_NAME));
            clientInfo.setReference(reference);
            clientInfo.setPinCode(pinCode);
            BeneficiaryInfoMailDto beneficiaryInfo = new BeneficiaryInfoMailDto();
            beneficiaryInfo.setReference(reference);
            beneficiaryInfo.setBeneficiaryEmail((String) transfer.get(Constants.BENEFICIARY_EMAIL));
            beneficiaryInfo.setBeneficiaryFirstName((String) transfer.get(Constants.BENEFICIARY_FIRSTNAME));
            beneficiaryInfo.setBeneficiaryLastName((String) transfer.get(Constants.BENEFICIARY_LASTNAME));
            emailServiceProxy.sendTransferClientInfo(clientInfo);
            emailServiceProxy.sendTransferBeneficiaryInfo(beneficiaryInfo);
        }
    }
}
