package by.kolenchik.core.dto.createupdatedto;

import by.kolenchik.core.domain.*;

import java.util.Date;
import java.util.List;

public class CreateUpdateContactDTO {

    private User user;
    private Address address;
    private List<Telephone> telephones;
    private List<Attachment> attachments;
    private ImagePOJO image;

    public CreateUpdateContactDTO() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Telephone> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<Telephone> telephones) {
        this.telephones = telephones;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public ImagePOJO getImage() {
        return image;
    }

    public void setImage(ImagePOJO image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CreateUpdateContactDTO{" +
                "user=" + user +
                ", address=" + address +
                ", telephones=" + telephones +
                ", attachments=" + attachments +
                ", image=" + image +
                '}';
    }
}
