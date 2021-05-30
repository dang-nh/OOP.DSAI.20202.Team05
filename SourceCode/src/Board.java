import BoardComponent.BossHouse;
import BoardComponent.ChessHouse;
import Constant.Constant;
import Static.CustomFont;

import java.awt.*;


/**
 * The board of game. Contain BossHouse and ChessHouse
 */
public class Board {
    public final int START_X = Constant.START_X;
    public final int START_Y = Constant.START_Y;

    // Position
    int x, y;

    // 10 ChessHouse from 1 -> 5 and 7 -> 11
    public ChessHouse[] chessHouses = new ChessHouse[12];

    // 2 BossHouse 0 and 6
    public BossHouse bossHouse0;
    public BossHouse bossHouse6;

    public Board() {
        x = START_X;
        y = START_Y;

        // Create ChessHouse for player 2
        for (int i = 1; i <= 5; i++) {
            chessHouses[i] = new ChessHouse();
        }

        // Create ChessHouse for player 1
        for (int i = 7; i <= 11; i++) {
            chessHouses[i] = new ChessHouse();
        }

        // Create BossHouse
        bossHouse0 = new BossHouse();
        bossHouse6 = new BossHouse();
        initBoard();
    }

    /**
     * Create and set initial coordinate for Board
     */
    public void initBoard() {
        // Chess house of player 2
        for (int i = 1; i <= 5; i++) {
            chessHouses[i].setChessHouse(i, x, y, 5);
            x += Constant.houseSize;
        }

        // Chess house of player 1
        x = START_X;
        y = (int) (START_Y + Constant.houseSize * 1.02);
        for (int i = 11; i >= 7; i--) {
            chessHouses[i].setChessHouse(i, x, y, 5);
            x += Constant.houseSize;
        }

        // Boss
        bossHouse0.setBossHouse(0, START_X - Constant.houseSize, START_Y, true);
        bossHouse6.setBossHouse(6, (int) (START_X + Constant.houseSize * 4.03), START_Y, true);
    }

    /**
     * Draw the score of each player
     *
     * @param stonePlayer1 => score of player 1
     * @param stonePlayer2 => score of player 2
     * @param bossPlayer1  => number boss player 1 has
     * @param bossPlayer2  => number boss player 2 has
     */
    public void drawScore(Graphics2D graphics2D, int stonePlayer1, int stonePlayer2, int bossPlayer1, int bossPlayer2) {
        graphics2D.setColor(new Color(109, 107, 82));
        graphics2D.setFont(CustomFont.mistral.deriveFont(Font.BOLD, 80));
        graphics2D.drawString(standScore(stonePlayer1 + bossPlayer1 * 5), 975, 668);
        graphics2D.drawString(standScore(stonePlayer2 + bossPlayer2 * 5), 225, 178);

        if (stonePlayer1 > 0 && stonePlayer1 < 9) {
            graphics2D.drawImage(Game.Stones[stonePlayer1], 726, 612, null);
        } else if (stonePlayer1 >= 9) {
            graphics2D.drawImage(Game.Stones[8], 726, 612, null);
        }
        if (stonePlayer2 > 0 && stonePlayer2 < 9) {
            graphics2D.drawImage(Game.Stones[stonePlayer2], 397, 116, null);
        } else if (stonePlayer2 >= 9) {
            graphics2D.drawImage(Game.Stones[8], 397, 116, null);
        }

        if (bossPlayer1 == 1) {
            graphics2D.drawImage(Game.eat1boss, 815, 640, null);
        } else if (bossPlayer1 == 2) {
            graphics2D.drawImage(Game.eat2boss, 785, 608, null);
        }

        if (bossPlayer2 == 1) {
            graphics2D.drawImage(Game.eat1boss, 477, 131, null);
        } else if (bossPlayer2 == 2) {
            graphics2D.drawImage(Game.eat2boss, 454, 106, null);
        }
    }

    /**
     * Convert score to standard form
     *
     * @param score => score
     * @return score
     */
    private String standScore(int score) {
        if (score < 10) {
            return "0" + score;
        } else {
            return String.valueOf(score);
        }
    }

    /**
     * Reset board to initial state
     */
    public void resetBoard() {
        // Set all to initial state
        for (int i = 1; i <= 5; i++) {
            chessHouses[i].setNumberStone(5);
        }
        for (int i = 7; i <= 11; i++) {
            chessHouses[i].setNumberStone(5);
        }
        bossHouse0.setBossHouse(0, true);
        bossHouse6.setBossHouse(0, true);
    }

    /**
     * Draw ChessHouse and BossHouse to the board
     */
    public void draw(Graphics2D graphics2D) {
        for (int i = 1; i <= 5; i++) {
            chessHouses[i].drawHouse(graphics2D);
        }
        for (int i = 7; i <= 11; i++) {
            chessHouses[i].drawHouse(graphics2D);
        }
        bossHouse0.drawHouse(graphics2D);
        bossHouse6.drawHouse(graphics2D);
    }
}
