import bagel.*;
import bagel.util.*;

public abstract class Entity extends MovingObject {

    protected static final int ABILITY_ACTIVE_MS = 1000;
    protected static final int ABILITY_COOLDOWN_MS = 2000;
    protected static final int INVINCIBLE_MS = 3000;
    protected static final int MS_TO_SEC = 1000;

    protected static final int IDLE = 0;
    protected static final int ATTACK = 1;

    protected static final int IMG_LEFT = 0;
    protected static final int IMG_RIGHT = 1;
    protected static final int IMG_ABILITY_LEFT = 2;
    protected static final int IMG_ABILITY_RIGHT = 3;

    protected boolean onCooldown = false;

    protected Image[] images;
    private int health;
    private int maxHealth;
    private int damagePoints;
    protected int state;
    protected Timer timer;
    protected boolean isTimerSet;
    protected Timer invincibleTimer;

    /**
     * Constructor for Entity class.
     */
    public Entity(Image[] images, Point position, double speed, int health, int damagePoints) {
        super(images[IMG_LEFT], images[IMG_RIGHT], position, speed);
        this.images = images;
        this.health = health;
        this.maxHealth = health;
        this.damagePoints = damagePoints;
        this.state = IDLE;
        this.isTimerSet = false;
    }

    public abstract void setState(int state);
    public abstract void checkStates();

    /**
     * Get the entity's health.
     * @return Entity's health as an integer.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Get the entity's maximum health.
     * @return Entity's maximum health as an integer.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Get the entity's damage points.
     * @return Entity's damage points as an integer.
     */
    public int getDamagePoints() {
        return damagePoints;
    }

    /**
     * Get the entity's state.
     * @return Entity's state as an integer.
     */
    public int getState() {
        return state;
    }

    /**
     * Set the entity's health.
     * @param health Entity's health.
     */
    public void setHealth(int health) {
        if (health > maxHealth) {
            this.health = maxHealth;
        } else if (health < 0) {
            this.health = 0;
        } else {
            this.health = health;
        }
    }

    /**
     * Get health percentage of the entity.
     * @return Health percentage of the entity.
     */
    public int getHealthPercentage() {
        return (int) Math.round((double) health / maxHealth * 100);
    }

    /**
     * Check if the entity is dead.
     * @return True if the entity is dead, false otherwise.
     */
    public boolean isDead() {
        return health <= 0;
    }

    public boolean isInvincible() {
        return invincibleTimer != null && !invincibleTimer.isFinished(ShadowDimension.frames);
    }
}
