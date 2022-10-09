import bagel.*;
import bagel.util.*;

/**
 * Fire which is created by demons. Fire can damage the player.
 */
public class Fire extends GameObject implements Attacker {

    private DrawOptions options;
    private int damagePoints;
    private final Demon fromDemon;

    private static final String ATTACK_MESSAGE = "Fire dealt damage";

    /**
     * Constructor for Fire class.
     * @param demon Demon that fired the fire.
     * @param image Image of the fire.
     * @param position Position of the fire.
     * @param options Draw options of the fire, which determines the rotation of the fire.
     * @param damagePoints Damage points of the fire.
     */
    public Fire(Demon demon, Image image, Point position, DrawOptions options, int damagePoints) {
        super(image, position);
        this.options = options;
        this.damagePoints = damagePoints;
        this.fromDemon = demon;
    }

    /**
     * Get the damage points of the fire.
     * @return Damage points of the fire as an integer.
     */
    public int getDamagePoints() {
        return damagePoints;
    }

    /**
     * Draw the fire to the screen.
     */
    public void draw() {
        Point position = getPosition();
        getImage().drawFromTopLeft(position.x, position.y, options);
    }

    /**
     * Attack the target by printing to the console.
     */
    @Override
    public void attack() {
        System.out.println(ATTACK_MESSAGE);
    }

    /**
     * Inflict damage to target and print to console.
     * @param target Target to inflict damage to.
     */
    @Override
    public void inflictDamageTo(Entity target) {
        target.takeDamage(damagePoints);
        if (!target.isInvincible()) {
            target.makeInvincible();
        }
        Entity.printDamage(fromDemon, target);
    }
}
