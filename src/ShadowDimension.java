import bagel.*;
import bagel.util.*;
import java.io.*;
import java.util.*;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
 * @author David Sha
 */

public class ShadowDimension extends AbstractGame {

    // constants
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int MAX_OBJECTS = 60;
    private static final String CSV_DELIMITER = ",";
    private static final String CSV_PATH = "res/level0.csv";

    // fonts
    private static final String FONT_PATH = "res/frostbite.ttf";
    private final Font FONT75 = new Font(FONT_PATH, 75);
    private final Font FONT40 = new Font(FONT_PATH, 40);
    private final Font FONT30 = new Font(FONT_PATH, 30);

    // game title
    private static final double GAME_TITLE_X = 260;
    private static final double GAME_TITLE_Y = 250;
    private static final String GAME_TITLE = "Shadow Dimension";

    // stages of the game
    private static final int START_SCREEN = 0;
    private static final int GAME_SCREEN = 1;
    private static final int GAME_OVER_SCREEN = 2;
    private static final int GAME_WIN_SCREEN = 3;

    // colours
    private static final Colour GREEN = new Colour(0, 0.8, 0.2);
    private static final Colour ORANGE = new Colour(0.9, 0.6, 0);
    private static final Colour RED = new Colour(1, 0, 0);

    // game objects
    private static final String[] OBJECT_NAMES = {"Player", "Wall", "Sinkhole"};

