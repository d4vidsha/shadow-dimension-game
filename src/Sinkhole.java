import bagel.*;
import bagel.util.*;

public class Sinkhole extends GameObject implements Attacker {

    public static final int DAMAGE_POINTS = 30;

    private static final Image IMAGE = new Image("res/sinkhole.png");
    private final int damagePoints;
    
    /**
     * Constructor for Sinkhole class.
     * @param image Image of the sinkhole.
     * @param position Position of the sinkhole.
     */
    public Sinkhole(Point position) {
        super(IMAGE, position);
        this.damagePoints = DAMAGE_POINTS;
    }

    /**
     * Constructor for Sinkhole class.
     * @param image Image of the sinkhole.
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
     * Inflict damage to the player.
     * @param player Player to inflict damage to.
     */
    public void inflictDamageTo(Targetable target) {
        target.takeDamage(damagePoints);
        Player player = (Player) target;
        Entity.printDamage(this, player);
    }

    /**
     * Attack.
     */
    @Override
    public void attack() {
        System.out.println("Sinkhole is dealing damage");
    }

    /**
     * To string method for sinkhole.
     */
    @Override
    public String toString() {
        return "Sinkhole at " + getPosition() + " with " + damagePoints + " damage points";
    }
}
