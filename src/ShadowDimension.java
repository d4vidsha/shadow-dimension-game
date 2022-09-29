import bagel.*;
import bagel.util.*;
import java.io.*;
import java.util.*;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2022
 * @author David Sha
 */

public class ShadowDimension extends AbstractGame {

    // the total number of frames rendered so far
    public static int frames = 0;

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
    // private GameObject[] sinkholes;
    // private GameObject[] walls;
    // private GameObject[] trees;
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

    // /**
    //  * Get the walls from the array of all objects.
    //  * @param objects Array of game objects.
    //  * @return Array of walls.
    //  */
    // public GameObject[] getWalls(GameObject[] objects) {
    //     ArrayList<GameObject> walls = new ArrayList<>();
    //     for (GameObject gameObject : objects) {
    //         if (gameObject instanceof Wall) {
    //             walls.add(gameObject);
    //         }
    //     }
    //     return walls.toArray(new GameObject[walls.size()]);
    // }

    // /**
    //  * Get the sinkholes from the array of all objects.
    //  * @param objects Array of game objects.
    //  * @return Array of sinkholes.
    //  */
    // public GameObject[] getSinkholes(GameObject[] objects) {
    //     ArrayList<GameObject> sinkholes = new ArrayList<>();
    //     for (GameObject gameObject : objects) {
    //         if (gameObject instanceof Sinkhole) {
    //             sinkholes.add(gameObject);
    //         }
    //     }
    //     return sinkholes.toArray(new GameObject[sinkholes.size()]);
    // }

    // /**
    //  * Get the trees from the array of all objects.
    //  * @param objects Array of game objects.
    //  * @return Array of trees.
    //  */
    // public GameObject[] getTrees(GameObject[] objects) {
    //     ArrayList<GameObject> trees = new ArrayList<>();
    //     for (GameObject gameObject : objects) {
    //         if (gameObject instanceof Tree) {
    //             trees.add(gameObject);
    //         }
    //     }
    //     return trees.toArray(new GameObject[trees.size()]);
    // }

