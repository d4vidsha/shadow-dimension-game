import bagel.*;
import bagel.util.*;

/**
 * Entity class which have features that are common to all entities in the game such as health points and damage points.
 */
public abstract class Entity extends MovingObject {

    // duration of player attack/cooldown ability
    protected static final int ABILITY_ACTIVE_MS = 1000;
    protected static final int ABILITY_COOLDOWN_MS = 2000;

    // duration of invincibility after taking damage for both demons and player
    protected static final int INVINCIBLE_MS = 3000;
    
    // other constants
    protected static final int MS_TO_SEC = 1000;
    protected static final int IDLE = 0;
    protected static final int ATTACK = 1;
    protected static final int IMG_LEFT = 0;
    protected static final int IMG_RIGHT = 1;
    protected static final int IMG_ABILITY_LEFT = 2;
    protected static final int IMG_ABILITY_RIGHT = 3;

    // damage string format
    private static final String DAMAGE_FORMAT = "%s inflicts %d damage points on %s. %s's current health: %d/%d";

    private int health;
    private int maxHealth;
    private int damagePoints;
    private String name;
    private int state;
    private Timer invincibleTimer;

    /**
     * Constructor for Entity class.
     * @param images Images of the entity.
     * @param position Position of the entity.
     * @param speed Speed of the entity.
     * @param health Health of the entity.
     * @param damagePoints Damage points of the entity.
     * @param name Name of the entity.
     */
    public Entity(Image[] images, Point position, double speed, int health, int damagePoints, String name) {
        super(images[IMG_LEFT], images[IMG_RIGHT], position, speed);
        this.health = health;
        this.maxHealth = health;
        this.damagePoints = damagePoints;
        this.name = name;
        this.state = IDLE;
    }

    /**
     * Check the state of the entity.
     */
    public abstract void checkStates();
    
    /**
     * Take damage to the entity.
     * @param damage Damage points to take.
     */
    public abstract void takeDamage(int damage);

    /**
     * Get the player's name.
     * @return Player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the player's name.
     * @param name Player's name.
     */
    public void setName(String name) {
        this.name = name;
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
     * @return Entity's state as an integer.
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

    /**
     * Get the entity's invincibility timer.
     * @return Entity's invincibility timer.
     */
    public Timer getInvincibleTimer() {
        return invincibleTimer;
    }

    /**
     * Set the entity's invincibility timer.
     * @param invincibleTimer Entity's invincibility timer.
     */
    public void setInvincibleTimer(Timer invincibleTimer) {
        this.invincibleTimer = invincibleTimer;
    }

    /**
     * Check if the entity is invincible.
     * @return True if the entity is invincible, false otherwise.
     */
    public boolean isInvincible() {
        return invincibleTimer != null && !invincibleTimer.isFinished(ShadowDimension.getFrames());
    }

    /**
     * Make the entity invincible.
     */
    public void makeInvincible() {
        setInvincibleTimer(new Timer(ShadowDimension.getFrames(), INVINCIBLE_MS / MS_TO_SEC));
    }

    /**
     * Print inflicted damage to the console. Attacker and Targetable are upcasted to Entity.
     * @param attacker Attacker.
     * @param target Target.
     */
    public static void printDamage(Attacker attacker, Entity target) {
        Entity A = (Entity) attacker;
        Entity B = (Entity) target;
        System.out.println(String.format(
            DAMAGE_FORMAT,
            A.getName(),
            A.getDamagePoints(),
            B.getName(),
            B.getName(),
            B.getHealth(),
            B.getMaxHealth())
        );
    }

    /**
     * Print inflicted damage to the console for sinkhole and player interaction.
     * @param sinkhole Sinkhole that inflicts damage.
     * @param player Player that takes damage.
     */
    public static void printDamage(Sinkhole sinkhole, Player player) {
        System.out.println(String.format(
            DAMAGE_FORMAT,
            sinkhole.getClass().getSimpleName(),
            sinkhole.getDamagePoints(),
            player.getName(),
            player.getName(),
            player.getHealth(),
            player.getMaxHealth())
        );
    }
}
