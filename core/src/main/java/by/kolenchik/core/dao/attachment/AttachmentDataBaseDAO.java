package by.kolenchik.core.dao.attachment;

import by.kolenchik.core.domain.Attachment;

import java.sql.SQLException;

public interface AttachmentDataBaseDAO {

    void create(Attachment attachment, Long id) throws SQLException;
}
