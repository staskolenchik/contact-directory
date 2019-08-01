package by.kolenchik.core.dao.address;

import by.kolenchik.core.domain.Address;
import by.kolenchik.core.dto.address.AddressSearchRequestDTO;
import by.kolenchik.core.dto.address.GeneralAddressDTO;

import java.sql.SQLException;
import java.util.List;

public interface AddressDAO {

    void createAddressByUserId(Address address, Long id) throws SQLException;

    List<Address> findAllAddresses(AddressSearchRequestDTO addressSearchRequestDTO) throws SQLException;

    List<Long> findAllUserIdByCountry(String country) throws SQLException;

    List<Long> findAllUserIdByCity(String city) throws SQLException ;

    List<Long> findAllUserIdByStreet(String street) throws SQLException;

    List<Long> findAllUserIdByBuilding(String building) throws SQLException;

    List<Long> findAllUserIdByApartment(String apartment) throws SQLException;

    List<Long> findAllUserIdByIndex(String index) throws SQLException;

    Address findAddressByUserId(Long id) throws SQLException;

    GeneralAddressDTO findGeneralAddressByUserId(Long id) throws SQLException;
}
