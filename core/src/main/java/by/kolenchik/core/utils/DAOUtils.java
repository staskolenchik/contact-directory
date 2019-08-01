package by.kolenchik.core.utils;

import by.kolenchik.core.domain.Image;

import java.io.File;

public class DAOUtils {

    public File getImageFile(Image image, String realPath) throws NullPointerException{

        realPath = realPath.replace('\\', '/');

        String fullSavePath = null;
        if (realPath.endsWith("/")) {
            fullSavePath = realPath + "images";
        } else {
            fullSavePath = realPath + File.separator + "images";
        }

        File repository = new File(fullSavePath);
        if (!repository.exists()) {
            repository.mkdir();
        }

        String imageExtension = image.getImageContentType().split("[/]")[1];

        String imagePath = fullSavePath + File.separator + image.getImageName() + "." + imageExtension;


        File imageFile = new File(imagePath);

        return imageFile;
    }
}
