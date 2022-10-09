import bagel.*;
import java.util.*;

/**
 * The levels of the game are represented by Level objects.
 */
public abstract class Level {

    // error messages
    private static final String NO_OBJECTS = "No objects specified.";

    /**
     * Level 0 stage.
     */
    public static final int LEVEL0_STAGE = 0;
    
    /**
     * Level 1 stage.
     */
    public static final int LEVEL1_STAGE = 1;
    
    /**
     * Game over stage.
     */
    public static final int GAME_OVER_STAGE = -2;
    
    /**
     * Game won stage.
     */
    public static final int GAME_WON_STAGE = -1;

    // fonts
    private static final String FONT_PATH = "res/frostbite.ttf";

    /**
     * Font size 75.
     */
    public static final Font FONT75 = new Font(FONT_PATH, 75);

    /**
     * Font size 40.
     */
    public static final Font FONT40 = new Font(FONT_PATH, 40);

    // variables
    private boolean prepareLevel;
    private boolean startScreen;
    private boolean endScreen;
    private Boundary boundary;
    private GameObject[] objects;
    private GameObject[] gameObjects;

    /**
     * Constructor for Level class.
     */
    public Level() {
        this.prepareLevel = true;
        this.startScreen = true;
        this.endScreen = false;
        this.boundary = null;
        this.objects = null;
        this.gameObjects = null;
    }

    /**
     * Run the level.
     * @param input Input object.
     */
    public abstract void run(Input input);

    /**
     * Start screen for the level.
     */
    protected abstract void startScreen();

    /**
     * Prepare the level for the game.
     */
    protected abstract void prepare();
    
    /**
     * Display the start screen for the level.
     * @param input Input object.
     */
    protected abstract void displayStartScreen(Input input);

    /**
     * Get the value of prepareLevel.
     */
    public boolean getPrepareLevel() {
        return prepareLevel;
    }

    /**
     * Set the value of prepareLevel.
     * @param prepareLevel Value of prepareLevel.
     */
    public void setPrepareLevel(boolean prepareLevel) {
        this.prepareLevel = prepareLevel;
    }

    /**
     * Get the value of startScreen.
     */
    public boolean getStartScreen() {
        return startScreen;
    }

    /**
     * Set the value of startScreen.
     * @param startScreen Value of startScreen.
     */
    public void setStartScreen(boolean startScreen) {
        this.startScreen = startScreen;
    }

    /**
     * Get the value of endScreen.
     */
    public boolean getEndScreen() {
        return endScreen;
    }

    /**
     * Set the value of endScreen.
     * @param endScreen Value of endScreen.
     */ 
    public void setEndScreen(boolean endScreen) {
        this.endScreen = endScreen;
    }

    /**
     * Get the boundary of the level.
     */
    public Boundary getBoundary() {
        return boundary;
    }

    /**
     * Set the boundary of the level.
     * @param boundary Boundary of the level.
     */
    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    /**
     * Get the objects of the level.
     */
    public GameObject[] getObjects() {
        return objects;
    }

    /**
     * Set the objects of the level.
     * @param objects Objects of the level.
     */
    public void setObjects(GameObject[] objects) {
        this.objects = objects;
    }

    /**
     * Get the game objects of the level.
     */
    public GameObject[] getGameObjects() {
        return gameObjects;
    }

