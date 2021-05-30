package Constant;


/**
 * Create to save the constant attribute of Game
 */
public class Constant {
    /**
     * Width of the frame
     */
    public static int frameWidth = 1280;
    /**
     * Height of the frame.
     */
    public static int frameHeight = 800;

    /**
     * Size of the house
     */
    public static int houseSize = 100;

    /**
     * Start position of X,Y
     */
    public static final int START_X = 304, START_Y = 280;

    /**
     * Time of one second in nanoseconds. 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanoSec = 1000000000L;

    /**
     * Time of one millisecond in nanoseconds. 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milliSecInNanoSec = 1000000L;

    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    public static final int GAME_FPS = 60;

    /**
     * Possible states of the game
     */
    public enum GameStateMenu {
        STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, PLAY,
        OPTIONS, RULES, PLAYING, GAMEOVER, PAUSE, SUMMARY
    }

    /**
     * Coordinate of button
     */
    // Main Menu
    public static int x_btn_start = frameWidth / 10 - 40, y_btn_start = frameWidth / 8 - 13;
    public static int x_btn_rules = frameWidth / 10 - 40, y_btn_rules = frameHeight / 8 + 175;
    public static int x_btn_options = frameWidth / 10 - 40, y_btn_options = frameHeight / 8 + 300;
    public static int x_btn_exit = frameWidth / 10 - 40, y_btn_exit = frameHeight / 8 + 425;

    // Animation
    public static int x_animation = 931, y_animation = 185;
}
