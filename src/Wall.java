import bagel.util.*;

public class Wall extends GameObject {
    
    /**
     * Constructor for Wall class.
     * @param image Image of the wall.
     * @param position Position of the wall.
     */
    public Wall(String image, Point position) {
        super(image, position);
    }

    /**
     * Bounce the player back to the previous position.
     * @param player Player to bounce back.
     */
    public void bounce(Player player) {
        Point prevPos = player.getPrevPos();
        player.setPosition(prevPos);
    }
}
