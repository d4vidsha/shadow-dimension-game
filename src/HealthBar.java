import bagel.*;
import bagel.util.*;

public class HealthBar extends Message {
    
    /**
     * Constructor for HealthBar class.
     * @param font Font of the health bar.
     * @param position Position of the health bar.
     * @param text Message of the health bar.
     */
    public HealthBar(Font font, String text, Point position) {
        super(font, text, position);
    }
}
