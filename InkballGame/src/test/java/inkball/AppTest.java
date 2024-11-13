package inkball;

//import org.junit.Before;

//import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
//import processing.event.KeyEvent;
//import processing.event.MouseEvent;
import processing.core.PVector;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    App app;

    @BeforeEach
    public void init(){
        app = new App();
        app.main(new String[]{""});
        PApplet.runSketch(new String[]{"App"}, app);
        try{
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        app.setup();
        app.getScore();
        app.getBallList();
    }

    /**
     * Test mouse pressed draw line.
     */
    @Test
    void testMousePressedDrawLine() {
        app.mouseButton = 37;
        app.ctrlPressed = false;
        app.stop = false;
        app.pmouseX = 10;
        app.pmouseY = 10;
        app.mouseX = 20;
        app.mouseY = 20;

        app.mousePressed(null);

        assertTrue( app.currentLine.points.size()!=0);
        app.stop=true;
        app.mouseDragged(null);
        app.stop=false;
        app.ctrlPressed=false;
        app.mouseButton=37;
        app.drawing=true;
        app.mouseDragged(null);

        app.stop=true;
        app.mouseReleased(null);
        app.stop=false;
        app.ctrlPressed=false;
        app.mouseButton=37;
        app.drawing=true;
        app.mouseReleased(null);
        assertTrue(!app.lineList.isEmpty());

        app.imageCache.put("background",null);
        app.stop=true;
        app.pause=false;
        app.timeOver=true;
        app.draw();
        app.timeOver=false;
        app.draw();
        app.gameEnd=true;
        app.draw();
        app.gameEnd=false;
        app.draw();


    }

    /**
     * Test mouse pressed remove line.
     */
    @Test
    void testMousePressedRemoveLine() {

        ArrayList<int[]> points = new ArrayList<>();
        Line currentLine = new Line(points);
        currentLine.points.add(new int[]{10, 10, 128, 98});
        currentLine.points.add(new int[]{20, 20, 139, 98});
        app.lineList.add(currentLine);
        app.mouseButton = 39;
        app.ctrlPressed = false;
        app.stop = false;

        app.mouseX = 15;
        app.mouseY = 15;

        app.mousePressed(null);

        assertEquals(0, app.lineList.size());
    }

    /**
     * Test test mouse pressed does not remove line .
     */
    @Test
    void testMousePressedDoesNotRemoveLine() {

        ArrayList<int[]> points = new ArrayList<>();
        Line currentLine = new Line(points);
        currentLine.points.add(new int[]{10, 10, 128, 98});
        currentLine.points.add(new int[]{20, 20, 139, 98});
        app.lineList.add(currentLine);
        app.mouseButton = 39;
        app.ctrlPressed = false;
        app.stop = false;

        app.mouseX = 100;
        app.mouseY = 100;

        app.mousePressed(null);

        assertEquals(1, app.lineList.size());

    }

    /**
     * Test mouse pressed ignored ifStopped.
     */
    @Test
    void testMousePressedIgnoredIfStopped() {
        app.stop = true;
        app.mouseButton = 37;
        app.ctrlPressed = false;
        app.mousePressed(null);

        assertTrue(app.lineList.isEmpty());
    }


    /**
     * Test key press.
     */
    @Test
    public void keyPressTest(){
        app.pause=false;
        app.handleKeyPress(' ');
        assertTrue(app.pause);
        app.draw();
        app.handleKeyPress(' ');
        assertFalse(app.pause);
        app.keyCode = 17;
        app.handleKeyPress((char)65535);
        assertTrue(app.ctrlPressed);
        app.handleKeyPress('r');
        app.timeOver=true;
        app.draw();
        app.gameEnd=true;
        app.handleKeyPress('R');

    }

    @Test
    public void testLoadBoard() {
        tile[][] tiles = new tile[18][18];

        tiles[0][0] = new tile(0, 0, '1');
        tiles[0][0].setWall();
        tiles[0][0].age = 2;

        tiles[3][0] = new tile(0, 0, '1');
        tiles[3][0].setWall();
        tiles[3][0].age = 2;


        tiles[0][1] = new tile(0, 32, '2');
        tiles[0][1].setWall();

        tiles[3][1] = new tile(0, 32, '3');
        tiles[3][1].setWall();

        tiles[3][2] = new tile(0, 32, '3');
        tiles[3][2].setWall();
        tiles[3][2].age=2;

        tiles[3][3] = new tile(0, 32, '2');
        tiles[3][3].setWall();
        tiles[3][3].age=2;


        tiles[1][0] = new tile(32, 0, '3');
        tiles[1][0].isHole = true;

        tiles[1][1] = new tile(32, 32, 'S');
        tiles[1][1].isBall = true;

        tiles[2][2] = new tile(64, 64, 'X');

        for (int row = 0; row < 18; row++) {
            for (int col = 0; col < 18; col++) {
                if (tiles[row][col] == null) {
                    tiles[row][col] = new tile(row * 32, col * 32, ' ');
                }
            }
        }

        app.tiles=tiles;
        app.loadBoard();


    }
}
