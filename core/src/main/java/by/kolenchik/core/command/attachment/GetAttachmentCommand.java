package by.kolenchik.core.command.attachment;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.dto.attachment.GetAttachmentDTO;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.service.attachment.AttachmentFSService;
import by.kolenchik.core.service.attachment.AttachmentFSServiceImpl;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

public class GetAttachmentCommand implements Command {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private HttpServletRequest request;
    private HttpServletResponse response;
    private AttachmentFSService attachmentFSService;

    public GetAttachmentCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.attachmentFSService = new AttachmentFSServiceImpl();
    }

    @Override
    public void execute() {

        log.debug("Start");

        String attachmentName = request.getHeader("attachment");


        try {
            GetAttachmentDTO attachment = attachmentFSService.getAttachment(attachmentName);

            response.setContentType(attachment.getContentType());
            response.setHeader("Content-disposition", "attachment; filename=" + attachment.getFileName());

            ServletOutputStream outputStream = response.getOutputStream();

            FileInputStream inputStream = new FileInputStream(attachment.getData());

            byte[] buffer = new byte[4096];
            Integer length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (IOException | ResourceException e) {
            log.warn(e.getMessage());
            try {
                response.sendError(404, "Resource was not found");
            } catch (IOException ex) {
                log.warn(ex.getMessage());
            }

        }
    }
}
