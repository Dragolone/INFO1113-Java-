package inkball;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SampleTest {

    @Test
    public void simpleTest() {
        /*App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);*/ // delay is to give time to initialise stuff before drawing begins
    }
    @Test
    public void  testMain(){
        Ball ball=new Ball(394,94,'1');
        ball.speedX=0;
        ball.speedY=-2;
        ball.x=3;
        ball.y=64;
        tile[][] tile2=readLevelFile("level4.txt");
        ball.collisionWithWall(tile2);
//        ball.speedY=-2;
//        ball.x=144;
//        ball.y=246;
//        ball.collisionWithWall(tile2);
//        ball.speedY=-2;
//        ball.x=394;
//        ball.y=97;
//        ball.collisionWithWall(tile2);

    }
    public tile[][] readLevelFile(String fileName) {
        tile[][] tiles;
        tiles = new tile[18][18];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int rowIndex = 0;
            boolean jumpNext = false;
            String line;
            while ((line = br.readLine()) != null) {
                for (int colIndex = 0; colIndex < line.length(); colIndex++) {
                    if (jumpNext) {
                        tiles[rowIndex][colIndex] = new tile(colIndex, rowIndex, ' ');
                        jumpNext = false;
                        continue;
                    }
                    char c = line.charAt(colIndex);
                    tile tile = new tile(colIndex, rowIndex, c);
                    tiles[rowIndex][colIndex] = tile;
                    if (c == '1' || c == '2' || c == '3' || c == '4' || c == 'X') {
                        tile.setWall();
                    }
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  tiles;
    }

}

// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
