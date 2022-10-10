import bagel.*;
import bagel.util.*;
import java.io.*;
import java.util.*;

/**
 * ShadowDimension is the main class of the game.
 * @author David Sha
 */

public class ShadowDimension extends AbstractGame {

    /**
     * The total number of frames rendered since the game started.
     */
    public static int frames = 0;

    /**
     * The stage of the game.
     */
    public static int stage = Level.LEVEL0_STAGE;

    // constants
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final String CSV_DELIMITER = ",";
    private static final Vector2[] DIRECTIONS = {
        Vector2.left,
        Vector2.right,
        Vector2.up,
        Vector2.down
    };
    private static final String GAME_OVER_MESSAGE = "GAME OVER!";
    private static final String GAME_WON_MESSAGE = "CONGRATULATIONS!";

    /**
     * Game title.
     */
    public static final String GAME_TITLE = "Shadow Dimension";

    // game objects
    private static final String PLAYER = "Fae";
    private static final String WALL = "Wall";
    private static final String SINKHOLE = "Sinkhole";
    private static final String TREE = "Tree";
    private static final String DEMON = "Demon";
    private static final String NAVEC = "Navec";
    private static final String[] OBJECT_NAMES = {PLAYER, WALL, SINKHOLE, TREE, DEMON, NAVEC};

    // boundary
    private static final String TOPLEFT = "TopLeft";
    private static final String BOTTOMRIGHT = "BottomRight";

    // stationary images
    private static final String WALL_IMAGE = "res/wall.png";
    private static final String TREE_IMAGE = "res/tree.png";

    // error messages
    private static final String INVALID_FACE = "Invalid face value.";
    private static final String NO_BOUNDARY_SPECIFIED = "No boundary specified.";
    private static final String RUN_TOO_LONG = "Frames exceeded maximum value. Game has been running for too long.";

    // game variables
    private Level0 level0;
    private Level1 level1;

    /**
     * Constructor for ShadowDimension class.
     */
    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        this.level0 = new Level0();
        this.level1 = new Level1();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Get the stage of the game.
     * @return stage of the game.
     */
    public static int getStage() {
        return stage;
    }

    /**
     * Set the stage of the game.
     * @param stage stage of the game.
     */
    public static void setStage(int stage) {
        ShadowDimension.stage = stage;
    }

    /**
     * Read the objects from the csv file.
     * @param csv Name of the csv file.
     * @param boundary Boundary of the game which will be added to GameObjects.
     * @return Array of GameObjects.
     */
    public static GameObject[] readObjects(String csv, int maxObjects) {
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
                            Barrier wall = new Barrier(WALL_IMAGE, pos);
                            objects[i] = wall;
                            break;
                        case SINKHOLE:
                            Sinkhole sinkhole = new Sinkhole(pos);
                            objects[i] = sinkhole;
                            break;
                        case TREE:
                            Barrier tree = new Barrier(TREE_IMAGE, pos);
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
                                throw new RuntimeException(INVALID_FACE);
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
    public static Boundary readBoundary(String csv) {
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
                    case TOPLEFT:
                        topLeft = new Point(x, y);
                        break;
                    case BOTTOMRIGHT:
                        bottomRight = new Point(x, y);
                        break;
                }
            }
            br.close();

            // create the boundary
            if (topLeft != null && bottomRight != null) {
                boundary = new Boundary(topLeft, bottomRight);
            } else {
                throw new RuntimeException(NO_BOUNDARY_SPECIFIED);
            }
            
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
    public static boolean contains(String[] arr, String targetValue) {
        for(String s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }
    
    /**
     * Get player object from the array of objects. We assume the first object
     * in the array is the player.
     * @param objects Array of game objects.
     * @return
     */
    public static Player getPlayer(GameObject[] objects) {
        return (Player) objects[0];
    }

    /**
     * Add an object to the array of game objects.
     * @param objects Array of game objects.
     * @param gameObject Object to add.
     */
    public static GameObject[] addGameObject(GameObject[] objects, GameObject gameObject) {
        ArrayList<GameObject> objectsList = new ArrayList<>(Arrays.asList(objects));
        objectsList.add(gameObject);
        return objectsList.toArray(new GameObject[objectsList.size()]);
    }

    /**
     * Remove the object from the array of game objects.
     * @param objects Array of game objects.
     * @param gameObject Object to remove.
     * @return Array of game objects with the object removed.
     */
    public static GameObject[] removeGameObject(GameObject[] objects, GameObject gameObject) {
        ArrayList<GameObject> objectsList = new ArrayList<>(Arrays.asList(objects));
        objectsList.remove(gameObject);
        return objectsList.toArray(new GameObject[objectsList.size()]);
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
            throw new RuntimeException(RUN_TOO_LONG);
        }

        // fastforward to level 1 when W key is pressed
        if (input.wasPressed(Keys.W)) {
            stage = Level.LEVEL1_STAGE;
        }

        // exit game when escape key is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // the stages of the game
        if (stage == Level.LEVEL0_STAGE) {
            level0.run(input);
        } else if (stage == Level.LEVEL1_STAGE) {
            level1.run(input);
        } else if (stage == Level.GAME_OVER_STAGE) {
            Level.gameEndMessage(GAME_OVER_MESSAGE);
        } else if (stage == Level.GAME_WON_STAGE) {
            Level.gameEndMessage(GAME_WON_MESSAGE);
        }
    }
}
