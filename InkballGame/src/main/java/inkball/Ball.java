package inkball;
import inkball.App;
import inkball.Hole;
import inkball.sprite;
import inkball.tile;
import processing.core.PVector;

import javax.crypto.interfaces.PBEKey;
import java.util.*;

/**
 * The Ball class represents a ball in the game. It extends the sprite class
 * and defines the behavior and properties of a ball, such as its movement, collision detection,
 * and scoring.
 */
public class Ball extends sprite {
    private int increase;
    private int decrease;

    public int speedY;
    public int speedX;
    public double ballSize = 32;
    public boolean inHole;
    public Random random = new Random();
    public int[] allSpeed = {2, -2};
    /**
     * Constructor for the Ball class.
     * Initializes the ball with its position, type, and speed.
     *
     * @param x    The x-coordinate of the ball.
     * @param y    The y-coordinate of the ball.
     * @param type The type of the ball, represented by a character.
     */
    public Ball(int x, int y, char type) {
        super(x, y, type);
        int randomX = random.nextInt(allSpeed.length);
        int randomY = random.nextInt(allSpeed.length);
        this.speedX = allSpeed[randomX];
        this.speedY = allSpeed[randomY];
        this.inHole = false;
        setBallScore(type);
    }

    /**
     * Sets the score values for the ball based on its type.
     *
     * @param type The type of the ball, represented by a character.
     */
    public void setBallScore(char type){
        if(type == '0'){
            this.increase = 70;
            this.decrease = 0;
        }else if(type == '4'){
            this.increase = 100;
            this.decrease = 100;
        }else{
            this.increase = 50;
            this.decrease = 25;
        }
    }

    /**
     * Detects if the ball collides with the wall.
     *
     * @param tiles A 2D array representing the tiles in the game,
     *              where each tile may represent a wall or open space.
     */
    public void collisionWithWall(tile[][] tiles) {
        int ballTop = this.y;
        int ballBottom = this.y + 32;
        int ballLeft = this.x;
        int ballRight = this.x + 32;
        int ballCol = xPositionToIndex(x+16);
        int ballRow = yPositionToIndex(y+16);
        int leftCol= xPositionToIndex(x-1);
        int rightCol= xPositionToIndex(x+32+1);

        int upRow= yPositionToIndex(y-1);
        int downRow= yPositionToIndex(y+32+1);
//        System.out.println("upRow="+upRow+",downRow="+downRow+",leftCol="+leftCol+",rightCol="+rightCol);

        checkCollision(tiles, upRow, ballCol, ballTop, Direction.UP);
        checkCollision(tiles, downRow, ballCol, ballBottom, Direction.DOWN);
        checkCollision(tiles, ballRow, leftCol, ballLeft, Direction.LEFT);
        checkCollision(tiles, ballRow, rightCol, ballRight, Direction.RIGHT);

        checkCornerCollision(tiles, upRow, leftCol, ballTop, ballLeft, Direction.UP_LEFT);
        checkCornerCollision(tiles, upRow, rightCol, ballTop, ballRight, Direction.UP_RIGHT);
        checkCornerCollision(tiles, downRow, leftCol, ballBottom, ballLeft, Direction.DOWN_LEFT);
        checkCornerCollision(tiles, downRow, rightCol, ballBottom, ballRight, Direction.DOWN_RIGHT);
    }

