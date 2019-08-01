package by.kolenchik.core.dao.contact;

import by.kolenchik.core.dao.address.AddressDAO;
import by.kolenchik.core.dao.address.AddressDAOImpl;
import by.kolenchik.core.dao.user.UserDAO;
import by.kolenchik.core.dao.user.UserDAOImpl;
import by.kolenchik.core.domain.*;
import by.kolenchik.core.exceptions.ResourceExistsException;
import by.kolenchik.core.jdbc.ConnectionService;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.service.attachment.AttachmentServiceClassFS;
import by.kolenchik.core.service.image.ImageServiceClass;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl implements ContactDAO {

    private static Logger log = Logger.getLogger(ContactDAOImpl.class);

    @Override
    public List<Contact> getContacts(Integer pageNumber, Integer pageSize, Connection connection)
            throws SQLException {

        log.debug("Start");

        Integer contactsStartQuery = pageNumber * pageSize;

        String sql = "SELECT contact.id, firstname, lastname, patronymic, " +
                "birthday, workplace, " +
                "address.id, country, city, street, building, apartment, address.index " +
                "FROM contacts.contact, contacts.address " +
                "WHERE contact.id=contact_id " +
                "ORDER BY contact.id DESC " +
                "LIMIT " + contactsStartQuery + ", " + pageSize;

        List<Contact> contactList = new ArrayList<Contact>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Long contactId = resultSet.getLong(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String patronymic = resultSet.getString(4);
            Date birthdaySql = resultSet.getDate(5);
            java.util.Date birthday = null;
            if (birthdaySql != null) {
                birthday = new java.util.Date(birthdaySql.getTime());
            }
            String workplace = resultSet.getString(6);
            Long addressId = resultSet.getLong(7);
            String country = resultSet.getString(8);
            String city = resultSet.getString(9);
            String street = resultSet.getString(10);
            String building = resultSet.getString(11);
            String apartment = resultSet.getString(12);
            String index = resultSet.getString(13);

            Contact contact = new Contact(
                    contactId, firstName, lastName, patronymic, birthday, workplace,
                    new Address(addressId, country, city, street, building, apartment, index)
            );

            log.trace(contact);

            contactList.add(contact);
        }


        log.debug("End");
        return contactList;
    }

    @Override
    public void addContact(Connection connection, Contact contact, String realPath) throws SQLException, ResourceException{

        String sqlCountSameContact = "SELECT COUNT(*) FROM contacts.contact " +
                "WHERE firstname = ? " +
                "AND lastname = ?";

        String sqlCreateContact = "INSERT INTO contacts.contact(firstname, lastname, " +
                "patronymic, birthday, sex, citizenship, marital_status, website, email, workplace) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";

        String sqlGetContactId = "SELECT contact.id FROM contacts.contact " +
                "WHERE firstname=? AND lastname=?";

        String sqlCreateAddress = "INSERT INTO contacts.address(address.contact_id, address.country, city, " +
                "street,building,apartment, address.index) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlCreateTelephone = "INSERT INTO contacts.telephone (telephone.contact_id, telephone.country, " +
                "operator, telephone.number, telephone.type, telephone.comment) " +
                "VALUES (?, ?, ?, ?, ?, ?)";



        //check if same contacts exists
        PreparedStatement pstmCount = connection.prepareStatement(sqlCountSameContact);
        pstmCount.setString(1, contact.getFirstName());
        pstmCount.setString(2, contact.getLastName());
        ResultSet rSCount = pstmCount.executeQuery();



        int countSameContact = 0;
        while (rSCount.next()) {
            countSameContact = rSCount.getInt(1);
        }

        log.debug("same contacts=" + countSameContact);
        if (countSameContact == 0) {
            //createFS new contact
            PreparedStatement pstmCreateContact = connection.prepareStatement(sqlCreateContact);
            pstmCreateContact.setString(1, contact.getFirstName());
            pstmCreateContact.setString(2, contact.getLastName());
            pstmCreateContact.setString(3, contact.getPatronymic());
            if (contact.getBirthday() != null) {
                Date birthdaySql = new Date(contact.getBirthday().getTime());
                pstmCreateContact.setDate(4, birthdaySql);
            } else {
                pstmCreateContact.setDate(4, null);
            }
            pstmCreateContact.setString(5, contact.getSex().getSexStr());
            pstmCreateContact.setString(6, contact.getCitizenship());
            pstmCreateContact.setString(7, contact.getMaritalStatus());
            pstmCreateContact.setString(8, contact.getWebsite());
            pstmCreateContact.setString(9, contact.getEmail());
            pstmCreateContact.setString(10, contact.getWorkplace());
            pstmCreateContact.executeUpdate();
            log.debug("createFS contact");

            //get auto-incremented contact id
            PreparedStatement pSGetContactId = connection.prepareStatement(sqlGetContactId);
            pSGetContactId.setString(1, contact.getFirstName());
            pSGetContactId.setString(2, contact.getLastName());
            ResultSet rsGetContactId = pSGetContactId.executeQuery();
            while (rsGetContactId.next()) {
                long contactId = rsGetContactId.getLong(1);
                contact.setId(contactId);
            }
            log.debug("get contact id");

            //createFS address ref to contact id
            PreparedStatement pSCreateAddress = connection.prepareStatement(sqlCreateAddress);
            pSCreateAddress.setLong(1, contact.getId());
            pSCreateAddress.setString(2, contact.getAddress().getCountry());
            pSCreateAddress.setString(3, contact.getAddress().getCity());
            pSCreateAddress.setString(4, contact.getAddress().getStreet());
            pSCreateAddress.setString(5, contact.getAddress().getBuilding());
            pSCreateAddress.setString(6, contact.getAddress().getApartment());
            pSCreateAddress.setString(7, contact.getAddress().getIndex());
            pSCreateAddress.executeUpdate();
            log.debug("createFS address");

            //createFS telephone ref to contact id
            List<Telephone> telephones = contact.getTelephones();

            if (telephones.size() > 0) {
                for (int i = 0; i < telephones.size(); i++) {
                    PreparedStatement pSCreateTelephone = connection.prepareStatement(sqlCreateTelephone);
                    pSCreateTelephone.setLong(1, contact.getId());
                    pSCreateTelephone.setInt(2, telephones.get(i).getCountry());
                    pSCreateTelephone.setInt(3, telephones.get(i).getOperator());
                    pSCreateTelephone.setInt(4, telephones.get(i).getNumber());
                    pSCreateTelephone.setString(5, telephones.get(i).getPhoneType().getValue());
                    pSCreateTelephone.setString(6, telephones.get(i).getComment());
                    pSCreateTelephone.executeUpdate();
                    log.debug("createFS telephone");
                }
            }
            List<Attachment> attachments = contact.getAttachments();

            if (attachments.size() > 0) {
                for (int i = 0; i < attachments.size(); i++) {
                    String sqlCreateAttachment = "INSERT INTO contacts.attachment (attachment.contact_id, " +
                            "attachment.name, attachment.uploaddate, attachment.comment) " +
                            "VALUES (?, ?, ?, ?)";

                    PreparedStatement pSCreateAttachment = connection.prepareStatement(sqlCreateAttachment);
                    pSCreateAttachment.setLong(1, contact.getId());
                    pSCreateAttachment.setString(2, attachments.get(i).getFileName());
                    Date date = null;

                    if (attachments.get(i).getFileUpload() != null) {
                        date = new Date(attachments.get(i).getFileUpload().getTime());
                        pSCreateAttachment.setDate(3, date);
                    } else {
                        pSCreateAttachment.setDate(3, null);
                    }

                    pSCreateAttachment.setString(4, attachments.get(i).getFileComment());
                    pSCreateAttachment.executeUpdate();
                }
            }


            /*try {
                String imageName = contact.getId().toString();
                contact.getImage().setImageName(imageName);
                ImageDataBaseDAO imageDataBaseDao = new ImageDataBaseDAOImpl();

            } catch (NullPointerException e) {
                log.error("Came no image from ui" + e.getMessage());
            }*/



        } else {
            //throw exception is contact.firstName and contact.lastName are already exist in db
            log.debug("same contact exists");
            throw new ResourceException("Contact's first name and last name are already exist!");
        }
    }


    @Override
    public Contact getContactById(Connection connection, Long id, String realPath)
            throws ResourceException, SQLException {

        log.debug("Start");

        String sqlGetContactAndAddressById = "SELECT contact.id, firstname, lastname, patronymic, " +
                " birthday, sex, citizenship, marital_status, website, email, workplace  " +
                " FROM contacts.contact " +
                " WHERE contact.id = ?";

        String sqlGetTelephonesById = "SELECT telephone.id, telephone.country, operator, telephone.number, " +
                "telephone.type , telephone.comment " +
                "FROM contacts.telephone " +
                "WHERE telephone.contact_id = ?";

        String sqlGetAttachmentsById = "SELECT attachment.id, attachment.name, uploaddate, attachment.comment " +
                "FROM contacts.attachment " +
                "WHERE attachment.contact_id = ?";

        Contact contact = null;

        PreparedStatement pSGetContactById = connection.prepareStatement(sqlGetContactAndAddressById);
        pSGetContactById.setLong(1, id);
        ResultSet rsGetContactById = pSGetContactById.executeQuery();

        while (rsGetContactById.next()) {

            Long contactId = rsGetContactById.getLong(1);
            String firstName = rsGetContactById.getString(2);
            String lastName = rsGetContactById.getString(3);
            String patronymic = rsGetContactById.getString(4);
            Date birthdaySql = rsGetContactById.getDate(5);

            java.util.Date birthday = null;

            if (birthdaySql != null) {
                birthday = new java.util.Date(birthdaySql.getTime());
            }
            String sexStr = rsGetContactById.getString(6);
            Sex sex = null;

            switch (sexStr) {
                case "MALE":
                    sex = Sex.MALE;
                    break;
                case "FEMALE":
                    sex = Sex.FEMALE;
                    break;
            }

            String citizenship = rsGetContactById.getString(7);
            String maritalStatus = rsGetContactById.getString(8);
            String website = rsGetContactById.getString(9);
            String email = rsGetContactById.getString(10);
            String workplace = rsGetContactById.getString(11);

            contact = new Contact(
                    contactId, firstName, lastName, patronymic, birthday, sex, citizenship,
                    maritalStatus, website, email, workplace
                    );

            try {
                AddressDAO addressDAO = new AddressDAOImpl();
                Address addressByUserId = addressDAO.findAddressByUserId(id);
                contact.setAddress(addressByUserId);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }

            log.trace(contact);
        }


        if (contact == null) {

            throw new ResourceException("Requested resource wasn't found");
        } else {

            PreparedStatement pSGetTelephonesById = connection.prepareStatement(sqlGetTelephonesById);
            pSGetTelephonesById.setLong(1, id);
            ResultSet rsGetTelephonesById = pSGetTelephonesById.executeQuery();
            List<Telephone> telephones = new ArrayList<>();

            while (rsGetTelephonesById.next()) {
                Long telephoneId = rsGetTelephonesById.getLong(1);
                Integer country = rsGetTelephonesById.getInt(2);
                Integer operator  = rsGetTelephonesById.getInt(3);
                Integer number = rsGetTelephonesById.getInt(4);
                String typeSql = rsGetTelephonesById.getString(5);
                PhoneType phoneType = null;
                switch (typeSql) {
                    case "HOME":
                        phoneType = PhoneType.HOME;
                        break;
                    case "MOBILE" :
                        phoneType = PhoneType.MOBILE;
                        break;
                }

                String comment = rsGetTelephonesById.getString(6);

                Telephone telephone = new Telephone(telephoneId, country, operator, number, phoneType, comment);
                telephones.add(telephone);
            }
            contact.setTelephones(telephones);

            List<Attachment> attachments = new ArrayList<>();
            PreparedStatement pSGetAttachmentsById = connection.prepareStatement(sqlGetAttachmentsById);
            pSGetAttachmentsById.setLong(1, id);

            ResultSet rSGetAttachmentsById = pSGetAttachmentsById.executeQuery();
            while (rSGetAttachmentsById.next()) {
                long attachmentId = rSGetAttachmentsById.getLong(1);
                String attachmentName = rSGetAttachmentsById.getString(2);
                Date msqlDate = rSGetAttachmentsById.getDate(3);
                java.util.Date date = null;

                if (msqlDate != null) {
                    date = new java.util.Date(msqlDate.getTime());
                }
                String attachmentComment = rSGetAttachmentsById.getString(4);

                attachments.add(new Attachment(attachmentId, attachmentName, date, attachmentComment));
            }
            contact.setAttachments(attachments);

            //get image by contact id

            ImageServiceClass imageServiceClass = new ImageServiceClass();
            ImagePOJO image = imageServiceClass.getImage(id);
            contact.setImage(image);

        }

        log.debug("End");
        return contact;
    }


    @Override
    public void updateContact(Connection connection, Contact contact, Long contactId, String realPath)
            throws ResourceExistsException, SQLException, ResourceException{

        log.debug("Start");

        String sqlFindContactById = "SELECT id FROM contacts.contact WHERE id=?";

        String sqlUpdateAddress = "UPDATE contacts.address SET country = ?, city = ?, " +
                "street = ?, building = ?, apartment = ?, " +
                "address.index = ? " +
                "WHERE contact_id =?;";

        String sqlUpdateContact = "UPDATE contacts.contact SET firstname = ?, " +
                "lastname = ?, patronymic = ?, birthday = ?, sex = ?, citizenship = ?," +
                " marital_status = ?, website = ?, email = ?,workplace = ? " +
                "WHERE id=?;";

        //check if contacts exists in db
        PreparedStatement pSFindContactById = connection.prepareStatement(sqlFindContactById);
        pSFindContactById.setLong(1, contactId);
        ResultSet rSFindContactById = null;

        try {
            rSFindContactById = pSFindContactById.executeQuery();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        boolean doesExist = false;

        if (rSFindContactById.next()) {
            Long id = rSFindContactById.getLong(1);
            if (id.equals(contactId)){
            doesExist = true;
            log.info(doesExist);
            contact.setId(contactId);
            pSFindContactById.close();
            }
        }

        if (doesExist) {

            /**
             * Check if exists with firstname and lastname
             */
            String firstName = contact.getFirstName();
            String lastName = contact.getLastName();
            UserDAO userDAO = new UserDAOImpl();

            Long id = userDAO.getUserIdByFirstnameAndLastname(firstName, lastName);
            if (!id.equals(contactId)) {
                throw new ResourceExistsException("Resource is already exists");
            }

            /**
             * update image
             */
            ImageServiceClass imageServiceClass = new ImageServiceClass();
            ImagePOJO image = contact.getImage();

            if (image.getData() == null) {
                imageServiceClass.delete(contactId);
            } else {
                image.setId(contactId);
                imageServiceClass.save(image);
            }


            /**
             * update attachments in db
             * */

            realPath = realPath.replace('\\', '/');

            String fullPath = null;
            if (realPath.endsWith("/")) {
                fullPath = realPath + "files";
            } else {
                fullPath = realPath + File.separator + "files";
            }

            List<Attachment> attachments = contact.getAttachments();
            log.debug(attachments + " attachments List");

            //deleteImageByUserId attachment from db, which doesn't exist in request
            String sqlGetAttachmentIdByContactId = "SELECT attachment.id FROM contacts.attachment " +
                    "WHERE attachment.contact_id = ? " +
                    "ORDER BY attachment.id ASC";

            PreparedStatement pSGetAttachmentIdByContactId =
                    connection.prepareStatement(sqlGetAttachmentIdByContactId);
            pSGetAttachmentIdByContactId.setLong(1, contactId);
            ResultSet rSGetAttachmentIdByContactId = pSGetAttachmentIdByContactId.executeQuery();

            List<Long> dbAttachmentIdList = new ArrayList<>();

            while (rSGetAttachmentIdByContactId.next()) {
                Long telephoneId = rSGetAttachmentIdByContactId.getLong(1);
                dbAttachmentIdList.add(telephoneId);
                log.debug(telephoneId + " id attachment exists in db");
            }

            if (dbAttachmentIdList.isEmpty() || attachments.isEmpty()) {
                for (int i = 0; i < dbAttachmentIdList.size(); i++) {

                    //deleteImageByUserId from filesystem
                    String sqlSelectAttachment = "SELECT attachment.name FROM contacts.attachment " +
                            "WHERE attachment.id = ?";

                    PreparedStatement pSSelectAttachment = connection.prepareStatement(sqlSelectAttachment);
                    pSSelectAttachment.setLong(1, dbAttachmentIdList.get(i));
                    ResultSet rSSelectAttachment = pSSelectAttachment.executeQuery();
                    String fileName = null;

                    while (rSSelectAttachment.next()) {
                        fileName = rSSelectAttachment.getString(1);
                        log.debug("file name from db =  " + fileName);
                    }

                    String fullPathtoFile = fullPath + File.separator + fileName;
                    File file = new File(fullPathtoFile);
                    boolean isDeleted = file.delete();
                    log.debug("file with path = " + fullPathtoFile + " , is deleted = " + isDeleted);


                    log.debug("deleteImageByUserId attachment with id=" + dbAttachmentIdList.get(i) +
                            ", where contact id=" + contactId + ".");
                    String sqlDeleteTelephone = "DELETE FROM contacts.attachment WHERE attachment.id =?";
                    PreparedStatement pSDeleteTelephone = connection.prepareStatement(sqlDeleteTelephone);
                    pSDeleteTelephone.setLong(1, dbAttachmentIdList.get(i));
                    pSDeleteTelephone.executeUpdate();


                    log.debug("SELECT attachment.name FROM contacts.attachment " +
                            "WHERE attachment.id = " + dbAttachmentIdList.get(i));
                }
            } else {
                for (int i = 0; i < dbAttachmentIdList.size(); i++) {
                    log.debug("check attachment with id =" + dbAttachmentIdList.get(i));
                    boolean[] isSimilar;

                    if (dbAttachmentIdList.size() >= attachments.size()) {
                        isSimilar = new boolean[dbAttachmentIdList.size()];
                        log.debug("isSimilar array =" + isSimilar.toString());
                        log.debug("isSimilar length equals dbAttachmentIdList.size() =" + dbAttachmentIdList.size());
                        log.debug("isSimilar length =" + isSimilar.length);
                    } else {
                        isSimilar = new boolean[attachments.size()];
                        log.debug("isSimilar array =" + isSimilar.toString());
                        log.debug("isSimilar length equals attachments.size() =" + attachments.size());
                        log.debug("isSimilar length =" + isSimilar.length);
                    }
                    int countSimilar = 0;
                    for (int j = 0; j < attachments.size(); j++) {
                        if(dbAttachmentIdList.get(i).equals(attachments.get(j).getFileId())) {
                            isSimilar[j] = true;
                        } else {
                            isSimilar[j] = false;
                        }
                    }
                    log.debug("isSimilar array =" + isSimilar.toString());
                    for (int j = 0; j < isSimilar.length; j++) {
                        if (isSimilar[j]) {
                            log.debug("attachment with id =" + dbAttachmentIdList.get(i) +
                                    " matches with same telephone.id from UI");
                            countSimilar++;
                        }
                    }
                    if (countSimilar == 0) {


                        //deleteImageByUserId from filesystem
                        String sqlSelectAttachment = "SELECT attachment.name FROM contacts.attachment " +
                                "WHERE attachment.id = ?";

                        PreparedStatement pSSelectAttachment = connection.prepareStatement(sqlSelectAttachment);
                        pSSelectAttachment.setLong(1, dbAttachmentIdList.get(i));
                        ResultSet rSSelectAttachment = pSSelectAttachment.executeQuery();
                        String fileName = null;

                        while (rSSelectAttachment.next()) {
                            fileName = rSSelectAttachment.getString(1);
                            log.debug("file name from db =  " + fileName);
                        }

                        String fullPathtoFile = fullPath + File.separator + fileName;
                        File file = new File(fullPathtoFile);
                        boolean isDeleted = file.delete();
                        log.debug("file with path = " + fullPathtoFile + " , is deleted = " + isDeleted);


                        //deleteImageByUserId from  database
                        log.debug("deleteImageByUserId attachment with id=" + dbAttachmentIdList.get(i) +
                                ", where contact id=" + contactId + ".");
                        String sqlDeleteAttachment = "DELETE FROM contacts.attachment WHERE attachment.id =?";
                        PreparedStatement pSDeleteAttachment = connection.prepareStatement(sqlDeleteAttachment);
                        pSDeleteAttachment.setLong(1, dbAttachmentIdList.get(i));
                        pSDeleteAttachment.executeUpdate();


                    }
                }
            }

            for (int i = 0; i < attachments.size(); i++) {
                //if new attachment
                log.debug("attachment #" + i);
                Attachment attachment = attachments.get(i);
                log.debug(attachment.toString());

                if (attachment.getFileId() == null) {
                    log.debug("new Attachment logic, add into db");

                    String sqlCreateAttachment = "INSERT INTO contacts.attachment (attachment.contact_id, " +
                            "attachment.name, attachment.uploaddate, attachment.comment) " +
                            "VALUES (?, ?, ?, ?)";
                    PreparedStatement pSCreateAttachment = null;
                    try {
                        pSCreateAttachment = connection.prepareStatement(sqlCreateAttachment);
                    } catch (SQLException e) {
                        log.error(e.getMessage());
                    }
                    pSCreateAttachment.setLong(1, contactId);
                    pSCreateAttachment.setString(2, attachment.getFileName());
                    Date date = null;

                    if (attachment.getFileUpload() != null) {
                        date = new Date(attachment.getFileUpload().getTime());
                        pSCreateAttachment.setDate(3, date);
                    } else {
                        pSCreateAttachment.setDate(3, null);
                    }

                    pSCreateAttachment.setString(4, attachment.getFileComment());
                    pSCreateAttachment.executeUpdate();
                    log.debug("new attachment successfully added");

                } else {
                    log.debug("old attachment logic, updateImageByUserId attachment in db");

                    String sqlUpdateAttachment = "UPDATE contacts.attachment " +
                            "SET attachment.name = ?, uploaddate = ?, attachment.comment = ? " +
                            "WHERE (attachment.id = ?)";

                    PreparedStatement pSUpdateAttachment = connection.prepareStatement(sqlUpdateAttachment);
                    pSUpdateAttachment.setString(1, attachment.getFileName());
                    Date sqlDate = null;
                    if (attachment.getFileUpload() != null) {
                        sqlDate = new Date(attachment.getFileUpload().getTime());
                        pSUpdateAttachment.setDate(2, sqlDate);
                    } else {
                        pSUpdateAttachment.setDate(2, sqlDate);
                    }
                    pSUpdateAttachment.setString(3, attachment.getFileComment());
                    pSUpdateAttachment.setLong(4, attachment.getFileId());
                    pSUpdateAttachment.executeUpdate();
                    log.debug("old Attachment logic, updateImageByUserId attachment succeeded");
                }
            }


            /**
             * updateByUserId telephones in db
             * */

            List<Telephone> telephones = contact.getTelephones();
            log.debug(telephones + " telephones List");

            //deleteImageByUserId telephone from db, which doesn't exist in request
            String sqlGetTelephonesIdByContactId = "SELECT telephone.id FROM contacts.telephone " +
                    "WHERE telephone.contact_id = ? " +
                    "ORDER BY telephone.id ASC";
            PreparedStatement pSGetTelephonesIdByContactId =
                    connection.prepareStatement(sqlGetTelephonesIdByContactId);
            pSGetTelephonesIdByContactId.setLong(1, contactId);
            ResultSet rSGetTelephonesIdByContactId = null;

            try {
                rSGetTelephonesIdByContactId = pSGetTelephonesIdByContactId.executeQuery();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }

            List<Long> dbTelephoneIdList = new ArrayList<>();
            while (rSGetTelephonesIdByContactId.next()) {
                Long telephoneId = rSGetTelephonesIdByContactId.getLong(1);
                dbTelephoneIdList.add(telephoneId);
                log.debug(telephoneId + " id telephone exists in db");
            }
            rSGetTelephonesIdByContactId.close();
            pSGetTelephonesIdByContactId.close();

            if (dbTelephoneIdList.isEmpty() || telephones.isEmpty()) {
                for (int i = 0; i < dbTelephoneIdList.size(); i++) {
                    log.debug("deleteImageByUserId telephone with id=" + dbTelephoneIdList.get(i) +
                            ", where contact id=" + contactId + ".");
                    String sqlDeleteTelephone = "DELETE FROM contacts.telephone WHERE telephone.id =?";
                    PreparedStatement pSDeleteTelephone = connection.prepareStatement(sqlDeleteTelephone);
                    pSDeleteTelephone.setLong(1, dbTelephoneIdList.get(i));
                    pSDeleteTelephone.executeUpdate();
                }
            } else {
                for (int i = 0; i < dbTelephoneIdList.size(); i++) {
                    log.debug("check telephone with id =" + dbTelephoneIdList.get(i));
                    boolean[] isSimilar;

                    if (dbTelephoneIdList.size() >= telephones.size()) {
                        isSimilar = new boolean[dbTelephoneIdList.size()];
                        log.debug("isSimilar array =" + isSimilar.toString());
                        log.debug("isSimilar length equals dbTelephoneIdList.size() =" + dbTelephoneIdList.size());
                        log.debug("isSimilar length =" + isSimilar.length);
                    } else {
                        isSimilar = new boolean[telephones.size()];
                        log.debug("isSimilar array =" + isSimilar.toString());
                        log.debug("isSimilar length equals telephones.size() =" + telephones.size());
                        log.debug("isSimilar length =" + isSimilar.length);
                    }
                    int countSimilar = 0;
                    for (int j = 0; j < telephones.size(); j++) {
                        if(dbTelephoneIdList.get(i).equals(telephones.get(j).getId())) {
                            isSimilar[j] = true;
                        } else {
                            isSimilar[j] = false;
                        }
                    }
                    log.debug("isSimilar array =" + isSimilar.toString());
                    for (int j = 0; j < isSimilar.length; j++) {
                        if (isSimilar[j]) {
                            log.debug("telephone with id =" + dbTelephoneIdList.get(i) +
                                    " matches with same telephone.id from UI");
                            countSimilar++;
                        }
                    }
                    if (countSimilar == 0) {
                        log.debug("deleteImageByUserId telephone with id=" + dbTelephoneIdList.get(i) +
                                ", where contact id=" + contactId + ".");
                        String sqlDeleteTelephone = "DELETE FROM contacts.telephone WHERE telephone.id =?";
                        PreparedStatement pSDeleteTelephone = connection.prepareStatement(sqlDeleteTelephone);
                        pSDeleteTelephone.setLong(1, dbTelephoneIdList.get(i));
                        pSDeleteTelephone.executeUpdate();
                    }
                }
            }

            for (int i = 0; i < telephones.size(); i++) {
                //if new telephone
                log.debug("telephone #" + i);
                Telephone telephone = telephones.get(i);
                log.debug(telephone.toString());
                if (telephone.getId() == null) {
                    log.debug("new Telephone logic, add into db");
                    String sqlCreateTelephone = "INSERT INTO contacts.telephone (telephone.contact_id, " +
                            "telephone.country, operator, telephone.number, telephone.type, comment) " +
                            "VALUES (?,?,?,?,?,?)";
                    PreparedStatement pSCreateTelephone = null;
                    try {
                        pSCreateTelephone = connection.prepareStatement(sqlCreateTelephone);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    pSCreateTelephone.setLong(1, contactId);
                    pSCreateTelephone.setInt(2, telephone.getCountry());
                    pSCreateTelephone.setInt(3, telephone.getOperator());
                    pSCreateTelephone.setInt(4, telephone.getNumber());
                    pSCreateTelephone.setString(5, telephone.getPhoneType().getValue());
                    pSCreateTelephone.setString(6, telephone.getComment());
                    pSCreateTelephone.executeUpdate();
                    log.debug("new Telephone successfully added");
                } else {
                    log.debug("old Telephone logic, updateImageByUserId telephone in db");
                    String sqlUpdateTelephone = "UPDATE contacts.telephone " +
                            "SET telephone.country = ?, operator = ?, telephone.number = ?, " +
                            "telephone.type = ?, telephone.comment = ? " +
                            "WHERE (telephone.id = ?)";
                    PreparedStatement pSUpdateTelephone = connection.prepareStatement(sqlUpdateTelephone);
                    pSUpdateTelephone.setInt(1, telephone.getCountry());
                    pSUpdateTelephone.setInt(2, telephone.getOperator());
                    pSUpdateTelephone.setInt(3, telephone.getNumber());
                    pSUpdateTelephone.setString(4, telephone.getPhoneType().getValue());
                    pSUpdateTelephone.setString(5, telephone.getComment());
                    pSUpdateTelephone.setLong(6, telephone.getId());
                    pSUpdateTelephone.executeUpdate();
                    log.debug("old Telephone logic, updateImageByUserId telephone succeeded");
                }
            }

            //updateImageByUserId address ref to contact id
            PreparedStatement psUpdateAddress = connection.prepareStatement(sqlUpdateAddress);

            psUpdateAddress.setString(1, contact.getAddress().getCountry());
            psUpdateAddress.setString(2, contact.getAddress().getCity());
            psUpdateAddress.setString(3, contact.getAddress().getStreet());
            psUpdateAddress.setString(4, contact.getAddress().getBuilding());
            psUpdateAddress.setString(5, contact.getAddress().getApartment());
            psUpdateAddress.setString(6, contact.getAddress().getIndex());
            psUpdateAddress.setLong(7, contactId);
            psUpdateAddress.executeUpdate();

            //updateImageByUserId contact ref to contact id
            PreparedStatement psUpdateContact = connection.prepareStatement(sqlUpdateContact);
            psUpdateContact.setString(1, contact.getFirstName());
            psUpdateContact.setString(2, contact.getLastName());
            psUpdateContact.setString(3, contact.getPatronymic());

            Date birthdaySql = null;
            java.util.Date date = contact.getBirthday();

            if (contact.getBirthday() != null) {
                birthdaySql = new Date(contact.getBirthday().getTime());
            }

            psUpdateContact.setDate(4, birthdaySql);
            psUpdateContact.setString(5, contact.getSex().getSexStr());
            psUpdateContact.setString(6, contact.getCitizenship());
            psUpdateContact.setString(7, contact.getMaritalStatus());
            psUpdateContact.setString(8, contact.getWebsite());
            psUpdateContact.setString(9, contact.getEmail());
            psUpdateContact.setString(10, contact.getWorkplace());
            psUpdateContact.setLong(11, contactId);
            psUpdateContact.executeUpdate();

        } else {
            throw new ResourceException("Resource was not found");
        }
    }

    @Override
    public void deleteContact(Connection connection, Long contactId, String realPath)
            throws ResourceException, SQLException {

        String sqlFindUserById = "SELECT id FROM contacts.contact WHERE id=?";

        String sqlDeleteTelephone = "DELETE FROM contacts.telephone " +
                "WHERE contact_id =?";

        String sqlDeleteAddress = "DELETE FROM contacts.address " +
                "WHERE contact_id =?";

        String sqlDeleteContact = "DELETE FROM contacts.contact WHERE id = ?";

        String sqlDeleteAttachment = "DELETE FROM contacts.attachment " +
                "WHERE contact_id = ?";

        connection = ConnectionService.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sqlFindUserById);
        pstmt.setLong(1, contactId);

        ResultSet resultSet;
        boolean isExist = false;
        try {
            resultSet = pstmt.executeQuery();
            log.debug(resultSet);

            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                if (contactId.equals(id)) {
                    isExist = true;
                    log.debug(isExist);

                } else {
                    log.debug("Requested resource wasn't found");
                    throw new ResourceException("Requested resource wasn't found");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (isExist) {

            ImageServiceClass imageServiceClass = new ImageServiceClass();
            imageServiceClass.delete(contactId);

            log.debug("Select attachment from db start");

            String sqlSelectAttachment = "SELECT attachment.name FROM contacts.attachment " +
                    "WHERE attachment.contact_id = ?";

            PreparedStatement pSSelectAttachment = connection.prepareStatement(sqlSelectAttachment);
            pSSelectAttachment.setLong(1, contactId);
            ResultSet rSSelectAttachment = pSSelectAttachment.executeQuery();

            while (rSSelectAttachment.next()) {

                log.debug("deleteImageByUserId files from filesystem start");

                String fileName = rSSelectAttachment.getString(1);

                AttachmentServiceClassFS attachmentServiceClassFS = new AttachmentServiceClassFS();
                attachmentServiceClassFS.delete(fileName);
            }

            rSSelectAttachment.close();
            pSSelectAttachment.close();
            log.debug("Select attachment ane from db end");

            //deleteImageByUserId files from db
            log.debug("Delete attachment from db start");
            PreparedStatement pSDeleteAttachment = connection.prepareStatement(sqlDeleteAttachment);
            pSDeleteAttachment.setLong(1, contactId);
            pSDeleteAttachment.executeUpdate();
            log.debug("Delete attachment from db end");

            PreparedStatement pSDeleteTelephone = connection.prepareStatement(sqlDeleteTelephone);
            pSDeleteTelephone.setLong(1, contactId);
            pSDeleteTelephone.executeUpdate();

            PreparedStatement pstm = connection.prepareStatement(sqlDeleteAddress);
            pstm.setLong(1, contactId);
            pstm.executeUpdate();


            PreparedStatement pst = connection.prepareStatement(sqlDeleteContact);
            pst.setLong(1, contactId);
            pst.executeUpdate();

        } else {
            log.debug("Requested resource wasn't found");
            throw new ResourceException("Requested resource wasn't found");
        }
    }

    public Integer getContactsCount(Connection connection) throws SQLException {

        log.debug("Start");

        String sql = "SELECT COUNT(contact.id) FROM contacts.contact";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        Integer contactsCount = null;

        while (resultSet.next()) {
            contactsCount = resultSet.getInt(1);
        }

        log.debug("End");

        return contactsCount;
    }
}
