import bagel.*;
import bagel.util.*;

/**
 * All objects that move in the game are MovingObjects.
 */
public abstract class MovingObject extends GameObject {
    
    private final double originalSpeed;
    private double speed;
    private Image imageLeft;
    private Image imageRight;
    private Point prevPos;
    private boolean isLeft;

    /**
     * Constructor for MovingObject class.
     * @param imageLeft Image of the entity facing left.
     * @param imageRight Image of the entity facing right.
     * @param position Position of the entity.
     * @param speed Speed of the entity.
     */
    public MovingObject(Image imageLeft, Image imageRight, Point position, double speed) {
        super(imageRight, position);
        this.imageLeft = imageLeft;
        this.imageRight = imageRight;
        this.prevPos = position;
        this.originalSpeed = speed;
        this.speed = speed;
        this.isLeft = false;
    }

    /**
     * Get the object's previous position.
     * @return Object's previous position.
     */
    public Point getPrevPos() {
        return prevPos;
    }

    /**
     * Get the object's speed.
     * @return Object's speed as an integer.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Set the object's speed.
     * @param speed Object's speed as an integer.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Set the left and right images of the object.
     * @param imageLeft Image of the object facing left.
     * @param imageRight Image of the object facing right.
     */
    public void setImages(Image imageLeft, Image imageRight) {
        this.imageLeft = imageLeft;
        this.imageRight = imageRight;
    }

    /**
     * Update the images of the object.
     */
    public void updateImages() {
        if (isLeft) {
            setImage(imageLeft);
        } else {
            setImage(imageRight);
        }
    }

    /**
     * Move the player to the specified position.
     * @param position Position to move the player to.
     */
    public void move(Point position) {
        prevPos = getPosition();
        setPosition(position);
        if (position.x < prevPos.x) {
            isLeft = true;
        } else if (position.x > prevPos.x) {
            isLeft = false;
        }
        updateImages();
    }

    /**
     * Move the player to a given direction.
     * @param direction Direction to move the player to.
     */
    public void move(Vector2 direction) {
        move(getPosition().asVector().add(direction.mul(speed)).asPoint());
    }

    /** 
     * Move the player left.
     * @param boundary Boundary of the game.
     */
    public void moveLeft(Boundary boundary) {
        move(Vector2.left);
        if (!boundary.contains(getPosition())) {
            this.setPosition(new Point(boundary.getTopLeft().x, getPosition().y));
        }
    }

    /**
     * Move the player right.
     * @param boundary Boundary of the game.
     */
    public void moveRight(Boundary boundary) {
        move(Vector2.right);
        if (!boundary.contains(getPosition())) {
            this.setPosition(new Point(boundary.getBottomRight().x, getPosition().y));
        }
    }

    /**
     * Move the player up.
     * @param boundary Boundary of the game.
     */
    public void moveUp(Boundary boundary) {
        move(Vector2.up);
        if (!boundary.contains(getPosition())) {
            this.setPosition(new Point(getPosition().x, boundary.getTopLeft().y));
        }
    }

    /**
     * Move the player down.
     * @param boundary Boundary of the game.
     */
    public void moveDown(Boundary boundary) {
        move(Vector2.down);
        if (!boundary.contains(getPosition())) {
            this.setPosition(new Point(getPosition().x, boundary.getBottomRight().y));
        }
    }

    /**
     * Make the object face left.
     */
    public void faceLeft() {
        isLeft = true;
    }

    /**
     * Make the object face right.
     */
    public void faceRight() {
        isLeft = false;
    }

    /**
     * Given a timescale, update the object's speed to the original speed multiplied by the timescale. Scales
     * by a factor of 50%.
     * @param timescale Timescale to scale the speed by.
     */
    public void timescaleSpeed(int timescale) {
        if (timescale >= 0) {
            setSpeed(originalSpeed * Math.pow(1.5, Math.abs(timescale)));
        } else {
            setSpeed(originalSpeed * Math.pow(0.5, Math.abs(timescale)));
        }
    }
}
