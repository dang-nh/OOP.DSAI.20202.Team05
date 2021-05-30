import BoardComponent.BossHouse;
import BoardComponent.ChessHouse;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Storage the GameState of game
 * Use to Rollback if necessary
 */
public class GameHistory {
    ArrayList<GameState> gameStateStack;
    static final int x = 230;
    static final int y = 650;
    static final Shape shape = new Rectangle2D.Double(x, y, 160, 75);
    int preToken = 0;   // to return player token after rollback
    Game game;
    ChessHouse[] chessHouses;
    BossHouse bossHouse0;
    BossHouse bossHouse6;
    Player player1;
    Player player2;

    public GameHistory(Game game) {
        this.game = game;
        this.gameStateStack = new ArrayList<>();
        GameState initState = new GameState();
        this.gameStateStack.add(initState);
        this.gameStateStack.add(initState);
        chessHouses = game.board.chessHouses;
        bossHouse0 = game.board.bossHouse0;
        bossHouse6 = game.board.bossHouse6;
        player1 = game.player1;
        player2 = game.player2;
    }

    public GameHistory(){
        this.gameStateStack = new ArrayList<>();
        GameState initState = new GameState();
        this.gameStateStack.add(initState);
        this.gameStateStack.add(initState);
    }

    public void add(GameState state) {
        gameStateStack.add(state);
    }

    /**
     * The function execute Roll back
     * Call when turnToken = 7
     */
    public void Rollback() {
        // Check number of instance in history
        // if < 3 return
        GameState lastState;
        if (gameStateStack.size() < 3) {
            System.out.println(gameStateStack.size());
            game.turnToken = this.preToken;
            this.preToken = 0;
            return;
        }

        // else remove 2 last step
        gameStateStack.remove(gameStateStack.size() - 1);
        gameStateStack.remove(gameStateStack.size() - 1);

        // take last step
        lastState = lastGameState();

        // roll back board according to last State
        bossHouse0.setHasBoss(lastState.bossHouse0_save.hasBoss);
        bossHouse0.setNumberStone(lastState.bossHouse0_save.numStone_have);
        bossHouse6.setHasBoss(lastState.bossHouse6_save.hasBoss);
        bossHouse6.setNumberStone(lastState.bossHouse6_save.numStone_have);

        // set stone again
        for (int i = 1; i < 12; i++) {
            if (i == 6) continue;
            chessHouses[i].setNumberStone(lastState.numberStoneSave[i]);
        }

        // set score again
        player1.numberBoss_have = lastState.player1_save.numberBoss_have;
        player1.numberStone_have = lastState.player1_save.numberStone_have;
        player2.numberBoss_have = lastState.player2_save.numberBoss_have;
        player2.numberStone_have = lastState.player2_save.numberStone_have;

        // set token for player continue playing
        game.turnToken = this.preToken;

        this.preToken = 0;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameHistory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public GameState lastGameState(){
        return gameStateStack.get(gameStateStack.size() - 1);
    }

    /**
     * Draw the Rollback button
     *
     * @param graphics2D => paint component
     */
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(Game.btn_rollBack, x, y, null);
    }
}
