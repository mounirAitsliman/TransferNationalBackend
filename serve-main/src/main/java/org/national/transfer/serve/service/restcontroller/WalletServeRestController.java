package org.national.transfer.serve.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.serve.service.dto.RecapDto;
import org.national.transfer.serve.service.service.WalletServeService;
import org.national.transfer.serve.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CommonsLog
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/wallet")
@SuppressWarnings({"rawtypes", "unchecked"})
public class WalletServeRestController {

    private final WalletServeService walletServeService;

    @GetMapping("/getTransfer/{reference}")
    public ResponseEntity<RecapDto> getTransfer(@PathVariable String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        RecapDto recap = walletServeService.getTransfer(reference);
        log.debug(LoggingUtils.getEndMessage(recap));
        return ResponseEntity.status(HttpStatus.OK).body(recap);
    }

    @GetMapping("/getWallet/{identityNumber}")
    public ResponseEntity<Map<String, Object>> getWallet(@PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        Map<String, Object> result = walletServeService.getWallet(identityNumber);
        log.debug(LoggingUtils.getEndMessage(result));
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/addWallet")
    public ResponseEntity<Map<String, Object>> addWallet(@RequestBody Map<String, Object> newWallet) {
        log.info(LoggingUtils.getStartMessage(newWallet));
        Map<String, Object> result = walletServeService.addWallet(newWallet);
        log.debug(LoggingUtils.getEndMessage(result));
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/email/check/{identityNumber}")
    public ResponseEntity<?> checkOTP(@RequestParam String otp, @PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(otp, identityNumber));
        walletServeService.checkOTP(otp, identityNumber);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Map> validateTransaction(@RequestParam String canalId) {
        log.info(LoggingUtils.getMessage(canalId));
        Map recap = walletServeService.validateTransaction(canalId);
        log.debug(LoggingUtils.getEndMessage(recap));
        return ResponseEntity.status(HttpStatus.OK).body(recap);
    }
}
