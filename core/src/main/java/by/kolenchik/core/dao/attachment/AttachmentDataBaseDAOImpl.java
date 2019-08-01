package by.kolenchik.core.dao.attachment;

import by.kolenchik.core.domain.Attachment;
import by.kolenchik.core.jdbc.ConnectionService;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttachmentDataBaseDAOImpl implements AttachmentDataBaseDAO{

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private Connection connection = ConnectionService.getConnection();

    public void create(Attachment attachment, Long id) throws SQLException {

        log.debug("Start");

        String sql = "INSERT INTO contacts.attachment (attachment.contact_id, " +
                " attachment.name, uploaddate, attachment.comment) " +
                " VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, attachment.getFileName());

        Date date = new Date(new java.util.Date().getTime());

        preparedStatement.setDate(3, date);
        preparedStatement.setString(4, attachment.getFileComment());

        preparedStatement.executeUpdate();

        log.debug("End");
    }
}
