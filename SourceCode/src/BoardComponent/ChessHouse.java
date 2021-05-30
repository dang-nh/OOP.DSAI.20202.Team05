package BoardComponent;

import Constant.Constant;
import Static.CustomFont;
import Static.ImageGame;

import java.awt.*;
import java.awt.geom.Rectangle2D;


/**
 * The ChessHouse, contains only Stone
 * Draw it on the board
 * Extends the abstract class House
 */
public class ChessHouse extends House {
    private boolean isChoose;

    /**
     * Equal to 1 or -1
     * If in side of player1. Direction = 1 => turn right and direction = -1 is turn left and vice versa.
     */
    private int direction;

    public ChessHouse() {
        this.setX(0);
        this.setY(0);
        this.setHouseID(0);
        isChoose = false;
        direction = 0;
    }

    public void setChessHouse(int houseID, int x, int y, int numberStone) {
        this.setX(x);
        this.setY(y);
        this.setHouseID(houseID);
        this.setNumberStone(numberStone);
        this.setShape(new Rectangle2D.Double(x, y, Constant.houseSize, Constant.houseSize));
    }

    /**
     * Draw the bossHouse to the board
     */
    public void drawHouse(Graphics2D graphics2D) {
        graphics2D.setColor(Color.blue);
        // Draw the chess House
        if ((isChoose) && (getHouseID() > 6)) {
            graphics2D.drawImage(ImageGame.houseChosen_1, getX() - 100, getY(), null);
            if (direction == 1) {
                graphics2D.drawImage(ImageGame.houseChosen1_left, getX() - 100 + 10, getY() + 118, null);
            } else if (direction == -1) {
                graphics2D.drawImage(ImageGame.houseChosen1_right, getX() - 100 + 212, getY() + 123, null);
            }
        }
        if ((isChoose) && (getHouseID() < 6)) {
            graphics2D.drawImage(ImageGame.houseChosen_2, getX() - 100, getY() - 100, null);
            if (direction == 1) {
                graphics2D.drawImage(ImageGame.houseChosen2_right, getX() - 100 + 210, getY() - 100 + 39, null);
            } else if (direction == -1) {
                graphics2D.drawImage(ImageGame.houseChosen2_left, getX() - 100 + 18, getY() - 100 + 41, null);
            }
        }
        // Draw stone in house
        graphics2D.setColor(Color.gray);
        if (getNumberStone() == 0) {
            if (isChoose) {
                System.out.println("House is empty!");
            }
        } else if ((getNumberStone() <= 7) && (getNumberStone() >= 1)) {
            graphics2D.drawImage(ImageGame.Stones[getNumberStone()], getX(), getY(), null);
        } else {
            graphics2D.drawImage(ImageGame.Stones[8], getX(), getY(), null);
        }

        // Draw number of stone
        if (getNumberStone() > 5) {
            graphics2D.setColor(new Color(109, 107, 82));
            graphics2D.setFont(CustomFont.mistral.deriveFont(Font.BOLD, 20));
            graphics2D.drawString(String.valueOf(getNumberStone()), getX() + 5, getY() + 92);
        }
    }

    @Override
    public void increaseStone() {
        super.increaseStone();
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public int getDirect() {
        return direction;
    }

    public void setDirect(int direction) {
        this.direction = direction;
    }
}
