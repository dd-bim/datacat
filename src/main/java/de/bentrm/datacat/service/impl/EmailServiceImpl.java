package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.EmailConfirmationRequest;
import de.bentrm.datacat.service.EmailProperties;
import de.bentrm.datacat.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    final static String confirmEmailTemplate = """
            Dear ${name},

            Thank you for registering at ${homeUrl}. 
            To be able to login to your account, you'll need to confirm your email address.

            Please open ${confirmUrl}${token} and enter your confirmation token: ${token}

            If you did not register at ${homeUrl}, someone else may have used your email address
            to open an account. In this case, please ignore this message.

            Kind regards
            """;

    private final JavaMailSender sender;

    private final EmailProperties emailProperties;

    @Autowired
    public EmailServiceImpl(JavaMailSender sender, EmailProperties emailProperties) {
        this.sender = sender;
        this.emailProperties = emailProperties;
    }

    @Override
    public void sendEmailConfirmation(@NotNull EmailConfirmationRequest emailConfirmationRequest) {
        StringSubstitutor substitutor = new StringSubstitutor(Map.ofEntries(
                Map.entry("name", emailConfirmationRequest.getUser().getName()),
                Map.entry("homeUrl", emailProperties.getHomeUrl()),
                Map.entry("confirmUrl", emailProperties.getConfirmUrl()),
                Map.entry("token", emailConfirmationRequest.getToken())
        ));
        String body = substitutor.replace(confirmEmailTemplate);

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Please confirm your account settings");
        message.setFrom(emailProperties.getConfirmFrom());
        message.setTo(emailConfirmationRequest.getSentTo());
        message.setText(body);
        try {
            sender.send(message);
        } catch (MailException e) {
            log.warn(e.getLocalizedMessage());
        }
    }
}
