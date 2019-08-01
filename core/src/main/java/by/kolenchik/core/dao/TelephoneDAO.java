package by.kolenchik.core.dao;

import by.kolenchik.core.domain.Telephone;

import java.sql.SQLException;

public interface TelephoneDAO {

    void createTelephoneByUserId(Telephone telephone, Long id) throws SQLException;
}