    /**
     * Set the game objects of the level.
     * @param gameObjects Game objects of the level.
     */
    public void setGameObjects(GameObject[] gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * Game message screen for the game.
     * @param message Message to display.
     */
    public static void gameEndMessage(String message) {
        Message msg = new Message(FONT75, message);
        msg.draw();
    }

    /**
     * Get the objects from the array of all objects.
     * In this function we assume that the first object in the array is a player.
     * @return Array of stationary objects.
     */
    public GameObject[] extractGameObjects() {
        if (getObjects() == null) {
            throw new RuntimeException(NO_OBJECTS);
        }
        return Arrays.copyOfRange(getObjects(), 1, objects.length);
    }

    /**
     * Check if player collides with any game objects. If so, handle the collision.
     * @param player Player object.
     */
    protected void checkCollisions(Player player) {
        
        // check if player hit a sinkhole (while not being invincible)
        if (player.collides(gameObjects, Sinkhole.class) && !player.isInvincible()) {

            // get specific sinkhole collided with and inflict damage
            Sinkhole sinkhole = (Sinkhole) player.getCollidedObject(gameObjects, Sinkhole.class);
            sinkhole.inflictDamageTo(player);

            // remove sinkhole from game
            gameObjects = ShadowDimension.removeGameObject(gameObjects, sinkhole);
        }

        // check if player hit a barrier
        if (player.collides(gameObjects, Barrier.class)) {

            // block player from moving
            Barrier wall = (Barrier) player.getCollidedObject(gameObjects, Barrier.class);
            wall.block(player);
        }
    }

    /**
     * Check if the player has died. If so, end the game.
     * @param player Player object.
     */
    protected void checkPlayerDeath(Player player) {
        if (player.isDead()) {
            ShadowDimension.setStage(GAME_OVER_STAGE);
        }
    }

    /**
     * Move the demons in the game. If demon collides with a barrier, boundary or a sinkhole, it will move in the 
     * opposite direction.
     */
    protected void moveDemons() {
        for (GameObject object : gameObjects) {
            if (!(object instanceof Demon)) {
                continue;
            }
            Demon demon = (Demon) object;
            demon.move(demon.getDirection());

            // if the demon hits a barrier, sinkhole or boundary, reverse the direction
            if (demon.collides(gameObjects, Barrier.class)
                || demon.collides(gameObjects, Sinkhole.class)
                || !boundary.contains(demon.getPosition())) {

                // reverse the direction
                demon.setDirection(demon.getDirection().mul(-1));

                // move the demon in the opposite direction
                demon.move(demon.getDirection());
            }
        }
    }

    /**
     * Check if any demons are dead or if they are not invincible anymore. If so, remove them from the game.
     */
    protected void checkDemons() {
        for (GameObject gameObject : gameObjects) {
            if (!(gameObject instanceof Demon)) {
                continue;
            }

            Demon demon = (Demon) gameObject;
            if (demon.isDead()) {
                if (demon instanceof Navec) {
                    ShadowDimension.setStage(GAME_WON_STAGE);
                }
                gameObjects = ShadowDimension.removeGameObject(gameObjects, demon);
            }
            demon.checkStates();
        }
    }

    /**
     * Check if demons should attack if the player is within it's attack radius. Renders fire to the screen. 
     * If player touches a demon's fire, inflict damage to the player.
     * @param player Player object.
     */
    protected void demonsAttack(Player player) {
        for (GameObject gameObject : gameObjects) {
            if (!(gameObject instanceof Demon)) {
                continue;
            }
            Demon demon = (Demon) gameObject;
            if (demon.isInAttackRadius(player)) {
                demon.attack();
                Fire fire = demon.shootFireAt(player);
                fire.draw();
                if (player.collides(fire) && !player.isInvincible()) {
                    fire.inflictDamageTo(player);
                }
            }
        }
    }

    /**
     * Attack if player presses A. If in attack state, inflict damage to demons.
     * @param input Input object.
     * @param player Player object.
     */
    protected void playerAttack(Input input, Player player) {

        // trigger attack state
        if (input.wasPressed(Keys.A)) {
            player.attack();
        }

        // if player is in attack state, inflict damage to demons
        if (player.isAttacking()) {
            ArrayList<GameObject> demons = new ArrayList<GameObject>();

            if (player.collides(gameObjects, Demon.class)) {
                demons.addAll(player.getCollidedObjects(gameObjects, Demon.class));
            }

            if (player.collides(gameObjects, Navec.class)) {
                demons.addAll(player.getCollidedObjects(gameObjects, Navec.class));
            }
            
            for (GameObject gameObject : demons) {
                Demon demon = (Demon) gameObject;
                if (!demon.isInvincible()) {
                    player.inflictDamageTo(demon);
                }
            }
        }
    }

    /**
     * Draw the objects in the game given an array of objects.
     * @param objects Array of objects to draw.
     */
    protected void drawObjects(GameObject[] objects) {
        for (GameObject object : objects) {
            object.draw();
        }
    }

    /**
     * Draw the background for the game.
     * @param background Background image as a string.
     */
    protected void drawBackground(String background) {
        Image bgImage = new Image(background);
        bgImage.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }
}
