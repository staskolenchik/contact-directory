package by.kolenchik.core.service;

import by.kolenchik.core.dao.contact.ContactDAO;
import by.kolenchik.core.dao.contact.ContactDAOImpl;
import by.kolenchik.core.domain.Contact;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.exceptions.ResourceExistsException;
import by.kolenchik.core.jdbc.ConnectionService;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ContactService {

    private static Logger log = Logger.getLogger(ContactService.class);

    private ContactDAO contactDAO = new ContactDAOImpl();

    public ContactService() {
    }

    public List<Contact> getContacts(Integer pageNumber, Integer pageSize) {

        Connection connection = null;
        List<Contact> contacts = null;

        try {
            connection = ConnectionService.getConnection();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            contacts = contactDAO.getContacts(pageNumber, pageSize, connection);
        } catch (SQLException e) {
            log.error(e);
            if (connection != null) {
                ConnectionService.rollback(connection);
            }
        } finally {
            if (connection != null) {
                ConnectionService.close(connection);
            }
        }
        return contacts;
    }

    public Contact getContactById(Long id, String realPath) throws ResourceException {

        log.debug("Start");

        Connection connection = ConnectionService.getConnection();
        Contact contact = null;

        try {
            log.debug("1");
            contact = contactDAO.getContactById(connection, id, realPath);
            log.info("Successfully get contact with id=" + id);
        } catch (SQLException e) {
            log.error(e);
            if (connection != null) {
                ConnectionService.rollback(connection);
            }
        } finally {
            if (connection != null) {
                ConnectionService.close(connection);
            }
        }
        log.debug("End");
        return contact;
    }

    public void updateContact(Contact contact, Long id, String realPath)
            throws ResourceException, ResourceExistsException {

        log.debug("Start");

        Connection connection = ConnectionService.getConnection();

        try {

            contactDAO.updateContact(connection, contact, id, realPath);
            log.info("contact with id=" + id + " successfully updated");

        } catch (SQLException e) {

            log.error(e);

            if (connection != null) {
                ConnectionService.rollback(connection);
            }

        } catch (ResourceExistsException e) {

            log.warn(e.getMessage());
            throw new ResourceExistsException("User exists");

        } catch (ResourceException e) {

            throw new ResourceException("Resource doesn't exists");
        } finally {

            if (connection != null) {
                ConnectionService.close(connection);
            }
        }

        log.debug("End");
    }

    public void deleteContact(Long contactId,String fullPath) throws ResourceException {
        Connection connection = ConnectionService.getConnection();
        try {
            contactDAO.deleteContact(connection, contactId, fullPath);
            log.info("contact with id=" + contactId + " successfully deleted");
        } catch (SQLException e) {
            log.error(e);
            if (connection != null) {
                ConnectionService.rollback(connection);
            }
        } finally {
            if (connection != null) {
                ConnectionService.close(connection);
            }
        }
    }

    public Integer countContacts() {
        Connection connection = ConnectionService.getConnection();
        Integer contactsCount = null;
        try {
            contactsCount = contactDAO.getContactsCount(connection);
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
        return contactsCount;
    }
}
