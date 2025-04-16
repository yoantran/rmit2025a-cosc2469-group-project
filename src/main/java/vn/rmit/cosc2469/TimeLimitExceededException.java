package vn.rmit.cosc2469;

public class TimeLimitExceededException extends RuntimeException {
    public TimeLimitExceededException(String message) {
        super(message);
    }
}
