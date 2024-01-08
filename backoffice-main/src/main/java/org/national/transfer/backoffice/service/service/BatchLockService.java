package org.national.transfer.backoffice.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.backoffice.service.proxy.EmissionServiceProxy;
import org.national.transfer.backoffice.service.utils.Constants;
import org.national.transfer.backoffice.service.utils.LoggingUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@CommonsLog
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
@EnableAsync
public class BatchLockService {

    private final EmissionServiceProxy emissionServiceProxy;
    private final MongoTemplate mongoTemplate;
    private String COLLECTION_NAME = "siron";

    @Async
    @Scheduled(cron = "0 00 00 * * *", zone = "Europe/Paris")
    public void batchLock() {
        log.info(LoggingUtils.getStartMessage());
        LocalDate validityTime = LocalDate.now();
        DateTimeFormatter dfor = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<Map<String, Object>> transfers = emissionServiceProxy.getAllTransfers();
        List<Map<String, String>> siron = (List) mongoTemplate.findAll(List.class, COLLECTION_NAME);

        transfers.stream().filter(transfer -> {
            if (transfer.get("validityDate").equals(dfor.format(validityTime)) || siron.stream().anyMatch(sir -> sir.get("_id").equals(transfer.get("clientIdentityNumber")))) {
                emissionServiceProxy.updateTransferStatus(Constants.BLOCKED, (String) transfer.get("identityNumber"));
            }
            return true;
        });
    }
}
