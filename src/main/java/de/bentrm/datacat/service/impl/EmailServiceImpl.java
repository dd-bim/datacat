package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.EmailConfirmationRequest;
import de.bentrm.datacat.properties.ApplicationProperties;
import de.bentrm.datacat.properties.EmailProperties;
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

            Thank you for registering at ${url}. 
            To be able to login to your account, you'll need to confirm your email address.

            Please open ${confirmUrl}${token} and enter your confirmation token: ${token}

            If you did not register at ${homeUrl}, someone else may have used your email address
            to open an account. In this case, please ignore this message.

            Kind regards
            """;

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public void sendEmailConfirmation(@NotNull EmailConfirmationRequest emailConfirmationRequest) {
        @NotNull final EmailProperties emailProperties = applicationProperties.getMail();
        StringSubstitutor substitutor = new StringSubstitutor(Map.ofEntries(
                Map.entry("name", emailConfirmationRequest.getUser().getName()),
                Map.entry("url", applicationProperties.getUrl()),
                Map.entry("confirmUrl", emailProperties.getConfirmLink()),
                Map.entry("token", emailConfirmationRequest.getToken())
        ));
        String body = substitutor.replace(confirmEmailTemplate);

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Please confirm your account settings");
        message.setFrom(emailProperties.getFrom());
        message.setTo(emailConfirmationRequest.getSentTo());
        message.setText(body);
        try {
            if (javaMailSender != null) javaMailSender.send(message);
            else log.info("Email confirmation: {}", message);
        } catch (MailException e) {
            log.warn(e.getLocalizedMessage());
        }
    }
}
