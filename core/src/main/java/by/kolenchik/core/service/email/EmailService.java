package by.kolenchik.core.service.email;

import by.kolenchik.core.dto.email.SendEmailDTO;
import org.apache.commons.mail.EmailException;

public interface EmailService {

    void send(SendEmailDTO sendEmailDTO) throws EmailException;

}
