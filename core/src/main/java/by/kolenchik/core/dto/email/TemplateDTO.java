package by.kolenchik.core.dto.email;

public class TemplateDTO {

    private String text;

    public TemplateDTO() {
    }

    public TemplateDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
