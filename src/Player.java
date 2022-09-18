import bagel.*;
import bagel.util.*;

public class Player extends Entity implements Attacker, Targetable {

    private static final Image[] IMAGES = {
        new Image("res/fae/faeLeft.png"),
        new Image("res/fae/faeRight.png"),
        new Image("res/fae/faeAttackLeft.png"),
        new Image("res/fae/faeAttackRight.png")
    };

    private static final int IMG_LEFT = 0;
    private static final int IMG_RIGHT = 1;

    // constants
    private static final int MAX_PLAYER_HEALTH = 100;
    private static final int DAMAGE_POINTS = 20;
    private static final int SPEED = 2;
    private static final double GATE_X = 950;
    private static final double GATE_Y = 670;

    // instance variables
    private final Image imageLeft;
    private final Image imageRight;
    private String name;
    private Point prevPos;

    /**
     * Constructor for Player class.
     * @param imageLeft Image of the player facing left.
     * @param imageRight Image of the player facing right.
     * @param position Position of the player.
     */
    public Player(Point position) {
        super(IMAGES[IMG_LEFT], IMAGES[IMG_RIGHT], position, SPEED, MAX_PLAYER_HEALTH, DAMAGE_POINTS);
        this.imageLeft = IMAGES[IMG_LEFT];
        this.imageRight = IMAGES[IMG_RIGHT];
        this.prevPos = position;
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
     * Move the player to the specified position.
     * @param position Position to move the player to.
     */
    public void move(Point position) {
        prevPos = getPosition();

        if (position.x > prevPos.x) {
            setImage(imageRight);
        } else if (position.x < prevPos.x) {
            setImage(imageLeft);
        }

        setPosition(position);
    }

    /**
     * Move the player to a given direction.
     */
    @Override
    public void move(Vector2 direction) {
        move(getPosition().asVector().add(direction.mul(SPEED)).asPoint());
    }

    /** 
     * Move the player left.
     */
    public void moveLeft(Boundary boundary) {
        move(Vector2.left);
        if (!boundary.contains(getPosition())) {
            this.setPosition(new Point(boundary.getTopLeft().x, getPosition().y));
        }
    }

    /**
     * Move the player right.
     */
    public void moveRight(Boundary boundary) {
        move(Vector2.right);
        if (!boundary.contains(getPosition())) {
            this.setPosition(new Point(boundary.getBottomRight().x, getPosition().y));
        }
    }

    /**
     * Move the player up.
     */
    public void moveUp(Boundary boundary) {
        move(Vector2.up);
        if (!boundary.contains(getPosition())) {
            this.setPosition(new Point(getPosition().x, boundary.getTopLeft().y));
        }
    }

    /**
     * Move the player down.
     */
    public void moveDown(Boundary boundary) {
        move(Vector2.down);
        if (!boundary.contains(getPosition())) {
            this.setPosition(new Point(getPosition().x, boundary.getBottomRight().y));
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
     * Get the player's previous position.
     * @return Player's previous position.
     */
    public Point getPrevPos() {
        return prevPos;
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
     * @param target
     */
    @Override
    public void inflictDamage(Targetable target) {
        target.takeDamage(DAMAGE_POINTS);
    }

    /**
     * Attack the target by showing an attack animation.
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
