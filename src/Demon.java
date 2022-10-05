import bagel.*;
import bagel.util.*;

public class Demon extends Entity implements Attacker, Targetable {
    
    // speed of passive demon
    public static final double PASSIVE_SPEED = 0;
    
    // qualities of default demon
    public static final int DEFAULT_ATTACK_RADIUS = 150;
    public static final int DEFAULT_MAX_HEALTH = 40;
    public static final int DEFAULT_DAMAGE_POINTS = 10;
    public static final Image DEFAULT_FIRE = new Image("res/demon/demonFire.png");
    public static final String DEFAULT_NAME = "Demon";
    private static final Image[] DEFAULT_DEMON_IMAGES = {
        new Image("res/demon/demonLeft.png"),
        new Image("res/demon/demonRight.png"),
        new Image("res/demon/demonInvincibleLeft.png"),
        new Image("res/demon/demonInvincibleRight.png")
    };
    
    // variables for demon
    private int attackRadius;
    private Image fireImage;
    private Vector2 direction;
    private Image[] images;

    /**
     * Constructor for Demon class.
     * @param position Position of the demon.
     * @param speed Speed of the demon.
     * @param direction Direction the demon moves in.
     */
    public Demon(Point position, double speed, Vector2 direction) {
        super(DEFAULT_DEMON_IMAGES, position, speed, DEFAULT_MAX_HEALTH, DEFAULT_DAMAGE_POINTS, DEFAULT_NAME);
        this.direction = direction;
        this.attackRadius = DEFAULT_ATTACK_RADIUS;
        this.fireImage = DEFAULT_FIRE;
        this.images =  DEFAULT_DEMON_IMAGES;
    }

    /**
     * Constructor for Demon class.
     * @param fireImage Image of the fire shot by demon.
     * @param images Images of the demon.
     * @param attackRadius Attack radius of the demon.
     * @param maxHealth Maximum health of the demon.
     * @param damagePoints Damage points of the demon.
     * @param position Position of the demon.
     * @param speed Speed of the demon.
     * @param direction Direction the demon moves in.
     * @param name Name of the demon.
     */
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
     * @param direction Direction to move in for the demon.
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
     * @param attackRadius Attack radius of the demon.
     */
    public void setAttackRadius(int attackRadius) {
        this.attackRadius = attackRadius;
    }

    /**
     * Inflict damage to the target.
     * @param target Target to inflict damage to.
     */
    @Override
    public void inflictDamageTo(Targetable target) {
        target.takeDamage(getDamagePoints());
        printDamage(this, target);
    }

    /**
     * Attack the target by shooting fire.
     */
    @Override
    public void attack() {
        setState(ATTACK);
    }

    /**
     * Take damage from the attacker.
     * @param damage Damage points to take.
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

    /**
     * Check the state of the demon. Check if the demon is invincible still or not.
     */
    @Override
    public void checkStates() {
        if (!isInvincible()) {
            setImages(images[IMG_LEFT], images[IMG_RIGHT]);
        }
    }

    /**
     * Check if the target is in attack range of the demon. All measurements are taken from the centre of the demon.
     * @param position Position of the target.
     * @return True if the target is in attack range of the demon, false otherwise.
     */
    public boolean isInAttackRadius(Point position) {
        return this.getRectangle().centre().distanceTo(position) <= attackRadius;
    }

    /**
     * Check if the target is in attack range of the demon. All measurements are taken from the centre of the demon
     * and the target.
     * @param target Target to check.
     * @return True if the target is in attack range of the demon, false otherwise.
     */
    public boolean isInAttackRadius(GameObject target) {
        return isInAttackRadius(target.getRectangle().centre());
    }

    /**
     * Shoot fire at the target. Returns the Fire object created.
     * @param target Target to shoot fire at.
     * @return Fire object created.
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

    /**
     * Draw the demon with it's health bar.
     */
    @Override
    public void draw() {
        HealthBar.drawHealthBar(this);
        super.draw();
    }
}
