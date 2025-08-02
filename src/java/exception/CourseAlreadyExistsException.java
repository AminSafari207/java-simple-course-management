package exception;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException(String courseTitle, int courseId) {
      super("Course '" + courseTitle + "' with ID '" + courseId + "' already exists.");
    }
}
