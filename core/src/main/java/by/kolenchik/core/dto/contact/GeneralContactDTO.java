package by.kolenchik.core.dto.contact;

import by.kolenchik.core.dto.address.GeneralAddressDTO;

import java.util.Date;

public class GeneralContactDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date birthday;
    private String workplace;

    private GeneralAddressDTO address;

    public GeneralContactDTO() {
    }

    public GeneralContactDTO(Long id, String firstName, String lastName, String patronymic,
                             Date birthday, String workplace, GeneralAddressDTO address) {
        this.id = id;
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

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public GeneralAddressDTO getAddress() {
        return address;
    }

    public void setAddress(GeneralAddressDTO address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "GeneralContactDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", workplace='" + workplace + '\'' +
                ", address=" + address +
                '}';
    }
}
