package org.national.transfer.emission.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.emission.service.collection.Transfers;
import org.national.transfer.emission.service.dto.BeneficiaryInfoMailDto;
import org.national.transfer.emission.service.dto.ClientInfoMailDto;
import org.national.transfer.emission.service.dto.KYC;
import org.national.transfer.emission.service.exception.AmountHighException;
import org.national.transfer.emission.service.exception.NotFoundException;
import org.national.transfer.emission.service.exception.TransferException;
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
import java.util.stream.Collectors;

@Service
@CommonsLog
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class CashEmissionService {

    private final ClientProspectServiceProxy clientProspectServiceProxy;
    private final EmailServiceProxy emailServiceProxy;
    private final WalletServiceProxy walletServiceProxy;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper mapper;
    private String COLLECTION_TRANSACTION = "transfers";
    @Value("${max.amount.per.transfer.cash}")
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
        KYC kyc = new KYC();
        if (!CollectionUtils.isEmpty(result)) {
            kyc = mapper.convertValue(result, KYC.class);
            transfer.put(Constants.IDENTITY_NUMBER, kyc.getIdentityNumber());
            transfer.put(Constants.CLIENT_FULL_NAME, kyc.getLastname() + " " + kyc.getFirstname());
            transfer.put(Constants.CLIENT_EMAIL, kyc.getEmail());
        }
        log.info(LoggingUtils.getMessage(kyc));
        return kyc;
    }

    public KYC addProspect(KYC prospect) {
        log.info(LoggingUtils.getStartMessage(prospect));
        transfer.put(Constants.IDENTITY_NUMBER, prospect.getIdentityNumber());
        transfer.put(Constants.CLIENT_FULL_NAME, prospect.getLastname() + " " + prospect.getFirstname());
        transfer.put(Constants.CLIENT_EMAIL, prospect.getEmail());
        Map result = mapper.convertValue(prospect, Map.class);
        Map output = clientProspectServiceProxy.addProspectClient(result);
        log.debug(LoggingUtils.getEndMessage(output));
        return mapper.convertValue(output, KYC.class);
    }

    public void checkAmount(String codeAgent, Double amount) {
        log.info(LoggingUtils.getStartMessage(codeAgent, amount));
        Double walletAmount = walletServiceProxy.getWalletAmount(codeAgent);
        transfer.put(Constants.CODE_AGENT, codeAgent);
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

    public Transfers validateTransaction(String canalId) {
        log.info(LoggingUtils.getStartMessage(canalId));
        LocalDate creationTime = LocalDate.now();
        LocalDate validityTime = LocalDate.now().plusDays(7);
        DateTimeFormatter dfor = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String ref = "837" + createTransactionReference(10L);
        String pinCode = generatePinCode();
        Transfers transfers = new Transfers();

        transfers.setReference(ref);
        transfers.setPinCode(pinCode);
        transfers.setAgentId((String) transfer.get(Constants.CODE_AGENT));
        transfers.setBeneficiaryEmail((String) transfer.get(Constants.BENEFICIARY_EMAIL));
        transfers.setBeneficiaryFirstName((String) transfer.get(Constants.BENEFICIARY_FIRSTNAME));
        transfers.setBeneficiaryLastName((String) transfer.get(Constants.BENEFICIARY_LASTNAME));
        transfers.setCanalId(canalId);
        transfers.setClientFullName((String) transfer.get(Constants.CLIENT_FULL_NAME));
        transfers.setClientIdentityNumber((String) transfer.get(Constants.IDENTITY_NUMBER));
        transfers.setClientEmail(String.valueOf(transfer.get(Constants.CLIENT_EMAIL)));
        transfers.setAmount((Double) transfer.get(Constants.TRANSFER_AMOUNT));
        transfers.setTotalAmount((Double) transfer.get(Constants.TOTAL_OPERATION_AMOUNT));
        transfers.setFeeAmount((Double) transfer.get(Constants.FEE_AMOUNT));
        transfers.setWithNotification((Boolean) transfer.get(Constants.WITH_NOTIFICATION));
        transfers.setStatus(Constants.TO_SERVE);
        transfers.setEmissionType(Constants.CASH_EMISSION_TYPE);
        transfers.setCreationDate(creationTime.format(dfor));
        transfers.setValidityDate(validityTime.format(dfor));
        mongoTemplate.save(transfers, COLLECTION_TRANSACTION);

        //walletServiceProxy.updateWalletAmount((Double) transfer.get(Constants.TOTAL_OPERATION_AMOUNT), (String) transfer.get(Constants.CODE_AGENT));

        sendEmails((Boolean) transfer.get(Constants.WITH_NOTIFICATION), ref, pinCode);
        log.debug(LoggingUtils.getEndMessage(transfers));
        return transfers;
    }

    public Map<String, Object> getTransfer(String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        Map<String, Object> trans = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(reference)), Map.class, COLLECTION_TRANSACTION);
        if (CollectionUtils.isEmpty(trans)) {
            throw new NotFoundException(Constants.EX_TRANSFER_NOT_FOUND);
        }
        if (trans.get("status").equals(Constants.SERVED) || trans.get("status").equals(Constants.BLOCKED)) {
            throw new TransferException("TRANSFER_BLOCKED_OR_SERVED");
        }
        log.debug(LoggingUtils.getEndMessage(trans));
        return trans;
    }

    public void updateTransferStatus(String status, String reference) {
        Map<String, Object> trans = new HashMap<>();
        trans = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(reference)), Map.class, COLLECTION_TRANSACTION);
        trans.put("status", status);
        mongoTemplate.save(trans, COLLECTION_TRANSACTION);
    }

    public List<Transfers> getAllToServeTransfers() {
        List<Map> trans = findIBykeyAndValue("status", Constants.TO_SERVE, COLLECTION_TRANSACTION);
        log.info(LoggingUtils.getMessage(trans));
        List<Transfers> transfersList = mapper.convertValue(trans, List.class);
        return transfersList;
    }

    public List<Transfers> getAllLockedTransfers() {
        List<Map> trans = findIBykeyAndValue("status", Constants.BLOCKED, COLLECTION_TRANSACTION);
        log.info(LoggingUtils.getMessage(trans));
        List<Transfers> transfersList = mapper.convertValue(trans, List.class);
        return transfersList;
    }

    public List<Transfers> getAllTransfers() {
        log.info(LoggingUtils.getMessage());
        List<Map> tran = mongoTemplate.findAll(Map.class, COLLECTION_TRANSACTION);
        log.info(LoggingUtils.getStartMessage(tran));
        List<Transfers> transfersList = mapper.convertValue(tran, List.class);
        return transfersList;
    }

    private String generatePinCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        log.debug(LoggingUtils.getEndMessage(number));
        return String.format("%04d", number);
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

    private List<Map> findIBykeyAndValue(String key, Object value, String collectionName) {
        log.debug(LoggingUtils.getMessage(key, value));

        Query query = new Query();
        query = Query.query(Criteria.where(key).is(String.valueOf(value)));

        log.debug(query.toString());
        List<Map> outputs = mongoTemplate.find(query, Map.class, collectionName);
        log.debug(outputs);
        return outputs;
    }
}
