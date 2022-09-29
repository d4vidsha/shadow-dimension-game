import bagel.*;
import bagel.util.*;

public class Navec extends Demon {
    
    public static final int ATTACK_RADIUS = 200;                // in pixels
    public static final int MAX_HEALTH = 2 * Demon.DEFAULT_MAX_HEALTH;
    public static final int DAMAGE_POINTS = 0;                  // demon has no damage points as fire is its attack

    private static final Image[] IMAGES = {
        new Image("res/navec/navecLeft.png"),
        new Image("res/navec/navecRight.png"),
        new Image("res/navec/navecInvincibleLeft.png"),
        new Image("res/navec/navecInvincibleRight.png")
    };

    /**
     * Constructor for Navec class.
     * @param position Position of Navec.
     * @param speed Speed of Navec.
     * @param health Health of Navec.
     * @param damagePoints Damage points of Navec.
     */
    public Navec(Point position, double speed, Vector2 direction) {
        super(IMAGES, ATTACK_RADIUS, MAX_HEALTH, DAMAGE_POINTS, position, speed, direction);
    }
}
