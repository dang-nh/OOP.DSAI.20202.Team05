import Constant.Constant;
import Static.CustomFont;
import Static.ImageGame;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * MainGame that controls the game (Game.java) that created it, update it and
 * Draw it on the screen.
 */
public class MainGame extends Frame {

    public static int frameWidth;
    public static int frameHeight;

    public static final long secInNanoSec = Constant.secInNanoSec;
    public static final long milliSecInNanoSec = Constant.milliSecInNanoSec;
    public static final int GAME_FPS = Constant.GAME_FPS;

    /**
     * Current state of the game
     */
    public static Constant.GameStateMenu gameStateMenu;
    public static Constant.GameStateMenu preStateMenu;

    /**
     * Elapsed game time in nanoseconds.
     */
    public static long gameTime;
    // Used for calculating elapsed time.
    private long lastTime;

    // Use for state GameOVer. Calculate the time to show the Score of each player and show icon.
    private long gameOverTimeCount;
    int p1ScoreCount;
    int p2ScoreCount;
    int player1Score, player2Score;
    public static boolean music_on;

    // Image background introduce
    private BufferedImage bg_introduce;

    // Image button pause
    public static BufferedImage btn_pause, pause_panel;

    // Main menu
    private BufferedImage bg_menu;
    private BufferedImage btn_options, btn_exit, btn_rules, btn_start;
    private BufferedImage sound_option, btn_sound_on, btn_sound_off;
    private BufferedImage result_img, victory_icon, lose_icon, playAgain, btn_menu;

    // Coordinate of the result
    int x_result, y_result;

    // Rules of game
    private BufferedImage btn_previous, btn_back, btn_next;
    public BufferedImage[] rules;
    // Page of rules
    private int pageRule;

    // The actual game
    private Game game;

    public MainGame() {
        super();
        gameStateMenu = Constant.GameStateMenu.VISUALIZING;

        //We start game in new thread.
        Thread gameThread = new Thread(this::GameLoop);
        gameThread.start();
    }

    /**
     * Set variables and objects. This method is intended to set the variables
     * and objects for this class, variables and objects for the actual game can
     * be set in Game.java.
     */
    private void Initialize() {
        rules = new BufferedImage[9];
        pageRule = 1;
        gameOverTimeCount = 0;
        music_on = true;
    }

    private void preStarting() {
        bg_introduce = ImageGame.bg_introduce;
    }

