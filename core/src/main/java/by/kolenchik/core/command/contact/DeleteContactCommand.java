package by.kolenchik.core.command.contact;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.service.ContactService;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteContactCommand implements Command {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private ContactService contactService;
    private Long id;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public DeleteContactCommand(ContactService contactService, Long id,HttpServletRequest request, HttpServletResponse response) {
        this.contactService = contactService;
        this.id = id;
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {

        String realPath = request.getServletContext().getRealPath("");

        try {
            contactService.deleteContact(id , realPath);
        } catch (ResourceException e) {
            log.warn(e.getMessage());
            try {
                response.sendError(400, "Resource wasn't found");
            } catch (IOException ex) {
                log.warn(ex.getMessage());
            }
        }
    }
}
