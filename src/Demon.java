import bagel.*;
import bagel.util.*;

public class Demon extends Entity implements Attacker, Targetable {
    
    private static final Image[] IMAGES = {
        new Image("res/demon/demonLeft.png"),
        new Image("res/demon/demonRight.png"),
        new Image("res/demon/demonInvincibleLeft.png"),
        new Image("res/demon/demonInvincibleRight.png"),
    };

    private static final int IMG_LEFT = 0;
    private static final int IMG_RIGHT = 1;
    private static final int IMG_INVINCIBLE_LEFT = 2;
    private static final int IMG_INVINCIBLE_RIGHT = 3;
    
    /**
     * Constructor for Demon class.
     * @param position Position of the demon.
     * @param speed Speed of the demon.
     * @param health Health of the demon.
     * @param damagePoints Damage points of the demon.
     */
    public Demon(Point position, int speed, int health, int damagePoints) {
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
