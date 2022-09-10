import bagel.util.*;

public class Boundary {
    
    private Point topLeft;
    private Point bottomRight;

    /**
     * Constructor for Boundary class.
     * @param topLeft Top left point of the boundary.
     * @param bottomRight Bottom right point of the boundary.
     */
    public Boundary(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Get the top left point of the boundary.
     * @return Top left point of the boundary.
     */
    public Point getTopLeft() {
        return topLeft;
    }

    /**
     * Set the top left point of the boundary.
     * @param topLeft Top left point of the boundary.
     */
    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    /**
     * Get the bottom right point of the boundary.
     * @return Bottom right point of the boundary.
     */
    public Point getBottomRight() {
        return bottomRight;
    }

    /**
     * Set the bottom right point of the boundary.
     * @param bottomRight Bottom right point of the boundary.
     */
    public void setBottomRight(Point bottomRight) {
        this.bottomRight = bottomRight;
    }

    /**
     * Check if a point is within the boundary.
     * @param point Point to check.
     * @return True if the point is within the boundary, false otherwise.
     */
    public boolean contains(Point point) {
        return point.x >= topLeft.x && point.x <= bottomRight.x 
            && point.y >= topLeft.y && point.y <= bottomRight.y;
    }
}
