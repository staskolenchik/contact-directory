package by.kolenchik.core.dao.image;

import by.kolenchik.core.domain.ImagePOJO;
import by.kolenchik.core.utils.ClassNameUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageDAOClass {

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private static File imageDirObj;

    static {

        String dirServerPath = "C:" + File.separator + "file-server";

        File fileDir = new File(dirServerPath);

        if (!fileDir.exists()) {
            if (fileDir.mkdir()) {
                log.debug("Made directory for server files with path =" + dirServerPath);
            }
        }

        File imageDir = new File(fileDir, "images");

        if (!imageDir.exists()) {
            if (imageDir.mkdir()) {
                log.debug("Made directory for server files with path =" + dirServerPath);
            }
        }

        imageDirObj = imageDir;

        log.debug("Save images in absolute path =" + imageDirObj.getAbsolutePath());
    }

    public void save(ImagePOJO image) throws IOException {

        log.debug("Start");

        String imageName = image.getId().toString();
        String imageType = image.getContentType().split("[/]")[1];
        String imageData = image.getData();
        byte[] imageBytes = imageData.getBytes();

        File imageFile = new File(imageDirObj, imageName + "." + imageType);

        Base64 base64 = new Base64();
        FileUtils.writeByteArrayToFile(imageFile,base64.decode(imageBytes));

        log.debug("End");
    }


    public void delete(Long id) throws IOException, NullPointerException {

        log.debug("Start");

        File[] imageFiles = imageDirObj.listFiles();

        if (id != null) {

            for (int i = 0; i < imageFiles.length; i++) {

                if (imageFiles[i].getName().split("[.]")[0].equals(id.toString())) {

                    log.debug("Is image deleted? - " + imageFiles[i].delete());
                }
            }
        }
        log.debug("End");
    }

    public ImagePOJO getImage(Long id) throws IOException, NullPointerException {

        log.debug("Start");

        File[] imageFiles = imageDirObj.listFiles();

        for (int i = 0; i < imageFiles.length; i++) {

            if (imageFiles[i].getName().split("[.]")[0].equals(id.toString())) {

                ImagePOJO imagePOJO = new ImagePOJO();

                byte[] imageFileAsBytes = FileUtils.readFileToByteArray(imageFiles[i]);
                String imageData = java.util.Base64.getEncoder().encodeToString(imageFileAsBytes);

                Path imagePath = imageFiles[i].toPath();
                String imageMimeType = Files.probeContentType(imagePath);

                imagePOJO.setId(id);
                imagePOJO.setData(imageData);
                imagePOJO.setContentType(imageMimeType);

                log.trace(imagePOJO);

                return imagePOJO;
            }
        }

        return null;
    }
}
