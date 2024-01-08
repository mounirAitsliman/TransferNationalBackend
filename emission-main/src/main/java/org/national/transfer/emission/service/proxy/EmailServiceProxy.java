package org.national.transfer.emission.service.proxy;

import org.national.transfer.emission.service.dto.BeneficiaryInfoMailDto;
import org.national.transfer.emission.service.dto.ClientInfoMailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "email-service", url = "sms.national-transfer.svc.cluster.local:8084")
public interface EmailServiceProxy {

    @PostMapping("/ws-mail/mail/otp")
    void sendOTP(@RequestBody Map<String, String> mailDto, @RequestParam String otp);

    @PostMapping("/ws-mail/mail/clientInfo")
    void sendTransferClientInfo(@RequestBody ClientInfoMailDto clientInfoMailDto);

    @PostMapping("/ws-mail/mail/beneficiaryInfo")
    void sendTransferBeneficiaryInfo(@RequestBody BeneficiaryInfoMailDto beneficiaryInfoMailDto);
}
