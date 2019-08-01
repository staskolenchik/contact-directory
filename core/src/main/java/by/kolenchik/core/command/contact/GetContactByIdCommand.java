package by.kolenchik.core.command.contact;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.domain.Contact;
import by.kolenchik.core.service.ContactService;
import by.kolenchik.core.exceptions.ResourceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class GetContactByIdCommand implements Command {

    private static Logger log = Logger.getLogger(GetContactByIdCommand.class);

    private ContactService contactService = new ContactService();
    private Long id;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public GetContactByIdCommand(Long id,
                                 HttpServletRequest request, HttpServletResponse response) {
        this.id = id;
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {

        log.debug("Start");

        String realPath = request.getServletContext().getRealPath("");

        Contact contact = null;

        try {
            contact = contactService.getContactById(id, realPath);
        } catch (ResourceException e) {
            try {
                response.sendError(404, "Contact with id=" + id + " doesn't exist!");
            } catch (IOException ex) {
                log.error(ex);
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            mapper.setDateFormat(outputFormat);
            mapper.writeValue(response.getWriter(), contact);
            log.info("server send contact=" + contact);
        } catch (IOException e) {
            log.error(e.getMessage());
            try {
                response.sendError(404, "Resource was not found");
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
