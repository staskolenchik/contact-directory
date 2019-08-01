package by.kolenchik.core.dao.address;

import by.kolenchik.core.domain.Address;
import by.kolenchik.core.dto.address.AddressSearchRequestDTO;
import by.kolenchik.core.dto.address.GeneralAddressDTO;
import by.kolenchik.core.jdbc.ConnectionService;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAOImpl implements AddressDAO {

    private static Logger log = Logger.getLogger(AddressDAOImpl.class);

    private Connection connection = ConnectionService.getConnection();

    @Override
    public void createAddressByUserId(Address address, Long id) throws SQLException {

        log.debug("Start");

        String sql = "INSERT INTO contacts.address(address.contact_id, address.country, city, " +
                " street,building,apartment, address.index) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pSCreateAddress = connection.prepareStatement(sql);
        pSCreateAddress.setLong(1, id);
        pSCreateAddress.setString(2, address.getCountry());
        pSCreateAddress.setString(3, address.getCity());
        pSCreateAddress.setString(4, address.getStreet());
        pSCreateAddress.setString(5, address.getBuilding());
        pSCreateAddress.setString(6, address.getApartment());
        pSCreateAddress.setString(7, address.getIndex());
        pSCreateAddress.executeUpdate();

        log.debug("End");
    }

    @Override
    public List<Address> findAllAddresses(AddressSearchRequestDTO addressSearchRequestDTO) throws SQLException {

        log.debug("finding addresses with next arguments =" + addressSearchRequestDTO);

        String sqlFindAllAddressess = "SELECT address.id, address.contact_id, address.country, " +
                " city, street, building, apartment, address.index FROM contacts.address " +
                " WHERE address.country = ?" +
                " AND city = ? " +
                " AND street = ? " +
                " AND building = ? " +
                " AND apartment = ? " +
                " AND address.index = ? " ;

        List<Address> addressList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAllAddressess);
        log.debug(preparedStatement);
        preparedStatement.setString(1, addressSearchRequestDTO.getCountry());
        preparedStatement.setString(2, addressSearchRequestDTO.getCity());
        preparedStatement.setString(3, addressSearchRequestDTO.getStreet());
        preparedStatement.setString(4, addressSearchRequestDTO.getBuilding());
        preparedStatement.setString(5, addressSearchRequestDTO.getApartment());
        preparedStatement.setString(6, addressSearchRequestDTO.getIndex());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Long id = resultSet.getLong(1);
            Long contactId = resultSet.getLong(2);
            String country = resultSet.getString(3);
            String city = resultSet.getString(4);
            String street = resultSet.getString(5);
            String building = resultSet.getString(6);
            String apartment = resultSet.getString(7);
            String index = resultSet.getString(8);
            Address fullAddress = new Address(id, contactId, country, city, street, building, apartment, index);
            addressList.add(fullAddress);

        }

        log.debug("Amount of addresses found =" + addressList.size());
        return addressList;
    }

    public List<Long> findAllUserIdByCountry(String country) throws SQLException {

        log.debug(country);

        String sql = "SELECT address.contact_id FROM contacts.address " +
                " WHERE address.country = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, country);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("userId = "+ userId);
        }

        log.debug("End");
        return userIdList;
    }


    public List<Long> findAllUserIdByCity(String city) throws SQLException {

        log.debug(city);

        String sql = "SELECT address.contact_id FROM contacts.address " +
                " WHERE address.city = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, city);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("userId = "+ userId);
        }
        log.debug("End");
        return userIdList;
    }

    public List<Long> findAllUserIdByStreet(String street) throws SQLException {

        log.debug(street);

        String sql = "SELECT address.contact_id FROM contacts.address " +
                " WHERE address.street = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, street);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("userId = "+ userId);
        }
        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByBuilding(String building) throws SQLException {

        log.debug(building);

        String sql = "SELECT address.contact_id FROM contacts.address " +
                " WHERE address.building = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, building);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("userId = "+ userId);
        }
        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByApartment(String apartment) throws SQLException {

        log.debug(apartment);

        String sql = "SELECT address.contact_id FROM contacts.address " +
                " WHERE address.apartment = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, apartment);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("userId = "+ userId);
        }
        log.debug("End");
        return userIdList;
    }

    @Override
    public List<Long> findAllUserIdByIndex(String index) throws SQLException {

        log.debug(index);

        String sql = "SELECT address.contact_id FROM contacts.address " +
                " WHERE address.index = ?";

        List<Long> userIdList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, index);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Long userId = resultSet.getLong(1);
            userIdList.add(userId);
            log.trace("userId = "+ userId);
        }
        log.debug("End");
        return userIdList;
    }

    @Override
    public Address findAddressByUserId(Long id) throws SQLException {

        log.debug("Start");

        String sql = "SELECT address.id, address.contact_id, address.country, " +
                " city, street, building, apartment, address.index FROM contacts.address " +
                " WHERE address.contact_id = ?" ;

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Address address = new Address();

        while (resultSet.next()) {
            Long addressId = resultSet.getLong(1);
            Long userId = resultSet.getLong(2);
            String country = resultSet.getString(3);
            String city = resultSet.getString(4);
            String street = resultSet.getString(5);
            String building = resultSet.getString(6);
            String apartment = resultSet.getString(7);
            String index = resultSet.getString(8);

            address = new Address(
                    addressId, userId, country, city, street, building, apartment, index
            );
            log.trace(address);
        }
        log.debug("End");
        return address;
    }

    @Override
    public GeneralAddressDTO findGeneralAddressByUserId(Long id) throws SQLException {

        log.debug("Start");

        String sql = "SELECT address.country, city, street, " +
                " building, apartment, address.index FROM contacts.address " +
                " WHERE address.contact_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        GeneralAddressDTO generalAddressDTO = new GeneralAddressDTO();

        while (resultSet.next()) {
            String country = resultSet.getString(1);
            String city = resultSet.getString(2);
            String street = resultSet.getString(3);
            String building = resultSet.getString(4);
            String apartment = resultSet.getString(5);
            String index = resultSet.getString(6);
            generalAddressDTO =
                    new GeneralAddressDTO(country, city, street, building, apartment, index);
            log.trace(generalAddressDTO);
        }
        log.debug("End");


        return generalAddressDTO;
    }
}
