package by.kolenchik.core.domain;

import java.util.Date;

public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date birthday;
    private Sex sex;
    private String citizenship;
    private String maritalStatus;
    private String website;
    private String email;
    private String workplace;

    public User() {
    }

    public User(String firstName, String lastName, String patronymic, Date birthday,
                Sex sex, String citizenship, String maritalStatus, String website,
                String email, String workplace) {
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

    public User(Long id, String firstName, String lastName, String patronymic, Date
            birthday, Sex sex, String citizenship, String maritalStatus, String website,
                String email, String workplace) {
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

    @Override
    public String toString() {
        return "User{" +
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
                '}';
    }
}
