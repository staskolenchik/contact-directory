package by.kolenchik.core.service.image;

import by.kolenchik.core.dao.image.ImageDAOClass;
import by.kolenchik.core.domain.ImagePOJO;
import by.kolenchik.core.exceptions.ResourceException;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ImageServiceClass {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private ImageDAOClass imageDAOClass;

    public ImageServiceClass() {
        this.imageDAOClass = new ImageDAOClass();
    }


    public void save(ImagePOJO image) throws ResourceException {
        try {

            imageDAOClass.save(image);

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResourceException("No image created");
        }
    }


    public void delete(Long id) throws ResourceException {
        try {
            imageDAOClass.delete(id);
        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage());
            throw new ResourceException("No image deleted");
        }
    }


    public ImagePOJO getImage(Long id) throws ResourceException {

        try {
            log.debug("Start");

            ImagePOJO image = imageDAOClass.getImage(id);

            log.debug("End");

            return image;

        } catch (IOException|NullPointerException e) {
            log.error(e.getMessage());
            throw new ResourceException("No image deleted");
        }
    }
}
