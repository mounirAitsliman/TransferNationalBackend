package org.national.transfer.emission.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.emission.service.collection.Transfers;
import org.national.transfer.emission.service.dto.KYC;
import org.national.transfer.emission.service.service.WalletEmissionService;
import org.national.transfer.emission.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@CommonsLog
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/wallet")
public class WalletEmissionRestController {

    private WalletEmissionService walletEmissionService;

    @GetMapping("/{identityNumber}")
    public ResponseEntity<KYC> getKYC(@PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        KYC kyc = walletEmissionService.getKYC(identityNumber);
        log.debug(LoggingUtils.getEndMessage(kyc));
        return ResponseEntity.status(HttpStatus.OK).body(kyc);
    }

    @PostMapping("/{identityNumber}")
    public ResponseEntity<?> checkAmount(@PathVariable String identityNumber, @RequestParam Double amount) {
        log.info(LoggingUtils.getStartMessage(identityNumber, amount));
        walletEmissionService.checkAmount(identityNumber, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/feeManagement/{feeMode}")
    public ResponseEntity<?> feeManagement(@PathVariable String feeMode, @RequestParam Double amount,
                                           @RequestParam Double totalTransferAmount,
                                           @RequestParam Double feeTransferAmount,
                                           @RequestParam Boolean withNotification) {
        log.info(LoggingUtils.getStartMessage(feeMode, amount, totalTransferAmount, feeTransferAmount, withNotification));
        walletEmissionService.amountTransferAfterFee(feeMode, amount, totalTransferAmount, feeTransferAmount, withNotification);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getBeneficiaries/{identityNumber}")
    public ResponseEntity<Set<Map<String, Object>>> getAllBeneficiariesByIdentityNumber(@PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        Set result = walletEmissionService.getAllBeneficiariesByIdentityNumber(identityNumber);
        log.debug(LoggingUtils.getEndMessage(result));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/addBeneficiary")
    public ResponseEntity<?> addBeneficiary(@RequestBody Map<String, String> beneficiaryCoordinates) {
        log.info(LoggingUtils.getStartMessage(beneficiaryCoordinates));
        walletEmissionService.addBeneficiary(beneficiaryCoordinates);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/email/check/{identityNumber}")
    public ResponseEntity<?> checkOTP(@RequestParam String otp, @PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(otp, identityNumber));
        walletEmissionService.checkOTP(otp, identityNumber);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/validate/{identityNumber}")
    public ResponseEntity<Transfers> validateTransaction(@PathVariable String identityNumber,
                                                         @RequestParam String agentId, @RequestParam String canalId) {
        log.info(LoggingUtils.getMessage(identityNumber, agentId, canalId));
        Transfers transfer = walletEmissionService.validateTransaction(identityNumber, agentId, canalId);
        log.debug(LoggingUtils.getEndMessage(transfer));
        return ResponseEntity.status(HttpStatus.CREATED).body(transfer);
    }
}
