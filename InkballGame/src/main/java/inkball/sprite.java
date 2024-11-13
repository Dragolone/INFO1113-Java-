package inkball;

public class sprite {
    public int x;
    public int y;
    public char type;

    /**
     * Constructor for the sprite class.
     * Initializes the sprite with a specified position and type.
     * @param x The x-index of the sprite.
     * @param y The y-index of the sprite.
     * @param type The type of the sprite.
     */
    public sprite(int x, int y, char type) {
        this.x = xIndexToPosition(x);
        this.y = yIndexToPosition(y);
        this.type = type;
    }
    /**
     * Gets the x-coordinate of the sprite.
     * @return The x-coordinate of the sprite.
     */
    public int getX() {
        return this.x;
    }
    /**
     * Gets the y-coordinate of the sprite.
     * @return The y-coordinate of the sprite.
     */
    public int getY() {
        return this.y;
    }
    /**
     * Gets the type of the sprite.
     * @return The type of the sprite.
     */
    public char getType() {
        return this.type;
    }

    /**
     * Converts an x-coordinate position to its corresponding index.
     * @param x The x-coordinate position.
     * @return The corresponding x-index.
     */
    public int xPositionToIndex(int x) {
        if(x<0) return -1;
        else
        return (int) x / 32;
    }
    /**
     * Converts a y-coordinate position to its corresponding index.
     * @param y The y-coordinate position.
     * @return The corresponding y-index.
     */
    public int yPositionToIndex(int y) {
        if(y<64){
            return -1;
        }else
        return (int) (y - 64) / 32;
    }
    /**
     * Sets the x-coordinate of the sprite.
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the sprite.
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Converts an x-index to its corresponding x-coordinate position.
     * @param x The x-index.
     * @return The corresponding x-coordinate position.
     */
    public int xIndexToPosition(int x){
        int PositionOfX = (x*32);
        return PositionOfX;
    }

    /**
     * Converts a y-index to its corresponding y-coordinate position.
     * @param y The y-index.
     * @return The corresponding y-coordinate position.
     */
    public int yIndexToPosition(int y){
        int PositionOfY = 64 + (y*32);
        return PositionOfY;
    }

    /**
     * Returns a string representation of the sprite.
     * @return A string representing the sprite's x-coordinate, y-coordinate, and type.
     */
    @Override
    public String toString() {
        return "x: " + this.x + ", y: " + this.y + ", type: " + this.type;
    }

}
