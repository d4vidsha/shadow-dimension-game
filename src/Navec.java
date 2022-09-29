import bagel.*;
import bagel.util.*;

public class Navec extends Entity implements Attacker, Targetable  {
    
    private static final Image[] IMAGES = {
        new Image("res/navec/navecLeft.png"),
        new Image("res/navec/navecRight.png"),
        new Image("res/navec/navecInvincibleLeft.png"),
        new Image("res/navec/navecInvincibleRight.png")
    };

    private Vector2 direction;

    /**
     * Constructor for Navec class.
     * @param position Position of Navec.
     * @param speed Speed of Navec.
     * @param health Health of Navec.
     * @param damagePoints Damage points of Navec.
     */
    public Navec(Point position, int speed, int health, int damagePoints, Vector2 direction) {
        super(IMAGES, position, speed, health, damagePoints);
        this.direction = direction;
    }

    /**
     * Get direction of Navec.
     */
    public Vector2 getDirection() {
        return direction;
    }

    /**
     * Inflict damage to the target.
     */
    @Override
    public void inflictDamageTo(Targetable target) {
        target.takeDamage(getDamagePoints());
    }

    /**
     * Attack the target by showing an attack animation.
     */
    @Override
    public void attack() {
        setState(ATTACK);
    }

    /**
     * Take damage from the attacker.
     */
    @Override
    public void takeDamage(int damage) {
        this.setHealth(this.getHealth() - damage);
    }

    @Override
    public void setState(int state) {

    }

    @Override
    public void checkStates() {

    }
}
