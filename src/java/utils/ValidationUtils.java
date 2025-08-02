package utils;

public class ValidationUtils {
    public static void validateId(int id) {
        if (id < 0) throw new IllegalArgumentException("id must 0 or positive.");
    }

    public static void validateString(String logName, String str) {
        if (str == null) throw new NullPointerException(logName + " can not be null.");
        if (str.trim().isEmpty()) throw new IllegalArgumentException(logName + " can not be empty strings.");
    }

    public static void validateString(String logName, String str, int minLength) {
        validateString(logName, str);
        if (str.trim().length() < minLength) throw new IllegalArgumentException(logName + " must have at least " + minLength + " characters.");
    }
}