    public void initCoordinate() {
        x_result = (frameWidth - result_img.getWidth()) / 2;
        y_result = (frameHeight - result_img.getHeight()) / 2;
    }

    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is
     * updated and then the game is drawn on the screen.
     */
    private void GameLoop() {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        // This variables are used for calculating the time that defines for how long we should put thread to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;

        while (true) {
            beginTime = System.nanoTime();

            switch (gameStateMenu) {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    game.UpdateGame(gameTime, mousePosition());
                    lastTime = System.nanoTime();

                    if (Frame.mouseButtonState(1)) {
                        if (new Rectangle(frameWidth - btn_pause.getWidth() - 20, 30, btn_pause.getWidth(), btn_pause.getHeight()).contains(mousePosition())) {
                            gameStateMenu = Constant.GameStateMenu.PAUSE;
                        }
                    }
                    break;
                case MAIN_MENU:
                    gameMenu();
                    break;
                case PLAY:
                    newGame();
                    break;
                case SUMMARY:
                    summary();
                    gameStateMenu = Constant.GameStateMenu.GAMEOVER;
                    break;
                case GAMEOVER:
                    gameover();
                    break;
                case PAUSE:
                    pause();
                    break;
                case OPTIONS:
                    options();
                    break;
                case RULES:
                    gameRules();
                    break;
                case GAME_CONTENT_LOADING:
                    //...
                    break;
                case STARTING:
                    // Sets variables and objects.
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();
                    initCoordinate();
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // When all things that are called above finished, we change game status to main menu.
                    gameStateMenu = Constant.GameStateMenu.MAIN_MENU;
                    break;
                case VISUALIZING:
                    if (this.getWidth() > 1 && visualizingTime > secInNanoSec) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        preStarting();
                        // When we get size of frame we change status.
                        gameStateMenu = Constant.GameStateMenu.STARTING;
                    } else {
                        frameWidth = Constant.frameWidth;
                        frameHeight = Constant.frameHeight;
                        // calculate time visualize
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

            // Repaint the screen each loop
            repaint();

            timeTaken = System.nanoTime() - beginTime;

            // Period update game
            long GAME_UPDATE_PERIOD = secInNanoSec / GAME_FPS;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milliSecInNanoSec; // In milliseconds
            // Thread FPS
            if (timeLeft < 10) {
                timeLeft = 10; //set a minimum
            }
            try {
                // Provides the necessary delay and also yields control so that other thread can do work.
                sleep(timeLeft);
            } catch (InterruptedException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Draw the game to the screen. It is called through repaint() method in
     * GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.LIGHT_GRAY);
        graphics2D.setFont(CustomFont.mistral.deriveFont(Font.BOLD, 40));
        if (gameStateMenu != Constant.GameStateMenu.VISUALIZING) {
            graphics2D.drawString("Loading.....", frameWidth / 2 - 70, frameHeight / 2);
        } else {
            graphics2D.drawString(String.valueOf(gameStateMenu), frameWidth / 2 - 70, frameHeight / 2);
        }
        switch (gameStateMenu) {
            case PLAYING -> {
                game.draw(graphics2D, gameTime);
                graphics2D.drawImage(btn_pause, frameWidth - btn_pause.getWidth() - 20, 30, null);
            }
            case MAIN_MENU -> {
                graphics2D.drawImage(bg_menu, 0, 0, frameWidth, frameHeight, null);
                graphics2D.drawImage(btn_start, Constant.x_btn_start, Constant.y_btn_start, null);
                graphics2D.drawImage(btn_rules, Constant.x_btn_rules, Constant.y_btn_rules, null);
                graphics2D.drawImage(btn_options, Constant.x_btn_options, Constant.y_btn_options, null);
                graphics2D.drawImage(btn_exit, Constant.x_btn_exit, Constant.y_btn_exit, null);
            }
            case GAMEOVER -> {
                game.draw(graphics2D, gameTime);
                graphics2D.setColor(new Color(255, 144, 29));
                graphics2D.setFont(CustomFont.mistral.deriveFont(Font.BOLD, 80));
                graphics2D.drawImage(result_img, x_result, y_result, null);

                if (gameOverTimeCount > GAME_FPS) {
                    if (gameOverTimeCount % 3 == 0) {
                        if (p1ScoreCount < player1Score) {
                            p1ScoreCount++;
                        }
                        if (p2ScoreCount < player2Score) {
                            p2ScoreCount++;
                        }
                    }
                    graphics2D.drawString(String.valueOf(p1ScoreCount), x_result + result_img.getWidth() / 2 + 24, y_result + result_img.getHeight() / 2 + 81);
                    graphics2D.drawString(String.valueOf(p2ScoreCount), x_result + result_img.getWidth() / 2 + 24, y_result + result_img.getHeight() * 3 / 4 + 23);
                }

                // Confirm the winner and draw victory icon
                if (gameOverTimeCount > GAME_FPS * 7 / 2) {
                    if (player1Score > player2Score) {
                        graphics2D.drawImage(victory_icon, x_result + 520, y_result + result_img.getHeight() / 2 + 8, null);
                    } else if (player1Score < player2Score) {
                        graphics2D.drawImage(victory_icon, x_result + 520, y_result + result_img.getHeight() * 3 / 4 - 37, null);
                    }
                }

                // Draw lose icon
                if (gameOverTimeCount > GAME_FPS * 10 / 2) {
                    if (player1Score > player2Score) {
                        graphics2D.drawImage(lose_icon, x_result + 572, y_result + result_img.getHeight() * 3 / 4 - 37, null);
                    } else if (player1Score < player2Score) {
                        graphics2D.drawImage(lose_icon, x_result + 572, y_result + result_img.getHeight() / 2 + 8, null);
                    }
                }
                if (gameOverTimeCount > GAME_FPS * 12 / 2) {
                    graphics2D.drawImage(playAgain, x_result + (result_img.getWidth() - playAgain.getWidth()) / 4 - 37, y_result + 539, null);
                    graphics2D.drawImage(btn_menu, x_result + (result_img.getWidth() - btn_menu.getWidth()) * 3 / 4 + 14, y_result + 539, null);
                }
            }
            case PAUSE -> {
                game.draw(graphics2D, gameTime);
                graphics2D.drawImage(pause_panel, 240, 176, null);
            }
            case OPTIONS -> {
                // Sound UI
                graphics2D.drawImage(bg_menu, 0, 0, frameWidth, frameHeight, null);
                graphics2D.drawImage(btn_back, 0, 0, null);
                graphics2D.drawImage(sound_option, (frameWidth - sound_option.getWidth()) / 2, (frameHeight - sound_option.getHeight()) / 2, null);
                if (music_on) {
                    graphics2D.drawImage(btn_sound_on, (frameWidth + sound_option.getWidth()) / 2 + 50, (frameHeight - sound_option.getHeight()) / 2 - 27, null);
                } else {
                    graphics2D.drawImage(btn_sound_off, (frameWidth + sound_option.getWidth()) / 2 + 50, (frameHeight - sound_option.getHeight()) / 2 - 27, null);
                }
            }
            case RULES -> {
                graphics2D.drawImage(rules[pageRule], 0, 0, null);
                graphics2D.drawImage(btn_back, 0, 0, null);
                if (pageRule > 1) {
                    graphics2D.drawImage(btn_previous, 10, frameHeight - btn_previous.getHeight() - 10, null);
                }
                if (pageRule < 8) {
                    graphics2D.drawImage(btn_next, frameWidth - btn_next.getWidth() - 10, frameHeight - btn_next.getHeight() - 10, null);
                }
            }
            case STARTING -> graphics2D.drawImage(bg_introduce, 0, 0, null);
            case GAME_CONTENT_LOADING -> graphics2D.drawString("Loading.....", frameWidth / 2 - 70, frameHeight / 2);
            case SUMMARY -> game.draw(graphics2D, gameTime);
        }
    }

    /**
     * Game Menu
     */
    private void gameMenu() {
        if (Frame.mouseButtonState(MouseEvent.BUTTON1)) {
            if (new Rectangle(Constant.x_btn_start, Constant.y_btn_start, btn_start.getWidth(), btn_start.getHeight()).contains(mousePosition())) {
                gameStateMenu = Constant.GameStateMenu.PLAY;
            }

            if (new Rectangle(Constant.x_btn_rules, Constant.y_btn_rules, btn_rules.getWidth(), btn_rules.getHeight()).contains(mousePosition())) {
                gameStateMenu = Constant.GameStateMenu.RULES;
            }

            if (new Rectangle(Constant.x_btn_options, Constant.y_btn_options, btn_options.getWidth(), btn_options.getHeight()).contains(mousePosition())) {
                gameStateMenu = Constant.GameStateMenu.OPTIONS;
            }

            if (new Rectangle(Constant.x_btn_exit, Constant.y_btn_exit, btn_exit.getWidth(), btn_exit.getHeight()).contains(mousePosition())) {
                int resp = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                        "Exit?", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    return;
                }
            }
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        preStateMenu = Constant.GameStateMenu.MAIN_MENU;
    }

    private void summary() {
        player1Score = game.player1.numberStone_have + game.player1.numberBoss_have * 5;
        player2Score = game.player2.numberStone_have + game.player2.numberBoss_have * 5;
        p1ScoreCount = 0;
        p2ScoreCount = 0;
        if (player1Score > player2Score) {
            for (int i = 2; i < game.gameHistory.gameStateStack.size(); i += 2) {
                game.gameHistory.gameStateStack.get(i).preStep.setWinner(1);
            }
            for (int i = 3; i < game.gameHistory.gameStateStack.size(); i += 2) {
                game.gameHistory.gameStateStack.get(i).preStep.setWinner(-1);
            }
        } else if (player2Score > player1Score) {
            for (int i = 3; i < game.gameHistory.gameStateStack.size(); i += 2) {
                game.gameHistory.gameStateStack.get(i).preStep.setWinner(1);
            }
            for (int i = 2; i < game.gameHistory.gameStateStack.size(); i += 2) {
                game.gameHistory.gameStateStack.get(i).preStep.setWinner(-1);
            }
        }

        if (music_on) {
            Game.INGAME.stop();
            try {
                Game.GAMEOVER.play();
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void gameover() {
        if (Frame.mouseButtonState(MouseEvent.BUTTON1) && (gameOverTimeCount > GAME_FPS * 12 / 2)) {
            if (new Rectangle(x_result + (result_img.getWidth() - playAgain.getWidth()) / 4, y_result + 510, playAgain.getWidth(), playAgain.getHeight()).contains(mousePosition())) {
                Game.GAMEOVER.stop();
                restartGame();
                gameStateMenu = Constant.GameStateMenu.PLAYING;
            }
        }
        if (Frame.mouseButtonState(MouseEvent.BUTTON1) && (gameOverTimeCount > GAME_FPS * 12 / 2)) {
            if (new Rectangle(x_result + (result_img.getWidth() - btn_menu.getWidth()) * 3 / 4, y_result + 510, btn_menu.getWidth(), btn_menu.getHeight()).contains(mousePosition())) {
                Game.GAMEOVER.stop();
                try {
                    sleep(300);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                gameStateMenu = Constant.GameStateMenu.MAIN_MENU;
            }
        }
        gameOverTimeCount++;
    }

    private void gameRules() {
        if (Frame.keyboardKeyState(KeyEvent.VK_LEFT) && (pageRule > 1)) {
            pageRule--;
            try {
                sleep(300);
            } catch (InterruptedException ignored) {
            }
        }
        if (Frame.keyboardKeyState(KeyEvent.VK_RIGHT) && (pageRule < 8)) {
            pageRule++;
            try {
                sleep(300);
            } catch (InterruptedException ignored) {
            }
        }
        if (Frame.mouseButtonState(1)) {
            if (new Rectangle(0, 0, btn_back.getWidth(), btn_back.getHeight()).contains(mousePosition())) {
                gameStateMenu = preStateMenu;
                try {
                    sleep(300);
                } catch (InterruptedException ignored) {
                }
            }
            if ((new Rectangle(10, frameHeight - btn_previous.getHeight() - 10, btn_previous.getWidth(), btn_previous.getHeight()).contains(mousePosition()))
                    && (pageRule > 1)) {
                pageRule--;
                try {
                    sleep(300);
                } catch (InterruptedException ignored) {
                }
            }
            if ((new Rectangle(frameWidth - btn_next.getWidth() - 10, frameHeight - btn_next.getHeight() - 10, btn_next.getWidth(), btn_next.getHeight()).contains(mousePosition()))
                    && (pageRule < 8)) {
                pageRule++;
                try {
                    sleep(300);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private void options() {
        if (Frame.mouseButtonState(1)) {
            if (new Rectangle(0, 0, btn_back.getWidth(), btn_back.getHeight()).contains(mousePosition())) {
                gameStateMenu = Constant.GameStateMenu.MAIN_MENU;
                try {
                    sleep(300);
                } catch (InterruptedException ignored) {
                }
            }
            if (new Rectangle((frameWidth + sound_option.getWidth()) / 2 + 50, (frameHeight - sound_option.getHeight()) / 2 - 27, btn_sound_on.getWidth(), btn_sound_on.getHeight()).contains(mousePosition())) {
                music_on = !music_on;
                try {
                    sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private void pause() {
        if (Frame.mouseButtonState(MouseEvent.BUTTON1)) {
            if (new Rectangle(550, 266, 205, 90).contains(mousePosition())) {
                gameStateMenu = Constant.GameStateMenu.PLAYING;
            }

            if (new Rectangle(550, 375, 205, 90).contains(mousePosition())) {
                gameStateMenu = Constant.GameStateMenu.RULES;
            }

            if (new Rectangle(550, 480, 205, 90).contains(mousePosition())) {
                Game.INGAME.stop();
                gameStateMenu = Constant.GameStateMenu.MAIN_MENU;
                try {
                    //Provides the necessary delay and also yields control so that other thread can do work.
                    sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        }
        preStateMenu = Constant.GameStateMenu.PAUSE;
    }

    /**
     * Starts new game.
     */
    private void newGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        game = new Game();
    }

    /**
     * Restart game - reset game time and call RestartGame() method of game
     * reset some variables.
     */
    private void restartGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        gameOverTimeCount = 0;
        game.RestartGame();
        if (MainGame.music_on) {
            if (Game.INGAME.isPlaying()) {
                Game.INGAME.stop();
            }
            try {
                Game.INGAME.playLoop(Clip.LOOP_CONTINUOUSLY);
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Returns the position of the mouse pointer in game frame/window
     * If mouse position is null than this method return 0,0 coordinate.
     *
     * @return Point of mouse coordinates.
     */
    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();

            if (mp != null) {
                return this.getMousePosition();
            } else {
                return new Point(0, 0);
            }
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    /**
     * This method is called when keyboard key is released.
     *
     * @param event KeyEvent
     */
    @Override
    public void keyReleasedGameWindow(KeyEvent event) {
        switch (gameStateMenu) {
            case GAMEOVER:
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartGame();
                }
                break;
            case PLAYING:
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameStateMenu = Constant.GameStateMenu.PAUSE;
                }
                break;
            case PAUSE:
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameStateMenu = Constant.GameStateMenu.PLAYING;
                }
                break;
            case MAIN_MENU:
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
                break;
            case RULES:
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameStateMenu = preStateMenu;
                }
                break;
            case OPTIONS:
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameStateMenu = Constant.GameStateMenu.MAIN_MENU;
                }
                break;
        }
    }

    /**
     * Load files - images, sounds, ... This method is intended to load files
     * for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent() {
        bg_menu = ImageGame.bg_menu;

        btn_start = ImageGame.btn_start;
        btn_rules = ImageGame.btn_rules;
        btn_options = ImageGame.btn_options;
        btn_exit = ImageGame.btn_exit;

        rules = ImageGame.rules;
        btn_back = ImageGame.btn_back;
        btn_previous = ImageGame.btn_previous;
        btn_next = ImageGame.btn_next;

        btn_pause = ImageGame.btn_pause;
        pause_panel = ImageGame.pause_panel;

        result_img = ImageGame.result_img;
        victory_icon = ImageGame.victory_icon;
        lose_icon = ImageGame.lose_icon;
        playAgain = ImageGame.playAgain;
        btn_menu = ImageGame.btn_menu;

        sound_option = ImageGame.sound_option;
        btn_sound_on = ImageGame.btn_sound_on;
        btn_sound_off = ImageGame.btn_sound_off;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
