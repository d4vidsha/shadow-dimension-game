import bagel.*;
import bagel.util.*;

public class Fire extends GameObject {
    
    private static final Image IMAGE = new Image("res/fire.png");

    /**
     * Constructor for Fire class.
     * @param image Image of the fire.
     * @param position Position of the fire.
     */
    public Fire(Point position) {
        super(IMAGE, position);
    }
}
