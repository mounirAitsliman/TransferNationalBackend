package org.national.transfer.prospect.client.service.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.prospect.client.service.model.ProspectClient;
import org.national.transfer.prospect.client.service.service.ProspectClientService;
import org.national.transfer.prospect.client.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CommonsLog
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/prospect")
public class ProspectClientRestController {

    private final ProspectClientService prospectClientService;

    @PostMapping
    public ResponseEntity<ProspectClient> addProspectClient(@RequestBody ProspectClient prospectClient) {
        log.info(LoggingUtils.getStartMessage(prospectClient));
        ProspectClient prospect = prospectClientService.addProspectClient(prospectClient);
        log.debug(LoggingUtils.getEndMessage(prospect));
        return ResponseEntity.status(HttpStatus.CREATED).body(prospect);
    }

    @GetMapping("/{identityNumber}")
    public ResponseEntity<ProspectClient> getProspect(@PathVariable String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        ProspectClient prospect = prospectClientService.getProspect(identityNumber);
        log.debug(LoggingUtils.getEndMessage(prospect));
        return ResponseEntity.status(HttpStatus.OK).body(prospect);
    }
}
