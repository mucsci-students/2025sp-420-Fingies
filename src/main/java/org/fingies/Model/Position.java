package org.fingies.Model;

/**
 * For handling the position of an object in the GUI
 * @author trush
 */
public class Position {
    private int x;
    private int y;

    /**
     * This is an invalid position, only the view can assign valid positions
     */
    public Position() {
        x = -1;
        y = -1;
    }

    /**
     * Alternate constructor, sets x and y position
     * @param x the x position for the object
     * @param y the y position for the object
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Alternate constructor, makes a copy of a position
     * @param pos The position to copy the x and y coordinates from
     */
    public Position(Position pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    /**
     * Gets the x position of the object
     * @return the current x (left & right) position
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x position of the object.
     * @param x the x position to set to
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y position of the object.
     * @return the current y (up & down) position
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y position of the object.
     * @param y the y position to set to
     */
    public void setY(int y) {
        this.y = y;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
    
    
}
