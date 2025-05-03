package vn.rmit.cosc2469;

public class TimeLimitExceededException extends RuntimeException {

    public TimeLimitExceededException() {
        super();
    }

    public TimeLimitExceededException(String message) {
        super(message);
    }

    public TimeLimitExceededException(Throwable cause) {
        super(cause);
    }

    public TimeLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
