package by.kolenchik.core.dto.contact;

public class ContactsSearchRequestDTO {

    private String firstname;
    private String lastname;
    private String patronymic;
    private String birthday;
    private String sex;
    private String citizenship;
    private String maritalStatus;

    private String country;
    private String city;
    private String street;
    private String building;
    private String apartment;
    private String index;

    public ContactsSearchRequestDTO(String firstname, String lastname, String patronymic, String birthday,
                                    String sex, String citizenship, String maritalStatus, String country,
                                    String city, String street, String building, String apartment, String index) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.sex = sex;
        this.citizenship = citizenship;
        this.maritalStatus = maritalStatus;
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
        this.index = index;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
