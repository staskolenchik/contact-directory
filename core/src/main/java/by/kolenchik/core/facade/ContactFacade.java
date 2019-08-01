package by.kolenchik.core.facade;

import by.kolenchik.core.domain.*;
import by.kolenchik.core.dto.contactNew.GeneralContactUIDTO;
import by.kolenchik.core.dto.createupdatedto.CreateUpdateContactDTO;
import by.kolenchik.core.dto.user.GeneralUserDTO;
import by.kolenchik.core.exceptions.ApplicationExeption;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.exceptions.ResourceExistsException;
import by.kolenchik.core.service.address.AddressService;
import by.kolenchik.core.service.address.AddressServiceImpl;
import by.kolenchik.core.service.attachment.AttachmentDBServiceImpl;
import by.kolenchik.core.service.image.*;
import by.kolenchik.core.service.telephone.TelephoneService;
import by.kolenchik.core.service.telephone.TelephoneServiceImpl;
import by.kolenchik.core.service.user.UserService;
import by.kolenchik.core.service.user.UserServiceImpl;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class ContactFacade {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private UserService userService;
    private AddressService addressService;
    private TelephoneService telephoneService;
    private AttachmentDBServiceImpl attachmentDBServiceImpl;
    /*private ImageDBService imageDBService;
    private ImageFSService imageFSService;*/

    public ContactFacade() {
        this.userService = new UserServiceImpl();
        this.addressService = new AddressServiceImpl();
        this.telephoneService = new TelephoneServiceImpl();
        this.attachmentDBServiceImpl = new AttachmentDBServiceImpl();
        /*this.imageDBService = new ImageDBServiceImpl();
        this.imageFSService = new ImageFSServiceImpl();*/
    }

    public void create(CreateUpdateContactDTO createUpdateContactDTO, String realPath)
            throws ApplicationExeption, ResourceExistsException{

        log.debug("Start");

        User user = createUpdateContactDTO.getUser();
        log.trace(user);
        Address address = createUpdateContactDTO.getAddress();
        log.trace(address);
        List<Telephone> telephones = createUpdateContactDTO.getTelephones();

        List<Attachment> attachments = createUpdateContactDTO.getAttachments();

        ImagePOJO image = createUpdateContactDTO.getImage();
        /*Image image = createUpdateContactDTO.getImage();*/

        try {

            Boolean isUserUnique = userService.isUserUnique(user.getFirstName(), user.getLastName());

            if (isUserUnique) {

                userService.create(user);

                Long userId =
                        userService.getUserIdByFirstnameAndLastname(user.getFirstName(), user.getLastName());

                addressService.createAddressByUserId(address, userId);

                for (Telephone telephone : telephones) {
                    telephoneService.create(telephone, userId);
                }

                for (Attachment attachment : attachments) {
                    attachmentDBServiceImpl.create(attachment, userId);
                }

                if (image != null) {
                    log.debug(image);

                    image.setId(userId);

                    ImageServiceClass imageServiceClass = new ImageServiceClass();
                    imageServiceClass.save(image);

                }

            } else {
                throw new ResourceExistsException("Resource is already exists");
            }

        } catch (ResourceException e) {
            log.warn(e.getMessage());
            throw new ApplicationExeption("Resource wasn't found");
        } catch (ResourceExistsException ex) {
            log.warn(ex.getMessage());
            throw new ResourceExistsException("Resource is already exists");
        }

    }

    public GeneralContactUIDTO getAllGeneralContacts(Integer pageNumber, Integer pageSize) {

        log.debug("Start");

        GeneralContactUIDTO generalContactUIDTO = new GeneralContactUIDTO();

        List<GeneralUserDTO> generalUserDTOs = userService.getAllGeneralUserDTOs(pageNumber, pageSize);

        return generalContactUIDTO;
    }
}
