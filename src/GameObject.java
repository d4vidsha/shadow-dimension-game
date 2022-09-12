import bagel.*;
import bagel.util.*;

public abstract class GameObject {

    // constants
    public static final int IDLE = 0;
    public static final int ATTACK = 1;
    public static final int INVINCIBLE = 2;

    // common attributes for all game objects
    private Image image;
    private Rectangle rectangle;

    /**
     * Constructor for GameObject class.
     * @param image Image of the game object.
     * @param position Position of the game object.
     */
    public GameObject(String image, Point position) {
        this.image = new Image(image);
        this.rectangle = deriveRectangle(position, this.image);
    }

    /**
     * Draw the object to the screen.
     */
    public void draw() {
        Point position = getPosition();
        image.drawFromTopLeft(position.x, position.y);
    }

    /**
     * Draw the object to the screen given a boundary. If the object is outside the boundary, 
     * an exception will be thrown.
     * @param boundary Boundary to draw the object in.
     */
    public void draw(Boundary boundary) {
        Point position = getPosition();
        if (boundary.contains(position)) {
            draw();
        } else {
            throw new RuntimeException("Position is outside of boundary");
        }
    }

    /**
     * Get the position of the game object.
     * @return Position of the game object as a point.
     */
    public Point getPosition() {
        return rectangle.topLeft();
    }

    /**
     * Set the position of the game object.
     * @param position Position of the game object as a point.
     */
    public void setPosition(Point position) {
        rectangle.moveTo(position);
    }

    /**
     * Get the image of the game object.
     * @return Image of the game object.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Set the image of the game object.
     * @param image Image of the game object.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Get the rectangle of the game object.
     * @return Rectangle of the game object.
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Set the rectangle of the game object.
     * @param rectangle Rectangle of the game object.
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Derive the rectangle from the position and image.
     * @param position Position of the game object.
     * @param image Image of the game object.
     * @return Rectangle of the game object.
     */
    private Rectangle deriveRectangle(Point position, Image image) {
        return new Rectangle(position, image.getWidth(), image.getHeight());
    }

    /**
     * Check if the game object collides with another game object.
     * @param gameObjects Game objects to check collision with.
     * @return True if the game object collides with another game object, false otherwise.
     */
    public boolean collides(GameObject[] gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getRectangle().intersects(this.getRectangle())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the collided object from a given array of game objects.
     * @param gameObjects Array of game objects to check collision with.
     * @return Collided game object, if any. Otherwise, null.
     */
    public GameObject getCollidedObject(GameObject[] gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getRectangle().intersects(this.getRectangle())) {
                return gameObject;
            }
        }
        return null;
    }
}
