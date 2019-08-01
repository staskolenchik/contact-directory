package by.kolenchik.core.dao;

import by.kolenchik.core.domain.Telephone;
import by.kolenchik.core.jdbc.ConnectionService;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelephoneDAOImpl implements TelephoneDAO {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private Connection connection = ConnectionService.getConnection();

    @Override
    public void createTelephoneByUserId(Telephone telephone, Long id) throws SQLException {

        log.debug("Start");

        String sql = "INSERT INTO contacts.telephone (telephone.contact_id, telephone.country, " +
                " operator, telephone.number, telephone.type, telephone.comment) " +
                " VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement pSCreateTelephone = connection.prepareStatement(sql);

        pSCreateTelephone.setLong(1, id);
        pSCreateTelephone.setInt(2, telephone.getCountry());
        pSCreateTelephone.setInt(3, telephone.getOperator());
        pSCreateTelephone.setInt(4, telephone.getNumber());
        pSCreateTelephone.setString(5, telephone.getPhoneType().getValue());
        pSCreateTelephone.setString(6, telephone.getComment());

        pSCreateTelephone.executeUpdate();
        connection.commit();
        log.debug("End");
    }
}
