package org.national.transfer.prospect.client.service.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.prospect.client.service.model.Beneficiaries;
import org.national.transfer.prospect.client.service.service.ProspectClientService;
import org.national.transfer.prospect.client.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CommonsLog
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/beneficiary")
public class BeneficiariesRestController {

    private final ProspectClientService clientService;

    @GetMapping("/{identityNumber}")
    public ResponseEntity<Set<Beneficiaries>> getAllBeneficiariesByProspectClientId(@PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllBeneficiariesByProspectClientId(identityNumber));
    }

    @PostMapping("/{identityNumber}")
    public ResponseEntity<Beneficiaries> addBeneficiary(@PathVariable String identityNumber, @RequestBody Beneficiaries beneficiary) {
        log.info(LoggingUtils.getStartMessage(identityNumber, beneficiary));
        Beneficiaries beneficiaries = clientService.addBeneficiary(identityNumber, beneficiary);
        log.debug(LoggingUtils.getEndMessage(beneficiaries));
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaries);
    }

    @DeleteMapping("/{beneficiaryEmail}")
    public ResponseEntity<?> deleteBeneficiary(@PathVariable String beneficiaryEmail) {
        log.info(LoggingUtils.getStartMessage(beneficiaryEmail));
        clientService.deleteBeneficiary(beneficiaryEmail);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
