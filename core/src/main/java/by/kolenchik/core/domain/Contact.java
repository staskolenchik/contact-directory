package by.kolenchik.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class Contact {

    private Long id;
    private String firstName;
    private String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String patronymic;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date birthday;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Sex sex;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String citizenship;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String maritalStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String website;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workplace;
    private Address address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Telephone> telephones;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Attachment> attachments;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ImagePOJO image;

    public Contact() {
    }

    public Contact(String firstName, String lastName, String patronymic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    public Contact(String firstName, String lastName, String patronymic,
                   Date birthday, String workplace, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.workplace = workplace;
        this.address = address;
    }

    public Contact(Long id, String firstName, String lastName, String patronymic, Date birthday,
                   Sex sex, String citizenship, String maritalStatus, String website, String email, String workplace) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.sex = sex;
        this.citizenship = citizenship;
        this.maritalStatus = maritalStatus;
        this.website = website;
        this.email = email;
        this.workplace = workplace;
    }

    public Contact(Long id, String firstName, String lastName, String patronymic, Date birthday,
                   Sex sex, String citizenship, String maritalStatus, String website, String email,
                   String workplace, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.sex = sex;
        this.citizenship = citizenship;
        this.maritalStatus = maritalStatus;
        this.website = website;
        this.email = email;
        this.workplace = workplace;
        this.address = address;
    }

    public Contact(Long contactId, String firstName, String lastName, String patronymic,
                   Date birthday, String workplace, Address address) {
        this.id = contactId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.workplace = workplace;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void setTelephones(List<Telephone> telephones) {
        this.telephones = telephones;
    }

    public ImagePOJO getImage() {
        return image;
    }

    public void setImage(ImagePOJO image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", sex=" + sex +
                ", citizenship='" + citizenship + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", workplace='" + workplace + '\'' +
                ", address=" + address +
                ", telephones=" + telephones +
                ", attachments=" + attachments +
                ", image=" + image +
                '}';
    }
}
