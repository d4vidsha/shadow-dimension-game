import bagel.*;
import bagel.util.*;

public class Navec extends Entity implements Attacker, Targetable  {
    
    private static final Image[] IMAGES = {
        new Image("res/navec/navecLeft.png"),
        new Image("res/navec/navecRight.png"),
        new Image("res/navec/navecInvincibleLeft.png"),
        new Image("res/navec/navecInvincibleRight.png")
    };

    private static final int IMG_LEFT = 0;
    private static final int IMG_RIGHT = 1;
    private static final int IMG_INVINCIBLE_LEFT = 2;
    private static final int IMG_INVINCIBLE_RIGHT = 3;

    /**
     * Constructor for Navec class.
     * @param position Position of Navec.
     * @param speed Speed of Navec.
     * @param health Health of Navec.
     * @param damagePoints Damage points of Navec.
     */
    public Navec(Point position, int speed, int health, int damagePoints) {
        super(IMAGES[IMG_LEFT], IMAGES[IMG_RIGHT], position, speed, health, damagePoints);
    }

    /**
     * Move the demon.
     */
    @Override
    public void move(Vector2 direction) {
        // implement this method
    }

    /**
     * Inflict damage to the target.
     */
    @Override
    public void inflictDamage(Targetable target) {
        target.takeDamage(getDamagePoints());
    }

    /**
     * Attack the target by showing an attack animation.
     */
    @Override
    public void attack() {
        super.setState(ATTACK);
    }

    /**
     * Take damage from the attacker.
     */
    @Override
    public void takeDamage(int damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
