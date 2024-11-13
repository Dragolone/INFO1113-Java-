package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.core.PVector;

import java.io.*;
import java.util.*;
/**
 * The App class represents the main application for the Inkball game.
 * It initializes the game board, handles user inputs, manages game states, and renders each frame of the game.
 */
public class App extends PApplet {
    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;
    public HashMap<String, PImage> imageCache;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH / CELLSIZE;
    public static final int BOARD_HEIGHT = 20;
    public static final int TILE_SIZE = 18;
    public static final int INITIAL_PARACHUTES = 1;
    public static final int FPS = 30;
    private boolean firsCreate = true;

    public String configPath;

    public static Random random = new Random();
    private PImage initialBackground;
    private int score;
    private ArrayList<Spawner> spawnerList;
    private ArrayList<Hole> holeList;
    private ArrayList<Ball> ballList;
    private ArrayList<Ball> candidateBallList;
    public tile[][] tiles;
    private tile transitionUpTile;
    private tile transitionDownTile;

    public ArrayList<Line> lineList;
    public Line currentLine;
    public boolean drawing;
    public boolean ctrlPressed;

    public int time;
    public int curLevel=0;
    public int totalLevel;

    private float timeCount ;
    private long lastTimeCount;
    private long lastTime;

    private boolean restart;
    public boolean pause;

    public boolean stop;
    public boolean timeOver;
    public boolean levelEnd;
    public boolean gameEnd;

    public float spawnInterval;
    public double scoreRightModifier;
    public double scoreWrongModifier;

