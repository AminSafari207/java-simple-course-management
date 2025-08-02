package exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(int studentId) {
        super("Student with ID '" + studentId + "' is not found.");
    }
}
