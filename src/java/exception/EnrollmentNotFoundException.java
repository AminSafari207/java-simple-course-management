package exception;

public class EnrollmentNotFoundException extends RuntimeException {
    public EnrollmentNotFoundException(int enrollmentId) {
        super("Enrollment with ID '" + enrollmentId + "' is not found.");
    }
}

