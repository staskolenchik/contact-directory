package by.kolenchik.core.dao.contact;

import by.kolenchik.core.domain.Contact;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.exceptions.ResourceExistsException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ContactDAO {

    List<Contact> getContacts(Integer pageNumber, Integer pageSize, Connection connection) throws SQLException;
    Contact getContactById(Connection connection, Long id, String realPath) throws ResourceException, SQLException;
    void addContact(Connection connection, Contact contact, String realPath) throws ResourceException, SQLException;
    void updateContact(Connection connection, Contact contact, Long contactId, String fullPath)
            throws ResourceExistsException, SQLException, ResourceException;
    void deleteContact(Connection connection, Long id, String fullPath) throws ResourceException, SQLException;

    Integer getContactsCount(Connection connection) throws SQLException;

}
