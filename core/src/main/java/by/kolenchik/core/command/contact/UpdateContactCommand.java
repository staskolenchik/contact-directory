package by.kolenchik.core.command.contact;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.domain.Contact;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.exceptions.ResourceExistsException;
import by.kolenchik.core.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class UpdateContactCommand implements Command {

    private static Logger log = Logger.getLogger(UpdateContactCommand.class);

    private ContactService contactService = new ContactService();
    private Long id;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public UpdateContactCommand(ContactService contactService, Long id,
                                HttpServletRequest request, HttpServletResponse response) {
        this.id = id;
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {

        String realPath = request.getServletContext().getRealPath("");

        Contact contact = null;

        try {

            ObjectMapper mapper = new ObjectMapper();
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            mapper.setDateFormat(outputFormat);
            contact = mapper.readValue(request.getReader(), Contact.class);
            log.debug("updated contact = " + contact.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                response.sendError(404, "No suitable content in request");
            } catch (IOException ex) {
                log.warn(e.getMessage());
            }
        }

        try {
            contactService.updateContact(contact, id, realPath);
        } catch (ResourceException e) {
            log.error(e.getMessage());
            try {
                response.sendError(400, "Resource wasn't found. Cannot updateImageByUserId contact");
            } catch (IOException ex) {
                log.warn(e.getMessage());
            }
        } catch (ResourceExistsException e) {
            try {
                response.sendError(409, "Contact with requested firstname and lastname already exists");
            } catch (IOException ex) {
                log.warn(e.getMessage());
            }
        }
    }
}

