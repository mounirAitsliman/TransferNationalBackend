package org.national.transfer.backoffice.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.backoffice.service.service.LockService;
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
@RequestMapping("/lock")
public class LockRestController {

    private final LockService lockService;

    @GetMapping("/getAllToServeTransfers")
    public ResponseEntity<List<Map>> getAllToServeTransfers() {
        log.info(LoggingUtils.getStartMessage());
        List<Map> transfers = lockService.getAllToServeTransfers();
        log.info(LoggingUtils.getMessage(transfers));
        return ResponseEntity.status(HttpStatus.OK).body(transfers);
    }

    @PostMapping("/{reference}")
    public ResponseEntity<?> lockTransfer(@PathVariable String reference) {
        log.info(LoggingUtils.getStartMessage(reference));
        lockService.lockTransfer(reference);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
