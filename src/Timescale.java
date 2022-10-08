import bagel.*;

public class Timescale {

    // timescale
    public static final int DEFAULT_TIMESCALE = 0;
    public static final int MAX_TIMESCALE = 3;
    public static final int MIN_TIMESCALE = -3;
    public static int timescale = DEFAULT_TIMESCALE;
    private static final String SPED_UP = "Sped up, Speed: %d";
    private static final String SLOWED_DOWN = "Slowed down, Speed: %d";

    private GameObject[] gameObjects;

    /**
     * Constructor for Timescale class.
     * @param gameObjects List of game objects.
     */
    Timescale(GameObject[] gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * Update timescale speeds for all the moving game objects.
     */
    public void update() {
        for (GameObject gameObject : gameObjects) {
            if (!(gameObject instanceof MovingObject)) {
                continue;
            }
            MovingObject movingObject = (MovingObject) gameObject;
            movingObject.timescaleSpeed(timescale);
        }
    }

    /**
     * Increase timescale for the game.
     */
    public void increase() {
        if (timescale < MAX_TIMESCALE) {
            timescale++;
            System.out.println(String.format(SPED_UP, timescale));
            update();
        }
    }

    /**
     * Decrease timescale for the game.
     */
    public void decrease() {
        if (timescale > MIN_TIMESCALE) {
            timescale--;
            System.out.println(String.format(SLOWED_DOWN, timescale));
            update();
        }
    }

    /**
     * Given the input, if the `L` key is pressed, increase timescale. If the `K` key is pressed, decrease timescale.
     * @param input Input from the user.
     */
    public void controls(Input input) {
        if (input.wasPressed(Keys.L)) {
            increase();
        } else if (input.wasPressed(Keys.K)) {
            decrease();
        }
    }
}
