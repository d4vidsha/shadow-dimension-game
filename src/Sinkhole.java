import bagel.*;
import bagel.util.*;

/**
 * Sinkholes can deal damage to the player and are stationary objects that become cleared once the player steps on them.
 */
public class Sinkhole extends GameObject implements Attacker {

    private static final Image IMAGE = new Image("res/sinkhole.png");
    private static final String ATTACK_MESSAGE = "Sinkhole is dealing damage";

    /**
     * Default sinkhole damage points.
     */
    public static final int DAMAGE_POINTS = 30;
    
    private final int damagePoints;
    
    /**
     * Constructor for Sinkhole class.
     * @param position Position of the sinkhole.
     */
    public Sinkhole(Point position) {
        super(IMAGE, position);
        this.damagePoints = DAMAGE_POINTS;
    }

    /**
     * Constructor for Sinkhole class.
     * @param position Position of the sinkhole.
     * @param damagePoints Damage points the sinkhole can inflict.
     */
    public Sinkhole(Point position, int damagePoints) {
        super(IMAGE, position);
        this.damagePoints = damagePoints;
    }

    /**
     * Get the damage points of the sinkhole.
     * @return Damage points of the sinkhole as an integer.
     */
    public int getDamagePoints() {
        return damagePoints;
    }

    /**
     * Inflict damage to entity.
     * @param target Target to inflict damage to.
     */
    public void inflictDamageTo(Entity target) {
        target.takeDamage(damagePoints);
        Player player = (Player) target;
        Entity.printDamage(this, player);
    }

    /**
     * Attack. Prints to console.
     */
    @Override
    public void attack() {
        System.out.println(ATTACK_MESSAGE);
    }
}
