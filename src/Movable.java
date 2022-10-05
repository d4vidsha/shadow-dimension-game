import bagel.util.*;

public interface Movable {
    
    /**
     * Move the object in the specified direction.
     * @param direction Direction to move the object in.
     */
    void move(Vector2 direction);
}