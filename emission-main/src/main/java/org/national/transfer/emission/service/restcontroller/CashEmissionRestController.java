package org.national.transfer.emission.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.emission.service.collection.Transfers;
import org.national.transfer.emission.service.dto.KYC;
import org.national.transfer.emission.service.service.CashEmissionService;
import org.national.transfer.emission.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CommonsLog
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/cash")
public class CashEmissionRestController {

    private final CashEmissionService cashEmissionService;

    @GetMapping("/{identityNumber}")
    public ResponseEntity<KYC> getKYC(@PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        KYC kyc = cashEmissionService.getKYC(identityNumber);
        log.debug(LoggingUtils.getEndMessage(kyc));
        return ResponseEntity.status(HttpStatus.OK).body(kyc);
    }

    @PostMapping("/addProspect")
    public ResponseEntity<KYC> addProspect(@RequestBody KYC prospect) {
        log.info(LoggingUtils.getStartMessage(prospect));
        KYC kyc = cashEmissionService.addProspect(prospect);
        log.debug(LoggingUtils.getEndMessage(kyc));
        return ResponseEntity.status(HttpStatus.CREATED).body(kyc);
    }

    @PostMapping("/{codeAgent}")
    public ResponseEntity<?> checkAmount(@PathVariable String codeAgent, @RequestParam Double amount) {
        log.info(LoggingUtils.getStartMessage(codeAgent, amount));
        cashEmissionService.checkAmount(codeAgent, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/feeManagement/{feeMode}")
    public ResponseEntity<?> feeManagement(@PathVariable String feeMode, @RequestParam Double amount,
                                           @RequestParam Double totalTransferAmount,
                                           @RequestParam Double feeTransferAmount,
                                           @RequestParam Boolean withNotification) {
        log.info(LoggingUtils.getStartMessage(feeMode, amount, totalTransferAmount, feeTransferAmount, withNotification));
        cashEmissionService.amountTransferAfterFee(feeMode, amount, totalTransferAmount, feeTransferAmount, withNotification);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getBeneficiaries/{identityNumber}")
    public ResponseEntity<Set<Map<String, Object>>> getAllBeneficiariesByIdentityNumber(@PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        Set result = cashEmissionService.getAllBeneficiariesByIdentityNumber(identityNumber);
        log.debug(LoggingUtils.getEndMessage(result));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/addBeneficiary")
    public ResponseEntity<?> addBeneficiary(@RequestBody Map<String, String> beneficiaryCoordinates) {
        log.info(LoggingUtils.getStartMessage(beneficiaryCoordinates));
        cashEmissionService.addBeneficiary(beneficiaryCoordinates);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Transfers> validateTransaction(@RequestParam String canalId) {
        log.info(LoggingUtils.getMessage(canalId));
        Transfers transfer = cashEmissionService.validateTransaction(canalId);
        log.debug(LoggingUtils.getEndMessage(transfer));
        return ResponseEntity.status(HttpStatus.CREATED).body(transfer);
    }

    @GetMapping("/getTransfer/{reference}")
    public ResponseEntity<Map<String, Object>> getTransfer(@PathVariable String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        Map transfer = cashEmissionService.getTransfer(reference);
        log.debug(LoggingUtils.getEndMessage(transfer));
        return ResponseEntity.status(HttpStatus.OK).body(transfer);
    }

    @PutMapping("/updateTransferStatus/{status}/{reference}")
    public ResponseEntity<Object> updateTransferStatus(@PathVariable String status, @PathVariable String reference) {
        log.info(LoggingUtils.getStartMessage(status, reference));
        cashEmissionService.updateTransferStatus(status, reference);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getAllToServeTransfers")
    public ResponseEntity<List<Transfers>> getAllToServeTransfers() {
        List<Transfers> transfers = cashEmissionService.getAllToServeTransfers();
        log.info(LoggingUtils.getMessage(transfers));
        return ResponseEntity.status(HttpStatus.OK).body(transfers);
    }

    @GetMapping("/getAllLockedTransfers")
    public ResponseEntity<List<Transfers>> getAllLockedTransfers() {
        List<Transfers> transfers = cashEmissionService.getAllLockedTransfers();
        log.info(LoggingUtils.getMessage(transfers));
        return ResponseEntity.status(HttpStatus.OK).body(transfers);
    }

    @GetMapping("/getAllTransfers")
    public ResponseEntity<List<Transfers>> getAllTransfers() {
        log.info(LoggingUtils.getStartMessage());
        List<Transfers> transfersList = cashEmissionService.getAllTransfers();
        log.debug(LoggingUtils.getEndMessage(transfersList));
        return ResponseEntity.status(HttpStatus.OK).body(transfersList);
    }
}
