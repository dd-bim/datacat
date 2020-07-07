package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.EmailConfirmationRequest;
import de.bentrm.datacat.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        to open an account. Please ignore this message.
                    
        Kind regards
        """;

    private final JavaMailSender sender;

    @Value("${datacat.url}")
    private String homeUrl;

    @Value("${datacat.auth.confirm.url}")
    private String confirmUrl;

    @Value("${datacat.auth.confirm.from}")
    private String confirmFrom;

    @Autowired
    public EmailServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendEmailConfirmation(@NotNull EmailConfirmationRequest emailConfirmationRequest) {
        StringSubstitutor substitutor = new StringSubstitutor(Map.ofEntries(
                Map.entry("name", emailConfirmationRequest.getUser().getName()),
                Map.entry("homeUrl", homeUrl),
                Map.entry("confirmUrl", confirmUrl),
                Map.entry("token", emailConfirmationRequest.getToken())
        ));
        String body = substitutor.replace(confirmEmailTemplate);

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Please confirm your account settings");
        message.setFrom(confirmFrom);
        message.setTo(emailConfirmationRequest.getSentTo());
        message.setText(body);
        try {
            sender.send(message);
        } catch (MailException e) {
            log.warn(e.getLocalizedMessage());
        }
    }
}
