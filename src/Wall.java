import bagel.util.*;

public class Wall extends StationaryObject implements Barrier {
    
    /**
     * Constructor for Wall class.
     * @param image Image of the wall.
     * @param position Position of the wall.
     */
    public Wall(String image, Point position) {
        super(image, position);
    }
}
