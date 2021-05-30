import Constant.Constant;
import Static.AudioAction;
import Static.ImageGame;
import Static.Sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


public class Game {
    public Board board;
    public Player player1;
    public Player player2;
    public Controller controller;
    public GameHistory gameHistory;

    /**
     * Use to describe the turn
     * 0 => finish game and summary
     * 1 => turn of player 1
     * 2 => turn of player 2
     * 3 => turn of controller handle player 1
     * 4 => turn of controller handle player 2
     * 5 => turn of animation 1, prepare for turn of player 2
     * 6 => turn of animation 2, prepare for turn of player 1
     * 7 => turn of history for RollBack
     */
    public int turnToken;
    public int lastToken;

    // Image
    public BufferedImage background;
    public static BufferedImage[] Stones;
    public static BufferedImage boss0, boss6;
    public static BufferedImage eat1boss, eat2boss;
    public static BufferedImage houseChosen_1, houseChosen_2, houseChosen1_left, houseChosen2_left, houseChosen1_right, houseChosen2_right;
    public static BufferedImage avatar_player1, avatar_player2, turn_focus;

    // Hand action
    public static BufferedImage pickStone, dropStone, putHandDown, holdHand, emptyHand, pickStone_1, pickStone_2;

    // Button RollBack
    public static BufferedImage btn_rollBack;

    // Animation
    public static BufferedImage HVTDat1, HVTDat2;
    public static BufferedImage[] HVTDat;
    public int animationStep;

    private long timeFlag;
    public final int TIME_LIMIT = 60; // Limit time of turn

    public static Random random = new Random();

    // Sound
    public static final AudioAction GAMEOVER = Sound.GAMEOVER;
    public static final AudioAction INGAME = Sound.INGAME;
    public static final AudioAction EAT_BOSS = Sound.EAT_BOSS;
    public static final AudioAction EAT_MUCH = Sound.EAT_MUCH;
    public static final AudioAction ADD_STONE = Sound.ADD_STONE;
    public static final AudioAction ROLLBACK = Sound.ROLLBACK;
    public static final AudioAction GOOD_DIRECT = Sound.GOOD_DIRECT;
    public static final AudioAction GET_MUCH = Sound.GET_MUCH;


    public Game() {
        MainGame.gameStateMenu = Constant.GameStateMenu.GAME_CONTENT_LOADING;
        Thread threadInitGame = new Thread(() -> {
            // Sets variables and objects for the game.
            Initialize();
            // Load game files (images, sounds, ...)
            LoadContent();
            MainGame.gameStateMenu = Constant.GameStateMenu.PLAYING;
        });
        threadInitGame.start();
    }

    /**
     * Set variables and objects for the game.
     */
    private void Initialize() {
        if (MainGame.music_on) {
            if (Game.INGAME.isPlaying()) {
                Game.INGAME.stop();
            }
            try {
                Game.INGAME.playLoop(Clip.LOOP_CONTINUOUSLY);
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                ex.printStackTrace();
            }
        }

        // Create board
        this.board = new Board();

        // Create Player1
        player1 = new Player();
        player1.initPlayer(this, null, 1);

        // Create Player2
        player2 = new Player();
        player2.initPlayer(this, null, 2);

        // Create controller
        controller = new Controller(this);

        // Create game history
        gameHistory = new GameHistory(this);

        // Choose player play first
        turnToken = 1 + random.nextInt(2); // random 1 or 2

        // Image of stones
        Stones = new BufferedImage[10];

        timeFlag = MainGame.gameTime;

        // Animation
        HVTDat = new BufferedImage[8];
        animationStep = 1;
    }

    /**
     * Restart game - reset some variables.
     */
    public void RestartGame() {
        board.resetBoard();
        player1.resetPlayer();
        player2.resetPlayer();
        this.gameHistory = new GameHistory(this);
        turnToken = 1 + random.nextInt(2); // random 1 or 2;
        timeFlag = MainGame.gameTime;
        animationStep = 1;
    }

    /**
     * Change time of this turn to second
     */
    public long standTime(long gameTime) {
        return (gameTime - timeFlag) / MainGame.secInNanoSec;
    }

