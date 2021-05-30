package Storage;


/**
 * Create to save state of bossHouse
 */
public class BossSave {
    public int numStone_have;
    public boolean hasBoss;

    public BossSave() {}

    public BossSave(int numStone_have, boolean hasBoss) {
        this.numStone_have = numStone_have;
        this.hasBoss = hasBoss;
    }
}
