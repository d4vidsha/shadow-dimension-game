import bagel.util.*;

public interface Barrier {

    /**
     * Bounce the entity back to the previous position.
     * @param entity Entity to bounce back.
     */
    default void bounce(MovableEntity entity) {
        Point prevPos = entity.getPrevPos();
        entity.setPosition(prevPos);
    }
}
