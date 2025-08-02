package exception;

public class StudentAlreadyExistsException extends RuntimeException {
    public StudentAlreadyExistsException(String studentName, int studentId) {
        super("Student '" + studentName + "' with ID '" + studentId + "' already exists.");
    }
}
