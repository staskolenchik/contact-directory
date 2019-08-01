package by.kolenchik.core.command.contact;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.dto.createupdatedto.CreateUpdateContactDTO;
import by.kolenchik.core.exceptions.ApplicationExeption;
import by.kolenchik.core.exceptions.ResourceExistsException;
import by.kolenchik.core.facade.ContactFacade;
import by.kolenchik.core.utils.ClassNameUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class CreateContactCommand implements Command {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CreateContactCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {

        log.debug("Create contact");

        try {
            String realPath = request.getServletContext().getRealPath("");

            ObjectMapper mapper = new ObjectMapper();
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            mapper.setDateFormat(outputFormat);
            CreateUpdateContactDTO contact = mapper.readValue(request.getReader(), CreateUpdateContactDTO.class);

            log.trace(contact);

            ContactFacade contactFacade = new ContactFacade();
            contactFacade.create(contact, realPath);

        } catch (IOException | ApplicationExeption e) {
            log.error(e.getMessage());
            try {
                response.sendError(404, "Application error");
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        } catch (ResourceExistsException e) {
            log.warn(e.getMessage());
            try {
                response.sendError(409, "User is already exists");
            } catch (IOException ex) {
                log.warn(e.getMessage());
            }
        }
    }
}
