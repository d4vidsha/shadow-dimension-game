import bagel.*;
import bagel.util.*;

public class Demon extends Entity implements Attacker, Targetable {
    
    private final boolean IS_NAVEC;

    /**
     * Constructor for Demon class.
     * @param imageLeft Image of the demon facing left.
     * @param imageRight Image of the demon facing right.
     * @param position Position of the demon.
     * @param speed Speed of the demon.
     * @param health Health of the demon.
     * @param damagePoints Damage points of the demon.
     * @param isNavec Whether the demon is Navec.
     */
    public Demon(String imageLeft, String imageRight, Point position, int speed, int health, int damagePoints, boolean isNavec) {
        super(imageLeft, imageRight, position, speed, health, damagePoints);
        this.IS_NAVEC = isNavec;
    }

    /**
     * Constructor for Demon class.
     * @param imageLeft Image of the demon facing left.
     * @param imageRight Image of the demon facing right.
     * @param position Position of the demon.
     * @param speed Speed of the demon.
     * @param health Health of the demon.
     * @param damagePoints Damage points of the demon.
     * @param isNavec Whether the demon is Navec.
     */
    public Demon(Image imageLeft, Image imageRight, Point position, int speed, int health, int damagePoints, boolean isNavec) {
        super(imageLeft, imageRight, position, speed, health, damagePoints);
        this.IS_NAVEC = isNavec;
    }

    /**
     * Get whether the demon is Navec.
     * @return Whether the demon is Navec.
     */
    public boolean isNavec() {
        return IS_NAVEC;
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
