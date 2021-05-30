import Constant.Constant;
import BoardComponent.BossHouse;
import BoardComponent.ChessHouse;
import GameStep.Step;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;


/**
 * Controller of the game (the Hand)
 * Handle the game with 5 state
 * If turnToken = 3 => handle for player 1
 * If turnToken = 4 => handle for player 2
 */
public class Controller {
    private final int STOP = 0;
    private final int GO = 1;
    private final int EAT = 2;
    private final int CHECK = 3;
    private final int WAIT = 4;

    int count = 0;
    private final int NUMBER_IMAGE = 25;

    private Game game;
    private final Board board;

    // Number stone in hand when drop
    private int stoneInHand;

    // Coordinate of hand
    private int x_hand, y_hand;

    int selected_Id;
    int direction;
    int count_eat_number;
    int state;

    /**
     * Eat stone step
     */
    int action_eatStep = 0;
    boolean isEating = false;
    int count_eat = 0;

    /**
     * Get stone from board
     * if action_pick = 1 => pick stone, if action_pick = 2 => put hand down
     */
    int action_pickStep = 0;

    /**
     * Drop stone
     */
    boolean isDropping = false;

    ChessHouse[] chessHouses;
    BossHouse bossHouse0;
    BossHouse bossHouse6;
    Player player1;
    Player player2;

    public Controller() {
        stoneInHand = 0;
        this.board = new Board();
        this.player1 = new Player();
        this.player2 = new Player();
        this.chessHouses = this.board.chessHouses;
        this.bossHouse0 = this.board.bossHouse0;
        this.bossHouse6 = this.board.bossHouse6;
        this.isEating = false;
    }

    public Controller(Game game) {
        this.game = game;
        this.board = game.board;
        x_hand = game.board.START_X;
        y_hand = game.board.START_Y;
        stoneInHand = 0;
        chessHouses = game.board.chessHouses;
        this.bossHouse0 = game.board.bossHouse0;
        this.bossHouse6 = game.board.bossHouse6;
        player1 = game.player1;
        player2 = game.player2;
        this.state = WAIT;
        this.count = 0;
        this.action_pickStep = 0;
    }

