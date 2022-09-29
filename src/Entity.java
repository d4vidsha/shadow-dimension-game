import bagel.*;
import bagel.util.*;

public abstract class Entity extends MovingObject {

    private static final int ABILITY_ACTIVE_MS = 1000;
    private static final int ABILITY_COOLDOWN_MS = 2000;
    private static final int MS_TO_SEC = 1000;

    protected static final int IDLE = 0;
    protected static final int ATTACK = 1;
    protected static final int INVINCIBLE = 2;

    private static final int IMG_LEFT = 0;
    private static final int IMG_RIGHT = 1;
    private static final int IMG_ABILITY_LEFT = 2;
    private static final int IMG_ABILITY_RIGHT = 3;

    private boolean onCooldown = false;

    private Image[] images;
    private int health;
    private int maxHealth;
    private int damagePoints;
    private int state;
    private Timer timer;
    private boolean isTimerSet;

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

        // set a timer and the images for the respective state
        if (!isTimerSet) {
            if (state == ATTACK) {
                timer = new Timer(ShadowDimension.frames, ABILITY_ACTIVE_MS / MS_TO_SEC);
                setImages(images[IMG_ABILITY_LEFT], images[IMG_ABILITY_RIGHT]);
            } else if (state == INVINCIBLE) {
                timer = new Timer(ShadowDimension.frames, ABILITY_ACTIVE_MS / MS_TO_SEC);
                setImages(images[IMG_ABILITY_LEFT], images[IMG_ABILITY_RIGHT]); 
        } else if (state == IDLE) {
                timer = new Timer(ShadowDimension.frames, ABILITY_COOLDOWN_MS / MS_TO_SEC);
                setImages(images[IMG_LEFT], images[IMG_RIGHT]);
            } else {
                throw new IllegalArgumentException("Invalid state");
            }
            isTimerSet = true;
        }
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

    /**
     * Check state and update image accordingly. Keeps track of time to determine when to switch back to idle state.
     */
    public void checkState() {
        if (state == IDLE) {
        }

        if ((state == ATTACK || state == INVINCIBLE) && !onCooldown) {
            if (timer.isFinished(ShadowDimension.frames)) {
                isTimerSet = false;
                setState(IDLE);
                onCooldown = true;
            }
        }

        if (onCooldown) {
            if (state != IDLE) {
                setState(IDLE);
            }
            if (timer.isFinished(ShadowDimension.frames)) {
                isTimerSet = false;
                onCooldown = false;
            }
        }
        
        updateImages();
    }
}
