package by.kolenchik.core.service.attachment;

import by.kolenchik.core.dao.attachment.AttachmentDAOClassFS;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class AttachmentServiceClassFS {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    public void save(List<FileItem> items) {

        AttachmentDAOClassFS attachmentDAOClassFS = new AttachmentDAOClassFS();

        try {
            attachmentDAOClassFS.save(items);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }


    public void delete(String fileName) {

        AttachmentDAOClassFS attachmentServiceClassFS = new AttachmentDAOClassFS();

        try {
            attachmentServiceClassFS.delete(fileName);
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}