    /**
     * Update game logic.
     *
     * @param gameTime      gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition) {
        lastToken = turnToken;
        switch (turnToken) {
            case 1: // turn of player 1
                // check time over or not
                if (standTime(gameTime) < TIME_LIMIT) {
                    player1.turn(gameTime, mousePosition);
                } else {
                    player1.autoPlay();
                }
                break;

            case 2: // turn of player 2
                if (standTime(gameTime) < TIME_LIMIT) {
                    player2.turn(gameTime, mousePosition);
                } else {
                    player2.autoPlay();
                }
                break;

            case 3: // turn of controller for player 1
                controller.handleGame(player1.getStep());
                break;

            case 4: // turn of controller for player 2
                controller.handleGame(player2.getStep());
                break;

            case 7: // Rollback to previous turn
                if (MainGame.music_on) {
                    if (ROLLBACK.isPlaying()) {
                        ROLLBACK.stop();
                    }
                    try {
                        ROLLBACK.play();
                    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                        e.printStackTrace();
                    }
                }
                gameHistory.Rollback();
                break;

            case 0: // game over
                MainGame.gameStateMenu = Constant.GameStateMenu.SUMMARY;
                break;
        }
        if (lastToken != turnToken) {
            timeFlag = gameTime;
        }
    }

    /**
     * Draw the game to the screen.
     *
     * @param graphics2D Graphics2D
     */
    public void draw(Graphics2D graphics2D, long gameTime) {

        long timeCount = (gameTime - timeFlag);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.drawImage(background, 0, 0, null);

        graphics2D.drawImage(avatar_player1, 581, 35, null);
        graphics2D.drawImage(avatar_player2, 465, 575, null);

        graphics2D.setColor(new Color(111, 106, 72));
        graphics2D.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        if ((turnToken == 1) || (turnToken == 2)) {
            graphics2D.drawString((TIME_LIMIT - 1 - timeCount / MainGame.secInNanoSec) + "s", 941, 127);
        }

//        graphics2D.drawString(String.valueOf("Token: " + turnToken), 900, 140);

        // Draw again the board after handle
        board.draw(graphics2D);
        controller.draw(graphics2D);
        graphics2D.setColor(new Color(51, 153, 153));

        // Play Again button
        gameHistory.draw(graphics2D);

        // Draw the score of each player
        board.drawScore(graphics2D, player1.numberStone_have, player2.numberStone_have, player1.numberBoss_have, player2.numberBoss_have);

        // Draw animation
        switch (turnToken) {
            case 1:
                graphics2D.drawImage(turn_focus, 465 - 50, 575 - 35, null);
            case 3:
                graphics2D.drawImage(HVTDat1, Constant.x_animation, Constant.y_animation, null);
                break;
            case 2:
                graphics2D.drawImage(turn_focus, 581 - 50, 0, null);
            case 4:
                graphics2D.drawImage(HVTDat2, Constant.x_animation, Constant.y_animation, null);
                break;
            case 5:
                animation1(graphics2D);
                break;
            case 6:
                animation2(graphics2D);
                break;
        }
    }

    public void animation1(Graphics2D g2d) {
        if (animationStep == 1) {
            g2d.drawImage(HVTDat[1], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 2) {
            g2d.drawImage(HVTDat[2], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 3) {
            g2d.drawImage(HVTDat[3], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 4) {
            g2d.drawImage(HVTDat[4], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 5) {
            g2d.drawImage(HVTDat[5], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 6) {
            g2d.drawImage(HVTDat[6], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 7) {
            g2d.drawImage(HVTDat[7], Constant.x_animation, Constant.y_animation, null);
        }
        try {
            Thread.sleep(0);
        } catch (Exception ignored) {
        }
        if (animationStep != 7) {
            animationStep++;
        } else {
            turnToken = 2;
        }
    }

    public void animation2(Graphics2D g2d) {
        if (animationStep == 1) {
            g2d.drawImage(HVTDat[1], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 2) {
            g2d.drawImage(HVTDat[2], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 3) {
            g2d.drawImage(HVTDat[3], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 4) {
            g2d.drawImage(HVTDat[4], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 5) {
            g2d.drawImage(HVTDat[5], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 6) {
            g2d.drawImage(HVTDat[6], Constant.x_animation, Constant.y_animation, null);
        }
        if (animationStep == 7) {
            g2d.drawImage(HVTDat[7], Constant.x_animation, Constant.y_animation, null);
        }
        try {
            Thread.sleep(0);
        } catch (Exception ignored) {
        }
        if (animationStep != 1) {
            animationStep--;
        } else {
            turnToken = 1;
        }
    }

    /**
     * Load game files images, sounds,....
     */
    private void LoadContent() {
        background = ImageGame.background;
        avatar_player1 = ImageGame.avatar_player1;
        avatar_player2 = ImageGame.avatar_player2;
        turn_focus = ImageGame.turn_focus;

        boss0 = ImageGame.boss0;
        boss6 = ImageGame.boss6;
        eat1boss = ImageGame.eat1boss;
        eat2boss = ImageGame.eat2boss;

        Stones = ImageGame.Stones;

        houseChosen_1 = ImageGame.houseChosen_1;
        houseChosen1_left = ImageGame.houseChosen1_left;
        houseChosen1_right = ImageGame.houseChosen1_right;
        houseChosen_2 = ImageGame.houseChosen_2;
        houseChosen2_left = ImageGame.houseChosen2_left;
        houseChosen2_right = ImageGame.houseChosen2_right;

        putHandDown = ImageGame.putHandDown;
        holdHand = ImageGame.holdHand;
        emptyHand = ImageGame.emptyHand;

        pickStone = ImageGame.pickStone;
        dropStone = ImageGame.dropStone;
        pickStone_1 = ImageGame.pickStone_1;
        pickStone_2 = ImageGame.pickStone_2;

        btn_rollBack = ImageGame.btn_rollBack;

        HVTDat1 = ImageGame.HVTDat1;
        HVTDat2 = ImageGame.HVTDat2;
        HVTDat = ImageGame.HVTDat;
    }
}
