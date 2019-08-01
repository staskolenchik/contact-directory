package by.kolenchik.core.service.attachment;

import by.kolenchik.core.dto.attachment.GetAttachmentDTO;
import by.kolenchik.core.exceptions.ResourceException;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.util.List;

public interface AttachmentFSService {

    void createFS(List<FileItem> items, String filePath) throws Exception;

    void delete(File file, String filePath);

    String getTemplate(String realPath, String templateName) throws ResourceException;

    GetAttachmentDTO getAttachment(String fileName);
}
