package minesweeper;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.Random;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 864;
    public static int HEIGHT = 640;
    public static final int BOARD_WIDTH = WIDTH / CELLSIZE;
    public static final int GAMEBOARDHEIGHT = (HEIGHT - TOPBAR) / CELLSIZE;
    public static final int GAMEBOARDWIDTH = BOARD_WIDTH;
    public static final int FPS = 30;

    private PImage[][] board = new PImage[GAMEBOARDHEIGHT][GAMEBOARDWIDTH];
    private boolean[][] flags;
    private boolean[][] mines;
    private boolean[][] revealedTile;
    private int[][] setText;
    private PImage[] explosionImages = new PImage[10];
    private PImage flagImage;
    private PImage mineImage;

    private boolean gameOver;
    private boolean restart;
    private int seconds;
    private int currentImageIndex;

    private static int numberMines;
    private int explodedMines = 0;
    private boolean allMinesRevealed = false;
    private boolean gameWon = false;

    public static Random random = new Random();
    private ArrayList<Mine> mineList;

    public static int[][] mineCountColour = new int[][] {
            {0, 0, 0},
            {0, 0, 255},
            {0, 133, 0},
            {255, 0, 0},
            {0, 0, 132},
            {132, 0, 0},
            {0, 132, 132},
            {132, 0, 132},
            {32, 32, 32}
    };

    public App() {}

    private void initTile(){
        for(int i = 0; i < GAMEBOARDHEIGHT; i++){
            for(int j = 0; j < GAMEBOARDWIDTH; j++){
                board[i][j] = loadImage("minesweeper/tile1.png");
            }
        }
    }

    public int countMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < GAMEBOARDHEIGHT && newCol >= 0 && newCol < GAMEBOARDWIDTH) {
                    if (mines[newRow][newCol]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public boolean canBeReveal(int row, int col){
        return !mines[row][col] && !flags[row][col] && !revealedTile[row][col];
    }

    public void revealAEmptyTile(int row, int col){
        if (!canBeReveal(row, col)) return;
        revealedTile[row][col] = true;

        board[row][col] = loadImage("minesweeper/tile.png");

        int count = countMines(row, col);
        if (count == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow >= 0 && newRow < GAMEBOARDHEIGHT && newCol >= 0 && newCol < GAMEBOARDWIDTH) {
                        revealAEmptyTile(newRow, newCol);
                    }
                }
            }
        } else {
            setText[row][col] = count;
        }
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(FPS);
        flags = new boolean[GAMEBOARDHEIGHT][GAMEBOARDWIDTH];
        mines = new boolean[GAMEBOARDHEIGHT][GAMEBOARDWIDTH];
        setText = new int[GAMEBOARDHEIGHT][GAMEBOARDWIDTH];
        revealedTile = new boolean[GAMEBOARDHEIGHT][GAMEBOARDWIDTH];

        flagImage = loadImage("minesweeper/flag.png");
        mineImage = loadImage("minesweeper/mine.png");
        for (int j = 0; j < 10; j++) {
            explosionImages[j] = loadImage("minesweeper/mine" + j + ".png");
        }

        mineList = new ArrayList<>();
        seconds = 0;
        currentImageIndex = 0;
        gameOver = false;
        restart = false;
        gameWon = false;
        initTile();
        initMine();
    }

    @Override
    public void keyPressed(KeyEvent event){
        if(key == 'r'){
            restartGame();
        }
    }

    public void restartGame() {
        setup();
        seconds = 0;
        gameOver = false;
        gameWon = false;
        explodedMines = 0;
        allMinesRevealed = false;
        restart = false;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver && !gameWon){
            int x = (int) e.getX() / CELLSIZE;
            int y = (int) (e.getY() - TOPBAR) / CELLSIZE;
            if(mouseButton == LEFT){
                if(!flags[y][x] && !revealedTile[y][x]){
                    revealTile(x, y);
                }
            } else if(mouseButton == RIGHT){
                if(!revealedTile[y][x]){
                    flags[y][x] = !flags[y][x];
                }
            }
        }
    }

    private void revealTile(int x, int y) {
        if (!flags[y][x] && !revealedTile[y][x]) {
            if (mines[y][x]) {
                gameOver = true;
                allMinesRevealed = true;

                for (int i = 0; i < GAMEBOARDHEIGHT; i++) {
                    for (int j = 0; j < GAMEBOARDWIDTH; j++) {
                        if (mines[i][j]) {
                            board[i][j] = mineImage;
                        }
                    }
                }

                for (int i = 0; i < mineList.size(); i++) {
                    Mine m = mineList.get(i);
                    if (m.row == y && m.col == x) {
                        mineList.remove(i);
                        mineList.add(0, m);
                        break;
                    }
                }

                redraw();
            } else {
                revealAEmptyTile(y, x);
            }
        }
    }

    public void mineExploration() {
        if (frameCount % 3 == 0 && explodedMines < mineList.size()) {
            mineList.get(explodedMines).start = true;
            explodedMines++;
        }

        for (Mine m : mineList) {
            if (m.start) {
                image(explosionImages[m.imageIndex], m.col * CELLSIZE, m.row * CELLSIZE + TOPBAR);
                m.imageIndex++;

                if (m.imageIndex >= explosionImages.length) {
                    m.imageIndex = explosionImages.length - 1;
                }
            }
        }
    }

    private void initMine() {
        for (int i = 0; i < numberMines; i++) {
            int x, y;
            do {
                y = random.nextInt(GAMEBOARDHEIGHT);
                x = random.nextInt(GAMEBOARDWIDTH);
            } while (mines[y][x]);
            mines[y][x] = true;

            mineList.add(new Mine(y, x));
        }
    }

    private void drawBoard(){
        int rowIndex = TOPBAR;
        for (int i = 0; i < GAMEBOARDHEIGHT; i++) {
            int columnIndex = 0;
            for (int j = 0; j < GAMEBOARDWIDTH; j++) {
                image(board[i][j], columnIndex, rowIndex);

                if(flags[i][j]){
                    image(flagImage, columnIndex, rowIndex);
                } else if(revealedTile[i][j]){
                    int count = setText[i][j];
                    if (count > 0){
                        int r = mineCountColour[count][0];
                        int g = mineCountColour[count][1];
                        int b = mineCountColour[count][2];
                        fill(r, g, b);
                        textSize(16);
                        textAlign(CENTER, CENTER);
                        text(setText[i][j], columnIndex + CELLSIZE / 2, rowIndex + CELLSIZE / 2);
                    }
                }
                columnIndex += CELLSIZE;
            }
            rowIndex += CELLSIZE;
        }
    }

    public boolean whetherYouWinTheGame() {
        boolean won = true;
        for (int i = 0; i < GAMEBOARDHEIGHT; i++) {
            for (int j = 0; j < GAMEBOARDWIDTH; j++) {
                if (!mines[i][j] && !revealedTile[i][j]) {
                    won = false;
                }
            }
        }

        if (won && !gameWon) {
            gameWon = true;
        }

        return won;
    }

    public void showWeLose(){
        fill(255, 0, 0);
        textSize(27);
        text("You Lose!", 400, 39);
    }

    public void showWeWin(){
        fill(0, 255, 0);
        textSize(27);
        text("You Win!", 400, 39);
    }

    public void hoveringOver() {
        int x = mouseX / CELLSIZE;
        int y = (mouseY - TOPBAR) / CELLSIZE;
        if (y >= 0 && x >= 0 && y < GAMEBOARDHEIGHT && x < GAMEBOARDWIDTH) {
            if(!flags[y][x] && !revealedTile[y][x]){
                image(loadImage("minesweeper/tile2.png"), x * CELLSIZE, y * CELLSIZE + TOPBAR);
            }
        }
    }

    @Override
    public void draw() {
        if (whetherYouWinTheGame()) {
            drawBoard();
            if (frameCount % FPS < 10) {
                showWeWin();
            }
        } else {
            if (!gameOver) {
                if (frameCount % FPS == 0) {
                    seconds++;
                }
                background(255);
                fill(0);
                textSize(23);
                text("Time: " + seconds, 652, 49);
                drawBoard();
                hoveringOver();
            } else {
                if (allMinesRevealed) {
                    mineExploration();
                    showWeLose();
                    if (restart) {
                        setup();
                        seconds = 0;
                        restart = false;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                numberMines = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                numberMines = 100;
            }
        } else {
            numberMines = 100;
        }
        PApplet.main("minesweeper.App");
    }

}

class Mine {
    public int row;
    public int col;
    public int imageIndex = 0;
    public boolean start = false;

    public Mine(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

