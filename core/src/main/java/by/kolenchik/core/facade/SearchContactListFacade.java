package by.kolenchik.core.facade;

import by.kolenchik.core.dto.address.AddressSearchRequestDTO;
import by.kolenchik.core.dto.address.GeneralAddressDTO;
import by.kolenchik.core.dto.contact.GeneralContactDTO;
import by.kolenchik.core.dto.contactNew.GeneralContactUIDTO;
import by.kolenchik.core.dto.user.GeneralUserDTO;
import by.kolenchik.core.dto.user.UserSearchRequestDTO;
import by.kolenchik.core.service.address.AddressService;
import by.kolenchik.core.service.address.AddressServiceImpl;
import by.kolenchik.core.service.user.UserService;
import by.kolenchik.core.service.user.UserServiceImpl;
import by.kolenchik.core.service.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchContactListFacade {

    private UserService userService = new UserServiceImpl();
    private AddressService addressService = new AddressServiceImpl();

    public GeneralContactUIDTO findAllContacts(UserSearchRequestDTO userSearchRequestDTO,
                                               AddressSearchRequestDTO addressSearchRequestDTO) {

        List<List<Long>> matchesList = new ArrayList<>();

        List<Long> userIdListByUser = null;
        List<Long> userIdListByAddress = null;

        if (userSearchRequestDTO != null) {
            userIdListByUser = userService.findAllUsersIdByUserSearch(userSearchRequestDTO);
            matchesList.add(userIdListByUser);
        }

        if (addressSearchRequestDTO != null) {
            userIdListByAddress = addressService.findAllUsersIdByAddressSearch(addressSearchRequestDTO);
            matchesList.add(userIdListByAddress);
        }

        ServiceUtils serviceUtils = new ServiceUtils();
        List<Long> userIdMatches = serviceUtils.findUserIdMatches(matchesList);

        List<GeneralContactDTO> generalContactDTOList = new ArrayList<>();

        for (Long id :
                userIdMatches) {
            GeneralUserDTO generalUserDTO = userService.findUserUIById(id);
            GeneralAddressDTO generalAddressDTO = addressService.findGeneralAddressByUserId(id);

            GeneralContactDTO generalContactDTO = new GeneralContactDTO(
                    generalUserDTO.getId(), generalUserDTO.getFirstname(), generalUserDTO.getLastname(),
                    generalUserDTO.getPatronymic(), generalUserDTO.getBirthday(), generalUserDTO.getWorkplace(),
                    generalAddressDTO
            );
            generalContactDTOList.add(generalContactDTO);
        }

        GeneralContactUIDTO generalContactUIDTO = new GeneralContactUIDTO();
        generalContactUIDTO.setContacts(generalContactDTOList);
        generalContactUIDTO.setContactsQuantity((long) generalContactDTOList.size());

        return generalContactUIDTO;
    }

}
