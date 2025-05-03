package vn.rmit.cosc2469;

public class SolverTimer {
    private static final long DEFAULT_TIME_LIMIT = 2 * 60 * 1000; // 2 minutes
    private long startTime;
    private long timeLimit;

    public SolverTimer() {
        this(DEFAULT_TIME_LIMIT);
    }

    public SolverTimer(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void checkTimeLimit() {
        if (getElapsedTime() > timeLimit) {
            throw new TimeLimitExceededException("Time limit exceeded.");
        }
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
}
