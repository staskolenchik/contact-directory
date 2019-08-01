package by.kolenchik.core.service.email;

import by.kolenchik.core.command.email.SendEmailListCommand;
import by.kolenchik.core.dto.email.SendEmailDTO;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import java.util.List;

public class EmailServiceImpl implements EmailService {

    private static Logger log = Logger.getLogger(SendEmailListCommand.class);


    @Override
    public void send(SendEmailDTO sendEmailDTO) throws EmailException {

        String emailAdd = sendEmailDTO.getEmail();
        String emailSub = sendEmailDTO.getSubject();
        String emailText = sendEmailDTO.getText();

        if (emailSub == null) {
            emailSub = "";
        }

        if (emailText == null) {
            emailText = "";
        }

        log.trace("email = " + emailAdd + ", " +
                "subject = " + emailSub + ", " +
                "text = " + emailText);

        log.debug("Start email configure");
        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(
                new DefaultAuthenticator("mnopqwaszx@gmail.com", "qwerty12345A")
        );
        email.setSSLOnConnect(true);
        email.setFrom("mnopqwaszx@gmail.com");
        email.setSubject(emailSub);
        email.setMsg(emailText);
        email.addTo(emailAdd);
        log.debug("End email configure");
        email.send();
        log.debug("Email sent");

    }
}
