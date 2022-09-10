import bagel.*;
import bagel.util.*;

public abstract class GameObject {
    
    // the game objects have the same boundary for the game
    protected static Boundary boundary;

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
     * Constructor for GameObject class.
     * @param image Image of the game object.
     * @param position Position of the game object.
     * @param boundary Boundary of the game object.
     */
    public GameObject(String image, Point position, Boundary boundary) {
        this.image = new Image(image);
        this.rectangle = deriveRectangle(position, this.image);
        GameObject.boundary = boundary;
    }

    /**
     * Draw the object to the screen.
     */
    public void draw() {
        Point position = getPosition();
        if (boundary.contains(position)) {
            image.drawFromTopLeft(position.x, position.y);
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
