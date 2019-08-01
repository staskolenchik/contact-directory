package by.kolenchik.core.dto.email;

import java.util.List;

public class SendEmailListDTO {

    private List<Long> idList;
    private String subject;
    private String template;
    private String text;

    public SendEmailListDTO() {
    }

    public SendEmailListDTO(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SendEmailListDTO{" +
                "idList=" + idList +
                ", subject='" + subject + '\'' +
                ", template='" + template + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
