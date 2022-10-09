import bagel.*;
import bagel.util.*;

/**
 * Barrier class that contains game objects which block the player from moving through them.
 */
public class Barrier extends GameObject {

    /**
     * Constructor for Barrier class.
     * @param image Image of the barrier.
     * @param position Position of the barrier.
     */
    public Barrier(Image image, Point position) {
        super(image, position);
    }

    /**
     * Constructor for Barrier class.
     * @param image Image of the barrier as a string.
     * @param position Position of the barrier.
     */
    public Barrier(String image, Point position) {
        super(new Image(image), position);
    }

    /**
     * Bounce the entity back to the previous position.
     * @param entity Entity to bounce back.
     */
    public void block(MovingObject entity) {
        Point prevPos = entity.getPrevPos();
        entity.setPosition(prevPos);
    }
}
