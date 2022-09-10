import bagel.*;
import bagel.util.*;

public class Message {

    private final Point position;
    private final String text;
    private final Font font;

    /**
     * Constructor for Message class.
     * @param font Font of the message.
     * @param text The contents of the message.
     */
    public Message(Font font, String text) {
        this.position = new Point((Window.getWidth() - font.getWidth(text))/2.0, Window.getHeight()/2.0);
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
