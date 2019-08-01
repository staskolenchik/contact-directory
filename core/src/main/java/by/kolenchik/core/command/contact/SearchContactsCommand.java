package by.kolenchik.core.command.contact;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.dto.address.AddressSearchRequestDTO;
import by.kolenchik.core.dto.contactNew.GeneralContactUIDTO;
import by.kolenchik.core.dto.user.UserSearchRequestDTO;
import by.kolenchik.core.facade.SearchContactListFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class SearchContactsCommand implements Command {

    private static Logger log = Logger.getLogger(SearchContactsCommand.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    public SearchContactsCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    @Override
    public void execute()  {

        String page = request.getParameter("page");

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String patronymic = request.getParameter("patronymic");
        String birthday = request.getParameter("birthday");
        String sex = request.getParameter("sex");
        String citizenship = request.getParameter("citizenship");
        String maritalStatus = request.getParameter("maritalStatus");

        UserSearchRequestDTO userSearchRequestDTO = null;

        if (firstname != null || lastname != null || patronymic != null || birthday != null ||
                sex != null || citizenship != null || maritalStatus != null) {
            userSearchRequestDTO = new UserSearchRequestDTO(
                    firstname, lastname, patronymic, birthday, sex, citizenship, maritalStatus
            );
        }

        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String street = request.getParameter("street");
        String building = request.getParameter("building");
        String apartment = request.getParameter("apartment");
        String index = request.getParameter("index");

        AddressSearchRequestDTO addressSearchRequestDTO = null;

        if (country != null || city != null || street != null || building != null ||
                apartment != null || index != null) {
            addressSearchRequestDTO =
                    new AddressSearchRequestDTO(country, city, street, building, apartment, index);
        }

        log.trace(userSearchRequestDTO);
        log.trace(addressSearchRequestDTO);

        SearchContactListFacade searchContactListFacade = new SearchContactListFacade();

        GeneralContactUIDTO allContacts = searchContactListFacade.findAllContacts(userSearchRequestDTO, addressSearchRequestDTO);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("MM/dd/yyyy"));
            PrintWriter writer = response.getWriter();
            objectMapper.writeValue(writer,allContacts);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            try {
                response.sendError(404, "data problem");
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }


        //get dto
        //stringify all contacts


    }
}
