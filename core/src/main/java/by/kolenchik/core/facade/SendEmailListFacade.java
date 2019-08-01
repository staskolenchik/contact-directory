package by.kolenchik.core.facade;

import by.kolenchik.core.dto.email.SendEmailDTO;
import by.kolenchik.core.dto.email.SendEmailListDTO;
import by.kolenchik.core.dto.user.GetEmailDTO;
import by.kolenchik.core.exceptions.ApplicationExeption;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.service.email.EmailService;
import by.kolenchik.core.service.email.EmailServiceImpl;
import by.kolenchik.core.service.template.TemplateService;
import by.kolenchik.core.service.template.TemplateServiceImpl;
import by.kolenchik.core.service.user.UserService;
import by.kolenchik.core.service.user.UserServiceImpl;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import java.util.List;

public class SendEmailListFacade {

    private static Logger log = Logger.getLogger(SendEmailListFacade.class);

    private EmailService emailService = new EmailServiceImpl();
    private UserService userService = new UserServiceImpl();
    private TemplateService templateService = new TemplateServiceImpl();

    public void sendEmailListUsingTemplate(SendEmailListDTO sendEmailListDTO, String realPath)
            throws ApplicationExeption {

        log.info("Start");

        try {
            List<Long> idList = sendEmailListDTO.getIdList();
            String templateName = sendEmailListDTO.getTemplate();

            for (int i = 0; i < idList.size(); i++) {

                Long id = idList.get(i);
                GetEmailDTO emailByUserId = userService.getEmailByUserId(id);
                String email = emailByUserId.getEmail();
                String subject = sendEmailListDTO.getSubject();
                String renderedTemplate = templateService.getRenderedTemplate(id, templateName, realPath);

                SendEmailDTO sendEmailDTO = new SendEmailDTO(email, subject, renderedTemplate);

                try {
                    emailService.send(sendEmailDTO);
                } catch (EmailException e) {
                    log.error(e.getMessage());
                }
            }
        } catch (ResourceException e) {
            log.error(e.getMessage());
            throw new ApplicationExeption("resource was not found");
        }
    }

    public void sendEmailList(SendEmailListDTO sendEmailListDTO) throws ApplicationExeption {

        log.info("Start");

        List<Long> idList = sendEmailListDTO.getIdList();

        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            GetEmailDTO emailByUserId = userService.getEmailByUserId(id);

            String email = emailByUserId.getEmail();
            String subject = sendEmailListDTO.getSubject();
            String text = sendEmailListDTO.getText();

            SendEmailDTO sendEmailDTO = new SendEmailDTO(email, subject, text);

            try {
                emailService.send(sendEmailDTO);
            } catch (EmailException e) {
                log.error(e.getMessage());
            }
        }
        log.info("End");
    }


}
