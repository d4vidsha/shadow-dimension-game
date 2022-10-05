import bagel.*;
import bagel.util.*;

public class HealthBar extends Message {

    // health percentage error message
    private static final String HEALTH_PERCENTAGE_ERROR = "Health percentage out of range";
    private static final String PERCENT = "%";

    /**
     * Constructor for HealthBar class.
     * @param font Font of the health bar.
     * @param text Message of the health bar.
     * @param position Position of the health bar.
     */
    public HealthBar(Font font, String text, Point position) {
        super(font, text, position);
    }

    /**
     * Draw the health bar for the player.
     * @param player Player object to draw the health bar for.
     */
    public static void drawHealthBar(Player player) {
        int health = player.getHealthPercentage();
        Message healthBar = new Message(FONT30, player.getHealthPercentage() + PERCENT, new Point(20, 25));
        DrawOptions drawOptions = new DrawOptions();

        if (65 <= health && health <= 100) {
            drawOptions.setBlendColour(GREEN);
        } else if (35 <= health && health < 65) {
            drawOptions.setBlendColour(ORANGE);
        } else if (0 <= health && health < 35) {
            drawOptions.setBlendColour(RED);
        } else {
            throw new RuntimeException(HEALTH_PERCENTAGE_ERROR);
        }
        healthBar.draw(drawOptions);
    }

    /**
     * Draw the health bar for an entity.
     * @param entity Entity object to draw the health bar for.
     */
    public static void drawHealthBar(Entity entity) {
        int health = entity.getHealthPercentage();
        Message healthBar = new Message(FONT15, 
                                        entity.getHealthPercentage() + PERCENT, 
                                        new Point(entity.getPosition().x, entity.getPosition().y - 6));
        DrawOptions drawOptions = new DrawOptions();

        if (65 <= health && health <= 100) {
            drawOptions.setBlendColour(GREEN);
        } else if (35 <= health && health < 65) {
            drawOptions.setBlendColour(ORANGE);
        } else if (0 <= health && health < 35) {
            drawOptions.setBlendColour(RED);
        } else {
            throw new RuntimeException(HEALTH_PERCENTAGE_ERROR);
        }
        healthBar.draw(drawOptions);
    }
}
