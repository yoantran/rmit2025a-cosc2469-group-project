package vn.rmit.cosc2469;

public class SolverTimer {
    private static long globalStartTime = -1;
    private static final long TIME_LIMIT_MS = 2 * 60 * 1000; // 2 minutes

    // Start the timer
    public static void startTimer() {
        globalStartTime = System.currentTimeMillis();
    }

    // Check if time limit has been exceeded
    public static boolean isTimedOut() {
        return globalStartTime != -1 &&
                System.currentTimeMillis() - globalStartTime > TIME_LIMIT_MS;
    }

    // Throw exception if time limit is exceeded
    public static void checkTimeout() {
        if (isTimedOut()) {
            throw new TimeLimitExceededException("Solving exceeded the 2-minute time limit.");
        }
    }

    // Get time elapsed in milliseconds
    public static long timeElapsed() {
        if (globalStartTime == -1) return 0;
        return System.currentTimeMillis() - globalStartTime;
    }
}
