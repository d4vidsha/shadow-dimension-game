import bagel.*;
import bagel.util.*;
import java.io.*;
import java.util.*;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2022
 * @author David Sha
 */

public class ShadowDimension extends AbstractGame {

    // the total number of frames rendered since the game started
    public static int frames = 0;

    // timescale
    public static final int DEFAULT_TIMESCALE = 0;
    public static final int MAX_TIMESCALE = 3;
    public static final int MIN_TIMESCALE = -3;
    public static int timescale = DEFAULT_TIMESCALE;

    private static final String FONT_PATH = "res/frostbite.ttf";
    public final Font FONT75 = new Font(FONT_PATH, 75);
    public final Font FONT40 = new Font(FONT_PATH, 40);

    // constants
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int LEVEL0_MAX_OBJECTS = 60;
    private static final int LEVEL1_MAX_OBJECTS = 29;
    private static final String CSV_DELIMITER = ",";
    private static final String LEVEL0_CSV = "res/level0.csv";
    private static final String LEVEL1_CSV = "res/level1.csv";
    private static final String LEVEL0_BACKGROUND = "res/background0.png";
    private static final String LEVEL1_BACKGROUND = "res/background1.png";
    private static final int LEVEL0_END_SCREEN_WAIT_SECONDS = 3;
    private static final Vector2[] DIRECTIONS = {
        Vector2.left,
        Vector2.right,
        Vector2.up,
        Vector2.down
    };

    // game title
    private static final double GAME_TITLE_X = 260;
    private static final double GAME_TITLE_Y = 250;
    private static final String GAME_TITLE = "Shadow Dimension";

    // stages of the game
    private static final int GAME_OVER_STAGE = -2;
    private static final int GAME_WON_STAGE = -1;
    private static final int LEVEL0_STAGE = 0;
    private static final int LEVEL1_STAGE = 1;

    // game objects
    private static final String PLAYER = "Fae";
    private static final String WALL = "Wall";
    private static final String SINKHOLE = "Sinkhole";
    private static final String TREE = "Tree";
    private static final String DEMON = "Demon";
    private static final String NAVEC = "Navec";
    private static final String[] OBJECT_NAMES = {PLAYER, WALL, SINKHOLE, TREE, DEMON, NAVEC};

    // initialising the game
    private int stage = LEVEL0_STAGE;
    private boolean prepareLevel = true;
    private boolean startScreen = true;
    private boolean endScreen = false;
    private Boundary boundary;
    private GameObject[] objects;
    private GameObject[] gameObjects;
    private Timer level0EndScreen;

    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Read the objects from the csv file.
     * @param csv Name of the csv file.
     * @param boundary Boundary of the game which will be added to GameObjects.
     * @return Array of GameObjects.
     */
    private GameObject[] readObjects(String csv, int maxObjects) {
        GameObject[] objects = new GameObject[maxObjects];
        
        try {
            File file = new File(csv);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] values;
            int i = 0;

            // read the csv file line by line
            while ((line = br.readLine()) != null) {
                values = line.split(CSV_DELIMITER);
                double x = Double.parseDouble(values[1]);
                double y = Double.parseDouble(values[2]);
                Point pos = new Point(x, y);

                // instantiate randoms
                double speed;
                int direction;

                // create the object based on the type
                if (contains(OBJECT_NAMES, values[0])) {
                    switch (values[0]) {
                        case PLAYER:
                            Player player = new Player(pos);
                            objects[i] = player;
                            break;
                        case WALL:
                            Wall wall = new Wall(pos);
                            objects[i] = wall;
                            break;
                        case SINKHOLE:
                            Sinkhole sinkhole = new Sinkhole(pos);
                            objects[i] = sinkhole;
                            break;
                        case TREE:
                            Tree tree = new Tree(pos);
                            objects[i] = tree;
                            break;
                        case DEMON:
                            // determine equally randomly if the demon is passive or aggressive
                            // where 0 is passive and 1 is aggressive
                            int aggressive = (int) (Math.random() * 2);

                            if (aggressive == 0) {
                                speed = Demon.PASSIVE_SPEED;
                            } else { 
                                // determine a random speed between 0.2 and 0.7
                                speed = 0.2 + Math.random() * 0.5;
                            }

                            // determine a random direction of left, right, up or down between 0 and 3
                            direction = (int) (Math.random() * 4);

                            Demon demon = new Demon(pos, speed, DIRECTIONS[direction]);

                            // randomly choose which direction the demon faces in the beginning
                            int face = (int) (Math.random() * 2);
                            if (face == 0) {
                                demon.faceLeft();
                            } else if (face == 1) {
                                demon.faceRight();
                            } else {
                                throw new RuntimeException("Invalid face value");
                            }
                            objects[i] = demon;
                            break;
                        case NAVEC:
                            // determine a random speed between 0.2 and 0.7
                            speed = 0.2 + Math.random() * 0.5;

                            // determine a random direction of left, right, up or down between 0 and 3
                            direction = (int) (Math.random() * 4);

                            Navec navec = new Navec(pos, speed, DIRECTIONS[direction]);
                            objects[i] = navec;
                            break;
                    }
                    i++;
                }
            }
            
            // reduce size of array
            objects = Arrays.copyOf(objects, i);

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }

