package exception;

public class InvalidIdSetException extends RuntimeException {
    public InvalidIdSetException(String className, int id) {
        super("ID '" + id + "' in class '" + className + "' is already set once!" );
    }
}
