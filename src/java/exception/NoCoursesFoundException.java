package exception;

public class NoCoursesFoundException extends RuntimeException {
    public NoCoursesFoundException() {
        super("No courses exist in the database.");
    }
}