    // /**
    //  * Get the demons from the array of all objects.
    //  * @param objects
    //  * @return
    //  */
    // public GameObject[] getDemons(GameObject[] objects) {
    //     ArrayList<GameObject> demons = new ArrayList<>();
    //     for (GameObject object : objects) {
    //         if (object instanceof Demon) {
    //             demons.add(object);
    //         }
    //     }
    //     return demons.toArray(new GameObject[demons.size()]);
    // }

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
        // sinkholes = getSinkholes(gameObjects);
        // walls = getWalls(gameObjects);
    }

    /**
     * Prepare the game for level 1.
     */
    private void prepareLevel1() {
        boundary = readBoundary(LEVEL1_CSV);
        objects = readObjects(LEVEL1_CSV, LEVEL1_MAX_OBJECTS);
        gameObjects = getGameObjects();
        // sinkholes = getSinkholes(gameObjects);
        // walls = getWalls(gameObjects);
        // trees = getTrees(gameObjects);
    }

    /**
     * The first level of the game.
     * @param input Input from the user which controls the player.
     * @param player Player object that is moved.
     */
    private void level0(Input input) {

        // start screen
        if (startScreen) {
            startlevel0();
            if (input.wasPressed(Keys.SPACE)) {
                startScreen = false;
            }
            return;
        }

        // end screen
        if (endScreen) {
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

        Player player = getPlayer(objects);

        // move the player
        player.update(input, boundary);
        player.checkStates();

        // draw everything
        drawBackground(LEVEL0_BACKGROUND);
        HealthBar.drawHealthBar(player);
        player.draw(boundary);
        drawObjects(gameObjects);

        // check if player is dead
        if (player.getHealthPercentage() <= 0) {
            stage = GAME_OVER_STAGE;
        }

        // check if player hit a wall or sinkhole
        if (player.collides(gameObjects, Sinkhole.class)) {

            // get specific sinkhole collided with and inflict damage
            Sinkhole sinkhole = (Sinkhole) player.getCollidedObject(gameObjects, Sinkhole.class);
            sinkhole.inflictDamageTo(player);

            // remove sinkhole from game
            gameObjects = removeGameObject(gameObjects, sinkhole);

        } else if (player.collides(gameObjects, Wall.class)) {

            // block player from moving
            Wall wall = (Wall) player.getCollidedObject(gameObjects, Wall.class);
            wall.block(player);
        }

        // if necessary, display winning message and move to next stage after 3 seconds
        if (getPlayer(objects).isAtGate()) {
            endScreen = true;
            level0EndScreen = new Timer(frames, LEVEL0_END_SCREEN_WAIT_SECONDS);
        }

        // attack if player presses A
        if (input.wasPressed(Keys.A)) {
            player.attack();
        }
    }

    /**
     * The second level of the game.
     * @param input Input from the user which controls the player.
     * @param player Player object that is moved.
     */
    private void level1(Input input) {

        // start screen
        if (startScreen) {
            startlevel1();
            if (input.wasPressed(Keys.SPACE)) {
                startScreen = false;
            }
            return;
        }

        Player player = getPlayer(objects);

        // move the player, demons and Navec
        player.update(input, boundary);
        player.checkStates();

        // move demons
        for (GameObject object : gameObjects) {
            if (!(object instanceof Demon)) {
                continue;
            }
            Demon demon = (Demon) object;
            demon.move(demon.getDirection());

            // If the demon hits a barrier, reverse the direction
            if (demon.collides(gameObjects, Tree.class) || demon.collides(gameObjects, Sinkhole.class) || !boundary.contains(demon.getPosition())) {

                // Reverse the direction
                demon.setDirection(demon.getDirection().mul(-1));

                // Move the demon in the opposite direction
                demon.move(demon.getDirection());
            }
        }

        // draw everything
        drawBackground(LEVEL1_BACKGROUND);
        HealthBar.drawHealthBar(player);
        player.draw(boundary);
        drawObjects(gameObjects);

        // check if player is dead
        if (player.getHealthPercentage() <= 0) {
            stage = GAME_OVER_STAGE;
        }

        // check if player hit a wall, sinkhole or tree
        if (player.collides(gameObjects, Sinkhole.class)) {

            // get specific sinkhole collided with and inflict damage
            Sinkhole sinkhole = (Sinkhole) player.getCollidedObject(gameObjects, Sinkhole.class);
            sinkhole.inflictDamageTo(player);

            // remove sinkhole from game
            gameObjects = removeGameObject(gameObjects, sinkhole);

        } else if (player.collides(gameObjects, Tree.class)) {

            // block player from moving
            Tree tree = (Tree) player.getCollidedObject(gameObjects, Tree.class);
            tree.block(player);
        }
        
        // attack if player presses A
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

        // check if any demons are dead or if they're not invincible anymore
        for (GameObject gameObject : gameObjects) {
            if (!(gameObject instanceof Demon) && !(gameObject instanceof Navec)) {
                continue;
            }
            Demon demon = (Demon) gameObject;
            if (demon.getHealthPercentage() <= 0) {
                if (demon instanceof Navec) {
                    stage = GAME_WON_STAGE;
                }
                gameObjects = removeGameObject(gameObjects, demon);
            }
            demon.checkStates();
        }

        // check if demons should attack
        for (GameObject gameObject : gameObjects) {
            if (!(gameObject instanceof Demon) && !(gameObject instanceof Navec)) {
                continue;
            }
            Demon demon = (Demon) gameObject;
            if (demon.isInAttackRadius(player)) {
                demon.attack();
                Fire fire = demon.shootFireAt(player);
                fire.draw();
                if (player.collides(fire)) {
                    fire.inflictDamageTo(player);
                }
            }
        }
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
