import bagel.*;
import bagel.util.*;

/**
 * Level 0 of the game.
 */
public class Level0 extends Level{
    private static final String LEVEL0_INSTRUCTIONS = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private static final int LEVEL0_END_SCREEN_WAIT_SECONDS = 3;
    private static final String LEVEL_COMPLETION_MESSAGE = "LEVEL COMPLETE!";
    private static final double GAME_TITLE_X = 260;
    private static final double GAME_TITLE_Y = 250;
    private static final String LEVEL0_BACKGROUND = "res/background0.png";
    private static final String LEVEL0_CSV = "res/level0.csv";
    private static final int LEVEL0_MAX_OBJECTS = 60;

    private Timer level0EndScreenTimer;

    /**
     * Constructor for Level0 class.
     */
    public Level0() {
        super();
        this.level0EndScreenTimer = null;
    }

    /**
     * Check if the player has beaten level 0. If so, move to the level 0 end screen.
     * @param input
     */
    private void checkCompletion(Player player) {
        // if necessary, display winning message and move to next stage after 3 seconds
        if (player.isAtGate()) {
            setEndScreen(true);
            level0EndScreenTimer = new Timer(ShadowDimension.frames, LEVEL0_END_SCREEN_WAIT_SECONDS);
        }
    }

    /**
     * Display start screen for level 0 until the player presses space.
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
     * Display end screen for level 0 until time is up.
     */
    protected void displayEndScreen() {
        Level.gameEndMessage(LEVEL_COMPLETION_MESSAGE);

        // wait
        if (level0EndScreenTimer.isFinished(ShadowDimension.frames)) {
            // begin next stage
            ShadowDimension.setStage(LEVEL1_STAGE);
        }
        
        return;
    }

    /**
     * Prepare the game for level 0.
     */
    protected void prepare() {
        setBoundary(ShadowDimension.readBoundary(LEVEL0_CSV));
        setObjects(ShadowDimension.readObjects(LEVEL0_CSV, LEVEL0_MAX_OBJECTS));
        setGameObjects(extractGameObjects());
    }

    /**
     * Start screen for level 0. This also prepares the level.
     */
    protected void startScreen() {
        Point gameTitlePos = new Point(GAME_TITLE_X, GAME_TITLE_Y);
        Point gameInstructionPos = new Point(GAME_TITLE_X + 90, GAME_TITLE_Y + 190);
        Message gameTitle = new Message(FONT75, ShadowDimension.GAME_TITLE, gameTitlePos);
        Message gameInstruction = new Message(FONT40, LEVEL0_INSTRUCTIONS, gameInstructionPos);
        gameTitle.draw();
        gameInstruction.draw();

        // prepare the level if necessary
        if (getPrepareLevel()) {
            prepare();
            setPrepareLevel(false);
        }
    }

    /**
     * The first level of the game.
     * @param input Input from the user which controls the player.
     * @param player Player object that is moved.
     */
    public void run(Input input) {

        if (getStartScreen()) {
            displayStartScreen(input);
            return;
        }

        if (getEndScreen()) {
            displayEndScreen();
            return;
        }

        Player player = ShadowDimension.getPlayer(getObjects());

        // move and update the player
        player.update(input, getBoundary());
        playerAttack(input, player);

        // draw everything
        drawBackground(LEVEL0_BACKGROUND);
        HealthBar.drawHealthBar(player);
        drawObjects(getGameObjects());
        player.draw(getBoundary());

        // check everything
        player.checkStates();
        checkCollisions(player);
        checkPlayerDeath(player);
        checkCompletion(player);
    }
}