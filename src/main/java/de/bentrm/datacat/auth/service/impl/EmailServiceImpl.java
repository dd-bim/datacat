package de.bentrm.datacat.auth.service.impl;

import de.bentrm.datacat.auth.domain.EmailConfirmationRequest;
import de.bentrm.datacat.auth.service.EmailService;
import de.bentrm.datacat.properties.AppProperties;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.hibernate.validator.constraints.URL;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

    final static String confirmEmailTemplate = """
            Dear ${name},

            Thank you for registering at ${url}. 
            To be able to login to your account, you'll need to confirm your email address.

            Please open ${confirmUrl}${token} and enter your confirmation token: ${token}

            If you did not register at ${url}, someone else may have used your email address
            to open an account. In this case, please ignore this message.

            Kind regards
            """;

    private final JavaMailSender javaMailSender;

    private final AppProperties properties;

    public EmailServiceImpl(JavaMailSender javaMailSender, AppProperties properties) {
        this.javaMailSender = javaMailSender;
        this.properties = properties;
    }

    @Timed("datacat.service.email.send")
    @Override
    public void sendEmailConfirmation(@NotNull EmailConfirmationRequest emailConfirmationRequest) {
        final var emailProperties = properties.getMail();
        @URL final String url = properties.getClient().getUrl();
        StringSubstitutor substitutor = new StringSubstitutor(Map.ofEntries(
                Map.entry("name", emailConfirmationRequest.getUser().getName()),
                Map.entry("url", url),
                Map.entry("confirmUrl", url + "/confirm?token="),
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
