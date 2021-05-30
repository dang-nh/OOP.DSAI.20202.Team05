import GameStep.Step;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Player of game
 */
public class Player {

    protected Game game;
    protected Board board;  // board of game
    protected Shape historyShape;

    public int numberStone_have;
    public int numberBoss_have;
    public int currentScore; // Score of player = chess + 5 * boss
    public String playerName;

    protected int chosenHouse;
    protected int direction;

    public int playerSide; // equal 1 or 2
    public Step step;

    Random random;

    public Player() {
    }

    public void initPlayer(Game game, String name, int playerSide) {
        this.game = game;
        this.historyShape = GameHistory.shape;
        this.step = new Step();
        playerName = name;
        this.board = game.board;
        this.playerSide = playerSide;
        numberStone_have = 0;
        numberBoss_have = 0;
        direction = 0;
        chosenHouse = 0;
        random = new Random();
    }

    public void resetStep() {
        chosenHouse = 0;
        direction = 0;
        this.step.choose_Id = 0;
        this.step.direction = 0;
        this.step.count_eat_number = 0;
        if (playerSide == 2) {
            for (int i = 1; i <= 5; i++) {
                board.chessHouses[i].setChoose(false);
                board.chessHouses[i].setDirect(0);
            }
        }
        if (playerSide == 1) {
            for (int i = 7; i <= 11; i++) {
                board.chessHouses[i].setChoose(false);
                board.chessHouses[i].setDirect(0);
            }
        }

    }

    public void turn(long gameTime, Point mousePosition) {
        /* Click mouse to 1 house => chosenHouse.
        * Show 2 button <-  and ->
        * Press VK_LEFT or VK_RIGHT => direction

        Put into step.choose_Id và step.direction for controller getStep()
        Finally return game.turnToken = 3 or 4 => turn of controller

        if (playerSide = 1) game.turnToken =3
        if (playerSide = 2) game.turnToken =4
         */
        if (Frame.mouseButtonState(1)) {
            // turn of player2
            if (playerSide == 2) {

                // Button playAgain
                if (historyShape.contains(mousePosition)) {
                    System.out.println("Rollback player 2");
                    giveTokenRollBack(game.turnToken);
                    return;
                }
                // Choose direction
                for (int i = 1; i <= 5; i++) {
                    if ((board.chessHouses[i].getNumberStone() > 0) && (board.chessHouses[i].getShape().contains(mousePosition))) {
                        for (int j = 1; j <= 5; j++) {
                            board.chessHouses[j].setChoose(false);
                        }
                        board.chessHouses[i].setChoose(true);
                        chosenHouse = i;              // house is chosen
                    }
                }
            }

            // turn of player 2
            if (playerSide == 1) {

                // Button playAgain
                if (historyShape.contains(mousePosition)) {
                    System.out.println("Rollback player 1");
                    giveTokenRollBack(game.turnToken);
                    return;
                }

                // Choose direction
                for (int i = 7; i <= 11; i++) {
                    if ((board.chessHouses[i].getNumberStone() > 0) && (board.chessHouses[i].getShape().contains(mousePosition))) {
                        for (int j = 7; j <= 11; j++) {
                            board.chessHouses[j].setChoose(false);
                        }
                        board.chessHouses[i].setChoose(true);
                        chosenHouse = i;              // house is chosen
                    }
                }
            }
        }

        // press Button choose house
        if (chosenHouse != 0) {
            if (Frame.mouseButtonState(1)) {
                if (playerSide == 1) {
                    if (new Rectangle(board.chessHouses[chosenHouse].getX() - 104 + 207, board.chessHouses[chosenHouse].getY() + 128, 71, 32).contains(mousePosition)) {
                        direction = -1;
                        board.chessHouses[chosenHouse].setDirect(-1);
                    }
                    if (new Rectangle(board.chessHouses[chosenHouse].getX() - 100 + 16, board.chessHouses[chosenHouse].getY() + 124, 75, 34).contains(mousePosition)) {
                        direction = 1;
                        board.chessHouses[chosenHouse].setDirect(1);
                    }
                }
                if (playerSide == 2) {
                    if (new Rectangle(board.chessHouses[chosenHouse].getX() - 100 + 24, board.chessHouses[chosenHouse].getY() - 100 + 40, 69, 30).contains(mousePosition)) {
                        direction = -1;
                        board.chessHouses[chosenHouse].setDirect(-1);
                    }
                    if (new Rectangle(board.chessHouses[chosenHouse].getX() - 100 + 204, board.chessHouses[chosenHouse].getY() - 100 + 40, 77, 37).contains(mousePosition)) {
                        direction = 1;
                        board.chessHouses[chosenHouse].setDirect(1);
                    }
                }
            }
            if (Frame.keyboardKeyState(KeyEvent.VK_LEFT)) {
                if (this.playerSide == 1) {
                    direction = 1;
                    board.chessHouses[chosenHouse].setDirect(1);
                } else {
                    direction = -1;
                    board.chessHouses[chosenHouse].setDirect(-1);
                }
            }
            if (Frame.keyboardKeyState(KeyEvent.VK_RIGHT)) {
                if (this.playerSide == 1) {
                    direction = -1;
                    board.chessHouses[chosenHouse].setDirect(-1);
                } else {
                    direction = 1;
                    board.chessHouses[chosenHouse].setDirect(1);
                }
            }
        }


        if (direction != 0) {
            this.step.choose_Id = chosenHouse;
            this.step.direction = direction;
            this.step.count_eat_number = 0;
            giveTurnToken(playerSide);
        }

    }

    /**
     * Auto play if over time
     */
    public void autoPlay() {
        if (chosenHouse != 0)
            board.chessHouses[chosenHouse].setChoose(false);

        this.step = randomStep();

        board.chessHouses[this.step.choose_Id].setChoose(true);
        board.chessHouses[this.step.choose_Id].setDirect(this.step.direction);
        giveTurnToken(playerSide);
    }

    /**
     * Give turn for Controller
     *
     * @param playerSide => equal to 1 or 2
     */
    public void giveTurnToken(int playerSide) {
        if (playerSide == 1) {
            game.turnToken = 3;
        } else {
            game.turnToken = 4;
        }
    }

    /**
     * Set Token for history
     * Token = 7
     *
     * @param preToken => previous turnToken
     */
    public void giveTokenRollBack(int preToken) {
        game.gameHistory.preToken = preToken;
        game.turnToken = 7;
    }

    public Step getStep() {
        return this.step;
    }

    /**
     * Random a step when auto play
     *
     * @return => Step
     */
    public Step randomStep() {
        Step step = new Step();
        step.count_eat_number = 0;
        if (playerSide == 1) {
            do {                     // Repeat the random until you find a house with stones
                step.choose_Id = 7 + random.nextInt(5);  //Random from 7 to 11
            } while (board.chessHouses[step.choose_Id].getNumberStone() <= 0);
        }
        if (playerSide == 2) {
            do {
                step.choose_Id = 1 + random.nextInt(5);  //Random from 1 to 5
            } while (board.chessHouses[step.choose_Id].getNumberStone() <= 0);
        }
        step.direction = 1 - (random.nextInt(2) * 2); // Random 1 or -1
        return step;
    }

    /**
     * Reset all attributes of player
     */
    public void resetPlayer() {
        this.numberStone_have = 0;
        this.numberBoss_have = 0;
        this.resetStep();
    }

}
