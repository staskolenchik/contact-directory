package by.kolenchik.core.service.attachment;

import by.kolenchik.core.domain.Attachment;
import by.kolenchik.core.exceptions.ResourceException;

public interface AttachmentDBService {

    void create(Attachment attachment, Long id) throws ResourceException;
}
