public class Timer {
    private static final int REFRESH_RATE = 60;
    private final int beginFrame;
    private final int endFrame;

    /**
     * Constructor for Frames class.
     * @param beginFrame Frame to start the timer.
     * @param seconds Number of seconds to run the timer.
     */
    public Timer(int beginFrame, int seconds) {
        this.beginFrame = beginFrame;
        this.endFrame = beginFrame + seconds * REFRESH_RATE;
    }

    /**
     * Check if the timer has finished.
     * @param currentFrame Current frame of the game.
     * @return True if the timer has finished, false otherwise.
     */
    public boolean isFinished(int currentFrame) {
        return currentFrame >= endFrame;
    }

    /**
     * Get beginning frame of the timer.
     * @return Beginning frame of the timer.
     */
    public int getBeginFrame() {
        return beginFrame;
    }
}
