package by.kolenchik.core.service.attachment;

import by.kolenchik.core.dao.attachment.AttachmentDataBaseDAOImpl;
import by.kolenchik.core.domain.Attachment;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.log4j.Logger;

import java.sql.SQLException;

public class AttachmentDBServiceImpl implements AttachmentDBService {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private AttachmentDataBaseDAOImpl attachmentDataBaseDAOImpl;

    public AttachmentDBServiceImpl() {
        this.attachmentDataBaseDAOImpl = new AttachmentDataBaseDAOImpl();
    }

    @Override
    public void create(Attachment attachment, Long id) throws ResourceException {

        log.debug("Start");

        try {
            attachmentDataBaseDAOImpl.create(attachment, id);
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new ResourceException("couldn't save file");
        }

        log.debug("End");
    }
}
