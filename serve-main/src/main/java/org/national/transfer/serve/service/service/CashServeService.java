package org.national.transfer.serve.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.serve.service.dto.KYC;
import org.national.transfer.serve.service.dto.RecapDto;
import org.national.transfer.serve.service.proxy.ClientProspectServiceProxy;
import org.national.transfer.serve.service.proxy.EmailServiceProxy;
import org.national.transfer.serve.service.proxy.EmissionServiceProxy;
import org.national.transfer.serve.service.proxy.WalletServiceProxy;
import org.national.transfer.serve.service.utils.Constants;
import org.national.transfer.serve.service.utils.LoggingUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@CommonsLog
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class CashServeService {

    private final EmailServiceProxy emailServiceProxy;
    private final WalletServiceProxy walletServiceProxy;
    private final ClientProspectServiceProxy clientProspectServiceProxy;
    private final EmissionServiceProxy emissionServiceProxy;
    private final ObjectMapper mapper;
    private Map<String, Object> transfer = new HashMap<>();

    public RecapDto getTransfer(String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        transfer = emissionServiceProxy.getTransfer(reference);
        log.debug(LoggingUtils.getEndMessage(transfer));
        RecapDto recap = mapper.convertValue(transfer, RecapDto.class);
        return recap;
    }

    public KYC addProspect(KYC prospect) {
        log.info(LoggingUtils.getStartMessage(prospect));
        Map result = mapper.convertValue(prospect, Map.class);
        Map output = clientProspectServiceProxy.addProspectClient(result);
        log.debug(LoggingUtils.getEndMessage(output));
        return mapper.convertValue(output, KYC.class);
    }

    public Map validateTransaction(String canalId) {
        walletServiceProxy.updateWalletAmount((Double) transfer.get("amount"), String.valueOf(transfer.get("agentId")));
        String reference = String.valueOf(transfer.get("_id"));
        emissionServiceProxy.updateTransferStatus(Constants.SERVED, reference);
        transfer.put(Constants.CANAL_ID, canalId);
        sendEmailNotification();
        return transfer;
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
