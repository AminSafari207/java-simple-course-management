package exception;

public class NoEnrollmentsFoundException extends RuntimeException {
    public NoEnrollmentsFoundException() {
        super("No enrollments exist in the database.");
    }
}
