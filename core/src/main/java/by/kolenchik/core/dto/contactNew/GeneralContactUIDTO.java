package by.kolenchik.core.dto.contactNew;

import by.kolenchik.core.dto.contact.GeneralContactDTO;

import java.util.List;

public class GeneralContactUIDTO {

    private Long contactsQuantity;
    private List<GeneralContactDTO> contacts;

    public GeneralContactUIDTO() {
    }

    public GeneralContactUIDTO(Long contactsQuantity, List<GeneralContactDTO> contacts) {
        this.contactsQuantity = contactsQuantity;
        this.contacts = contacts;
    }

    public Long getContactsQuantity() {
        return contactsQuantity;
    }

    public void setContactsQuantity(Long contactsQuantity) {
        this.contactsQuantity = contactsQuantity;
    }

    public List<GeneralContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<GeneralContactDTO> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "GeneralContactUIDTO{" +
                "usersCount=" + contactsQuantity +
                ", contacts=" + contacts +
                '}';
    }
}
