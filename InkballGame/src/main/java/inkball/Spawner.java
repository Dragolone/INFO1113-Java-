package inkball;


import java.util.*;

public class Spawner extends sprite{
    /**
     * Constructor for the Spawner class.
     * Initializes the spawner with its position and type.
     *
     * @param x    The x-coordinate of the spawner.
     * @param y    The y-coordinate of the spawner.
     * @param type The type of the spawner, represented by a character.
     */
    public Spawner(int x, int y, char type) {
        super(x, y, type);
    }

    /**
     * Spawns a new ball from the candidate ball list and adds it to the ball list.
     *
     * @param ballList        A list of currently active balls in the game,
     *                        where the newly spawned ball will be added.
     * @param candidateBallList A list of candidate balls available for spawning,
     *                          from which a ball will be selected to add to the ball list.
     */
    public void spawnerBall (ArrayList<Ball> ballList, ArrayList<Ball> candidateBallList){
        int ballX = xPositionToIndex(x);
        int ballY = yPositionToIndex(y);
        if(candidateBallList.size()!=0){
            Ball ball = new Ball(ballX,ballY,candidateBallList.get(0).getType());
            ballList.add(ball);
            candidateBallList.remove(0);
        }
    }
}

