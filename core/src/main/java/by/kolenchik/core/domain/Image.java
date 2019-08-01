package by.kolenchik.core.domain;

public class Image {

    private Long imageId;
    private String imageName;
    private String imageContentType;
    private String imageData;

    public Image() {
    }

    public Image(Long imageId, String imageName, String imageContentType) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.imageContentType = imageContentType;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", imageName='" + imageName + '\'' +
                ", imageContentType='" + imageContentType + '\'' +
                ", imageData='" + imageData + '\'' +
                '}';
    }
}
