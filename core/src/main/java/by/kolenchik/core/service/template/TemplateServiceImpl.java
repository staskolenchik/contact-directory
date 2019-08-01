package by.kolenchik.core.service.template;

import by.kolenchik.core.dao.address.AddressDAO;
import by.kolenchik.core.dao.address.AddressDAOImpl;
import by.kolenchik.core.dao.user.UserDAO;
import by.kolenchik.core.dao.user.UserDAOImpl;
import by.kolenchik.core.domain.Address;
import by.kolenchik.core.domain.User;
import by.kolenchik.core.exceptions.ResourceException;
import org.apache.log4j.Logger;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.File;
import java.sql.SQLException;

public class TemplateServiceImpl implements TemplateService {

    private static Logger log = Logger.getLogger(TemplateServiceImpl.class);

    private UserDAO userDAO = new UserDAOImpl();
    private AddressDAO addressDAO = new AddressDAOImpl();

    public String getRenderedTemplate(Long id, String templateName, String realPath) throws ResourceException {

        log.debug("start");

        try {
            realPath = realPath.replace('\\', '/');

            String templateDirPath = null;

            if (realPath.endsWith("/")) {
                templateDirPath = realPath + "emailTemplates" + File.separator  + "server";
            } else {
                templateDirPath = realPath + File.separator + "emailTemplates" + File.separator  + "server";
            }

            templateName = templateName.toLowerCase();
            templateName = templateName.replace(" ", "_");
            templateName = templateName.replace("(", "");
            templateName = templateName.replace(")", "");

            STGroup gr = new STGroupDir(templateDirPath, '$', '$');
            log.debug("modified template name =" + templateName);
            ST st = gr.getInstanceOf(templateName);

            User userById = userDAO.getUserById(id);
            Address addressByUserId = addressDAO.findAddressByUserId(id);

            log.trace(userById);

            st.add("user", userById);
            st.add("address", addressByUserId);

            String result = st.render();
            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResourceException("Resource exception");
        }
    }
}
