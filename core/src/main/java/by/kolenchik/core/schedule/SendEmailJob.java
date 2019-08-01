package by.kolenchik.core.schedule;

import by.kolenchik.core.dto.email.SendEmailDTO;
import by.kolenchik.core.service.email.EmailService;
import by.kolenchik.core.service.email.EmailServiceImpl;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SendEmailJob implements Job {

    private static Logger log = Logger.getLogger(SendEmailJob.class);

    private String email;
    private String subject;
    private String text;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.debug("Start");

        SendEmailDTO sendEmailDTO = new SendEmailDTO(email, subject, text);

        EmailService emailService = new EmailServiceImpl();

        try {
            emailService.send(sendEmailDTO);
        } catch (EmailException e) {
            log.error(e.getMessage());
        }

        log.debug("end");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }
}
