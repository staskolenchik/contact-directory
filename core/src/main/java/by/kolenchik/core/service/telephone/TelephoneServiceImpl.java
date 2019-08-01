package by.kolenchik.core.service.telephone;

import by.kolenchik.core.dao.TelephoneDAO;
import by.kolenchik.core.dao.TelephoneDAOImpl;
import by.kolenchik.core.domain.Telephone;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.log4j.Logger;

import java.sql.SQLException;

public class TelephoneServiceImpl implements TelephoneService {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    @Override
    public void create(Telephone telephone, Long id) {

        log.debug("Start");

        TelephoneDAO telephoneDAO = new TelephoneDAOImpl();
        try {
            telephoneDAO.createTelephoneByUserId(telephone, id);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        log.debug("End");
    }
}
