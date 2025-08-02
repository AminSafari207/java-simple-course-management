package utils;

import java.util.Collection;
import java.util.Map;

public class ValidationUtils {
    public static void validateId(int id) {
        if (id < 0) throw new IllegalArgumentException("id must be 0 or positive.");
    }

    public static void validateString(String str, String logName) {
        validateNotNull(str, logName);
        if (str.trim().isEmpty())
            throw new IllegalArgumentException(logName + " cannot be empty.");
    }

    public static void validateString(String str, int minLength, String logName) {
        validateString(str, logName);
        if (str.trim().length() < minLength)
            throw new IllegalArgumentException(logName + " must have at least " + minLength + " characters.");
    }

    public static void validateGrade(int grade) {
        if (grade < 0 || grade > 100)
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
    }

    public static void validateGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0)
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0.");
    }

    public static void validateNotNull(Object obj, String logName) {
        if (obj == null)
            throw new NullPointerException(logName + " cannot be null.");
    }

    public static void validateNotEmpty(Collection<?> collection, String logName) {
        validateNotNull(collection, logName);
        if (collection.isEmpty())
            throw new IllegalArgumentException(logName + " cannot be empty.");
    }

    public static void validateCollection(Collection<?> collection, String logName) {
        validateNotEmpty(collection, logName);
    }

    public static void validateMap(Map<?, ?> map, String logName) {
        validateNotNull(map, logName);
        if (map.isEmpty()) throw new IllegalArgumentException(logName + " cannot be empty.");
    }
}
