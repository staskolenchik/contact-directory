package by.kolenchik.core.service.template;

import by.kolenchik.core.exceptions.ResourceException;

public interface TemplateService {

    String getRenderedTemplate(Long id, String templateName, String realPath) throws ResourceException;
}
