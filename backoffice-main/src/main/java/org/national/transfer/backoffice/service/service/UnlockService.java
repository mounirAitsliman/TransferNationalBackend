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
public class UnlockService {

    private final EmissionServiceProxy emissionServiceProxy;

    public List<Map> getAllBlockedTransfers() {
        log.info(LoggingUtils.getMessage());
        List<Map> transfers = emissionServiceProxy.getAllLockedTransfers();
        log.debug(LoggingUtils.getEndMessage(transfers));
        return transfers;
    }

    public void lockTransfer(String reference) {
        log.info(LoggingUtils.getStartMessage());
        emissionServiceProxy.updateTransferStatus(Constants.UNLOCK_TO_SERVE, reference);
    }
}
