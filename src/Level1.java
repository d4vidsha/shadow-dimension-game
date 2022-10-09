import bagel.*;
import bagel.util.*;

/**
 * Level 1 of the game.
 */
public class Level1 extends Level {
    
    private static final String LEVEL1_INSTRUCTIONS = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private static final String LEVEL1_BACKGROUND = "res/background1.png";
    private static final String LEVEL1_CSV = "res/level1.csv";
    private static final int LEVEL1_MAX_OBJECTS = 29;
    private Timescale timescale;

    /**
     * Constructor for Level1 class.
     */
    public Level1() {
        super();
        this.timescale = null;
    }

    /**
     * Display start screen for level 1 until the player presses space.
     * @param input Input object.
     */
    protected void displayStartScreen(Input input) {
        startScreen();
        if (input.wasPressed(Keys.SPACE)) {
            setStartScreen(false);
        }
        return;
    }

    /**
     * Prepare the game for level 1.
     */
    protected void prepare() {
        setBoundary(ShadowDimension.readBoundary(LEVEL1_CSV));
        setObjects(ShadowDimension.readObjects(LEVEL1_CSV, LEVEL1_MAX_OBJECTS));
        setGameObjects(extractGameObjects());
        if (timescale == null) {
            timescale = new Timescale(getGameObjects());
        }
        timescale.update();
    }

    /**
     * Start screen for level 1. This also prepares the level.
     */
    protected void startScreen() {
        Point gameInstructionPos = new Point(350, 350);
        Message gameInstruction = new Message(FONT40, LEVEL1_INSTRUCTIONS, gameInstructionPos);
        gameInstruction.draw();

        // prepare the level if necessary
        if (getPrepareLevel()) {
            prepare();
            setPrepareLevel(false);
        }
    }

    /**
     * The second level of the game.
     * @param input Input from the user which controls the player.
     * @param player Player object that is moved.
     */
    public void run(Input input) {

        if (getStartScreen()) {
            displayStartScreen(input);
            return;
        }

        Player player = ShadowDimension.getPlayer(getObjects());
        timescale.controls(input);

        // move the player and demons (including Navec)
        player.update(input, getBoundary());
        playerAttack(input, player);
        moveDemons();

        // draw everything
        drawBackground(LEVEL1_BACKGROUND);
        HealthBar.drawHealthBar(player);
        drawObjects(getGameObjects());
        demonsAttack(player);
        player.draw(getBoundary());

        // check everything
        player.checkStates();
        checkCollisions(player);
        checkPlayerDeath(player);
        checkDemons();
    }
}
