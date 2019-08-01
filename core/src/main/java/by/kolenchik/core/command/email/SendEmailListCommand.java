package by.kolenchik.core.command.email;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.dto.email.SendEmailListDTO;
import by.kolenchik.core.exceptions.ApplicationExeption;
import by.kolenchik.core.facade.SendEmailListFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendEmailListCommand implements Command {

    private static Logger log = Logger.getLogger(SendEmailListCommand.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    private SendEmailListFacade sendEmailListFacade =
            new SendEmailListFacade();

    public SendEmailListCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {

        try {
            String realPath = request.getServletContext().getRealPath("");

            ObjectMapper objectMapper = new ObjectMapper();
            SendEmailListDTO sendEmailListDTO = objectMapper.readValue(request.getReader(), SendEmailListDTO.class);
            log.trace(sendEmailListDTO);

            if (sendEmailListDTO.getTemplate() == null || sendEmailListDTO.getTemplate().equals(""))
                sendEmailListFacade.sendEmailList(sendEmailListDTO);
            else
                sendEmailListFacade.sendEmailListUsingTemplate(sendEmailListDTO, realPath);

        } catch (IOException e) {
            try {
                response.sendError(404, "Wrong email format");
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        } catch (ApplicationExeption e) {
            log.error(e.getMessage());
            try {
                response.sendError(404, "Email sending error");
            } catch (IOException ex) {
                log.error(e.getMessage());
            }
        }
    }
}
