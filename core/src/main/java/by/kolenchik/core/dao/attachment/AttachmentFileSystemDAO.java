package by.kolenchik.core.dao.attachment;

import by.kolenchik.core.dto.attachment.GetAttachmentDTO;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface AttachmentFileSystemDAO {

    void create(List<FileItem> items, String filePath) throws Exception;

    boolean delete(File file, String filePath);

    String getTemplateFile(String realPath, String templateName) throws IOException;

    GetAttachmentDTO getAttachment(String fileName, String realPath) throws IOException;

}
