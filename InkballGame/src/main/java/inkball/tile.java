package inkball;


public class tile extends sprite{
    public boolean isWall;
    public boolean isBall;
    public boolean isHole;
    public int age;
    public boolean isBrick;

    /**
     * Constructor for the tile class.
     * Initializes the tile with its position and type.
     *
     * @param x    The x-coordinate of the tile.
     * @param y    The y-coordinate of the tile.
     * @param type The type of the tile, represented by a character.
     */
    public tile(int x, int y, char type) {
        super(x, y, type);
        isWall = false;
    }

    /**
     * Sets the tile as a wall and initializes its age.
     */
    public void setWall(){
        this.isWall = true;
        this.age=3;
    }

    /**
     * Moves the tile to the next position in a transition sequence.
     * Used for level transition animations.
     */
    public void transitionMove(){
            int indexX=xPositionToIndex(x);
            int indexY=yPositionToIndex(y);
            System.out.println("pre x="+x+",Y="+y);
            System.out.println("pre index="+indexX+",indexY="+indexY);
            if (indexY == 0 && indexX <  17) {
                indexX += 1;
            } else if (indexX ==  17 && indexY <17) {
                indexY += 1;
            } else if (indexY == 17 && indexX > 0) {
                indexX -= 1;
            } else if (indexX == 0 && indexY > 0) {
                indexY -= 1;
            }
            x=xIndexToPosition(indexX);
            y=yIndexToPosition(indexY);
//            System.out.println("after index="+indexX+",indexY="+indexY);
//            System.out.println("after x="+x+",Y="+y);

    }
}
