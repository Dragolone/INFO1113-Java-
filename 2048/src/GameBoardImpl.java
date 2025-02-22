import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoardImpl implements GameBoard {
    private int size;
    private int[][] board;
    private Random random = new Random();

    public GameBoardImpl(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.size = size;
        this.board = new int[size][size];
        spawnRandom(); // 初始生成一个瓦片
        spawnRandom(); // 初始生成第二个瓦片
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.size = size;
        this.board = new int[size][size]; // 重置棋盘
        spawnRandom();
        spawnRandom();
    }

    @Override
    public int[][] getBoard() {
        return board.clone(); // 返回副本以防止外部修改
    }

    @Override
    public void setBoard(int[][] board) {
        if (board == null || board.length != size || board[0].length != size) {
            throw new IllegalArgumentException("Invalid board dimensions");
        }
        this.board = board.clone();
    }

    @Override
    public void spawnRandom() {
        List<int[]> emptySpots = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    emptySpots.add(new int[]{i, j});
                }
            }
        }

        if (!emptySpots.isEmpty()) {
            int[] spot = emptySpots.get(random.nextInt(emptySpots.size()));
            // 90% 概率生成 2，10% 概率生成 4
            board[spot[0]][spot[1]] = (random.nextInt(10) == 0) ? 4 : 2;
        }
    }

    @Override
    public void drawBoard() {
        // 计算最大瓦片值以确定宽度（至少 3 位）
        int maxTile = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] > maxTile) {
                    maxTile = board[i][j];
                }
            }
        }
        int width = Math.max(3, String.valueOf(maxTile).length() + 1);

        // 绘制棋盘
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String value = board[i][j] == 0 ? "-" : String.valueOf(board[i][j]);
                System.out.printf("%" + width + "s", value);
                if (j < size - 1) System.out.print(" | ");
            }
            System.out.println();
            if (i < size - 1) {
                System.out.print("-".repeat((width + 3) * size - 3)); // 绘制分隔线
                System.out.println();
            }
        }
        System.out.println();
    }

    @Override
    public boolean isGameOver() {
        if (hasEmptySpot()) {
            return false;
        }

        // 检查是否有相邻的相同瓦片（可以合并）
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (j < size - 1 && board[i][j] == board[i][j + 1]) return false; // 右
                if (i < size - 1 && board[i][j] == board[i + 1][j]) return false; // 下
            }
        }
        return true;
    }

    private boolean hasEmptySpot() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // 辅助方法：将一行或一列的瓦片向左移动并合并
    private int[] shiftAndMerge(int[] row) {
        List<Integer> nonZero = new ArrayList<>();
        for (int value : row) {
            if (value != 0) {
                nonZero.add(value);
            }
        }

        // 合并相邻的相同瓦片
        for (int i = 0; i < nonZero.size() - 1; i++) {
            if (nonZero.get(i).equals(nonZero.get(i + 1))) {
                nonZero.set(i, nonZero.get(i) * 2);
                nonZero.remove(i + 1);
            }
        }

        // 填充结果数组（剩余部分用 0 填充）
        int[] result = new int[row.length];
        for (int i = 0; i < nonZero.size(); i++) {
            result[i] = nonZero.get(i);
        }
        return result;
    }

    @Override
    public boolean moveUp() {
        boolean moved = false;
        for (int j = 0; j < size; j++) {
            int[] column = new int[size];
            for (int i = 0; i < size; i++) {
                column[i] = board[i][j];
            }
            int[] newColumn = shiftAndMerge(column);
            for (int i = 0; i < size; i++) {
                if (board[i][j] != newColumn[i]) {
                    moved = true;
                }
                board[i][j] = newColumn[i];
            }
        }
        return moved;
    }

    @Override
    public boolean moveDown() {
        boolean moved = false;
        for (int j = 0; j < size; j++) {
            int[] column = new int[size];
            for (int i = 0; i < size; i++) {
                column[i] = board[size - 1 - i][j];
            }
            int[] newColumn = shiftAndMerge(column);
            for (int i = 0; i < size; i++) {
                if (board[size - 1 - i][j] != newColumn[i]) {
                    moved = true;
                }
                board[size - 1 - i][j] = newColumn[i];
            }
        }
        return moved;
    }

    @Override
    public boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < size; i++) {
            int[] row = board[i].clone();
            int[] newRow = shiftAndMerge(row);
            if (!java.util.Arrays.equals(row, newRow)) {
                moved = true;
            }
            board[i] = newRow;
        }
        return moved;
    }

    @Override
    public boolean moveRight() {
        boolean moved = false;
        for (int i = 0; i < size; i++) {
            int[] row = new int[size];
            for (int j = 0; j < size; j++) {
                row[j] = board[i][size - 1 - j];
            }
            int[] newRow = shiftAndMerge(row);
            for (int j = 0; j < size; j++) {
                if (board[i][size - 1 - j] != newRow[j]) {
                    moved = true;
                }
                board[i][size - 1 - j] = newRow[j];
            }
        }
        return moved;
    }
}