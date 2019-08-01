package by.kolenchik.core.dto.attachment;

import java.io.File;

public class GetAttachmentDTO {

    private String fileName;
    private String contentType;
    private File data;

    public GetAttachmentDTO(String fileName, String contentType, File data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }

    public File getData() {
        return data;
    }

    public void setData(File data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "GetAttachmentDTO{" +
                "fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", data=" + data +
                '}';
    }
}
