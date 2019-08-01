package by.kolenchik.core.command.email;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.dto.email.TemplateDTO;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.service.attachment.AttachmentFSService;
import by.kolenchik.core.service.attachment.AttachmentFSServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetEmailTemplateCommand implements Command {

    private static Logger log = Logger.getLogger(SendEmailListCommand.class);

    private HttpServletRequest request;
    private HttpServletResponse response;
    private AttachmentFSService attachmentFSService = new AttachmentFSServiceImpl();

    public GetEmailTemplateCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {

        String templateName = request.getHeader("template");

        String realPath = request.getServletContext().getRealPath("");


        try {
            if (!templateName.equals("")) {

                String templateStr = attachmentFSService.getTemplate(realPath, templateName);
                TemplateDTO templateDTO = new TemplateDTO(templateStr);

                ObjectMapper objectMapper = new ObjectMapper();
                PrintWriter writer = response.getWriter();
                objectMapper.writeValue(writer, templateDTO);

            } else {
                throw new ResourceException("empty header");
            }
        } catch (ResourceException|IOException e) {
            log.error(e.getMessage());
            try {
                response.sendError(404, "Template file wasn't found");
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }

    }
}
