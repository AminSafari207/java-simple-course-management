package utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class ValidationUtils {
    public static void validateId(int id) {
        if (id < 0) throw new IllegalArgumentException("id must 0 or positive.");
    }

    public static void validateString(String str, String logName) {
        validateNotNullObject(str, logName);
        if (str.trim().isEmpty()) throw new IllegalArgumentException(logName + " can not be empty strings.");
    }

    public static void validateString(String str, int minLength, String logName) {
        validateString(logName, str);
        if (str.trim().length() < minLength) throw new IllegalArgumentException(logName + " must have at least " + minLength + " characters.");
    }

    public static void validateGrade(int grade) {
        if (grade < 0 || grade > 100) throw new IllegalArgumentException("Grade must be between 0 and 100");
    }

    public static void validateGpa(double gpa) {
        if (gpa < 0.0 || gpa > 100.0) throw new IllegalArgumentException("GPA must be between 0.0 and 4.0.");
    }

    public static void validateNotNullObject(Object object, String logName) {
        if (object == null) throw new NullPointerException(logName + " can not be null.");
    }

    public static void validateNotEmptyCollection(Collection<?> collection, String logName) {
        if (collection.isEmpty()) throw new IllegalArgumentException(logName + " can not be empty.");
    }

    public static void validateCollection(Collection<?> collection, String logName) {
        validateNotNullObject(collection, logName);
        validateNotEmptyCollection(collection, logName);
    }
}
