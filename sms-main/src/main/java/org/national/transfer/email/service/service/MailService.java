package org.national.transfer.email.service.service;

import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.email.service.dto.BeneficiaryInfoMailDto;
import org.national.transfer.email.service.dto.ClientInfoMailDto;
import org.national.transfer.email.service.dto.MailDto;
import org.national.transfer.email.service.utils.LoggingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@CommonsLog
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOTP(MailDto mailDto, String otp) throws MessagingException {
        log.info(LoggingUtils.getStartMessage(mailDto));
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("transfertnational@gmail.com");
        helper.setTo(mailDto.getEmail());

        String subject = "Here's your OTP";

        String content = "<p>Hello Client " + mailDto.getClientFullName() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to confirm:</p>"
                + "<p><b>" + otp + "</b></p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void beneficiaryTransferInfo(BeneficiaryInfoMailDto beneficiaryInfoMailDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("transfertnational@gmail.com");
        helper.setTo(beneficiaryInfoMailDto.getBeneficiaryEmail());

        String subject = "Transfer Info";

        String content = "<p>Hello Beneficiary " + beneficiaryInfoMailDto.getBeneficiaryFirstName() + " " + beneficiaryInfoMailDto.getBeneficiaryLastName() + "</p>"
                + "<p>Here's your Transfer reference:</p> "
                + "<p><b>" + beneficiaryInfoMailDto.getReference() + "</b></p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void clientTransferInfo(ClientInfoMailDto clientInfoMailDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("transfertnational@gmail.com");
        helper.setTo(clientInfoMailDto.getClientEmail());

        String subject = "Transfer Info";

        String content = "<p>Hello Client " + clientInfoMailDto.getClientFullName() + "</p>"
                + "<p>Here's your Transfer reference:</p> "
                + "<p><b>" + clientInfoMailDto.getReference() + "</b></p>"
                + "and also the pinCode give it to the Transfer Beneficiary:</p>"
                + "<p><b>" + clientInfoMailDto.getPinCode() + "</b></p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void transferStatusChange(Map<String, String> clientInfo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("transfertnational@gmail.com");
        helper.setTo(clientInfo.get("clientEmail"));

        String subject = "Transaction Info";

        String content = "<p>Hello Client " + clientInfo.get("clientFullName") + "</p>"
                + "<p>Your transfer to " + clientInfo.get("beneficiaryFullName")
                + "is </p>" + clientInfo.get("status");

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