    /**
     * Detects if the ball collides with the wall.
     *
     * @param tiles     A 2D array representing the tiles in the game.
     * @param row      The row index of the tile being checked.
     * @param col      The column index of the tile being checked.
     * @param boundary  The boundary value that defines the collision area.
     * @param direction The direction in which the ball is moving.
     */
    public void checkCollision(tile[][] tiles, int row, int col, int boundary, Direction direction) {
        tile tile=null;
        if (row >= 0 && row < tiles.length && col >= 0 && col < tiles[0].length) {
            tile = tiles[row][col];
        }
            if (tile==null || tile.isWall) {
                switch (direction) {
                    case UP:
                        if (((tile==null && boundary<=64) || (tile!=null &&boundary <= tile.getY() + 32) )&& speedY < 0) {
                            speedY = -speedY;
                            hitWallIfNeeded(tile);
                            changeTypeIfNeeded(tile);

                        }
                        break;
                    case DOWN:
                        if (((tile==null && boundary>=64+18*32) ||(tile!=null && boundary >= tile.getY() ))&& speedY > 0) {
                            speedY = -speedY;
                            hitWallIfNeeded(tile);
                            changeTypeIfNeeded(tile);

                        }
                        break;
                    case LEFT:
                        if (((tile==null && boundary<=32) ||(tile!=null &&boundary <= tile.getX() + 32 ))&& speedX < 0) {
                            speedX = -speedX;
                            hitWallIfNeeded(tile);
                            changeTypeIfNeeded(tile);
//                            System.out.println("pong! direction="+direction);
                        }
                        break;
                    case RIGHT:
                        if (((tile==null && boundary>=18*32) ||(tile!=null &&boundary >= tile.getX())) && speedX > 0) {
                            speedX = -speedX;
                            hitWallIfNeeded(tile);
                            changeTypeIfNeeded(tile);
                        }
                        break;
                }
            }
//        }
    }

    /**
     * Detects if the ball collides with the corner wall.
     *
     * @param tiles     A 2D array representing the tiles in the game,
     *                  where each tile may represent a wall or open space.
     * @param row       The row index of the tile being checked for corner collision.
     * @param col       The column index of the tile being checked for corner collision.
     * @param boundary1 The first boundary value that defines the collision area at the corner.
     * @param boundary2 The second boundary value that defines the collision area at the corner.
     * @param direction The direction in which the ball is moving, which may affect the collision detection.
     */
    public void checkCornerCollision(tile[][] tiles, int row, int col, int boundary1, int boundary2, Direction direction) {
        tile tile=null;
        if (row >= 0 && row < tiles.length && col >= 0 && col < tiles[0].length) {
            tile = tiles[row][col];
        }
            if (tile==null || tile.isWall) {
                switch (direction) {
                    case UP_LEFT:
                        if (((tile==null && boundary1<=64 && boundary2<=0) || (tile!=null && boundary1 <= tile.getY() + 32 && boundary2 <= tile.getX() + 32)) && speedX < 0 && speedY < 0) {
                            speedX = -speedX;
                            speedY = -speedY;
                            hitWallIfNeeded(tile);
                            changeTypeIfNeeded(tile);
                        }
                        break;
                    case UP_RIGHT:
                        if (((tile==null && boundary1<=64 && boundary2>=18*32) ||(tile!=null && boundary1 <= tile.getY() + 32 && boundary2 >= tile.getX() ))&& speedX > 0 && speedY < 0) {
                            speedX = -speedX;
                            speedY = -speedY;
                            hitWallIfNeeded(tile);
                            changeTypeIfNeeded(tile);
                        }
                        break;
                    case DOWN_LEFT:
                        if (((tile==null && boundary1>=64+18*32 && boundary2<=0) || (tile!=null && boundary1 >= tile.getY() && boundary2 <= tile.getX() + 32 ))&& speedX < 0 && speedY > 0) {
                            speedX = -speedX;
                            speedY = -speedY;
                            hitWallIfNeeded(tile);
                            changeTypeIfNeeded(tile);
                        }
                        break;
                    case DOWN_RIGHT:
                        if ((tile==null && boundary1>=64+18*32 && boundary2>=18*32) ||(tile!=null && boundary1 >= tile.getY() && boundary2 >= tile.getX()) && speedX > 0 && speedY > 0) {
                            speedX = -speedX;
                            speedY = -speedY;
                            hitWallIfNeeded(tile);
                            changeTypeIfNeeded(tile);
                        }
                        break;
                }
            }

    }

    /**
     * Updates the tile if the ball hits a wall and reduces the wall's age.
     *
     * @param tile The tile that the ball has collided with.
     */
    private void hitWallIfNeeded(tile tile){
        if(tile !=null && (tile.getType() == this.type || tile.getType()=='X')){
//            System.out.println("tile brike");
            tile.age-=1;
            if(tile.age<=0){
                tile.isWall=false;
                tile.type=' ';
            }
        }
    }

