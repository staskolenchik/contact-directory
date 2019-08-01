package by.kolenchik.core.service.user;

import by.kolenchik.core.dao.user.UserDAO;
import by.kolenchik.core.dao.user.UserDAOImpl;
import by.kolenchik.core.domain.User;
import by.kolenchik.core.dto.user.GeneralUserDTO;
import by.kolenchik.core.dto.user.GetEmailDTO;
import by.kolenchik.core.dto.user.UserSearchRequestDTO;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.service.utils.ServiceUtils;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static Logger log = Logger.getLogger(UserServiceImpl.class);

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public void create(User user) throws ResourceException{
        try {
            userDAO.create(user);
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new ResourceException("Couldn't save user");
        }
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public Long getUserIdByFirstnameAndLastname(String firstname, String lastname) throws ResourceException {
        Long id;
        try {
            id = userDAO.getUserIdByFirstnameAndLastname(firstname, lastname);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ResourceException("Resource exception");
        }
        return id;
    }

    @Override
    public List<Long> findAllUsersIdByUserSearch(UserSearchRequestDTO userSearchRequestDTO) {

        log.debug("Start");

        List<Long> userIdListMatchesByUser = null;

        List<List<Long>> allСoincidences = new ArrayList<>();

        try {
            if (userSearchRequestDTO.getFirstname() != null) {
                List<Long> allUserIdByFirstname = userDAO.findAllUserIdByFirstname(userSearchRequestDTO.getFirstname());
                allСoincidences.add(allUserIdByFirstname);
            }
            if (userSearchRequestDTO.getLastname() != null) {
                List<Long> allUserIdByLastname = userDAO.findAllUserIdByLastname(userSearchRequestDTO.getLastname());
                allСoincidences.add(allUserIdByLastname);
            }
            if (userSearchRequestDTO.getPatronymic() != null) {
                List<Long> allUserIdByPatronymic = userDAO.findAllUserIdByPatronymic(userSearchRequestDTO.getPatronymic());
                allСoincidences.add(allUserIdByPatronymic);
            }
            if (userSearchRequestDTO.getBirthday() != null) {
                //get string "lt+MM/dd/yyyy
                String[] split = userSearchRequestDTO.getBirthday().split("[+]");
                if (split[0].equals("lt")) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        Date date = simpleDateFormat.parse(split[1]);
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        List<Long> allUserIdByBirthdayLessDay = userDAO.findAllUserIdByBirthdayLessDay(sqlDate);
                        allСoincidences.add(allUserIdByBirthdayLessDay);
                    } catch (ParseException e) {
                        log.error(e.getMessage());
                        log.debug("Set null into date parameter");
                    }
                } else if (split[0].equals("gt")) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        Date date = simpleDateFormat.parse(split[1]);
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        List<Long> allUserIdByBirthdayGreaterDay = userDAO.findAllUserIdByBirthdayGreaterDay(sqlDate);
                        allСoincidences.add(allUserIdByBirthdayGreaterDay);
                    } catch (ParseException e) {
                        log.error(e.getMessage());
                        log.debug("Set null into date parameter");
                    }
                }
            }
            if (userSearchRequestDTO.getSex() != null) {
                List<Long> allUserIdBySex = userDAO.findAllUserIdBySex(userSearchRequestDTO.getSex());
                allСoincidences.add(allUserIdBySex);
            }
            if (userSearchRequestDTO.getMaritalStatus() != null) {
                List<Long> allUserIdByMaritalStatus = userDAO.findAllUserIdByMaritalStatus(userSearchRequestDTO.getMaritalStatus());
                allСoincidences.add(allUserIdByMaritalStatus);
            }
            if (userSearchRequestDTO.getCitizenship() != null) {
                List<Long> allUserIdByCitizenship = userDAO.findAllUserIdByCitizenship(userSearchRequestDTO.getCitizenship());
                allСoincidences.add(allUserIdByCitizenship);
            }

            ServiceUtils serviceUtils = new ServiceUtils();
            userIdListMatchesByUser = serviceUtils.findUserIdMatches(allСoincidences);

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        log.debug("End");

        return userIdListMatchesByUser;

    }

    @Override
    public GeneralUserDTO findUserUIById(Long id) {


        GeneralUserDTO generalUserDTO;

        try {
            generalUserDTO = userDAO.findGeneralUserById(id);

            return generalUserDTO;

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public GetEmailDTO getEmailByUserId(Long id) throws ResourceException {

        log.debug("Start");
        GetEmailDTO getEmailDTO;

        try {
            String emailByUserId = userDAO.getEmailByUserId(id);
            if (emailByUserId != null) {
                getEmailDTO = new GetEmailDTO(id, emailByUserId);
            } else {
                throw new ResourceException("emailByUserId is null");
            }
        } catch (SQLException|ResourceException e) {
            log.error(e.getMessage());
            throw new ResourceException("emails weren't found");
        }
        log.trace(getEmailDTO);
        return getEmailDTO;
    }

    @Override
    public List<GetEmailDTO> getEmailListByUserIdList(Long[] longArray) throws ResourceException{

        log.debug("getEmailsByUserIdArray = " + Arrays.toString(longArray));

        List<GetEmailDTO> emailDTOList = new ArrayList<>();

        for (int i = 0; i < longArray.length; i++) {
            try {
                String emailByUserId = userDAO.getEmailByUserId(longArray[i]);
                if (emailByUserId != null) {
                    GetEmailDTO getEmailDTO = new GetEmailDTO(longArray[i], emailByUserId);
                    emailDTOList.add(getEmailDTO);
                    log.trace(getEmailDTO);
                }
            } catch (SQLException e) {
                log.error(e.getMessage());
                throw new ResourceException("emails weren't found");
            }
        }

        log.debug("emailDTOList size =" + emailDTOList.size());
        return emailDTOList;
    }

    @Override
    public List<Long> getAllUserIdsByDate(Date date) {

        log.debug("Start");
        List<Long> allUserIdByBirthday = null;

        try {
            allUserIdByBirthday = userDAO.getAllUserIdByDate(date);

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        log.debug("End");
        return allUserIdByBirthday;
    }

    public Boolean isUserUnique(String firstname, String lastname) throws ResourceException{

        log.debug("Start");

        Boolean isUserUnique = false;
        try {
            Long count = userDAO.countUserIdByFirstnameAndLastname(firstname, lastname);
            if (count != null && count == 0) {
                isUserUnique = true;
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new ResourceException("No user found");
        }

        log.debug("End");

        return isUserUnique;
    }

    @Override
    public List<GeneralUserDTO> getAllGeneralUserDTOs(Integer pageNumber, Integer pageSize) throws ResourceException{

        log.debug("Start");

        List<GeneralUserDTO> generalUserDTOs;
        try {
            generalUserDTOs = userDAO.getAllGeneralUserDTOs(pageNumber, pageSize);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new ResourceException("No user found");
        }

        log.debug("End");
        return generalUserDTOs;
    }

}
