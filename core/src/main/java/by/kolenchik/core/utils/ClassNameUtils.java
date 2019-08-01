package by.kolenchik.core.utils;

public class ClassNameUtils {

    private ClassNameUtils(){}

    public static String getCurrentClassName() {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            return e.getStackTrace()[1].getClassName();
        }
    }
}
