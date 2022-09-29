import bagel.*;
import bagel.util.*;

public class Demon extends Entity implements Attacker, Targetable {
    
    public static final double PASSIVE_SPEED = 0;
    public static final int ATTACK_RADIUS = 150;    // pixels
    public static final int MAX_HEALTH = 40;
    public static final int DAMAGE_POINTS = 0;      // demon has no damage points as fire is its attack

    private static final Image[] IMAGES = {
        new Image("res/demon/demonLeft.png"),
        new Image("res/demon/demonRight.png"),
        new Image("res/demon/demonInvincibleLeft.png"),
        new Image("res/demon/demonInvincibleRight.png")
    };
    
    private Vector2 direction;

    /**
     * Constructor for Demon class.
     * @param position Position of the demon.
     * @param speed Speed of the demon.
     * @param health Health of the demon.
     * @param damagePoints Damage points of the demon.
     */
    public Demon(Point position, double speed, Vector2 direction) {
        super(IMAGES, position, speed, MAX_HEALTH, DAMAGE_POINTS);
        this.direction = direction;
    }

    /**
     * Get the direction of the demon.
     */
    public Vector2 getDirection() {
        return direction;
    }

    /**
     * Set the direction of the demon.
     */
    public void setDirection(Vector2 direction) {
        this.direction = direction;
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
        // if the demon is invincible, do not take damage, otherwise take damage and become invincible
        if (!isInvincible()) {
            this.setHealth(this.getHealth() - damage);
            System.out.println("Demon health: " + this.getHealth());
            this.invincibleTimer = new Timer(ShadowDimension.frames, INVINCIBLE_MS / MS_TO_SEC);
            setImages(images[IMG_ABILITY_LEFT], images[IMG_ABILITY_RIGHT]);
        }
    }

    @Override
    public void setState(int state) {
        this.state = state;
        
        // if (state == ATTACK) {
            
        // }
    }

    @Override
    public void checkStates() {
        if (!isInvincible()) {
            setImages(images[IMG_LEFT], images[IMG_RIGHT]);
        }
    }

    /**
     * Check if the target is in attack range of the demon.
     * @param position Position of the target.
     * @return True if the target is in attack range of the demon, false otherwise.
     */
    public boolean isInAttackRadius(Point position) {
        return this.getRectangle().centre().distanceTo(position) <= ATTACK_RADIUS;
    }

    /**
     * Check if the target is in attack range of the demon.
     * @param target Target to check.
     * @return True if the target is in attack range of the demon, false otherwise.
     */
    public boolean isInAttackRadius(GameObject target) {
        return isInAttackRadius(target.getPosition());
    }

    /**
     * Shoot fire at the target.
     */
    public void shootFireAt(GameObject target) {
        // TO DO
    }

    @Override
    public void draw() {
        HealthBar.drawHealthBar(this);
        super.draw();
    }
}
