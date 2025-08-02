package exception;

public class NoStudentsFoundException extends RuntimeException {
    public NoStudentsFoundException() {
        super("No students exist in the database.");
    }
}
