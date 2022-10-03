import bagel.*;
import bagel.util.*;

public class Demon extends Entity implements Attacker, Targetable {
    
    public static final double PASSIVE_SPEED = 0;
    public static final int DEFAULT_ATTACK_RADIUS = 150;        // in pixels
    public static final int DEFAULT_MAX_HEALTH = 40;            // default max health of a demon
    public static final int DEFAULT_DAMAGE_POINTS = 10;
    public static final Image DEFAULT_DEMON_FIRE = new Image("res/demon/demonFire.png");
    public static final String DEFAULT_DEMON_NAME = "Demon";

    private int attackRadius;
    private Image fireImage;

    private static final Image[] DEFAULT_DEMON_IMAGES = {
        new Image("res/demon/demonLeft.png"),
        new Image("res/demon/demonRight.png"),
        new Image("res/demon/demonInvincibleLeft.png"),
        new Image("res/demon/demonInvincibleRight.png")
    };
    
    private Vector2 direction;
    private Image[] images;

    /**
     * Constructor for Demon class.
     * @param position Position of the demon.
     * @param speed Speed of the demon.
     * @param health Health of the demon.
     * @param damagePoints Damage points of the demon.
     */
    public Demon(Point position, double speed, Vector2 direction) {
        super(DEFAULT_DEMON_IMAGES, position, speed, DEFAULT_MAX_HEALTH, DEFAULT_DAMAGE_POINTS, DEFAULT_DEMON_NAME);
        this.direction = direction;
        this.attackRadius = DEFAULT_ATTACK_RADIUS;
        this.fireImage = DEFAULT_DEMON_FIRE;
        this.images =  DEFAULT_DEMON_IMAGES;
    }

    public Demon(Image fireImage, Image[] images, int attackRadius, int maxHealth, int damagePoints, 
                    Point position, double speed, Vector2 direction, String name) {
        super(images, position, speed, maxHealth, damagePoints, name);
        this.direction = direction;
        this.attackRadius = attackRadius;
        this.fireImage = fireImage;
        this.images = images;
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
     * Get the attack radius of the demon.
     */
    public int getAttackRadius() {
        return attackRadius;
    }

    /**
     * Set the attack radius of the demon.
     */
    public void setAttackRadius(int attackRadius) {
        this.attackRadius = attackRadius;
    }

    /**
     * Inflict damage to the target.
     */
    @Override
    public void inflictDamageTo(Targetable target) {
        target.takeDamage(getDamagePoints());
        printDamage(this, target);
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
            setInvincibleTimer(new Timer(ShadowDimension.frames, INVINCIBLE_MS / MS_TO_SEC));
            setImages(images[IMG_ABILITY_LEFT], images[IMG_ABILITY_RIGHT]);
        }
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
        return this.getRectangle().centre().distanceTo(position) <= attackRadius;
    }

    /**
     * Check if the target is in attack range of the demon.
     * @param target Target to check.
     * @return True if the target is in attack range of the demon, false otherwise.
     */
    public boolean isInAttackRadius(GameObject target) {
        return isInAttackRadius(target.getRectangle().centre());
    }

    /**
     * Shoot fire at the target.
     */
    public Fire shootFireAt(GameObject target) {
        // if the demon is not in attack range of the target, do not shoot fire
        if (!isInAttackRadius(target)) {
            return null;
        }

        // if the demon is in attack range of the target, shoot fire
        DrawOptions options = new DrawOptions();
        double x, y;
        if (target.getPosition().x <= this.getPosition().x && target.getPosition().y <= this.getPosition().y) {
            options.setRotation(2 * Math.PI / 4 * 0);
            x = getRectangle().topLeft().x - fireImage.getWidth();
            y = getRectangle().topLeft().y - fireImage.getHeight();
        } else if (target.getPosition().x <= this.getPosition().x && target.getPosition().y > this.getPosition().y) {
            options.setRotation(2 * Math.PI / 4 * 3);
            x = getRectangle().bottomLeft().x - fireImage.getWidth();
            y = getRectangle().bottomLeft().y;
        } else if (target.getPosition().x > this.getPosition().x && target.getPosition().y <= this.getPosition().y) {
            options.setRotation(2 * Math.PI / 4 * 1);
            x = getRectangle().topRight().x;
            y = getRectangle().topRight().y - fireImage.getHeight();
        } else if (target.getPosition().x > this.getPosition().x && target.getPosition().y > this.getPosition().y) {
            options.setRotation(2 * Math.PI / 4 * 2);
            x = getRectangle().bottomRight().x;
            y = getRectangle().bottomRight().y;
        } else {
            return null;
        }
        return new Fire(this, fireImage, new Point(x, y), options, getDamagePoints());
    }

    @Override
    public void draw() {
        HealthBar.drawHealthBar(this);
        super.draw();
    }
}
