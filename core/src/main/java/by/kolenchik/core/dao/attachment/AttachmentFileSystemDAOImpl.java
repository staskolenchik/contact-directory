package by.kolenchik.core.dao.attachment;

import by.kolenchik.core.dto.attachment.GetAttachmentDTO;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class AttachmentFileSystemDAOImpl implements AttachmentFileSystemDAO {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());




    @Override
    public void create(List<FileItem> items, String filePath) throws Exception {

        File fileSaveDir = new File(filePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();

            if (!item.isFormField()) {
                log.info("Item is not form field" + item.isFormField());
                String itemName = item.getName();
                String fullFilePath = filePath + File.separator + itemName;

                item.write(new File(fullFilePath));
                log.info("file saved to : " + fullFilePath);
            }
        }
    }

    @Override
    public boolean delete(File file, String filepath) {
        return file.delete();
    }

    @Override
    public String getTemplateFile(String realPath, String templateName) throws IOException {

        String templateStr = null;

        realPath = realPath.replace('\\', '/');
        String templateNameModified = templateName.toLowerCase();

        String templateDirPath = null;

        if (realPath.endsWith("/")) {
            templateDirPath = realPath + "emailTemplates" + File.separator + "ui";
        } else {
            templateDirPath = realPath + File.separator + "emailTemplates" + File.separator + "ui";
        }

        File file = new File(templateDirPath);
        File[] listFiles = file.listFiles();

        for (File f : listFiles) {
            String name = f.getName();
            String[] split = name.split("[.]");

            if (split[0].equals(templateNameModified)) {

                FileReader reader = new FileReader(f);
                BufferedReader bufferedReader = new BufferedReader(reader, 1000);
                StringBuffer strBuffer = new StringBuffer("");

                while (bufferedReader.ready()) {
                    strBuffer.append(bufferedReader.readLine());
                }

                bufferedReader.close();
                reader.close();
                templateStr = strBuffer.toString();
            }
        }

        return templateStr;
    }

    @Override
    public GetAttachmentDTO getAttachment(String fileName, String realPath) throws IOException{

        log.debug("Start");

        realPath = realPath.replace("\\", "/");

        String attachmentPath;
        if (realPath.endsWith("/")) {
            attachmentPath = realPath + "files" + File.separator + fileName;
        } else {
            attachmentPath = realPath + File.separator + "files" +  File.separator + fileName;
        }

        log.trace("attachmentPath =" + attachmentPath);

        File data = new File(attachmentPath);

        String contentType = Files.probeContentType(Paths.get(attachmentPath));

        GetAttachmentDTO getAttachmentDTO = new GetAttachmentDTO(fileName, contentType, data);
        log.trace(getAttachmentDTO);

        log.debug("End");

        return getAttachmentDTO;
    }
}