    // initialising the game
    private int stage = START_SCREEN;
    private final Boundary boundary = readBoundary(CSV_PATH);
    private final GameObject[] objects = readObjects(CSV_PATH, boundary);
    private GameObject[] stationaryObjects = getStationaryGameObjects();
    private GameObject[] sinkholes = getSinkholes(stationaryObjects);
    private final GameObject[] walls = getWalls(stationaryObjects);

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
    private GameObject[] readObjects(String csv, Boundary boundary) {
        GameObject.boundary = boundary;
        GameObject[] objects = new GameObject[MAX_OBJECTS];
        
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

                // create the object based on the type
                if (contains(OBJECT_NAMES, values[0])) {
                    switch (values[0]) {
                        case "Player":
                            Player player = new Player("res/faeLeft.png", "res/faeRight.png", pos);
                            objects[i] = player;
                            break;
                        case "Wall":
                            Wall wall = new Wall("res/wall.png", pos);
                            objects[i] = wall;
                            break;
                        case "Sinkhole":
                            Sinkhole sinkhole = new Sinkhole("res/sinkhole.png", pos);
                            objects[i] = sinkhole;
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
     * Get the stationary objects from the array of all objects.
     * Stationary objects are objects that do not move. For example
     * walls and sinkholes are stationary objects. A player is NOT
     * a stationary object.
     * In this function we assume that the first object in the array
     * is a player.
     * @return Array of stationary objects.
     */
    public GameObject[] getStationaryGameObjects() {
        return Arrays.copyOfRange(objects, 1, objects.length);
    }

    /**
     * Draw the health bar for the player.
     * @param player Player object to draw the health bar for.
     */
    private void drawHealthBar(Player player) {
        int health = player.getHealthPercentage();
        Message healthBar = new Message(FONT30, player.getHealthPercentage() + "%", new Point(20, 25));
        DrawOptions drawOptions = new DrawOptions();

        if (65 <= health && health <= 100) {
            drawOptions.setBlendColour(GREEN);
        } else if (35 <= health && health < 65) {
            drawOptions.setBlendColour(ORANGE);
        } else if (0 <= health && health < 35) {
            drawOptions.setBlendColour(RED);
        } else {
            System.out.println("Health percentage out of range");
            System.exit(1);
        }
        healthBar.draw(drawOptions);
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
    private void drawBackground() {
        Image bgImage = new Image("res/background0.png");
        bgImage.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }

    /**
     * Get the walls from the array of all objects.
     * @param objects Array of game objects.
     * @return Array of walls.
     */
    public GameObject[] getWalls(GameObject[] objects) {
        ArrayList<GameObject> walls = new ArrayList<>();
        for (GameObject gameObject : objects) {
            if (gameObject instanceof Wall) {
                walls.add(gameObject);
            }
        }
        return walls.toArray(new GameObject[walls.size()]);
    }

    /**
     * Get the sinkholes from the array of all objects.
     * @param objects Array of game objects.
     * @return Array of sinkholes.
     */
    public GameObject[] getSinkholes(GameObject[] objects) {
        ArrayList<GameObject> sinkholes = new ArrayList<>();
        for (GameObject gameObject : objects) {
            if (gameObject instanceof Sinkhole) {
                sinkholes.add(gameObject);
            }
        }
        return sinkholes.toArray(new GameObject[sinkholes.size()]);
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
     * Start screen for the game.
     */
    private void startStage() {
        Point gameTitlePos = new Point(GAME_TITLE_X, GAME_TITLE_Y);
        Point gameInstructionPos = new Point(GAME_TITLE_X + 90, GAME_TITLE_Y + 190);
        String gameInstructionMsg = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
        Message gameTitle = new Message(FONT75, GAME_TITLE, gameTitlePos);
        Message gameInstruction = new Message(FONT40, gameInstructionMsg, gameInstructionPos);
        gameTitle.draw();
        gameInstruction.draw();
    }

    /**
     * Game screen for the game, where the player can move around and either
     * win or lose.
     * @param input Input from the user which controls the player.
     * @param player Player object that is moved.
     */
    private void gameStage(Input input, Player player) {
        drawBackground();
        drawHealthBar(player);
        player.draw();
        drawObjects(stationaryObjects);
        player.update(input);

        if (player.isAtGate()) {
            stage = GAME_WIN_SCREEN;
        }
    }

    /**
     * Game over screen for the game, where the player has lost.
     */
    private void gameOverStage() {
        Message lose = new Message(FONT75, "GAME OVER!");
        lose.draw();
    }

    /**
     * Game win screen for the game, where the player has won.
     */
    private void gameWinStage() {
        Message win = new Message(FONT75, "CONGRATULATIONS!");
        win.draw();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     * This is where the game stages are updated.
     * @param input Input from the user.
     */
    @Override
    protected void update(Input input) {
        // assume player is the first object in the array
        Player player = (Player) objects[0];

        // exit game when escape key is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // start game when space key is pressed
        if (input.wasPressed(Keys.SPACE) && stage == START_SCREEN) {
            stage = GAME_SCREEN;
        }
        
        // check if player is dead
        if (player.getHealthPercentage() <= 0) {
            stage = GAME_OVER_SCREEN;
        }

        // check if player hit a wall or sinkhole
        if (player.collides(sinkholes)) {
            // get specific sinkhole collided with and inflict damage
            Sinkhole sinkhole = (Sinkhole) player.getCollidedObject(sinkholes);
            int damagePoints = sinkhole.getDamagePoints();
            player.inflictDamage(damagePoints);
            // remove sinkhole from game
            sinkholes = removeGameObject(sinkholes, sinkhole);
            stationaryObjects = removeGameObject(stationaryObjects, sinkhole);
            System.out.println("Sinkhole inflicted " + damagePoints + " damage points on " + player.getName()
                 + ". " + player.getName() + "'s current health: " + player.getHealth() + "/" + player.getMaxHealth());
        } else if (player.collides(walls)) {
            // bounce player off wall
            Wall wall = (Wall) player.getCollidedObject(walls);
            wall.bounce(player);
        }

        // the stages of the game
        if (stage == START_SCREEN) {
            startStage();
        } else if (stage == GAME_SCREEN) {
            gameStage(input, player);
        } else if (stage == GAME_OVER_SCREEN) {
            gameOverStage();
        } else if (stage == GAME_WIN_SCREEN) {
            gameWinStage();
        }
    }
}
