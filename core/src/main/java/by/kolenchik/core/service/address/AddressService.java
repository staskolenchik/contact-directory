package by.kolenchik.core.service.address;

import by.kolenchik.core.domain.Address;
import by.kolenchik.core.dto.address.AddressSearchRequestDTO;
import by.kolenchik.core.dto.address.GeneralAddressDTO;

import java.util.List;

public interface AddressService {

    void createAddressByUserId(Address address, Long id);

    List<Address> getAllAddresses();

    Address getAddressByUserId(Long id);

    List<Long> findAllUsersIdByAddressSearch(AddressSearchRequestDTO addressSearchRequestDTO);

    GeneralAddressDTO findGeneralAddressByUserId(Long id);
}
