import bagel.*;
import bagel.util.*;
import java.util.*;

/**
 * All objects drawn to the screen in the game are GameObjects.
 */
public abstract class GameObject {

    // error message for point outside of boundary
    private static final String OUT_OF_BOUNDARY = "Position is outside of boundary";

    private Image image;
    private Rectangle rectangle;

    /**
     * Constructor for GameObject class.
     * @param image Image of the game object.
     * @param position Position of the game object.
     */
    public GameObject(Image image, Point position) {
        this.image = image;
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
            throw new RuntimeException(OUT_OF_BOUNDARY);
        }
    }

    /**
     * Draw the object to the screen given a boundary and draw options. If the object is outside the boundary,
     * an exception will be thrown.
     * @param boundary Boundary to draw the object in.
     * @param options Draw options to draw the object with.
     */
    public void draw(Boundary boundary, DrawOptions options) {
        Point position = getPosition();
        if (boundary.contains(position)) {
            image.drawFromTopLeft(position.x, position.y, options);
        } else {
            throw new RuntimeException(OUT_OF_BOUNDARY);
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
     * Set the image of the game object, while also updating the rectangle.
     * @param image Image of the game object.
     */
    public void setImage(Image image) {
        this.image = image;
        this.rectangle = deriveRectangle(getPosition(), this.image);
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
    public boolean collides(GameObject[] gameObjects, Class<?> type) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getClass() == type && collides(gameObject)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the game object collides with another game object.
     * @param gameObject Game object to check collision with.
     * @return True if the game object collides with another game object, false otherwise.
     */
    public boolean collides(GameObject gameObject) {
        return gameObject.getRectangle().intersects(getRectangle());
    }

    /**
     * Get the collided object from a given array of game objects.
     * @param gameObjects Array of game objects to check collision with.
     * @return Collided game object, if any. Otherwise, null.
     */
    public GameObject getCollidedObject(GameObject[] gameObjects, Class<?> type) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getClass() == type && gameObject.getRectangle().intersects(this.getRectangle())) {
                return gameObject;
            }
        }
        return null;
    }

    /**
     * Get the collided objects from a given array of game objects.
     * @param gameObjects Array of game objects to check collision with.
     * @return Array of collided game objects, if any. Otherwise, null.
     */
    public ArrayList<GameObject> getCollidedObjects(GameObject[] gameObjects, Class<?> type) {
        ArrayList<GameObject> collidedObjects = new ArrayList<>();
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getClass() == type && gameObject.getRectangle().intersects(this.getRectangle())) {
                collidedObjects.add(gameObject);
            }
        }
        return collidedObjects;
    }
}
