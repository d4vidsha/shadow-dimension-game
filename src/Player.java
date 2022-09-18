import bagel.*;
import bagel.util.*;

public class Player extends Entity implements Attacker, Targetable {

    private static final Image[] IMAGES = {
        new Image("res/fae/faeLeft.png"),
        new Image("res/fae/faeRight.png"),
        new Image("res/fae/faeAttackLeft.png"),
        new Image("res/fae/faeAttackRight.png")
    };

    // constants
    private static final int MAX_PLAYER_HEALTH = 100;
    private static final int DAMAGE_POINTS = 20;
    private static final int SPEED = 2;
    private static final double GATE_X = 950;
    private static final double GATE_Y = 670;

    // instance variables
    private String name;

    /**
     * Constructor for Player class.
     * @param imageLeft Image of the player facing left.
     * @param imageRight Image of the player facing right.
     * @param position Position of the player.
     */
    public Player(Point position) {
        super(IMAGES, position, SPEED, MAX_PLAYER_HEALTH, DAMAGE_POINTS);
        this.name = "Fae";
    }

    /**
     * Update the player's position given inputs.
     * @param input Input object to get inputs from.
     */
    public void update(Input input, Boundary boundary) {
        if (input.isDown(Keys.LEFT)) {
            this.moveLeft(boundary);
        } else if (input.isDown(Keys.RIGHT)) {
            this.moveRight(boundary);
        } else if (input.isDown(Keys.UP)) {
            this.moveUp(boundary);
        } else if (input.isDown(Keys.DOWN)) {
            this.moveDown(boundary);
        }
    }

    /**
     * Check if the player has won by being at the gate.
     * @return True if the player has won, false otherwise.
     */
    public boolean isAtGate() {
        Point pos = getPosition();
        return pos.x >= GATE_X && pos.y >= GATE_Y;
    }

    /** 
     * Get maximum health of the player.
     * @return Maximum health of the player.
     */
    public int getMaxHealth() {
        return MAX_PLAYER_HEALTH;
    }

    /**
     * Get the player's name.
     * @return Player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the player's name.
     * @param name Player's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Inflict damage to the target.
     * @param target Target to inflict damage to.
     */
    @Override
    public void inflictDamage(Targetable target) {
        target.takeDamage(DAMAGE_POINTS);
    }

    /**
     * Attack the target by showing the attack images.
     */
    @Override
    public void attack() {
        super.setState(ATTACK);
    }

    /**
     * Take damage to the player.
     * @param damage Amount of damage to inflict.
     */
    @Override
    public void takeDamage(int damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
