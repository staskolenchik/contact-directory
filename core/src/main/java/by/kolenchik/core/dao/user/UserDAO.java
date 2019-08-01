package by.kolenchik.core.dao.user;

import by.kolenchik.core.domain.User;
import by.kolenchik.core.dto.user.FindAllUsersDTO;
import by.kolenchik.core.dto.user.GeneralUserDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    void create(User user) throws SQLException;

    User getUserById(Long id) throws SQLException;

    Long getUserIdByFirstnameAndLastname(String firstname, String lastname) throws SQLException;

    List<User> findAllUsers(FindAllUsersDTO findAllUsersDTO);

    List<Long> findAllUserIdByFirstname(String firstname) throws SQLException;

    List<Long> findAllUserIdByLastname(String lastname) throws SQLException;

    List<Long> findAllUserIdByPatronymic(String patronymic) throws SQLException;

    List<Long> findAllUserIdByBirthdayLessDay(Date date) throws SQLException;

    List<Long> findAllUserIdByBirthdayGreaterDay(Date date) throws SQLException;

    List<Long> findAllUserIdBySex(String sex) throws SQLException;

    List<Long> findAllUserIdByMaritalStatus(String maritalStatus) throws SQLException;

    List<Long> findAllUserIdByCitizenship(String citizenship) throws SQLException;

    GeneralUserDTO findGeneralUserById(Long id) throws SQLException;

    String getEmailByUserId(Long id) throws SQLException;

    List<Long> getAllUserIdByDate(java.util.Date date) throws SQLException;

    Long countUserIdByFirstnameAndLastname(String firstname, String lastname) throws SQLException;

    List<GeneralUserDTO> getAllGeneralUserDTOs(Integer pageNumber, Integer pageSize) throws SQLException;
}
