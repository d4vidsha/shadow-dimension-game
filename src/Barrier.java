import bagel.*;
import bagel.util.*;

public class Barrier extends GameObject {

    public Barrier(Image image, Point position) {
        super(image, position);
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
