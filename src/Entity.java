import bagel.*;
import bagel.util.*;

public abstract class Entity extends MovingObject {
    
    protected static final int IDLE = 0;
    protected static final int ATTACK = 1;
    protected static final int INVINCIBLE = 2;

    private int health;
    private int maxHealth;
    private int damagePoints;
    private int state;

    /**
     * Constructor for Entity class.
     */
    public Entity(String imageLeft, String imageRight, Point position, int speed, int health, int damagePoints) {
        super(imageLeft, imageRight, position, speed);
        this.health = health;
        this.maxHealth = health;
        this.damagePoints = damagePoints;
        this.state = IDLE;
    }

    /**
     * Constructor for Entity class.
     */
    public Entity(Image imageLeft, Image imageRight, Point position, int speed, int health, int damagePoints) {
        super(imageLeft, imageRight, position, speed);
        this.health = health;
        this.maxHealth = health;
        this.damagePoints = damagePoints;
        this.state = IDLE;
    }

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
     * Set the entity's state.
     * @param state Entity's state as an integer.
     */
    public void setState(int state) {
        this.state = state;
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
}
