package org.national.transfer.backoffice.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.backoffice.service.proxy.EmissionServiceProxy;
import org.national.transfer.backoffice.service.utils.Constants;
import org.national.transfer.backoffice.service.utils.LoggingUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@CommonsLog
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class LockService {

    private final EmissionServiceProxy emissionServiceProxy;

    public List<Map> getAllToServeTransfers() {
        log.info(LoggingUtils.getMessage());
        List<Map> transfers = emissionServiceProxy.getAllToServeTransfers();
        log.debug(LoggingUtils.getEndMessage(transfers));
        return transfers;
    }

    public void lockTransfer(String reference) {
        log.info(LoggingUtils.getStartMessage());
        emissionServiceProxy.updateTransferStatus(Constants.BLOCKED, reference);
    }
}
