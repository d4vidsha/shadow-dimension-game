import bagel.*;
import bagel.util.*;

public class Message {

    // fonts
    private static final String FONT_PATH = "res/frostbite.ttf";
    public static final Font FONT75 = new Font(FONT_PATH, 75);
    public static final Font FONT40 = new Font(FONT_PATH, 40);
    public static final Font FONT30 = new Font(FONT_PATH, 30);
    public static final Font FONT15 = new Font(FONT_PATH, 15);

    // colours
    public static final Colour GREEN = new Colour(0, 0.8, 0.2);
    public static final Colour ORANGE = new Colour(0.9, 0.6, 0);
    public static final Colour RED = new Colour(1, 0, 0);

    private final Point position;
    private final String text;
    private final Font font;

    /**
     * Constructor for Message class.
     * @param font Font of the message.
     * @param text The contents of the message.
     */
    public Message(Font font, String text) {
        this.position = new Point((Window.getWidth() - font.getWidth(text)) / 2.0, Window.getHeight() / 2.0);
        this.text = text;
        this.font = font;
    }

    /**
     * Constructor for Message class
     * @param font Font of the message.
     * @param text The contents of the message.
     * @param position Bottom left position of the message.
     */
    public Message(Font font, String text, Point position) {
        this.position = position;
        this.text = text;
        this.font = font;
    }
    
    /**
     * Draw the message on the screen.
     */
    public void draw() {
        font.drawString(text, position.x, position.y);
    }

    /**
     * Draw the message on the screen given DrawOptions.
     * @param options DrawOptions to draw the message.
     */
    public void draw(DrawOptions options) {
        font.drawString(text, position.x, position.y, options);
    }
}
