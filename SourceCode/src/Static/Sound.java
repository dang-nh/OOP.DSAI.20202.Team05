package Static;


/**
 * All the sound of this game
 */
public class Sound {
    public static AudioAction GAMEOVER;
    public static AudioAction INGAME;
    public static AudioAction EAT_BOSS;
    public static AudioAction ADD_STONE;
    public static AudioAction ROLLBACK;
    public static AudioAction GOOD_DIRECT;
    public static AudioAction GET_MUCH;

    static {
        GAMEOVER = new AudioAction("/Static/sounds/gameover.wav");
        GAMEOVER.setVolumeNumber(90);

        INGAME = new AudioAction("/Static/sounds/ingame.wav");
        INGAME.setVolumeNumber(90);

        EAT_BOSS = new AudioAction("/Static/sounds/eat_boss.wav");
        EAT_BOSS.setVolumeNumber(100);

        ADD_STONE = new AudioAction("/Static/sounds/add_stone.wav");
        ADD_STONE.setVolumeNumber(100);

        ROLLBACK = new AudioAction("/Static/sounds/rollBack.wav");
        ROLLBACK.setVolumeNumber(100);

        GOOD_DIRECT = new AudioAction("/Static/sounds/goodDirect.wav");
        GOOD_DIRECT.setVolumeNumber(100);

        GET_MUCH = new AudioAction("/Static/sounds/getMuchStone.wav");
        GET_MUCH.setVolumeNumber(100);
    }
}
