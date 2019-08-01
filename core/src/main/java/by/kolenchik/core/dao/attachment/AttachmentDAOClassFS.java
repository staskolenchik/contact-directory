package by.kolenchik.core.dao.attachment;

import by.kolenchik.core.dto.attachment.GetAttachmentDTO;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class AttachmentDAOClassFS {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private static File attachmentDirObj;

    static {

        String dirServerPath = "C:" + File.separator + "file-server";

        File fileDir = new File(dirServerPath);

        if (!fileDir.exists()) {
            if (fileDir.mkdir()) {
                log.debug("Made directory for server files with path =" + dirServerPath);
            }
        }

        File attachmentDir = new File(fileDir, "attachments");

        if (!attachmentDir.exists()) {
            if (attachmentDir.mkdir()) {
                log.debug("Made directory for server files with path =" + attachmentDir.getAbsolutePath());
            }
        }

        attachmentDirObj = attachmentDir;

        log.debug("Save images in absolute path =" + attachmentDirObj.getAbsolutePath());
    }

    public void save(List<FileItem> items) throws Exception {

        log.debug("Start");
        log.trace("items size =" + items.size());

        Iterator<FileItem> iter = items.iterator();

        while (iter.hasNext()) {
            FileItem item = iter.next();

            if (!item.isFormField()) {
                log.debug("is formField? - " + item.isFormField());

                String fileName = item.getName();

                File attachment = new File(attachmentDirObj, fileName);

                item.write(attachment);
            }
        }
    }

    public GetAttachmentDTO getAttachment(String attachmentName) throws IOException, NullPointerException {

        log.debug("Start");

        File[] attachmentFiles = attachmentDirObj.listFiles();

        for (int i = 0; i < attachmentFiles.length; i++) {

            if (attachmentFiles[i].getName().equals(attachmentName)) {

                String contentType = Files.probeContentType(attachmentFiles[i].toPath());

                GetAttachmentDTO getAttachmentDTO =
                        new GetAttachmentDTO(attachmentName, contentType, attachmentFiles[i]);

                log.trace(getAttachmentDTO);

                return getAttachmentDTO;
            }
        }

        log.debug("End");

        return null;

    }

    public boolean delete(String attachmentName) throws IOException, NullPointerException {

        log.debug("Start");

        File[] attachmentFiles = attachmentDirObj.listFiles();

        for (int i = 0; i < attachmentFiles.length; i++) {

            if (attachmentFiles[i].getName().equals(attachmentName)) {

                return attachmentFiles[i].delete();
            }
        }

        log.debug("End");

        return false;
    }
}
