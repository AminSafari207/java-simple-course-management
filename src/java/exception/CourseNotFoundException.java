package exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(int courseId) {
      super("Course with ID '" + courseId + "' is not found.");
    }
}
