package inkball;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BallTest {

    private Ball ball;
    private App mockApp;
    private tile[][] tiles;
    private ArrayList<Hole> holes;
    private ArrayList<Line> lines;

    @BeforeEach
    public void setUp() {
        ball = new Ball(1, 1, '1');
        mockApp = new App();
        mockApp.main(new String[]{""});
        PApplet.runSketch(new String[]{"App"}, mockApp);
        try{
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        Ball ball=new Ball(-1,1,' ');
        ball.xPositionToIndex(-1);
        ball.yPositionToIndex(1);


        Line line=new Line(new ArrayList<>());
        ArrayList<int[]> test=new ArrayList<>();
        test.add(new int[2]);
        Line line1=new Line(test);
        Hole hole=new Hole(1,1,' ');

        mockApp.setup();
        ArrayList<int[]> points = new ArrayList<>();
        Line currentLine = new Line(points);
        currentLine.points.add(new int[]{64, 98, 128, 98});
        currentLine.points.add(new int[]{128, 98, 139, 98});
    }

    @Test
    public void testAllCornerCollisions() {
        tiles = new tile[18][18];
        for (int i = 0; i < 18; i++) {

            for (int j = 0; j < 18; j++) {
                tiles[i][j] = new tile(i , j , '1');
            }
        }

        tiles[5][5].setWall();
        tiles[10][10].setWall();

        Ball collisionObject = new Ball(1,1,'2');

        collisionObject.speedX=-1;
        collisionObject.setSpeedY(-1);
        collisionObject.speedY=-1;

        collisionObject.checkCornerCollision(tiles, -1, -1, 50, 0, Ball.Direction.UP_LEFT);

        assertEquals(1, collisionObject.speedX);
        assertEquals(1, collisionObject.speedY);

        collisionObject.speedX=-1;
        collisionObject.speedY=-1;
        collisionObject.checkCornerCollision(tiles,   5, 5, 70, 10, Ball.Direction.UP_LEFT);

        assertEquals(1, collisionObject.speedX);
        assertEquals(1, collisionObject.speedY);


        collisionObject.speedX=1;

        collisionObject.speedY=-1;
        collisionObject.checkCornerCollision(tiles, -1, 18, 50, 18 * 32, Ball.Direction.UP_RIGHT);
        assertEquals(-1, collisionObject.speedX);
        assertEquals(1, collisionObject.speedY);

        collisionObject.speedX=1;
        collisionObject.speedY=-1;
        collisionObject.checkCornerCollision(tiles, 10, 10, 70, 320, Ball.Direction.UP_RIGHT);
        assertEquals(-1, collisionObject.speedX);
        assertEquals(1, collisionObject.speedY);


        collisionObject.speedX=-1;
        collisionObject.speedY=1;
        collisionObject.checkCornerCollision(tiles, 18, -1, 64 + 18 * 32, 0, Ball.Direction.DOWN_LEFT);
        assertEquals(1, collisionObject.speedX);


        collisionObject.speedX=1;
        collisionObject.speedY=1;
        collisionObject.checkCornerCollision(tiles, 10, 10, 640, 320, Ball.Direction.DOWN_RIGHT);
        assertEquals(-1, collisionObject.speedX);
        assertEquals(-1, collisionObject.speedY);

        mockApp.pause=true;
        ball.move(mockApp);
        mockApp.pause=false;
        ball.move(mockApp);
    }


    /**
     * Test ball initial conditions.
     */
    @Test
    public void testBallInitialConditions() {
        // Test initial position and speed
        assertEquals(32, ball.x);
        assertEquals(96, ball.y);
        assertEquals('1', ball.type);
    }

    /**
     * Test ball movement.
     */
    @Test
    public void testBallMovement() {
        int initialX = ball.x;
        int initialY = ball.y;
        ball.move(mockApp);
        assertNotEquals(initialX, ball.x);
        assertNotEquals(initialY, ball.y);
    }

    /**
     * test collision with wall.
     */
    @Test
    public void testCollisionWithWall() {
        // testUpCollisionWithWall
        ball.speedX=2;
        ball.speedY=-2;
        ball.x=32*14;
        ball.y=96;
        ball.collisionWithWall(mockApp.getTileList());
        assertTrue(ball.speedX == 2 && ball.speedY == 2);

        // testDownCollisionWithWall
        ball.speedX=2;
        ball.speedY=2;
        ball.x=32;
        ball.y=64+32*16;
        ball.collisionWithWall(mockApp.getTileList());
        assertTrue(ball.speedX == 2 && ball.speedY == -2);

        // testLeftCollisionWithWall
        ball.speedX=-2;
        ball.speedY=2;
        ball.x=32;
        ball.y=64+32*14;
        ball.collisionWithWall(mockApp.getTileList());
        assertTrue(ball.speedX == 2 && ball.speedY == 2);

        // testRightCollisionWithWall
        ball.speedX=2;
        ball.speedY=2;
        ball.x=32*16;
        ball.y=64+32*2;
        ball.collisionWithWall(mockApp.getTileList());
        assertTrue(ball.speedX == -2 && ball.speedY == 2);


        // testRightCollisionWithBrike
        for(int jj=0;jj<2;jj++){
            ball.speedX=2;
            ball.speedY=2;
            ball.x=32*16;
            ball.y=64+32*2;
            ball.collisionWithWall(mockApp.getTileList());
            assertTrue(ball.speedX == -2 && ball.speedY == 2);
        }
        assertFalse(mockApp.getTileList()[2][8].isWall);



    }

    /**
     * Test collision with hole.
     */
    @Test
    public void testCollisionWithHole() {
        ball.x = 384-16;
        ball.y = 288-16;
        ball.speedX=0;
        ball.speedY=1;
        double beforeBallSize=ball.ballSize;
        ball.collisionWithHole(mockApp.getHoleList(), mockApp);
        for(int i=0;i<=70;i++){
            ball.collisionWithHole(mockApp.getHoleList(), mockApp);
        }
        assertTrue(ball.inHole);
    }

    /**
     * Test collision with wrong hole.
     */
    @Test
    public void testCollisionWithWrongHole() {
        ball.ballSize=32;
        ball.x = 384-16;
        ball.y = 288-16;
        ball.type='2';
        ball.speedX=0;
        ball.speedY=1;
        double beforeBallSize=ball.ballSize;
        ball.collisionWithHole(mockApp.getHoleList(), mockApp);
        for(int i=0;i<=70;i++){
            ball.collisionWithHole(mockApp.getHoleList(), mockApp);
        }
        assertTrue(ball.inHole);
        assertTrue(mockApp.getCandidateBallList().contains(ball));
    }

    /**
     * Test collision with line.
     */
    @Test
    public void testCollisionWithLine() {
        ArrayList<int[]> points = new ArrayList<>();
        Line currentLine = new Line(points);
        currentLine.points.add(new int[]{0, 0, 128, 98});
        currentLine.points.add(new int[]{60, 60, 139, 98});
        mockApp.lineList.add(currentLine);

        ball.x = 14;
        ball.y = 14;
        ball.speedX=2;
        ball.speedY=0;
        ball.collisionWithLine(mockApp.lineList);
        assertEquals(0, ball.speedX);
        assertEquals(2, ball.speedY);
    }

    @Test
    public void runAllCollisionTests() {
        tile[][] tiles = new tile[18][18];

        for (int row = 0; row < 18; row++) {
            for (int col = 0; col < 18; col++) {
                tiles[row][col] = new tile(row * 32, col * 32, '1');
            }
        }
        tiles[1][1].setWall();

        testCheckCollision(Ball.Direction.UP, -1, 1, 50, 0, -10, 0, 10, tiles); // tile == null, boundary <= 64
        testCheckCollision(Ball.Direction.UP, 1, 1, 60, 0, -10, 0, 10, tiles); // tile != null, boundary <= tile.getY() + 32

        testCheckCollision(Ball.Direction.DOWN, 18, 1, 64 + 18 * 32, 0, 10, 0, -10, tiles); // tile == null, boundary >= 64 + 18*32
        testCheckCollision(Ball.Direction.DOWN, 1, 1, 32, 0, 10, 0, -10, tiles); // tile != null, boundary >= tile.getY()

        testCheckCollision(Ball.Direction.LEFT, 1, -1, 30, -10, 0, 10, 0, tiles); // tile == null, boundary <= 32
        testCheckCollision(Ball.Direction.LEFT, 1, 1, 60, -10, 0, 10, 0, tiles); // tile != null, boundary <= tile.getX() + 32

        testCheckCollision(Ball.Direction.RIGHT, 1, 18, 18 * 32, 10, 0, -10, 0, tiles); // tile == null, boundary >= 18*32
        testCheckCollision(Ball.Direction.RIGHT, 1, 1, 32, 10, 0, -10, 0, tiles); // tile != null, boundary >= tile.getX()
    }
    public void testCheckCollision(Ball.Direction direction, int row, int col, int boundary, int initialSpeedX, int initialSpeedY, int expectedSpeedX, int expectedSpeedY, tile[][] tiles) {
        Ball ball = new Ball(0,0,'1');
        ball.speedX = 1;
        ball.speedY = 1;

        ball.checkCollision(tiles, row, col, boundary, direction);

    }



}