    /**
     * Constructor for the App class.
     * Initializes the config path to "config.json".
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size. "config.json" is the name of json file name.
     */
    @Override
    public void settings() {
        WIDTH = TILE_SIZE * CELLSIZE;

        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
    public void setLeveltxtFromJson() {
        JSONObject config = loadJSONObject(configPath);
        JSONArray jsonArray = config.getJSONArray("levels");
        totalLevel=jsonArray.size();
        JSONObject levelStages = jsonArray.getJSONObject(curLevel);

        readLevelFile(levelStages.getString("layout"));
        this.spawnInterval=levelStages.getFloat("spawn_interval");
        this.scoreRightModifier=levelStages.getDouble("score_increase_from_hole_capture_modifier");
        this.scoreWrongModifier=levelStages.getDouble("score_decrease_from_wrong_hole_modifier");
        this.timeCount=spawnInterval;

        JSONArray spawnerBalls = levelStages.getJSONArray("balls");
        time = levelStages.getInt("time");
        int xPosition = 40;

        for (int i = 0; i < spawnerBalls.size(); i++) {
            String color = spawnerBalls.getString(i);
            Ball ball = null;
            int yPosition = 10;
            if (color.equals("grey")) {
                ball = new Ball(0, 0, '0');
            } else if (color.equals("orange")) {
                ball = new Ball(0, 0, '1');
            } else if (color.equals("blue")) {
                ball = new Ball(0, 0, '2');
            } else if (color.equals("green")) {
                ball = new Ball(0, 0, '3');
            } else if (color.equals("yellow")) {
                ball = new Ball(0, 0, '4');
            }
            if (ball != null) {
                ball.setX(xPosition);
                ball.setY(yPosition);
                candidateBallList.add(ball);
                xPosition += 32;
            }

        }
    }

    /**
     * Loads the level from a JSON file and initializes the gaming graph.
     *
     * @param fileName The name of the JSON file containing the level data.
     *                 This file should be formatted correctly to define the
     *                 layout and elements of the game level.
     * @return A completed graph representing the initialized gaming layout.
     */
    public void readLevelFile(String fileName) {
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

                    if (c == 'B' && colIndex + 1 < line.length()) {
                        Ball ball = new Ball(colIndex, rowIndex, line.charAt(colIndex + 1));
                        ballList.add(ball);
                        jumpNext = true;
                    } else if (c == 'H' && colIndex + 1 < line.length()) {
                        Hole hole = new Hole(colIndex, rowIndex, line.charAt(colIndex + 1));
                        holeList.add(hole);
                        jumpNext = true;
                    } else if (c == 'S') {
                        Spawner spawner = new Spawner(colIndex, rowIndex, c);
                        spawnerList.add(spawner);
                    } else if (c == '1' || c == '2' || c == '3' || c == '4' || c == 'X') {
                        tile.setWall();
                    }
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /**
     * Initializes the game by setting up the frame rate, background, and game components.
     */
    public void setup() {
        frameRate(FPS);
        background(200);
        spawnerList = new ArrayList<>();
        holeList = new ArrayList<>();
        ballList = new ArrayList<>();
        tiles = new tile[TILE_SIZE][TILE_SIZE];

        if(firsCreate){
            initialBackground  = get();
            firsCreate = false;
        }
        if(curLevel==0){
            score = 0;
        }

        timeCount=spawnInterval;
        lastTime = millis();
        candidateBallList = new ArrayList<>();
        lastTimeCount = millis();
        drawing = false;
        ctrlPressed=false;
        lineList = new ArrayList<>();
        restart = false;
        timeOver=false;
        stop=false;
        gameEnd=false;
        levelEnd=false;
        pause = false;

        setLeveltxtFromJson();

        imageCache = new HashMap<>();

        cacheImage("tile", "inkball/tile.png");
        cacheImage("entrypoint", "inkball/entrypoint.png");
        cacheImage("wall0", "inkball/wall0.png");
        cacheImage("wall1", "inkball/wall1.png");
        cacheImage("wall2", "inkball/wall2.png");
        cacheImage("wall3", "inkball/wall3.png");
        cacheImage("wall4", "inkball/wall4.png");
        cacheImage("hole0", "inkball/hole0.png");
        cacheImage("hole1", "inkball/hole1.png");
        cacheImage("hole2", "inkball/hole2.png");
        cacheImage("hole3", "inkball/hole3.png");
        cacheImage("hole4", "inkball/hole4.png");
        cacheImage("ball0", "inkball/ball0.png");
        cacheImage("ball1", "inkball/ball1.png");
        cacheImage("ball2", "inkball/ball2.png");
        cacheImage("ball3", "inkball/ball3.png");
        cacheImage("ball4", "inkball/ball4.png");

        cacheImage("brick0", "inkball/brick0.png");
        cacheImage("brick1", "inkball/brick1.png");
        cacheImage("brick2", "inkball/brick2.png");
        cacheImage("brick3", "inkball/brick3.png");
        cacheImage("brick4", "inkball/brick4.png");

    }

    /**
     * Loads images into the cache for efficient access during gameplay.
     *
     * @param key  A unique identifier for the image, used to retrieve it from the cache.
     * @param path The file path of the image to be loaded into the cache.
     *             This should be a valid path to an image file in the project.
     */
    private void cacheImage(String key, String path) {
        PImage img = loadImage(path);
        if (img != null) {
            imageCache.put(key, img);
        } else {
            System.out.println("Failed to load image: " + path);
        }
    }

    /**
     * Receives the key pressed signal from the keyboard.
     *
     * @param event The key event containing information about the key that was pressed,
     *              including the key code and any modifier keys that were active.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        handleKeyPress(event.getKey());
    }
    public void handleKeyPress(char key){
        if (key == 'r' || key == 'R') {
            if(gameEnd) curLevel=0;
            restart = true;
            stop=false;
            levelEnd=false;
            timeOver=false;
            gameEnd=false;
        }
        if (!stop && key == ' '){
            pause = !pause;
        }
        if (key == CODED && keyCode == CONTROL) {
            ctrlPressed = true;
        }
    }


    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased() {

        if (key == CODED && keyCode == CONTROL) {
            ctrlPressed = false;
        }
    }

    /**
     * Receives the mouse pressed signal from the mouse input.
     *
     * @param e The mouse event containing information about the mouse button pressed,
     *          the location of the mouse, and any modifier keys that were active.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(stop) return;
        if(!ctrlPressed && mouseButton==LEFT) {
            drawing = true;
            ArrayList<int[]> points = new ArrayList<>();
            currentLine = new Line(points);
            currentLine.points.add(new int[]{pmouseX, pmouseY, mouseX, mouseY});
        }
        if (mouseButton == RIGHT || (mouseButton == LEFT && ctrlPressed)) {
            int removeIndex = -1;
            float threshold = 5;

            double clickX = mouseX;
            double clickY = mouseY;

            for (int i = 0; i < lineList.size(); i++) {
                ArrayList<int[]> points = lineList.get(i).points;

                for (int j = 0; j < points.size() - 1; j++) {
                    int[] lineStart = points.get(j);
                    int[] lineEnd = points.get(j + 1);

                    PVector p1 = new PVector(lineStart[0], lineStart[1]);
                    PVector p2 = new PVector(lineEnd[0], lineEnd[1]);
                    PVector clickPos = new PVector((float) clickX, (float) clickY);

                    double distanceP1Click = dist(p1.x, p1.y, clickPos.x, clickPos.y);
                    double distanceP2Click = dist(p2.x, p2.y, clickPos.x, clickPos.y);
                    double distanceP1P2 = dist(p1.x, p1.y, p2.x, p2.y);
                    if (distanceP1Click + distanceP2Click <= distanceP1P2 + threshold) {
                        removeIndex = i;
                        break;
                    }
                }
                if (removeIndex != -1) {
                    lineList.remove(removeIndex);
                    System.out.println("delete");
                    break;
                }
            }
        }

    }

    /**
     * Receive mouse dragged signal from the keyboard.
     *
     * @param e The mouse event containing information about the mouse button pressed,
     *          the location of the mouse, and any modifier keys that were active.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if(stop) return;
        if (!ctrlPressed && mouseButton==LEFT && drawing) {
            currentLine.points.add(new int[]{pmouseX, pmouseY, mouseX, mouseY});
        }
    }

    /**
     * Receive mouse released signal from the keyboard.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(stop) return;
        if(!ctrlPressed && mouseButton==LEFT){
            drawing = false;
            lineList.add(currentLine);
        }

    }

    /**
     * Transition animation for loading to the next level.
     */
    public void loadTransition(){
        PImage img = imageCache.get("wall4");
        image(img, transitionUpTile.getX(),transitionUpTile.getY());
        image(img, transitionDownTile.getX(),transitionDownTile.getY());

        if (time<=0) {
            levelEnd=false;
            stop=false;
            setup();
        }
        if (millis() - lastTime >= 67) {
            time-=1;
            score+=1;
            transitionUpTile.transitionMove();
            transitionDownTile.transitionMove();
            lastTime = millis();
        }
    }

    /**
     * Draw lines in the game by current frame.
     */
    public void loadLines() {
        for (Line line : lineList) {
            strokeWeight(10);
            ArrayList<int[]> points = line.points;
            for (int[] point : points) {
                line((float) point[0], (float) point[1], point[2], point[3]);
            }
        }
        if (drawing) {
            strokeWeight(10);
            for (int[] point : currentLine.points) {
                line((float) point[0], (float) point[1], point[2], point[3]);
            }
        }
    }

    /**
     * Draw only tiles and walls in the game by current frame.
     */
    public void loadBoard() {
        for (int row = 0; row < TILE_SIZE; row++) {
            for (int col = 0; col < TILE_SIZE; col++) {
                tile tile = tiles[row][col];
                if (tile != null && !tile.isBall && !tile.isHole) {
                    PImage img = null;
                    switch (tile.getType()) {
                        case '1':
                            if(tile.age==3)
                                img = imageCache.get("wall1");
                            else
                                img = imageCache.get("brick1");
                            break;
                        case '2':
                            if(tile.age==3)
                                img = imageCache.get("wall2");
                            else
                                img = imageCache.get("brick2");
                            break;
                        case '3':
                            if(tile.age==3)
                                img = imageCache.get("wall3");
                            else
                                img = imageCache.get("brick3");
                            break;
                        case '4':
                            if(tile.age==3)
                                img = imageCache.get("wall4");
                            else
                                img = imageCache.get("brick4");
                            break;
                        case 'X':
                            if(tile.age==3)
                                img = imageCache.get("wall0");
                            else
                                img = imageCache.get("brick0");
                            break;
//                        case 'C':
//                            img = imageCache.get("brick0");
//                            break;
//                        case 'D':
//                            img = imageCache.get("brick1");
//                            break;
//                        case 'E':
//                            img = imageCache.get("brick2");
//                            break;
//                        case 'F':
//                            img = imageCache.get("brick3");
//                            break;
//                        case 'G':
//                            img = imageCache.get("brick4");
//                            break;
                        case 'S':
                            img = imageCache.get("entrypoint");
                            break;
                        default:
                            img = imageCache.get("tile");
                            break;
                    }

                    if (img != null) {
                        image(img, tile.getX(), tile.getY());
                    } else {
                        System.out.println("Failed to load image for tile type: " + tile.getType());
                    }
                } else if (tile != null && tile.isHole) {
                    char holeType = tile.getType();
                    if (Character.isDigit(holeType)) {
                        PImage img = imageCache.get("hole" + holeType);
                        if (img != null) {
                            image(img, tile.getX(), tile.getY());
                        } else {
                            System.out.println("Failed to load hole image for type: " + holeType);
                        }
                    } else {
                        System.out.println("Invalid hole type for tile at (" + row + ", " + col + "): " + holeType);
                    }
                } else if (tile != null && tile.isBall) {
                    PImage img = imageCache.get("ball" + tile.getType());
                    if (img != null) {
                        image(img, tile.getX(), tile.getY());
                    } else {
                        System.out.println("Failed to load ball image for type: " + tile.getType());
                    }
                }
            }
        }
    }


    /**
     * Load the picture of the various holes.
     */
    public void loadHole() {
        for (Hole hole : holeList) {
            String imageSource = "inkball/hole" + hole.getType() + ".png";
            PImage img = loadImage(imageSource);
            if (img != null) {
                image(img, hole.getX(), hole.getY());
            } else {
                System.out.println("Failed to load image: " + imageSource);
            }
        }
    }

    public String getBallImageSource(char type) {
        return "inkball/ball" + type + ".png";
    }

    /**
     * Draw balls in the game by current frame.
     */
    public void loadBall() {
        Iterator<Ball> iterator = ballList.iterator();
        while (iterator.hasNext()) {
            Ball ball = iterator.next();
            String ballSource = getBallImageSource(ball.getType());
            PImage img = loadImage(ballSource);

            if (img != null) {
                image(img, ball.getX(), ball.getY(), (float) ball.getBallSize(), (float) ball.getBallSize());


            } else {
                System.out.println("Failed to load ball image for type: " + ball.getType());
            }

            if (!pause && !stop) {
                ball.move(this);
            }

            if (ball.inHole) {
                iterator.remove();

            }
        }
        if(!stop && ballList.isEmpty() && candidateBallList.isEmpty()){
            curLevel=curLevel+1;
            stop=true;
            if(curLevel>=totalLevel){
                gameEnd=true;
            }else{
                levelEnd=true;
                transitionUpTile = new tile(0,0,'4');
                transitionDownTile = new tile(17,17,'4');
            }
        }
    }

    /**
     * Draw top bar in the game by current frame.
     */
    public void loadTopBar() {
        image(initialBackground, 0, 0);
        fill(200);

        fill(0);
        textSize(20);
        text("Score: " + score, 450, 30);
        text("Time: " + time, 450, 58);
        if(!pause && !stop){
            long currentTime = millis();
            if (currentTime - lastTime >= 1000) {
                if(time==0){
                    timeOver=true;
                    stop=true;
                }
                time -= 1;
                lastTime = currentTime;
                if(time<0) time=0;

            }
        }

    }


    /**
     * Draw candidate ball in the game by current frame.
     */
    public void loadCandidateBall() {
        fill(0);
        rect(10, 15, 160, 32);

        fill(0);
        textSize(20);

        if (candidateBallList.size() > 0) {
            text(String.format("%.1f", timeCount), 190, 40);
            long currentTime = millis();

            if (!pause && !stop && currentTime - lastTimeCount >= 100) {
                timeCount -= 0.1/30;
                if (timeCount <= 0.01f) {
                    timeCount = 10.0f;
                    int spawnerIndex = random.nextInt(spawnerList.size());
                    Spawner spawner = spawnerList.get(spawnerIndex);
                    spawner.spawnerBall(ballList, candidateBallList);
                }
                lastTimeCount = currentTime;
            }

            for (int index = 0; index < candidateBallList.size(); index++) {
                Ball ball = candidateBallList.get(index);
                if (ball.getX() <= (index * 32) + 10) {
                    ball.setSpeedX(0);
                } else {
                    ball.setSpeedX(-1);
                }
                ball.setX(ball.getX() + ball.speedX);

                String ballSource = getBallImageSource(ball.getType());
                int visibleStartX = ball.getX();
                int visibleWidth = Math.min(visibleStartX + 32, 170) - visibleStartX;

                image(loadImage(ballSource), ball.getX(), 10+ 8, visibleWidth, 32, 0, 0, visibleWidth, 32);
                                fill(200);
//                fill(0);
//                textSize(10);
////                text("x= " + ball.getX(), ball.getX()-48, ball.getY()+48);
//                text( ball.getY(), ball.getX()+32, ball.getY()+48);
            }
        }
    }


    /**
     * Draw the whole graph the gaming board.
     */
    @Override
    public void draw() {
        if (restart) {
            setup();
            System.out.println("restart");
            restart = false;
        }

        background(200);
        PImage backgroundImg = imageCache.get("background");
        if (backgroundImg != null) {
            image(backgroundImg, 0, 0, WIDTH, HEIGHT);
        }

        loadTopBar();
        loadBoard();
        loadHole();
        loadBall();
        loadCandidateBall();
        loadLines();

        if(levelEnd){
            loadTransition();
        }

        if (!stop && !pause) {

            long currentTime = millis();
            if (currentTime - lastTime >= 1000) {
                time -= 1;
                lastTime = currentTime;
            }

            timeCount -= 1.0f / FPS;

            if (timeCount <= 0.0f) {
                timeCount = 10.0f;
                int spawnerIndex = random.nextInt(spawnerList.size());
                Spawner spawner = spawnerList.get(spawnerIndex);
                spawner.spawnerBall(ballList, candidateBallList);
            }
        } else if(pause){
            fill(0);
            textSize(20);
            text("*** PAUSED ***", WIDTH / 2 - 55, 40);
        }else if(timeOver){
            fill(0);
            textSize(20);
            text("=== TIME 'S UP ===", WIDTH / 2 - 55, 40);
        }else if(gameEnd){
            fill(0);
            textSize(20);
            text("=== ENDED ===", WIDTH / 2 - 55, 40);
        }
    }

    /**
     * The startup function for the Inkball game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }
    /**
     * Returns the list of balls currently in the game.
     *
     * @return A list of Ball objects.
     */
    public ArrayList<Ball> getBallList() {
        return this.ballList;
    }
    /**
     * Returns the list of candidate balls in the game.
     *
     * @return A list of candidate Ball objects.
     */
    public ArrayList<Ball> getCandidateBallList() {
        return this.candidateBallList;
    }
    /**
     * Returns the list of holes currently in the game.
     *
     * @return A list of Hole objects.
     */
    public ArrayList<Hole> getHoleList() {
        return this.holeList;
    }

    /**
     * Returns the list of tiles on the game board.
     *
     * @return A 2D array of tile objects representing the game board.
     */
    public tile[][] getTileList() {
        return this.tiles;
    }
    /**
     * Returns the list of lines currently drawn in the game.
     *
     * @return A list of Line objects.
     */
    public ArrayList<Line> getLineList() {
        return this.lineList;
    }
    /**
     * Sets the game score to the given value.
     *
     * @param updateScore The new score value.
     */
    public void setScore(int updateScore){
        this.score = updateScore;
    }
    /**
     * Returns whether the game is currently paused.
     *
     * @return True if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return pause;
    }
    /**
     * Returns the current score of the game.
     *
     * @return The current score.
     */
    public int getScore(){
        return this.score;
    }

}
