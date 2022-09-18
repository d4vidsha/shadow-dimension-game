import bagel.*;
import bagel.util.*;

public abstract class MovingObject extends GameObject implements Movable {
    
    private final int speed;
    private Image imageLeft;
    private Image imageRight;
    private Point prevPos;

    /**
     * Constructor for MovingObject class.
     * @param imageLeft Image of the entity facing left.
     * @param imageRight Image of the entity facing right.
     * @param position Position of the entity.
     * @param speed Speed of the entity.
     */
    public MovingObject(Image imageLeft, Image imageRight, Point position, int speed) {
        super(imageRight, position);
        this.imageLeft = imageLeft;
        this.imageRight = imageRight;
        this.prevPos = position;
        this.speed = speed;
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
    public int getSpeed() {
        return speed;
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
     * @param direction Direction to move the player to.
     */
    @Override
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
}
