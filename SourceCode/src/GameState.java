import Storage.BossSave;
import Storage.PlayerSave;
import GameStep.Step;


/**
 * The state of each turn
 * Contain information of Player and BossHouse through each turn
 */
public class GameState {
    BossSave bossHouse0_save, bossHouse6_save; // save boss
    int[] numberStoneSave; // save chess house
    Step preStep; // save turn
    PlayerSave player1_save, player2_save; // save player
    public boolean gameContinue; // game continue or not

    public GameState() {
        this.bossHouse0_save = new BossSave(0, true);
        this.bossHouse6_save = new BossSave(0, true);
        this.numberStoneSave = new int[12];
        for (int i = 1; i < 12; i++) {
            if (i == 6) continue;
            this.numberStoneSave[i] = 5;
        }
        this.player1_save = new PlayerSave();
        this.player2_save = new PlayerSave();
        this.preStep = new Step();
        this.gameContinue = true;
    }

    public GameState(Game game, Step preStep, boolean gameContinue) {
        // save step
        this.preStep = new Step();
        this.preStep.choose_Id = preStep.choose_Id;
        this.preStep.direction = preStep.direction;

        // save boss
        this.bossHouse0_save = new BossSave(game.board.bossHouse0.getNumberStone(), game.board.bossHouse0.isHasBoss());
        this.bossHouse6_save = new BossSave(game.board.bossHouse6.getNumberStone(), game.board.bossHouse6.isHasBoss());

        // save house
        numberStoneSave = new int[12];
        for (int i = 1; i < 6; i++) {
            numberStoneSave[i] = game.board.chessHouses[i].getNumberStone();
        }
        for (int i = 7; i < 12; i++) {
            numberStoneSave[i] = game.board.chessHouses[i].getNumberStone();
        }

        // save score player
        player1_save = new PlayerSave(game.player1.numberStone_have, game.player1.numberBoss_have);
        player2_save = new PlayerSave(game.player2.numberStone_have, game.player2.numberBoss_have);
        this.gameContinue = gameContinue;
    }

    public GameState(Step preStep, Board board, Player player1, Player player2, boolean gameContinue) {

        // save step
        this.preStep = new Step();
        this.preStep.choose_Id = preStep.choose_Id;
        this.preStep.direction = preStep.direction;

        // save Boss
        this.bossHouse0_save = new BossSave(board.bossHouse0.getNumberStone(), board.bossHouse0.isHasBoss());
        this.bossHouse6_save = new BossSave(board.bossHouse6.getNumberStone(), board.bossHouse6.isHasBoss());

        // save house
        numberStoneSave = new int[12];
        for (int i = 1; i < 6; i++) {
            numberStoneSave[i] = board.chessHouses[i].getNumberStone();
        }
        for (int i = 7; i < 12; i++) {
            numberStoneSave[i] = board.chessHouses[i].getNumberStone();
        }

        // save score player
        player1_save = new PlayerSave(player1.numberStone_have, player1.numberBoss_have);
        player2_save = new PlayerSave(player2.numberStone_have, player2.numberStone_have);
        this.gameContinue = gameContinue;
    }
}
