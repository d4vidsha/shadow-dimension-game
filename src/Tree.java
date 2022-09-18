import bagel.*;
import bagel.util.*;

public class Tree extends GameObject implements Barrier {
    
    private static final Image IMAGE = new Image("res/tree.png");

    /**
     * Constructor for Tree class.
     * @param image Image of the tree.
     * @param position Position of the tree.
     */
    public Tree(Point position) {
        super(IMAGE, position);
    }
}