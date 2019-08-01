package by.kolenchik.core.command.contact;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.dto.user.GetEmailDTO;
import by.kolenchik.core.service.user.UserService;
import by.kolenchik.core.service.user.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetEmailsByUserIdCommand implements Command {

    private static Logger log = Logger.getLogger(GetEmailsByUserIdCommand.class);

    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserService userService = new UserServiceImpl();

    public GetEmailsByUserIdCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {


        try {
            String[] ids = request.getParameterValues("id");
            Long[] idArray = new Long[ids.length];

            for (int i = 0; i < ids.length; i++) {
                Long id = Long.parseLong(ids[i]);
                idArray[i] = id;
            }

            List<GetEmailDTO> emailsByUserId = userService.getEmailListByUserIdList(idArray);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                PrintWriter writer = response.getWriter();
                objectMapper.writeValue(writer, emailsByUserId);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                log.error(e.getMessage());
                try {
                    response.sendError(404, "Can't send emails by user's ids");
                } catch (IOException ex) {
                    log.error(e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            try {
                response.sendError(404, "Can't get list of emails");
            } catch (IOException ex) {
                log.error(e.getMessage());
            }
        }

    }
}
