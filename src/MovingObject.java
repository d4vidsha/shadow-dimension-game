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
     * Constructor for MovingObject class.
     * @param imageLeft Image of the entity facing left as String.
     * @param imageRight Image of the entity facing right as String.
     * @param position Position of the entity.
     * @param speed Speed of the entity.
     */
    public MovingObject(String imageLeft, String imageRight, Point position, int speed) {
        super(imageRight, position);
        this.imageLeft = new Image(imageLeft);
        this.imageRight = new Image(imageRight);
        this.prevPos = position;
        this.speed = speed;
    }

    /**
     * Get the entity's previous position.
     * @return Entity's previous position.
     */
    public Point getPrevPos() {
        return prevPos;
    }
}
