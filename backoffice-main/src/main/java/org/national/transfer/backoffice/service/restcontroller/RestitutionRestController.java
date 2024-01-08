package org.national.transfer.backoffice.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.backoffice.service.dto.RecapDto;
import org.national.transfer.backoffice.service.service.RestitutionService;
import org.national.transfer.backoffice.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CommonsLog
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/restitution")
public class RestitutionRestController {

    private final RestitutionService restitutionService;

    @GetMapping("/getTransfer/{reference}")
    public ResponseEntity<RecapDto> getTransfer(@PathVariable String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        RecapDto recap = restitutionService.getTransfer(reference);
        log.debug(LoggingUtils.getEndMessage(recap));
        return ResponseEntity.status(HttpStatus.OK).body(recap);
    }

    @PostMapping("/motif")
    public ResponseEntity<?> addMotif(@RequestParam String motif) {
        log.info(LoggingUtils.getStartMessage(motif));
        restitutionService.addMotif(motif);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Map> validateRestitution(@RequestParam String canalId) {
        log.info(LoggingUtils.getMessage(canalId));
        Map recap = restitutionService.validateRestitution(canalId);
        log.debug(LoggingUtils.getEndMessage(recap));
        return ResponseEntity.status(HttpStatus.OK).body(recap);
    }
}
