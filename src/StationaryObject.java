import bagel.util.*;

public abstract class StationaryObject extends GameObject {
    
    /**
     * Constructor for StationaryObject class.
     * @param image Image of the stationary object.
     * @param position Position of the stationary object.
     */
    public StationaryObject(String image, Point position) {
        super(image, position);
    }
    
}