    /**
     * Execute each step
     */
    public void handleGame(Step step) {
        // If state is WAIT
        // => is called
        // => turn to play

        if (this.state == WAIT) {
            this.selected_Id = step.choose_Id;
            this.direction = step.direction;
            this.count_eat_number = step.count_eat_number;

            // set up the coordinate of hand to get stone
            setCoordinateGetStone();

            // if pick stone is being taken
            if (actionPickStone()) {
                return;
            }

            // if finish pick stone action
            this.action_pickStep = 0;
            this.stoneInHand = getStone(this.selected_Id);
            if (this.stoneInHand > 8) {
                if (MainGame.music_on) {
                    if (Game.GET_MUCH.isPlaying()) {
                        Game.GET_MUCH.stop();
                    }
                    try {
                        Game.GET_MUCH.play();
                    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            moveNextHouse(this.direction);

            // after pick and move to next house => GO
            this.state = GO;
            this.isDropping = false;

            return;
        }

        if (this.count < NUMBER_IMAGE && this.state != CHECK && !(this.state == GO && this.isDropping) && this.action_eatStep != 1 && this.action_eatStep != 2) {
            increaseCoordinate();
            return;
        }
        this.count = 0;

        try {
            Thread.sleep(150);
        } catch (Exception ignored) {
        }

        // Check state of Controller
        switch (this.state) {
            case STOP -> {
                // change state to WAIT
                this.state = WAIT;
                this.isEating = false;
                System.out.println("HistoryStack: " + game.gameHistory.gameStateStack.size());
                setTurnToken(step);

                // if game not continue
                if (!checkGameContinue()) {
                    this.game.turnToken = 0;
                    calculateScore();
                    return;
                }

                // check board of this player empty or not
                if (checkBoardPlayer(step)) {
                    addMoreStone(step);
                }

                // add gameState to game history stack
                if (this.game.turnToken == 0) {
                    game.gameHistory.add(new GameState(game, step, false));
                } else {
                    game.gameHistory.add(new GameState(game, step, true));
                }

                // reset step to get ready for next turn
                resetStep();
            }
            case GO -> {
                // check if drop is being taken or not
                if (this.isDropping) {
                    this.isDropping = false;
                    return;
                }

                // drop stone
                this.isDropping = true;
                dropStone();
                moveNextHouse(this.direction);
                System.out.println("So Dan : " + this.stoneInHand + " Selected : " + this.selected_Id + " direction : " + this.direction);
                setCoordinate();
            }
            case EAT -> {
                // if in empty house => use emptyHand action
                if (this.count_eat != 1) {
                    this.count_eat++;
                    return;
                }

                // if eat action is being taken ( count_eat == 1 )
                if (this.action_eatStep != 2) {
                    this.action_eatStep++;
                    return;
                }
                this.action_eatStep = 0;
                this.count_eat = 0;
                eatAction(step, this.selected_Id);
                count_eat_number++;
                if (count_eat_number >= 2 && this.selected_Id != 0 && this.selected_Id != 6) {
                    if (MainGame.music_on) {
                        if (Game.GOOD_DIRECT.isPlaying()) {
                            Game.GOOD_DIRECT.stop();
                        }
                        try {
                            Game.GOOD_DIRECT.play();
                        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                moveNextHouse(this.direction);
                this.isEating = true;
                this.state = CHECK;
            }
            case CHECK -> {
                int result = checkFinalHouse(this.selected_Id, this.direction);
                System.out.println("Selected : " + this.selected_Id + " Direction : " + this.direction);
                System.out.println("Result : " + result);
                switch (result) {
                    // case move continue
                    case 1:
                        if (this.isEating) {
                            this.state = STOP;
                            break;
                        }

                        setCoordinateGetStone();
                        if (actionPickStone()) {
                            return;
                        }
                        this.action_pickStep = 0;
                        this.stoneInHand = getStone(this.selected_Id);
                        moveNextHouse(this.direction);
                        this.state = GO;
                        this.isDropping = false;
                        break;

                    // case eat
                    case 2:
                        moveNextHouse(this.direction);
                        this.isEating = true;
                        this.state = EAT;
                        break;
                    // case stop
                    case 0:
                        this.state = STOP;
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Check if pick stone is being taken
     * If in process => change to next action
     *
     * @return true or false
     */
    private boolean actionPickStone() {
        if (this.action_pickStep < 3) {
            try {
                Thread.sleep(150);
            } catch (Exception ignored) {
            }
            this.action_pickStep++;
            return true;
        }
        return false;
    }

    /**
     * Add 1 stone to each house of next turn player when drop
     *
     * @param step => step of game
     */
    public void addMoreStone(Step step) {
        if (MainGame.music_on) {
            if (Game.ADD_STONE.isPlaying()) {
                Game.ADD_STONE.stop();
            }
            try {
                Game.ADD_STONE.play();
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                ex.printStackTrace();
            }

        }
        if (nextTurnIsPlayer1(step)) {
            if (player1.numberStone_have < 5 && player1.numberBoss_have == 0) {
                this.game.turnToken = 0;
                return;
            } else if (player2.numberStone_have < 5 && player1.numberBoss_have != 0 && player1.numberStone_have < 5) {
                this.game.turnToken = 0;
                return;
            } else if (player1.numberBoss_have > 0 && player1.numberStone_have < 5) {
                player1.numberBoss_have--;
                player2.numberBoss_have++;
                player2.numberStone_have -= 5;
                player2.currentScore += 5;
                player1.numberStone_have += 5;
                player1.currentScore -= 5;
            }
            player1.numberStone_have -= 5;
            for (int i = 7; i <= 11; i++) {
                chessHouses[i].setNumberStone(1);
            }
        } else {
            if (player2.numberStone_have < 5 && player2.numberBoss_have == 0) {
                this.game.turnToken = 0;
                return;
            } else if (player1.numberStone_have < 5 && player2.numberBoss_have != 0 && player2.numberStone_have < 5) {
                this.game.turnToken = 0;
                return;
            } else if (player2.numberBoss_have > 0 && player2.numberStone_have < 5) {
                player2.numberBoss_have--;
                player1.numberBoss_have++;
                player1.numberStone_have -= 5;
                player1.currentScore += 5;
                player2.numberStone_have += 5;
                player2.currentScore -= 5;
            }
            player2.numberStone_have -= 5;
            for (int i = 1; i <= 5; i++) {
                chessHouses[i].setNumberStone(1);
            }
        }
    }

    /**
     * Get all stone of this house and set number of stone to 0
     *
     * @param selected_Id => house is chosen
     * @return number stone of this house
     */
    public int getStone(int selected_Id) {
        int numberStone = chessHouses[selected_Id].getNumberStone();
        chessHouses[selected_Id].setNumberStone(0);
        return numberStone;
    }

    /**
     * Move the next house in player turn
     *
     * @param direction => direction of step
     */
    public void moveNextHouse(int direction) {
        this.selected_Id = getNextHouse(direction, this.selected_Id);
    }

    /**
     * Get the next house Id
     *
     * @param direction => direction of step
     * @param current   => current house Id
     * @return house Id of next house
     */
    public int getNextHouse(int direction, int current) {
        if (direction == 1) {
            if (current == 11) {
                // turn back to 0
                current = 0;
            } else {
                current++;
            }
        } else if (current == 0) {
            // direction = -1 => turn back to 11
            current = 11;
        } else {
            current--;
        }
        return current;
    }

    /**
     * Drop the stone in hand. If number of stone in hand is 0 => change the state to CHECK
     */
    public void dropStone() {
        this.stoneInHand--;
        // Increase Stone in ChessHouse
        if (this.selected_Id != 6 && this.selected_Id != 0) {
            chessHouses[this.selected_Id].increaseStone();
        } else if (this.selected_Id == 0) {
            bossHouse0.increaseStone(); // increase stone in BossHouse
        } else {
            bossHouse6.increaseStone();
        }
        if (this.stoneInHand == 0) {
            this.state = CHECK;
        }
    }

    /**
     * Check if con not pick the stone
     *
     * @param direction => direction of player
     * @return 0 => Stop (2 house empty or boss house) 1 => continue (House contains stones and
     * is not Boss House) 2 => Eat (Empty house and next is a house contains stone)
     */
    public int checkFinalHouse(int current_Id, int direction) {
        int checked_Id;
        // In BossHouse
        if (current_Id == 0 || current_Id == 6) {
            // if in eating state
            if (this.isEating) {
                // check if boss house is empty or not
                if (checkEmpty(current_Id)) {
                    // check next house of current house
                    checked_Id = getNextHouse(direction, current_Id);
                    // if empty => stop
                    if (checkEmpty(checked_Id)) {
                        return 0;
                    }
                    return 2;
                }
                return 0;
            }
            return 0;
        }

        // don't in Boss House
        // if number of stones not equal to 0 => play continue
        if (chessHouses[current_Id].getNumberStone() != 0) {
            return 1;
        }

        // number of stones in current house equal to 0
        // check next house
        checked_Id = getNextHouse(direction, current_Id);
        // = 0 => stop
        // != 0 => eat
        if (checkEmpty(checked_Id)) {
            return 0;
        } else {
            if (checked_Id == 0 && bossHouse0.isHasBoss() && bossHouse0.getPopulation() < 5) {
                return 0;
            }
            if (checked_Id == 6 && bossHouse6.isHasBoss() && bossHouse6.getPopulation() < 5) {
                return 0;
            }
            return 2;
        }
    }

    /**
     * Check if house empty or not
     *
     * @param current_Id => the Id of current house
     * @return true or false
     */
    public boolean checkEmpty(int current_Id) {
        if (current_Id == 0) {
            return !bossHouse0.isHasBoss() && bossHouse0.getNumberStone() == 0;
        }
        if (current_Id == 6) {
            return !bossHouse6.isHasBoss() && bossHouse6.getNumberStone() == 0;
        }
        return chessHouses[current_Id].getNumberStone() == 0;
    }

    /**
     * Action when it stone
     *
     * @param step       => current step
     * @param current_Id => eat stone in this Id
     */
    public void eatAction(Step step, int current_Id) {
        if (current_Id == 0) {
            eatBoss(step, bossHouse0);
        } else if (current_Id == 6) {
            eatBoss(step, bossHouse6);
        } else {
            eatStone(step, current_Id);
        }
    }

    // eat stone
    public void eatStone(Step step, int current_Id) {
        if (isPlayer1(step)) {
            int numberStone = getStone(current_Id);
            player1.currentScore += numberStone;
            player1.numberStone_have += numberStone;
        } else {
            int numberStone = getStone(current_Id);
            player2.currentScore += numberStone;
            player2.numberStone_have += numberStone;
        }
    }

    // eat Boss
    public void eatBoss(Step step, BossHouse bossHouse) {
        if (bossHouse.isHasBoss()) {
            if (MainGame.music_on) {
                if (Game.EAT_BOSS.isPlaying()) {
                    Game.EAT_BOSS.stop();
                }
                try {
                    Game.EAT_BOSS.play();
                } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (isPlayer1(step)) {
            player1.currentScore += bossHouse.getNumberStone();
            player1.numberStone_have += bossHouse.getNumberStone();
            bossHouse.setNumberStone(0);
            if (bossHouse.isHasBoss()) {
                bossHouse.setHasBoss(false);
                player1.currentScore += 10;
                player1.numberBoss_have++;
            }
        } else {
            player2.currentScore += bossHouse.getNumberStone();
            player2.numberStone_have += bossHouse.getNumberStone();
            bossHouse.setNumberStone(0);
            if (bossHouse.isHasBoss()) {
                bossHouse.setHasBoss(false);
                player2.currentScore += 10;
                player2.numberBoss_have++;
            }
        }
    }

    /**
     * Check if game continue or not
     *
     * @return true or false
     */
    public boolean checkGameContinue() {
        return !checkEmpty(0) || !checkEmpty(6);
    }

    /**
     * Calculate the Score when game finish
     */
    public void calculateScore() {
        for (int i = 1; i <= 5; i++) {
            player2.numberStone_have += chessHouses[i].getNumberStone();
        }
        for (int i = 7; i <= 11; i++) {
            player1.numberStone_have += chessHouses[i].getNumberStone();
        }
    }

    /**
     * Check if this turn of player 1 or not
     *
     * @return true or false
     */
    public boolean isPlayer1(Step step) {
        return step.choose_Id > 6;
    }

    /**
     * Check next turn player 1
     *
     * @return true or false
     */
    public boolean nextTurnIsPlayer1(Step step) {
        return step.choose_Id < 6;
    }

    /**
     * Set token for turn
     * if player1 => turnToken = 5 else turnToken = 6
     *
     * @param step => current step
     */
    public void setTurnToken(Step step) {
        if (isPlayer1(step)) {
            game.turnToken = 5;
        } else {
            game.turnToken = 6;
        }
    }

    /**
     * Check if all house is empty => add more stone
     *
     * @param step => current step
     * @return true or false
     */
    public boolean checkBoardPlayer(Step step) {
        boolean allEmpty = true;
        if (nextTurnIsPlayer1(step)) {
            for (int i = 7; i <= 11; i++) {
                if (chessHouses[i].getNumberStone() != 0) {
                    allEmpty = false;
                    break;
                }
            }
        } else {
            for (int i = 1; i < 6; i++) {
                if (chessHouses[i].getNumberStone() != 0) {
                    allEmpty = false;
                    break;
                }
            }
        }
        return allEmpty;
    }

    /**
     * Reset step of all player
     */
    public void resetStep() {
        player1.resetStep();
        player2.resetStep();
    }

    /**
     * Set coordinate for hand
     */
    public void setCoordinate() {
        if (this.direction == 1) {
            if (this.selected_Id < 6 && this.selected_Id != 0) {
                this.x_hand = game.board.START_X + (this.selected_Id - 2) * Constant.houseSize;
                this.y_hand = game.board.START_Y;

            } else if (this.selected_Id > 6) {
                this.x_hand = game.board.START_X + (11 - this.selected_Id + 1) * Constant.houseSize;
                this.y_hand = game.board.START_Y + Constant.houseSize;
            } else if (this.selected_Id == 0) {
                this.x_hand = game.board.START_X;
                this.y_hand = game.board.START_Y + Constant.houseSize;
            } else {
                this.x_hand = game.board.START_X + Constant.houseSize * 4;
                this.y_hand = game.board.START_Y;
            }
        } else if (this.selected_Id < 6 && this.selected_Id != 0) {
            this.x_hand = game.board.START_X + this.selected_Id * Constant.houseSize;
            this.y_hand = game.board.START_Y;

        } else if (this.selected_Id > 6) {
            this.x_hand = game.board.START_X + (11 - this.selected_Id - 1) * Constant.houseSize;
            this.y_hand = game.board.START_Y + Constant.houseSize;
        } else if (this.selected_Id == 0) {
            this.x_hand = game.board.START_X;
            this.y_hand = game.board.START_Y;
        } else {
            this.x_hand = game.board.START_X + Constant.houseSize * 4;
            this.y_hand = game.board.START_Y + Constant.houseSize;
        }
    }

    /**
     * Set coordinate of hand to get Stone
     */
    public void setCoordinateGetStone() {
        setCoordinate();
        if (this.direction == 1) {
            if (this.selected_Id < 6) {
                this.x_hand += Constant.houseSize;
            } else {
                this.x_hand -= Constant.houseSize;
            }
        } else if (this.selected_Id > 6) {
            this.x_hand += Constant.houseSize;
        } else {
            this.x_hand -= Constant.houseSize;
        }
    }

    /**
     * Increase the coordinate of the hand
     */
    public void increaseCoordinate() {
        if (this.direction == -1) {
            this.count++;
            if (this.selected_Id > 6) {
                this.x_hand += 4;
            } else if (this.selected_Id < 6 && this.selected_Id != 0) {
                this.x_hand -= 4;
            } else if (this.selected_Id == 0 && !this.isEating) {
                this.y_hand += 4;
                this.x_hand = game.board.START_X
                        - (int) Math.sqrt(Constant.houseSize * Constant.houseSize - Math.pow((this.y_hand - game.board.START_Y - Constant.houseSize - 2), 2));
            } else if (!this.isEating) {
                this.y_hand -= 4;
                this.x_hand = game.board.START_X
                        + Constant.houseSize * 4 + (int) Math.sqrt(Constant.houseSize * Constant.houseSize - Math.pow((this.y_hand - game.board.START_Y), 2));
            } else if (this.selected_Id == 0) {
                if (this.count_eat == 0) {
                    this.x_hand -= 4;
                } else {
                    this.y_hand += 4;
                    this.x_hand = game.board.START_X
                            - (int) Math.sqrt(Constant.houseSize * Constant.houseSize - Math.pow((this.y_hand - game.board.START_Y - Constant.houseSize - 2), 2));
                }
            } else {
                if (this.count_eat == 0) {
                    this.x_hand += 4;
                } else {
                    this.y_hand -= 4;
                    this.x_hand = game.board.START_X
                            + Constant.houseSize * 4 + (int) Math.sqrt(Constant.houseSize * Constant.houseSize - Math.pow((this.y_hand - game.board.START_Y), 2));
                }
            }
        } else {
            this.count++;
            if (this.selected_Id > 6) {
                this.x_hand -= 4;
            } else if (this.selected_Id < 6 && this.selected_Id != 0) {
                this.x_hand += 4;
            } else if (this.selected_Id == 0 && !this.isEating) {
                this.y_hand -= 4;
                this.x_hand = game.board.START_X
                        - (int) Math.sqrt(Constant.houseSize * Constant.houseSize - Math.pow((this.y_hand - game.board.START_Y), 2));
            } else if (!this.isEating) {
                this.y_hand += 4;
                this.x_hand = game.board.START_X
                        + Constant.houseSize * 4 + (int) Math.sqrt(Constant.houseSize * Constant.houseSize - Math.pow((this.y_hand - game.board.START_Y - Constant.houseSize - 2), 2));
            } else if (this.selected_Id == 0) {
                if (this.count_eat == 0) {
                    this.x_hand -= 4;
                } else {
                    this.y_hand -= 4;
                    this.x_hand = game.board.START_X
                            - (int) Math.sqrt(Constant.houseSize * Constant.houseSize - Math.pow((this.y_hand - game.board.START_Y), 2));
                }
            } else {
                if (this.count_eat == 0) {
                    this.x_hand += 4;
                } else {
                    this.y_hand += 4;
                    this.x_hand = game.board.START_X
                            + Constant.houseSize * 4 + (int) Math.sqrt(Constant.houseSize * Constant.houseSize - Math.pow((this.y_hand - game.board.START_Y - Constant.houseSize - 2), 2));
                }
            }
        }
    }

    /**
     * Draw the Controller (the hand)
     *
     * @param graphics2D => draw component
     */
    public void draw(Graphics2D graphics2D) {
        if (this.state == WAIT) {
            if (this.action_pickStep == 1) {
                graphics2D.drawImage(Game.pickStone, this.x_hand, this.y_hand - Constant.houseSize / 2, null);
            } else if (this.action_pickStep == 2) {
                graphics2D.drawImage(Game.putHandDown, this.x_hand, this.y_hand - Constant.houseSize / 2, null);
            }
        } else if (this.state == GO) {
            if (this.isDropping) {
                // Drop Stone to board
                graphics2D.drawImage(Game.dropStone, this.x_hand, this.y_hand - Constant.houseSize / 2, null);
            } else {
                // Lift your hands up
                graphics2D.drawImage(Game.holdHand, this.x_hand, this.y_hand - Constant.houseSize / 2, null);
                graphics2D.setColor(Color.yellow);
                graphics2D.drawString(String.valueOf(stoneInHand), this.x_hand + Constant.houseSize / 2 - 7, this.y_hand - Constant.houseSize / 2 + 38);
            }

        } else if (this.state == CHECK) {
            if (this.action_pickStep == 1) {
                graphics2D.drawImage(Game.pickStone, this.x_hand, this.y_hand - Constant.houseSize / 2, null);
            } else if (this.action_pickStep == 2) {
                graphics2D.drawImage(Game.putHandDown, this.x_hand, this.y_hand - Constant.houseSize / 2, null);
            }
        } else if (this.state == EAT) {
            if (this.count_eat == 1 && this.action_eatStep != 0) {
                if (this.action_eatStep == 1) {
                    graphics2D.drawImage(Game.pickStone_1, this.x_hand, this.y_hand - Constant.houseSize / 2, null);
                } else if (this.action_eatStep == 2) {
                    graphics2D.drawImage(Game.pickStone_2, this.x_hand, this.y_hand - Constant.houseSize / 2 + 10, null);
                }
            } else { // action eat = 0 or in state EAT but in empty house
                graphics2D.drawImage(Game.emptyHand, this.x_hand, this.y_hand - Constant.houseSize / 2, null);
            }
        }

    }
}
