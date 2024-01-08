package org.national.transfer.serve.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.serve.service.dto.KYC;
import org.national.transfer.serve.service.dto.RecapDto;
import org.national.transfer.serve.service.service.CashServeService;
import org.national.transfer.serve.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CommonsLog
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/cash")
@SuppressWarnings({"rawtypes", "unchecked"})
public class CashServeRestController {

    private final CashServeService cashServeService;

    @GetMapping("/getTransfer/{reference}")
    public ResponseEntity<RecapDto> getTransfer(@PathVariable String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        RecapDto recap = cashServeService.getTransfer(reference);
        log.debug(LoggingUtils.getEndMessage(recap));
        return ResponseEntity.status(HttpStatus.OK).body(recap);
    }

    @PostMapping("/addProspect")
    public ResponseEntity<KYC> addProspect(@RequestBody KYC prospect) {
        log.info(LoggingUtils.getStartMessage(prospect));
        KYC kyc = cashServeService.addProspect(prospect);
        log.debug(LoggingUtils.getEndMessage(kyc));
        return ResponseEntity.status(HttpStatus.CREATED).body(kyc);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map> validateTransaction(@RequestParam String canalId) {
        log.info(LoggingUtils.getMessage(canalId));
        Map recap = cashServeService.validateTransaction(canalId);
        log.debug(LoggingUtils.getEndMessage(recap));
        return ResponseEntity.status(HttpStatus.OK).body(recap);
    }
}
