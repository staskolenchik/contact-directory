package by.kolenchik.core.domain;

public class ImagePOJO {

    private Long id;
    private String data;
    private String contentType;

    public ImagePOJO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "ImagePOJO{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
