package by.kolenchik.core.dao.user;

import by.kolenchik.core.domain.Sex;
import by.kolenchik.core.domain.User;
import by.kolenchik.core.dto.user.FindAllUsersDTO;
import by.kolenchik.core.dto.user.GeneralUserDTO;
import by.kolenchik.core.jdbc.ConnectionService;
import by.kolenchik.core.service.user.UserServiceImpl;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static Logger log = Logger.getLogger(UserServiceImpl.class);

    private Connection connection = ConnectionService.getConnection();

    @Override
    public void create(User user) throws SQLException {

        log.debug("Start");

        String sql = "INSERT INTO contacts.contact(firstname, lastname, " +
                " patronymic, birthday, sex, citizenship, marital_status, website, email, workplace) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pstmCreateContact = connection.prepareStatement(sql);
        pstmCreateContact.setString(1, user.getFirstName());
        pstmCreateContact.setString(2, user.getLastName());
        pstmCreateContact.setString(3, user.getPatronymic());
        if (user.getBirthday() != null) {
            Date birthdaySql = new Date(user.getBirthday().getTime());
            pstmCreateContact.setDate(4, birthdaySql);
        } else {
            pstmCreateContact.setDate(4, null);
        }
        pstmCreateContact.setString(5, user.getSex().getSexStr());
        pstmCreateContact.setString(6, user.getCitizenship());
        pstmCreateContact.setString(7, user.getMaritalStatus());
        pstmCreateContact.setString(8, user.getWebsite());
        pstmCreateContact.setString(9, user.getEmail());
        pstmCreateContact.setString(10, user.getWorkplace());

        pstmCreateContact.executeUpdate();

        log.debug("created user");
    }

    @Override
    public User getUserById(Long id) throws SQLException {

        log.debug("Start");

        String sql = "SELECT contact.id, firstname, lastname, patronymic, " +
                " birthday, sex, citizenship, marital_status, website, contact.email, workplace " +
                " FROM contacts.contact " +
                " WHERE contact.id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        User user = null;

        while (resultSet.next()) {
            log.debug("User in db");
            long contactId = resultSet.getLong(1);
            String firstname = resultSet.getString(2);
            String lastname = resultSet.getString(3);
            String patronymic = resultSet.getString(4);

            Date sqlbirthday = resultSet.getDate(5);
            java.util.Date birthday = null;

            if (sqlbirthday != null) {
                birthday = new java.util.Date(sqlbirthday.getTime());
            }

            String sqlSex = resultSet.getString(6);
            Sex sex = null;

            switch (sqlSex) {
                case "MALE" :
                    sex = Sex.MALE;
                    break;
                case "FEMALE" :
                    sex = Sex.FEMALE;
                    break;
            }

            String citizenship = resultSet.getString(7);
            String maritalStatus = resultSet.getString(8);
            String website = resultSet.getString(9);
            String email = resultSet.getString(10);
            String workplace = resultSet.getString(11);

            user = new User(contactId, firstname, lastname, patronymic, birthday,
                    sex, citizenship, maritalStatus, website, email, workplace);
        }

        log.trace(user);
        return user;
    }

    @Override
    public Long getUserIdByFirstnameAndLastname(String firstname, String lastname) throws SQLException {

        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE firstname=? AND lastname=?";

        PreparedStatement pSGetContactId = connection.prepareStatement(sql);
        pSGetContactId.setString(1, firstname);
        pSGetContactId.setString(2, lastname);
        ResultSet rsGetContactId = pSGetContactId.executeQuery();

        Long contactId = null;

        while (rsGetContactId.next()) {
            contactId = rsGetContactId.getLong(1);
            log.trace(contactId);
        }

        log.debug("End");

        return contactId;
    }

    @Override
    public List<User> findAllUsers(FindAllUsersDTO findAllUsersDTO) {


        return null;
    }

    @Override
    public List<Long> findAllUserIdByFirstname(String firstname) throws SQLException {

        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE firstname = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, firstname);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId =" + userId);
        }

        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByLastname(String lastname) throws SQLException {
        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE lastname = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, lastname);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId =" + userId);
        }

        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByPatronymic(String patronymic) throws SQLException {
        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE patronymic = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, patronymic);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId =" + userId);
        }

        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByBirthdayLessDay(Date date) throws SQLException {
        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE birthday <= ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId =" + userId);
        }

        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByBirthdayGreaterDay(Date date) throws SQLException {

        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE birthday >= ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId =" + userId);
        }

        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdBySex(String sex) throws SQLException {
        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE sex = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, sex);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId =" + userId);
        }

        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByMaritalStatus(String maritalStatus) throws SQLException {

        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE birthday = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, maritalStatus);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId =" + userId);
        }

        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByCitizenship(String citizenship) throws SQLException {
        log.debug("Start");

        String sql = "SELECT contact.id FROM contacts.contact " +
                " WHERE citizenship = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, citizenship);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId =" + userId);
        }

        log.debug("End");
        return userIdList;
    }

    @Override
    public GeneralUserDTO findGeneralUserById(Long id) throws SQLException {

        log.debug("Start");

        String sql = "SELECT firstname, lastname, patronymic, birthday, workplace " +
                " FROM contacts.contact WHERE contact.id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        GeneralUserDTO user = new GeneralUserDTO();

        while (resultSet.next()) {
            String firstname = resultSet.getString(1);
            String lastname = resultSet.getString(2);
            String patronymic = resultSet.getString(3);
            Date sqlDate = resultSet.getDate(4);
            java.util.Date date = null;
            if (sqlDate != null) {
                date = new Date(sqlDate.getTime());
            }
            String workplace = resultSet.getString(5);
            user = new GeneralUserDTO(id, firstname, lastname, patronymic, date, workplace);
            log.trace(user);
        }

        log.debug("End");
        return user;
    }

    @Override
    public String getEmailByUserId(Long id) throws SQLException {

        log.debug("Start");

        String sql = "SELECT contact.email FROM contacts.contact " +
                " WHERE contact.id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        String email = null;
        while (resultSet.next()) {
            email = resultSet.getString(1);
        }
        log.trace(email);
        log.debug("End ");
        return email;
    }

    @Override
    public List<Long> getAllUserIdByDate(java.util.Date date) throws SQLException {

        log.debug("Start");

        String sql = "SELECT contact.id from contacts.contact " +
                " WHERE birthday = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        Date sqlDate = new Date(date.getTime());
        preparedStatement.setDate(1, sqlDate);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Long> userIdList = new ArrayList<>();

        while (resultSet.next()) {
            long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("UserId = " + userId);
        }

        log.debug("end");
        return userIdList;
    }

    @Override
    public Long countUserIdByFirstnameAndLastname(String firstname, String lastname) throws SQLException {

        log.debug("Start");

        String sql = "SELECT COUNT(contact.id) FROM contacts.contact " +
                " WHERE firstname = ? " +
                " AND lastname = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, firstname);
        preparedStatement.setString(2, lastname);

        ResultSet resultSet = preparedStatement.executeQuery();
        Long count = null;

        while (resultSet.next()) {
            count = resultSet.getLong(1);
        }

        log.debug("End");

        return count;
    }

    @Override
    public List<GeneralUserDTO> getAllGeneralUserDTOs(Integer pageNumber, Integer pageSize) throws SQLException {

        log.debug("Start");

        String sql = "SELECT contact.id, firstname, lastname, patronymic, " +
                " birthday, workplace, " +
                " FROM contacts.contact " +
                " ORDER BY contact.id DESC " +
                " LIMIT ?, ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, pageNumber - 1);
        preparedStatement.setInt(2, pageSize);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<GeneralUserDTO> generalUserDTOList = new ArrayList<>();

        while (resultSet.next()) {
            Long id = resultSet.getLong(1);
            String firstname = resultSet.getString(2);
            String lastname = resultSet.getString(3);
            String patronymic = resultSet.getString(4);
            Date birthdaySql = resultSet.getDate(5);
            java.util.Date birthday = null;
            if (birthdaySql != null) {
                birthday = new java.util.Date(birthdaySql.getTime());
            }
            String workplace = resultSet.getString(6);
            GeneralUserDTO generalUserDTO =
                    new GeneralUserDTO(id, firstname, lastname, patronymic, birthday, workplace);
            generalUserDTOList.add(generalUserDTO);
        }

        log.debug("End");

        return generalUserDTOList;
    }
}
