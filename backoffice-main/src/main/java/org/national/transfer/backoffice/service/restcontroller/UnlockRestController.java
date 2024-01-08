package org.national.transfer.backoffice.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.backoffice.service.service.UnlockService;
import org.national.transfer.backoffice.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CommonsLog
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/unlock")
public class UnlockRestController {

    private final UnlockService unlockService;

    @GetMapping("/getAllBlockedTransfers")
    public ResponseEntity<List<Map>> getAllBlockedTransfers() {
        log.info(LoggingUtils.getStartMessage());
        List<Map> transfers = unlockService.getAllBlockedTransfers();
        log.info(LoggingUtils.getMessage(transfers));
        return ResponseEntity.status(HttpStatus.OK).body(transfers);
    }

    @PostMapping("/{reference}")
    public ResponseEntity<?> lockTransfer(@PathVariable String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        unlockService.lockTransfer(reference);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
