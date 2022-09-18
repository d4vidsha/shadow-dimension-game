import bagel.*;
import bagel.util.*;

public class Wall extends GameObject implements Barrier {
    
    private static final Image IMAGE = new Image("res/wall.png");

    /**
     * Constructor for Wall class.
     * @param image Image of the wall.
     * @param position Position of the wall.
     */
    public Wall(Point position) {
        super(IMAGE, position);
    }
}
