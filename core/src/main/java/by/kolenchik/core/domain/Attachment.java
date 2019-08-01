package by.kolenchik.core.domain;

import java.util.Date;

public class Attachment {

    private Long fileId;
    private String fileName;
    private Date fileUpload;
    private String fileComment;

    public Attachment() {
    }



    public Attachment(Long fileId, String fileName, Date fileUpload, String fileComment) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileUpload = fileUpload;
        this.fileComment = fileComment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(Date fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileComment() {
        return fileComment;
    }

    public void setFileComment(String fileComment) {
        this.fileComment = fileComment;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", fileUpload=" + fileUpload +
                ", fileComment='" + fileComment + '\'' +
                '}';
    }
}
