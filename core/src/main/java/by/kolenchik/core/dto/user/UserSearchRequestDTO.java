package by.kolenchik.core.dto.user;

public class UserSearchRequestDTO {

    private String firstname;
    private String lastname;
    private String patronymic;
    private String birthday;
    private String sex;
    private String citizenship;
    private String maritalStatus;

    public UserSearchRequestDTO() {
    }

    public UserSearchRequestDTO(String firstname, String lastname, String patronymic, String birthday,
                                String sex, String citizenship, String maritalStatus) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.sex = sex;
        this.citizenship = citizenship;
        this.maritalStatus = maritalStatus;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
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

    @Override
    public String toString() {
        return "UserSearchRequestDTO{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex='" + sex + '\'' +
                ", citizenship='" + citizenship + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                '}';
    }
}
