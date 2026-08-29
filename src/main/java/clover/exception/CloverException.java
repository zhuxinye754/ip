package clover.exception;
/**
 * Represents an error caused by an invalid Clover command or task input.
 */
public class CloverException extends Exception {
    private static final long serialVersionUID = 1L;

    public CloverException(String message) {
        super(message);
    }
}
