package inkball;
/**
 * The Hole class represents a hole in the game.
 * It extends the sprite class and is used to define the behavior and properties of a hole.
 */
public class Hole extends sprite{
    /**
     * Constructor for the Hole class.
     * Initializes the hole with its position and type.
     *
     * @param x    The x-coordinate of the hole.
     * @param y    The y-coordinate of the hole.
     * @param type The type of the hole, represented by a character.
     */
    public Hole(int x, int y, char type) {
        super(x, y, type);
        if(type==' ') System.out.println(type);
    }
}
