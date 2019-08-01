package by.kolenchik.core.service.attachment;

import by.kolenchik.core.dao.attachment.AttachmentDAOClassFS;
import by.kolenchik.core.dao.attachment.AttachmentFileSystemDAO;
import by.kolenchik.core.dao.attachment.AttachmentFileSystemDAOImpl;
import by.kolenchik.core.dto.attachment.GetAttachmentDTO;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class AttachmentFSServiceImpl implements AttachmentFSService {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private AttachmentFileSystemDAO attachmentFileSystemDAO;

    public AttachmentFSServiceImpl() {
        this.attachmentFileSystemDAO = new AttachmentFileSystemDAOImpl();
    }

    public void createFS(List<FileItem> items, String fullSavePath) throws Exception {
        attachmentFileSystemDAO.create(items, fullSavePath);
    }

    public void delete(File file, String filePath) {
        attachmentFileSystemDAO.delete(file, filePath);
    }

    @Override
    public String getTemplate(String realPath, String templateName) throws ResourceException {

        String templateFile;
        try {
            templateFile = attachmentFileSystemDAO.getTemplateFile(realPath, templateName);
        } catch (IOException|NullPointerException e) {
            log.error(e.getMessage());
            throw new ResourceException("Template wasn't found");
        }
        return templateFile;
    }

    @Override
    public GetAttachmentDTO getAttachment(String fileName) throws ResourceException {

        log.debug("Start");

        GetAttachmentDTO getAttachmentDTO;
        try {
            AttachmentDAOClassFS attachmentDAOClassFS = new AttachmentDAOClassFS();
            getAttachmentDTO = attachmentDAOClassFS.getAttachment(fileName);
        } catch (IOException | NullPointerException e) {
            log.warn(e.getMessage());
            throw new ResourceException("file wasn't found");
        }

        log.debug("End");

        return getAttachmentDTO;
    }

}
