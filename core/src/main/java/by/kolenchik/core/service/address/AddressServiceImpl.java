package by.kolenchik.core.service.address;

import by.kolenchik.core.dao.address.AddressDAO;
import by.kolenchik.core.dao.address.AddressDAOImpl;
import by.kolenchik.core.domain.Address;
import by.kolenchik.core.dto.address.AddressSearchRequestDTO;
import by.kolenchik.core.dto.address.GeneralAddressDTO;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.service.utils.ServiceUtils;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressServiceImpl implements AddressService {

    Logger log = Logger.getLogger(AddressServiceImpl.class);

    private AddressDAO addressDAO = new AddressDAOImpl();

    @Override
    public void createAddressByUserId(Address address, Long id)  throws ResourceException {
        try {
            addressDAO.createAddressByUserId(address, id);
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new ResourceException("No address found");
        }
    }

    @Override
    public List<Address> getAllAddresses() {
        return null;
    }

    @Override
    public Address getAddressByUserId(Long id) {
        return null;
    }

    @Override
    public List<Long> findAllUsersIdByAddressSearch(AddressSearchRequestDTO addressSearchRequestDTO) {

        log.debug("Start");

        List<List<Long>> idMatchesList = new ArrayList<>();

        List<Long> userIdMatches = new ArrayList<>();

        try {

            if ((addressSearchRequestDTO.getCountry() != null)) {
                List<Long> allUserIdByCountry =
                        addressDAO.findAllUserIdByCountry(addressSearchRequestDTO.getCountry());
                idMatchesList.add(allUserIdByCountry);
            }
            if (addressSearchRequestDTO.getCity() != null) {
                List<Long> allUserIdByCity =
                        addressDAO.findAllUserIdByCity(addressSearchRequestDTO.getCity());
                idMatchesList.add(allUserIdByCity);
            }
            if (addressSearchRequestDTO.getStreet() != null) {
                List<Long> allUserIdByStreet =
                        addressDAO.findAllUserIdByStreet(addressSearchRequestDTO.getStreet());
                idMatchesList.add(allUserIdByStreet);
            }
            if (addressSearchRequestDTO.getBuilding() != null) {
                List<Long> allUserIdByBuilding =
                        addressDAO.findAllUserIdByBuilding(addressSearchRequestDTO.getBuilding());
                idMatchesList.add(allUserIdByBuilding);
            }
            if (addressSearchRequestDTO.getApartment() != null) {
                List<Long> allUserIdByApartment =
                        addressDAO.findAllUserIdByApartment(addressSearchRequestDTO.getApartment());
                idMatchesList.add(allUserIdByApartment);
            }
            if (addressSearchRequestDTO.getIndex() != null) {
                List<Long> allUserIdByIndex =
                        addressDAO.findAllUserIdByIndex(addressSearchRequestDTO.getIndex());
                idMatchesList.add(allUserIdByIndex);
            }

            ServiceUtils serviceUtils = new ServiceUtils();
            userIdMatches = serviceUtils.findUserIdMatches(idMatchesList);


        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        log.debug("End");
        return userIdMatches;
    }

    @Override
    public GeneralAddressDTO findGeneralAddressByUserId(Long id) {

        GeneralAddressDTO generalAddressDTO = null;
        try {
            generalAddressDTO = addressDAO.findGeneralAddressByUserId(id);
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        return generalAddressDTO;
    }
}
