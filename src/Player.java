import bagel.*;
import bagel.util.*;

public class Player extends GameObject {

    // constants
    private static final int MAX_HEALTH = 100;
    private static final int SPEED = 2;
    private static final double WIN_X = 950;
    private static final double WIN_Y = 670;

    // instance variables
    private final Image imageLeft;
    private final Image imageRight;
    private int health;
    private Point prevPos;
    private String name;

    /**
     * Constructor for Player class.
     * @param imageLeft Image of the player facing left.
     * @param imageRight Image of the player facing right.
     * @param position Position of the player.
     */
    public Player(String imageLeft, String imageRight, Point position) {
        super(imageRight, position);
        this.imageLeft = new Image(imageLeft);
        this.imageRight = new Image(imageRight);
        this.health = MAX_HEALTH;
        this.prevPos = position;
        this.name = "Fae";
    }

    /**
     * Update the player's position given inputs.
     * @param input Input object to get inputs from.
     */
    public void update(Input input) {
        if (input.isDown(Keys.LEFT)) {
            this.moveLeft();
        } else if (input.isDown(Keys.RIGHT)) {
            this.moveRight();
        } else if (input.isDown(Keys.UP)) {
            this.moveUp();
        } else if (input.isDown(Keys.DOWN)) {
            this.moveDown();
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
     * Move the player left.
     */
    public void moveLeft() {
        Point oldPos = getPosition();
        Point newPos = new Point(oldPos.x - SPEED, oldPos.y);
        if (boundary.contains(newPos)) {
            this.move(newPos);
        }
    }

    /**
     * Move the player right.
     */
    public void moveRight() {
        Point oldPos = getPosition();
        Point newPos = new Point(oldPos.x + SPEED, oldPos.y);
        if (boundary.contains(newPos)) {
            this.move(newPos);
        }
    }

    /**
     * Move the player up.
     */
    public void moveUp() {
        Point oldPos = getPosition();
        Point newPos = new Point(oldPos.x, oldPos.y - SPEED);
        if (boundary.contains(newPos)) {
            this.move(newPos);
        }
    }

    /**
     * Move the player down.
     */
    public void moveDown() {
        Point oldPos = getPosition();
        Point newPos = new Point(oldPos.x, oldPos.y + SPEED);
        if (boundary.contains(newPos)) {
            this.move(newPos);
        }
    }

    /**
     * Check if the player has won by being at the gate.
     * @return True if the player has won, false otherwise.
     */
    public boolean isAtGate() {
        Point pos = getPosition();
        return pos.x >= WIN_X && pos.y >= WIN_Y;
    }

    /**
     * Get the player's health.
     * @return Player's health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set the player's health.
     * @param health Player's health.
     */
    public void setHealth(int health) {
        if (health > MAX_HEALTH) {
            this.health = MAX_HEALTH;
        } else if (health < 0) {
            this.health = 0;
        } else {
            this.health = health;
        }
    }

    /**
     * Get health percentage of the player.
     * @return Health percentage of the player.
     */
    public int getHealthPercentage() {
        return (int) Math.round((double) health / MAX_HEALTH * 100);
    }

    /**
     * Inflict damage to the player.
     * @param damage Amount of damage to inflict.
     */
    public void inflictDamage(int damage) {
        this.setHealth(this.getHealth() - damage);
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
        return MAX_HEALTH;
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
}
