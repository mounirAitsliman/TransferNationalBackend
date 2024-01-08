package org.national.transfer.wallet.service.service;

import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.wallet.service.exception.NotFoundException;
import org.national.transfer.wallet.service.model.Wallet;
import org.national.transfer.wallet.service.utils.Constants;
import org.national.transfer.wallet.service.utils.LoggingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@CommonsLog
@SuppressWarnings({"rawtypes", "unchecked"})
public class WalletService {

    private final String COLLECTION_NAME = "wallet";
    @Autowired
    private MongoTemplate mongoTemplate;

    public Wallet updateWalletStatus(String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        Wallet wallet = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(identityNumber)), Wallet.class);

        returnException(wallet);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String UpdatedDate = myDateObj.format(myFormatObj);
        wallet.setStatus(Constants.LOCKED);
        wallet.setLastUpdate(UpdatedDate);
        log.info(LoggingUtils.getMessage(wallet));
        mongoTemplate.save(wallet, COLLECTION_NAME);
        return wallet;
    }

    public String updateWalletAmount(Double newAmount, String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber, newAmount));
        Wallet wallet = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(identityNumber)), Wallet.class);

        returnException(wallet);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String UpdatedDate = myDateObj.format(myFormatObj);
        wallet.setAmount(wallet.getAmount() - newAmount);
        wallet.setLastUpdate(UpdatedDate);
        log.info(LoggingUtils.getMessage(wallet));
        mongoTemplate.save(wallet, COLLECTION_NAME);
        return "Balance updated";
    }

    public Wallet getWallet(String identityNumber) {
        Wallet wallet = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(identityNumber)), Wallet.class);
        returnException(wallet);
        return wallet;
    }

    public Double getWalletAmount(String identityNumber) {
        Wallet wallet = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(identityNumber)), Wallet.class);
        returnException(wallet);
        return wallet.getAmount();
    }

    public Wallet addWallet(Wallet wallet) {
        log.info(LoggingUtils.getStartMessage(wallet));
        Wallet result = mongoTemplate.insert(wallet, COLLECTION_NAME);
        return result;
    }

    private void returnException(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            throw new NotFoundException(Constants.EX_WALLET_NOT_FOUND);
        }
    }

    public String addAmountToWallet(Double newAmount, String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber, newAmount));
        Wallet wallet = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(identityNumber)), Wallet.class);

        returnException(wallet);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String UpdatedDate = myDateObj.format(myFormatObj);
        wallet.setAmount(wallet.getAmount() + newAmount);
        wallet.setLastUpdate(UpdatedDate);
        log.info(LoggingUtils.getMessage(wallet));
        mongoTemplate.save(wallet, COLLECTION_NAME);
        return "Balance updated";
    }

}
