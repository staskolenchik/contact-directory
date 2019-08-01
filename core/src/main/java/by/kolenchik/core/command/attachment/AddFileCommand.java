package by.kolenchik.core.command.attachment;

import by.kolenchik.core.command.Command;
import by.kolenchik.core.service.attachment.AttachmentFSService;
import by.kolenchik.core.service.attachment.AttachmentFSServiceImpl;
import by.kolenchik.core.service.attachment.AttachmentServiceClassFS;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddFileCommand implements Command {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private HttpServletRequest request;
    private HttpServletResponse response;
    private AttachmentFSService attachmentFSService = new AttachmentFSServiceImpl();

    public AddFileCommand(HttpServletRequest req, HttpServletResponse resp) {
        this.request = req;
        this.response = resp;
    }

    @Override
    public void execute() {

        log.debug("multipart request");

        File repository = new File("C:/temp-attachment");
        if (!repository.exists()) {
            repository.mkdir();
        }

        int maxMemorySize = 1024 * 1024 * 2;
        int maxRequestSize = 1024 * 1024 * 10;
        long maxFileSize = 1024 * 1024 * 5;

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(repository);
        factory.setSizeThreshold(maxMemorySize);

        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setSizeMax(maxRequestSize);
        upload.setFileSizeMax(maxFileSize);

        try {
            List<FileItem> items = upload.parseRequest(request);
            AttachmentServiceClassFS service = new AttachmentServiceClassFS();
            service.save(items);

        } catch (Exception e) {

            log.warn(e.getMessage());
            try {
                response.sendError(400, "Bad request");
            } catch (IOException ex) {
                log.warn(ex.getMessage());
            }
        }
    }
}
