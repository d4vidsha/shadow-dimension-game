import bagel.util.*;

public class Sinkhole extends GameObject {

    private final int damagePoints;
    
    /**
     * Constructor for Sinkhole class.
     * @param image Image of the sinkhole.
     * @param position Position of the sinkhole.
     */
    public Sinkhole(String image, Point position) {
        super(image, position);
        this.damagePoints = 30;
    }

    /**
     * Constructor for Sinkhole class.
     * @param image Image of the sinkhole.
     * @param position Position of the sinkhole.
     * @param damagePoints Damage points the sinkhole can inflict.
     */
    public Sinkhole(String image, Point position, int damagePoints) {
        super(image, position);
        this.damagePoints = damagePoints;
    }

    /**
     * Get the damage points of the sinkhole.
     * @return Damage points of the sinkhole as an integer.
     */
    public int getDamagePoints() {
        return damagePoints;
    }

    /**
     * Inflict damage to the player.
     * @param player Player to inflict damage to.
     */
    public void inflictDamage(Player player) {
        player.takeDamage(damagePoints);
        System.out.println("Sinkhole inflicted " + damagePoints + " damage points on " + player.getName()
            + ". " + player.getName() + "'s current health: " + player.getHealth() + "/" + player.getMaxHealth());
    }
}
