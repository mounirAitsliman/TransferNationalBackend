package org.national.transfer.email.service.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.email.service.dto.BeneficiaryInfoMailDto;
import org.national.transfer.email.service.dto.ClientInfoMailDto;
import org.national.transfer.email.service.dto.MailDto;
import org.national.transfer.email.service.service.MailService;
import org.national.transfer.email.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
@RequestMapping("/mail")
@CrossOrigin("*")
@CommonsLog
@RequiredArgsConstructor
public class MailRestController {

    private final MailService mailService;

    @PostMapping("/otp")
    public ResponseEntity<?> sendOTP(@RequestBody MailDto mailDto, @RequestParam String otp) throws MessagingException {
        log.info(LoggingUtils.getStartMessage(mailDto, otp));
        mailService.sendOTP(mailDto, otp);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/clientInfo")
    public ResponseEntity<?> sendTransferClientInfo(@RequestBody ClientInfoMailDto clientInfoMailDto) throws MessagingException {
        log.info(LoggingUtils.getStartMessage(clientInfoMailDto));
        mailService.clientTransferInfo(clientInfoMailDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/beneficiaryInfo")
    public ResponseEntity<?> sendTransferBeneficiaryInfo(@RequestBody BeneficiaryInfoMailDto beneficiaryInfoMailDto) throws MessagingException {
        log.info(LoggingUtils.getStartMessage(beneficiaryInfoMailDto));
        mailService.beneficiaryTransferInfo(beneficiaryInfoMailDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/notification")
    public ResponseEntity<?> sendStatusNotification(@RequestBody Map<String, String> clientInfo) throws MessagingException {
        log.info(LoggingUtils.getStartMessage(clientInfo));
        mailService.transferStatusChange(clientInfo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
