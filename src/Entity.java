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

    private int health;
    private int maxHealth;
    private int damagePoints;
    private String name;
    private int state;
    private Timer invincibleTimer;

    /**
     * Constructor for Entity class.
     */
    public Entity(Image[] images, Point position, double speed, int health, int damagePoints, String name) {
        super(images[IMG_LEFT], images[IMG_RIGHT], position, speed);
        this.health = health;
        this.maxHealth = health;
        this.damagePoints = damagePoints;
        this.name = name;
        this.state = IDLE;
    }

    public abstract void checkStates();

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
        return invincibleTimer != null && !invincibleTimer.isFinished(ShadowDimension.frames);
    }

    /**
     * Print inflicted damage to the console. Attacker and Targetable are upcasted to Entity.
     * @param attacker Attacker.
     * @param target Target.
     */
    public static void printDamage(Attacker attacker, Targetable target) {
        Entity A = (Entity) attacker;
        Entity B = (Entity) target;
        System.out.println(String.format(
            "%s inflicts %d damage points on %s. %s's current health: %d/%d",
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
     * @param A Sinkhole that inflicts damage.
     * @param B Player that takes damage.
     */
    public static void printDamage(Sinkhole A, Player B) {
        System.out.println(String.format(
            "%s inflicts %d damage points on %s. %s's current health: %d/%d",
            A.getClass().getSimpleName(),
            A.getDamagePoints(),
            B.getName(),
            B.getName(),
            B.getHealth(),
            B.getMaxHealth())
        );
    }
}
