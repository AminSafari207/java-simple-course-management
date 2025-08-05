package exception;

public class EnrollmentNotFoundException extends RuntimeException {
    public EnrollmentNotFoundException(int enrollmentId) {
        super("Enrollment with ID '" + enrollmentId + "' is not found.");
    }

    public EnrollmentNotFoundException(int studentId, int courseId) {
        super("Enrollment with student ID '" + studentId + "' and course ID '" + courseId + "' is not found.");
    }
}

