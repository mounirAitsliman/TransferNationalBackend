package org.national.transfer.serve.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.serve.service.dto.RecapDto;
import org.national.transfer.serve.service.service.ReverseService;
import org.national.transfer.serve.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CommonsLog
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/reverse")
@SuppressWarnings({"rawtypes", "unchecked"})
public class ReverseRestController {

    private final ReverseService reverseService;

    @GetMapping("/getTransfer/{reference}")
    public ResponseEntity<RecapDto> getTransfer(@PathVariable String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        RecapDto recap = reverseService.getTransfer(reference);
        log.debug(LoggingUtils.getEndMessage(recap));
        return ResponseEntity.status(HttpStatus.OK).body(recap);
    }

    @PostMapping("/motif")
    public ResponseEntity<?> addMotif(@RequestParam String motif) {
        log.info(LoggingUtils.getStartMessage(motif));
        reverseService.addMotif(motif);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Map> validateTransaction(@RequestParam String canalId) {
        log.info(LoggingUtils.getMessage(canalId));
        Map recap = reverseService.validateReverse(canalId);
        log.debug(LoggingUtils.getEndMessage(recap));
        return ResponseEntity.status(HttpStatus.OK).body(recap);
    }
}
