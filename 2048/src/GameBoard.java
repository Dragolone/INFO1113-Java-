/**
 * Game Board Interface
 * Implement this in your Concrete Class
 */
public interface GameBoard {
    /**
     * Getter for size
     *
     * @return int
     */
    public int getSize();

    /**
     * Setter for size
     * @param size must be positive
     * @throws IllegalArgumentException if size <= 0
     */
    public void setSize(int size);

    /**
     * Getter for board
     * @return int[][] multidimensional array
     */
    public int[][] getBoard();

    /**
     * Setter method for board
     * Used for testing board.
     * @param board the new game board.
     */
    public void setBoard(int[][] board);

    /**
     * Spawn a random tile
     * - This tile can have a value of 4 or 2
     * - Only spawn if there is a free spot
     */
    public void spawnRandom();

    /**
     * Draw the board by printing to System out.
     * You can have helper methods
     */
    public void drawBoard();

    /**
     * Check whether there are no moves such that any move is successful
     * @return true if no moves are successful, Else false
     */
    public boolean isGameOver();

    /**
     * Move the tiles up
     *
     * @return true if the movement was successful
     */
    public boolean moveUp();

    /**
     * Move the tiles down
     * @return true if the movement was successful
     */
    public boolean moveDown();

    /**
     * Move tiles to the right
     * @return true if successful
     */
    public boolean moveRight();

    /**
     * Move tiles to the left
     * @return true if the movement was successful
     */
    public boolean moveLeft();
}