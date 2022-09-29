import bagel.*;
import bagel.util.*;

public class Fire extends GameObject implements Attacker {

    private DrawOptions options;
    private int damagePoints;

    /**
     * Constructor for Fire class.
     * @param image Image of the fire.
     * @param position Position of the fire.
     */
    public Fire(Image image, Point position, DrawOptions options, int damagePoints) {
        super(image, position);
        this.options = options;
        this.damagePoints = damagePoints;
    }

    /**
     * Draw the fire to the screen.
     */
    public void draw() {
        Point position = getPosition();
        getImage().drawFromTopLeft(position.x, position.y, options);
    }

    /**
     * Attack.
     */
    @Override
    public void attack() {
        System.out.println("Fire is dealing damage");
    }

    /**
     * Inflict damage to target.
     */
    @Override
    public void inflictDamageTo(Targetable target) {
        target.takeDamage(damagePoints);
    }
}