    /**
     * Read in the boundary from the csv file.
     * @param csv Name of the csv file.
     * @return Boundary of the game.
     */
    private Boundary readBoundary(String csv) {
        Boundary boundary = null;
        
        try {
            File file = new File(csv);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] values;
            Point topLeft = null;
            Point bottomRight = null;

            // read the csv file line by line
            while ((line = br.readLine()) != null) {
                values = line.split(CSV_DELIMITER);
                double x = Double.parseDouble(values[1]);
                double y = Double.parseDouble(values[2]);
                switch (values[0]) {
                    case "TopLeft":
                        topLeft = new Point(x, y);
                        break;
                    case "BottomRight":
                        bottomRight = new Point(x, y);
                        break;
                }
            }
            
            // create the boundary
            if (topLeft != null && bottomRight != null) {
                boundary = new Boundary(topLeft, bottomRight);
            } else {
                System.out.println("No boundary specified");
                System.exit(1);
            }

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boundary;
    }

    /**
     * Checks if a string is in an array of strings.
     * @param arr Array of strings.
     * @param targetValue String to check if it is in the array.
     * @return True if the string is in the array, false otherwise.
     */
    private static boolean contains(String[] arr, String targetValue) {
        for(String s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }

    /**
     * Get the objects from the array of all objects.
     * In this function we assume that the first object in the array is a player.
     * @return Array of stationary objects.
     */
    public GameObject[] getGameObjects() {
        return Arrays.copyOfRange(objects, 1, objects.length);
    }

    /**
     * Draw the objects in the game given an array of objects.
     * @param objects Array of objects to draw.
     */
    private void drawObjects(GameObject[] objects) {
        for (GameObject object : objects) {
            object.draw();
        }
    }

    /**
     * Draw the background for the game.
     */
    private void drawBackground(String background) {
        Image bgImage = new Image(background);
        bgImage.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }

    /**
     * Get player object from the array of objects. We assume the first object
     * in the array is the player.
     * @param objects
     * @return
     */
    private Player getPlayer(GameObject[] objects) {
        return (Player) objects[0];
    }

    /**
     * Remove the object from the array of game objects.
     * @param objects Array of game objects.
     * @param gameObject Object to remove.
     * @return Array of game objects with the object removed.
     */
    public GameObject[] removeGameObject(GameObject[] objects, GameObject gameObject) {
        ArrayList<GameObject> objectsList = new ArrayList<>(Arrays.asList(objects));
        objectsList.remove(gameObject);
        return objectsList.toArray(new GameObject[objectsList.size()]);
    }

    /**
     * Add an object to the array of game objects.
     * @param objects Array of game objects.
     * @param gameObject Object to add.
     */
    public GameObject[] addGameObject(GameObject[] objects, GameObject gameObject) {
        ArrayList<GameObject> objectsList = new ArrayList<>(Arrays.asList(objects));
        objectsList.add(gameObject);
        return objectsList.toArray(new GameObject[objectsList.size()]);
    }

    /**
     * Game message screen for the game.
     * @param message Message to display.
     */
    private void gameEndMessage(String message) {
        Message msg = new Message(FONT75, message);
        msg.draw();
    }

    /**
     * Start screen for level 0. This also prepares the level.
     */
    private void startlevel0() {
        Point gameTitlePos = new Point(GAME_TITLE_X, GAME_TITLE_Y);
        Point gameInstructionPos = new Point(GAME_TITLE_X + 90, GAME_TITLE_Y + 190);
        String gameInstructionMsg = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
        Message gameTitle = new Message(FONT75, GAME_TITLE, gameTitlePos);
        Message gameInstruction = new Message(FONT40, gameInstructionMsg, gameInstructionPos);
        gameTitle.draw();
        gameInstruction.draw();

        // prepare the level if necessary
        if (prepareLevel) {
            prepareLevel0();
            prepareLevel = false;
        }
    }

    /**
     * Start screen for level 1. This also prepares the level.
     */
    private void startlevel1() {
        Point gameInstructionPos = new Point(350, 350);
        String gameInstructionMsg = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
        Message gameInstruction = new Message(FONT40, gameInstructionMsg, gameInstructionPos);
        gameInstruction.draw();

        // prepare the level if necessary
        if (prepareLevel) {
            prepareLevel1();
            prepareLevel = false;
        }
    }

    /**
     * Prepare the game for level 0.
     */
    private void prepareLevel0() {
        boundary = readBoundary(LEVEL0_CSV);
        objects = readObjects(LEVEL0_CSV, LEVEL0_MAX_OBJECTS);
        gameObjects = getGameObjects();
    }

    /**
     * Prepare the game for level 1.
     */
    private void prepareLevel1() {
        boundary = readBoundary(LEVEL1_CSV);
        objects = readObjects(LEVEL1_CSV, LEVEL1_MAX_OBJECTS);
        gameObjects = getGameObjects();
    }

    /**
     * Given a player and the array of game objects, check if the player has overlapped with any barrier objects and
     * if so, move the player back to the previous unoverlapped position. This overlap usually happens when the player 
     * is next to a barrier and changes to attack state. Since this is determined by the image, a different attack
     * image will affect the reliability of this function.
     * 
     * This function assumes that the attack image is larger in width than the idle image. Note that it DOES NOT
     * assume that the attack image is larger in height than the idle image as this is not the case for the current
     * player images.
     * 
     * Without this function, the player would be stuck on the barrier until the player returns to the idle state.
     * @param player Player object.
     * @param gameObjects Array of game objects.
     */
    private void makeAttackPositionValid(Player player, GameObject[] gameObjects) {
        if (player.isAttacking()) {

            // ensure player doesn't get stuck on a wall when attacking on the left side of the wall
            if (player.collides(gameObjects, Wall.class)) {
                Wall wall = (Wall) player.getCollidedObject(gameObjects, Wall.class);
                Point newPos = new Point(wall.getPosition().x - player.getImage().getWidth(), player.getPosition().y);
                player.setPosition(newPos);
            }

            // ensure player doesn't get stuck on a tree when attacking on the left side of the tree
            if (player.collides(gameObjects, Tree.class)) {
                Tree tree = (Tree) player.getCollidedObject(gameObjects, Tree.class);
                Point newPos = new Point(tree.getPosition().x - player.getImage().getWidth(), player.getPosition().y);
                player.setPosition(newPos);
            }
        }
    }

    /**
     * Check if player collides with any game objects. If so, handle the collision.
     * @param player Player object.
     */
    private void checkCollisions(Player player) {
        
        // check if player hit a sinkhole (while not being invincible)
        if (player.collides(gameObjects, Sinkhole.class) && !player.isInvincible()) {

            // get specific sinkhole collided with and inflict damage
            Sinkhole sinkhole = (Sinkhole) player.getCollidedObject(gameObjects, Sinkhole.class);
            sinkhole.inflictDamageTo(player);

            // remove sinkhole from game
            gameObjects = removeGameObject(gameObjects, sinkhole);
        }

        // check if player hit a wall
        if (player.collides(gameObjects, Wall.class)) {

            // block player from moving
            Wall wall = (Wall) player.getCollidedObject(gameObjects, Wall.class);
            wall.block(player);
        }

        // check if player hit a tree
        if (player.collides(gameObjects, Tree.class)) {

            // block player from moving
            Tree tree = (Tree) player.getCollidedObject(gameObjects, Tree.class);
            tree.block(player);
        }
    }

    /**
     * Check if the player has died. If so, end the game.
     * @param player Player object.
     */
    private void checkPlayerDeath(Player player) {
        if (player.isDead()) {
            stage = GAME_OVER_STAGE;
        }
    }

    /**
     * Check if the player has beaten level 0. If so, move to the level 0 end screen.
     * @param input
     */
    private void checkLevel0Completion(Player player) {
        // if necessary, display winning message and move to next stage after 3 seconds
        if (player.isAtGate()) {
            endScreen = true;
            level0EndScreen = new Timer(frames, LEVEL0_END_SCREEN_WAIT_SECONDS);
        }
    }

    /**
     * Display start screen for level 0 until the player presses space.
     * @param input Input object.
     */
    private void displayLevel0StartScreen(Input input) {
        startlevel0();
        if (input.wasPressed(Keys.SPACE)) {
            startScreen = false;
        }
        return;
    }

    /**
     * Display end screen for level 0 until time is up.
     */
    private void displayLevel0EndScreen() {
        gameEndMessage("LEVEL COMPLETE!");

        // wait 3 seconds
        if (level0EndScreen.isFinished(frames)) {
            // begin next stage
            stage = LEVEL1_STAGE;
            prepareLevel = true;
            startScreen = true;
            endScreen = false;
        }
        
        return;
    }

    /**
     * Display start screen for level 1 until the player presses space.
     * @param input Input object.
     */
    private void displayLevel1StartScreen(Input input) {
        startlevel1();
        if (input.wasPressed(Keys.SPACE)) {
            startScreen = false;
        }
        return;
    }

    /**
     * The first level of the game.
     * @param input Input from the user which controls the player.
     * @param player Player object that is moved.
     */
    private void level0(Input input) {

        if (startScreen) {
            displayLevel0StartScreen(input);
            return;
        }

        if (endScreen) {
            displayLevel0EndScreen();
            return;
        }

        Player player = getPlayer(objects);

        // move the player
        player.update(input, boundary);
        player.checkStates();

        // draw everything
        drawBackground(LEVEL0_BACKGROUND);
        HealthBar.drawHealthBar(player);
        player.draw(boundary);
        drawObjects(gameObjects);

        // check everything
        checkPlayerDeath(player);
        checkCollisions(player);
        checkLevel0Completion(player);

        playerAttack(input, player);

        makeAttackPositionValid(player, gameObjects);
    }

    /**
     * Update timescale speeds for all the moving game objects.
     */
    public void updateTimescale() {
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
    public void increaseTimescale() {
        if (timescale < MAX_TIMESCALE) {
            timescale++;
            System.out.println("Sped up, Speed: " + timescale);
            updateTimescale();
        }
    }

    /**
     * Decrease timescale for the game.
     */
    public void decreaseTimescale() {
        if (timescale > MIN_TIMESCALE) {
            timescale--;
            System.out.println("Slowed down, Speed: " + timescale);
            updateTimescale();
        }
    }

    /**
     * Given the input, if the "L" key is pressed, increase timescale. If the "K" key is pressed, decrease timescale.
     * @param input Input from the user.
     */
    private void timescaleControls(Input input) {
        if (input.wasPressed(Keys.L)) {
            increaseTimescale();
        } else if (input.wasPressed(Keys.K)) {
            decreaseTimescale();
        }
    }

    /**
     * Move the demons in the game. If demon collides with a barrier, boundary or a sinkhole, it will move in the 
     * opposite direction.
     */
    private void moveDemons() {
        for (GameObject object : gameObjects) {
            if (!(object instanceof Demon)) {
                continue;
            }
            Demon demon = (Demon) object;
            demon.move(demon.getDirection());

            // if the demon hits a barrier, sinkhole or boundary, reverse the direction
            if (demon.collides(gameObjects, Tree.class) 
                || demon.collides(gameObjects, Wall.class)
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
    private void checkDemons() {
        for (GameObject gameObject : gameObjects) {
            if (!(gameObject instanceof Demon)) {
                continue;
            }

            Demon demon = (Demon) gameObject;
            if (demon.isDead()) {
                if (demon instanceof Navec) {
                    stage = GAME_WON_STAGE;
                }
                gameObjects = removeGameObject(gameObjects, demon);
            }
            demon.checkStates();
        }
    }

    /**
     * Check if demons should attack if the player is within it's attack radius. If player touches a demon's fire,
     * inflict damage to the player.
     * @param player Player object.
     */
    public void checkDemonsAttack(Player player) {
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
     * Attack if player presses A.
     * @param input
     */
    private void playerAttack(Input input, Player player) {
        if (input.wasPressed(Keys.A)) {
            player.attack();
            if (player.isAttacking()) {
                if (player.collides(gameObjects, Demon.class)) {
                    Demon demon = (Demon) player.getCollidedObject(gameObjects, Demon.class);
                    if (!demon.isInvincible()) {
                        player.inflictDamageTo(demon);
                    }
                }

                if (player.collides(gameObjects, Navec.class)) {
                    Navec navec = (Navec) player.getCollidedObject(gameObjects, Navec.class);
                    if (!navec.isInvincible()) {
                        player.inflictDamageTo(navec);
                    }
                }
            }
        }
    }

    /**
     * The second level of the game.
     * @param input Input from the user which controls the player.
     * @param player Player object that is moved.
     */
    private void level1(Input input) {

        if (startScreen) {
            displayLevel1StartScreen(input);
            return;
        }

        Player player = getPlayer(objects);
        timescaleControls(input);

        // move the player and demons (including Navec)
        player.update(input, boundary);
        player.checkStates();
        moveDemons();

        // draw everything
        drawBackground(LEVEL1_BACKGROUND);
        HealthBar.drawHealthBar(player);
        player.draw(boundary);
        drawObjects(gameObjects);

        // check everything
        checkPlayerDeath(player);
        checkCollisions(player);
        
        playerAttack(input, player);

        checkDemons();
        checkDemonsAttack(player);
        makeAttackPositionValid(player, gameObjects);
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     * This is where the game stages are updated.
     * @param input Input from the user.
     */
    @Override
    protected void update(Input input) {
        
        // increment frames
        frames++;
        if (frames >= Integer.MAX_VALUE) {
            throw new RuntimeException("Frames exceeded maximum value. Game has been running for too long.");
        }

        // fastforward to level 1 when W key is pressed
        if (input.wasPressed(Keys.W)) {
            stage = LEVEL1_STAGE;
            prepareLevel = true;
            startScreen = true;
        }

        // exit game when escape key is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // the stages of the game
        if (stage == LEVEL0_STAGE) {
            level0(input);
        } else if (stage == LEVEL1_STAGE) {
            level1(input);
        } else if (stage == GAME_OVER_STAGE) {
            gameEndMessage("GAME OVER!");
        } else if (stage == GAME_WON_STAGE) {
            gameEndMessage("CONGRATULATIONS!");
        }
    }
}
