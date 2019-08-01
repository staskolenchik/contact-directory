package by.kolenchik.core.command.contact;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.domain.Contact;
import by.kolenchik.core.dto.contact.ContactsDTO;
import by.kolenchik.core.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class GetContactsCommand implements Command {

    private static Logger log = Logger.getLogger(GetContactsCommand.class);

    private Integer pageNumber;
    private Integer pageSize;
    private ContactService contactService;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public GetContactsCommand(Integer pageNumber, Integer pageSize, ContactService contactService,
                              HttpServletRequest request, HttpServletResponse response) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.contactService = contactService;
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {

        List<Contact> contacts = contactService.getContacts(pageNumber, pageSize);
        Integer contactsCount = contactService.countContacts();

        ContactsDTO contactsDTO = new ContactsDTO();
        contactsDTO.setContacts(contacts);
        contactsDTO.setContactsQuantity(contactsCount);

        ObjectMapper mapper = new ObjectMapper();
        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            mapper.setDateFormat(outputFormat);
            mapper.writeValue(response.getWriter(), contactsDTO);
            log.info("send contacts" + "\n amount of contacts =" + contactsCount);
        } catch (IOException e) {
            log.warn(e.getMessage());
            try {
                response.sendError(404, "resource wasn't found");
            } catch (IOException ex) {
                log.warn(ex.getMessage());
            }

        }
    }
}