    /**
     * Decides whether to change the ball's type based on the tile type.
     *
     * @param tile The tile object representing the current tile,
     *             which may affect the ball's type depending on its characteristics.
     */
    private void changeTypeIfNeeded(tile tile) {
        if (tile!=null &&  tile.getType()!=' ' && tile.getType() != 'X') {
            this.type = tile.getType();
        }

    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    /**
     * Detects if the ball collides with any hole in the hole list.
     *
     * @param holeList A list of `Hole` objects representing the holes in the game,
     *                 where each hole can potentially cause the ball to change state.
     * @param app      The `App` instance representing the current game application,
     *                 which may be used for handling game logic and state changes.
     */
    public void collisionWithHole(ArrayList<Hole> holeList, App app) {
        double ballCenterX = this.getX() + ballSize / 2;
        double ballCenterY = this.getY() + ballSize / 2;
        System.out.println("start buhuo"+",x="+x+",y="+y+",size="+ballSize+",speedX="+speedX+",speedY="+speedY);
        boolean isInHole=false;
        for (Hole hole : holeList) {
            double holeCenterX = hole.getX() + 32;
            double holeCenterY = hole.getY() + 32;
//            System.out.println("holex+"+holeCenterX+"y="+holeCenterY+",ballX="+ballCenterX+"ballY="+ballCenterY);

            double distance = Math.sqrt((ballCenterX - holeCenterX) * (ballCenterX - holeCenterX) + (ballCenterY - holeCenterY) * (ballCenterY - holeCenterY));
            System.out.println("distance="+distance);

            if (distance < 32) {
//                System.out.println("oleCenterX - ballCenterX="+(holeCenterX - ballCenterX));
//                System.out.println("(double)(holeCenterX - ballCenterX) * 0.005="+(double)(holeCenterX - ballCenterX) * 0.005);
//                System.out.println("((double)(32-distance))="+((double)(32-distance)));
//                System.out.println("( (double)(holeCenterX - ballCenterX) * 0.005)*((double)(32-distance))=");
                speedX +=( (double)(holeCenterX - ballCenterX) * 0.005)*((double)(32-distance));
                speedY += ((double)(holeCenterY - ballCenterY) * 0.005)*((double) (32-distance));
                isInHole=true;
                this.ballSize *= 0.98;

                if (distance<5 ||( speedX==0  && speedY==0)) { // Entered into the hole
                    System.out.println("candidate whith hole!!");
                    this.inHole = true;
                    updateGameScore(hole, app);
                    break;
                }
            }
        }
        if(isInHole==false) ballSize=32;
    }

    /**
     * Updates the game score based on the interaction between the ball and the hole.
     *
     * @param hole The `Hole` object representing the hole that the ball has collided with,
     *             which may determine the score update.
     * @param app  The `App` instance representing the current game application,
     *             used for managing game state and score tracking.
     */
    public void updateGameScore(Hole hole, App app){
        int updateScore =app.getScore();
        if(this.type == hole.getType() || hole.getType() == '0'){
            updateScore += this.increase*app.scoreRightModifier;
        } else if (this.type != '0') {
            updateScore -=this.decrease*app.scoreWrongModifier;
            ArrayList<Ball> candidateBalls = app.getCandidateBallList();
            this.x = 10 + (candidateBalls.size() + 2) * 32;
            this.y = 10;
            candidateBalls.add(this);
        }
        app.setScore(updateScore);
    }

    /**
     * Detects if the ball collides with the lines.
     *
     * @param lineList A list of `Line` objects representing the lines drawn in the game.
     */
    public void collisionWithLine(ArrayList<Line> lineList) {
        double ballCenterX = this.getX() + getBallSize() / 2;
        double ballCenterY = this.getY() + getBallSize() / 2;
        int removeIndex = -1;

        for (int i = 0; i < lineList.size(); i++) {
            ArrayList<int[]> points = lineList.get(i).points;

            for (int j = 0; j < points.size() - 1; j++) {
                int[] lineStart = points.get(j);
                int[] lineEnd = points.get(j + 1);

                PVector p1 = new PVector(lineStart[0], lineStart[1]);
                PVector p2 = new PVector(lineEnd[0], lineEnd[1]);

                PVector ballNextPos = new PVector((float) (ballCenterX + speedX), (float) (ballCenterY + speedY));

                double distanceP1BallV = dist(p1.x, p1.y, ballNextPos.x, ballNextPos.y);
                double distanceP2BallV = dist(p2.x, p2.y, ballNextPos.x, ballNextPos.y);
                double distanceP1P2 = dist(p1.x, p1.y, p2.x, p2.y);

                if (distanceP1BallV + distanceP2BallV <= distanceP1P2 + getBallSize() / 2) {
                    float dx = p2.x - p1.x;
                    float dy = p2.y - p1.y;

                    PVector normal1 = new PVector(-dy, dx).normalize();
                    PVector normal2 = new PVector(dy, -dx).normalize();

                    PVector closerNormal = getCloseNormal(p1, p2, normal1, normal2);

                    PVector ballVelocity = new PVector(speedX, speedY);
                    PVector newVelocity = calculateNewTrajectory(closerNormal, ballVelocity);

                    speedX = Math.round(newVelocity.x);
                    speedY = Math.round(newVelocity.y);

                    removeIndex = i;
                    break;
                }
            }
        }

        if (removeIndex >= 0) {
            lineList.remove(removeIndex);
        }
    }
    /**
     * Calculates the new trajectory of the ball after collision with a normal vector.
     *
     * @param n The normal vector representing the collision surface.
     * @param v The current velocity vector of the ball.
     * @return The new velocity vector after the collision.
     */
    public PVector calculateNewTrajectory(PVector n, PVector v) {
        float dotProduct = v.dot(n);
        return PVector.sub(v, PVector.mult(n, 2 * dotProduct));
    }
    /**
     * Determines which normal vector is closer to the ball's current position.
     *
     * @param p1 The first point of the line segment.
     * @param p2 The second point of the line segment.
     * @param n1 The first normal vector.
     * @param n2 The second normal vector.
     * @return The closer normal vector to the ball's current position.
     */
    public PVector getCloseNormal(PVector p1, PVector p2, PVector n1, PVector n2) {
        float ballCenterX = this.getX() + (float) getBallSize() / 2;
        float ballCenterY = this.getY() + (float) getBallSize() / 2;

        PVector midPoint = PVector.add(p1, p2).div(2);
        PVector midPointPlusN1 = PVector.add(midPoint, n1);
        PVector midPointPlusN2 = PVector.add(midPoint, n2);

        double distanceToN1 = dist(midPointPlusN1.x, midPointPlusN1.y, ballCenterX, ballCenterY);
        double distanceToN2 = dist(midPointPlusN2.x, midPointPlusN2.y, ballCenterX, ballCenterY);

        return distanceToN1 < distanceToN2 ? n1 : n2;
    }

    /**
     * Calculates the Euclidean distance between two points.
     *
     * @param x1 The x-coordinate of the first point.
     * @param y1 The y-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y2 The y-coordinate of the second point.
     * @return The Euclidean distance between the two points.
     */
    public double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /**
     * Moves the ball by updating its position based on its current velocity.
     *
     * @param app The `App` instance representing the current game application,
     *            used for accessing game state and managing interactions.
     */
    public void move(App app) {
        if (app.isPaused()) {
            return;
        }
        collisionWithWall(app.getTileList());
        collisionWithHole(app.getHoleList(), app);
        collisionWithLine(app.getLineList());

        y += speedY;
        x += speedX;

    }

    /**
     * Sets the x-axis speed of the ball.
     *
     * @param x The speed value for the x-axis.
     */
    public void setSpeedX(int x){
        this.speedX = x;
    }
    /**
     * Sets the y-axis speed of the ball.
     *
     * @param y The speed value for the y-axis.
     */
    public void setSpeedY(int y){
        this.speedY = y;
    }
    /**
     * Returns the current size of the ball.
     *
     * @return The size of the ball.
     */
    public double getBallSize(){
        return this.ballSize;
    }
}

