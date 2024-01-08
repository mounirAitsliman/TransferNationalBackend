package org.national.transfer.serve.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "email-service", url = "sms.national-transfer.svc.cluster.local:8084")
public interface EmailServiceProxy {

    @PostMapping("/ws-mail/notification")
    Object sendStatusNotification(@RequestBody Map<String, String> clientInfo);

    @PostMapping("/ws-mail/mail/otp")
    void sendOTP(@RequestBody Map<String, String> mailDto, @RequestParam String otp);
}
