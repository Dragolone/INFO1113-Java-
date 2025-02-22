import java.util.Scanner;

public class ChallengeGame {
    public static void main(String[] args) {
        int size = 4; // Default size is 4x4
        if (args.length > 1) {
            System.out.println("Invalid number of arguments. Please provide one argument for size or none for default settings.");
            return;
        } else if (args.length == 1) {
            try {
                size = Integer.parseInt(args[0]);
                if (size <= 0) {
                    throw new IllegalArgumentException("Size must be positive");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument, must be an int.");
                return;
            }
        }

        GameBoard game = build(size);
        Scanner scanner = new Scanner(System.in);

        while (!game.isGameOver()) {
            game.drawBoard();
            System.out.println("Enter direction (w/W for up, a/A for left, s/S for down, d/D for right, exit to quit):");
            String input = scanner.nextLine().trim();

            boolean moved = false;
            switch (input.toLowerCase()) {
                case "w":
                case "W":
                    moved = game.moveUp();
                    break;
                case "a":
                case "A":
                    moved = game.moveLeft();
                    break;
                case "s":
                case "S":
                    moved = game.moveDown();
                    break;
                case "d":
                case "D":
                    moved = game.moveRight();
                    break;
                case "exit":
                    System.out.println("Quitting game...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid command. Please use \"w/W\", \"a/A\", \"s/S\", and \"d/D\" for up, left, down, and right. Or \"exit\" to quit.");
                    continue;
            }

            if (moved) {
                game.spawnRandom(); // Generate new tiles after successful movement
            } else {
                System.out.println("Invalid move. No tiles can move that direction. Try again.");
            }

            // Check if 2048 has been reached
            if (hasTileValue(game.getBoard(), 2048)) {
                System.out.println("Congrats, now you can keep playing for a higher score.");
            }
        }

        game.drawBoard();
        System.out.println("Game over and exit the game.");
        scanner.close();
    }

    public static GameBoard build(int size) {
        return new GameBoardImpl(size);
    }

    private static boolean hasTileValue(int[][] board, int value) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == value) {
                    return true;
                }
            }
        }
        return false;
    }
}