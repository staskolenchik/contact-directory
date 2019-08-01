package by.kolenchik.core.service.user;

import by.kolenchik.core.domain.User;
import by.kolenchik.core.dto.user.GeneralUserDTO;
import by.kolenchik.core.dto.user.GetEmailDTO;
import by.kolenchik.core.dto.user.UserSearchRequestDTO;
import by.kolenchik.core.exceptions.ResourceException;

import java.util.Date;
import java.util.List;

public interface UserService {

    void create(User user) throws ResourceException;

    User getUserById(Long id);

    Long getUserIdByFirstnameAndLastname(String firstname, String lastname) throws ResourceException ;

    List<Long> findAllUsersIdByUserSearch(UserSearchRequestDTO userSearchRequestDTO);

    GeneralUserDTO findUserUIById(Long id);

    GetEmailDTO getEmailByUserId(Long id) throws ResourceException;

    List<GetEmailDTO> getEmailListByUserIdList(Long[] longArray) throws ResourceException;

    List<Long> getAllUserIdsByDate(Date date);

    Boolean isUserUnique(String firstname, String lastname);

    List<GeneralUserDTO> getAllGeneralUserDTOs(Integer pageNumber, Integer pageSize) throws ResourceException;
}
