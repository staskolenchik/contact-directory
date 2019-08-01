package by.kolenchik.core.dto.contact;

import by.kolenchik.core.domain.Contact;

import java.util.List;

public class ContactsDTO {

    private Integer contactsQuantity;
    private List<Contact> contacts;

    public ContactsDTO() {
    }

    public Integer getContactsQuantity() {
        return contactsQuantity;
    }

    public void setContactsQuantity(Integer contactsQuantity) {
        this.contactsQuantity = contactsQuantity;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
