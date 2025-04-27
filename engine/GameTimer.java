package engine;

/**
 * Manages a timer for tracking the duration of a game.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class GameTimer {
    private long startTime; // Start time in milliseconds

    /**
     * Constructor for GameTimer.
     *
     * Records the current time as the start time of the timer.
     */
    public GameTimer() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Calculates the elapsed time since the timer started, in seconds.
     *
     * @return The elapsed time in seconds.
     */
    public int gameTimeSeconds() {
        long endTime = System.currentTimeMillis();
        long elapsedTimeMillis = endTime - startTime;
        return (int) (elapsedTimeMillis / 1000); // Conversion to seconds
    }

    /**
     * Resets the timer to the current time.
     *
     * Updates the start time to the current system time.
     */
    public void reset() {
        this.startTime = System.currentTimeMillis();
    }
}