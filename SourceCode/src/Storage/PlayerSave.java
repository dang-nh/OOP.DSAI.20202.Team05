package Storage;

/**
 * Create to save score of player
 */
public class PlayerSave {
    public int numberStone_have;
    public int numberBoss_have;

    public PlayerSave() {
    }

    public PlayerSave(int numberStone_have, int numberBoss_have) {
        this.numberStone_have = numberStone_have;
        this.numberBoss_have = numberBoss_have;
    }
}
