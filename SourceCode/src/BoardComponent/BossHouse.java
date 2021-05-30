package BoardComponent;

import Constant.Constant;
import Static.CustomFont;
import Static.ImageGame;

import java.awt.*;
import java.awt.geom.Rectangle2D;


/**
 * The BossHouse, contains Boss and Stone
 * Draw it on the board
 * Extends the abstract class House
 */
public class BossHouse extends House {
    private int population;
    private boolean hasBoss;

    public BossHouse() {
        this.setX(0);
        this.setY(0);
        this.setHouseID(0);
    }

    public void setBossHouse(int numStone, boolean hasBoss) {
        this.setNumberStone(numStone);
        this.hasBoss = hasBoss;
    }

    public void setBossHouse(int houseID, int x, int y, boolean hasBoss) {
        this.setX(x);
        this.setY(y);
        this.setHouseID(houseID);
        this.hasBoss = hasBoss;
        this.setShape(new Rectangle2D.Double(x, y, Constant.houseSize * 0.6, Constant.houseSize));
    }

    /**
     * Population in boss house = number stone + numberBoss * 5
     *
     * @return population
     */
    public int getPopulation() {
        if (this.isHasBoss()) {
            this.population = this.getNumberStone() + 5;
        } else {
            this.population = this.getNumberStone();
        }
        return this.population;
    }

    /**
     * Check BossHouse contain boss or not
     *
     * @return true or false
     */
    public boolean isHasBoss() {
        return this.hasBoss;
    }

    public void setHasBoss(boolean hasBoss) {
        this.hasBoss = hasBoss;
    }

    /**
     * Draw the BossHouse to the board
     */
    public void drawHouse(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(109, 107, 82));
        graphics2D.setFont(CustomFont.mistral.deriveFont(Font.BOLD, 40));
        // Draw the boss House
        if (getHouseID() == 6) {
            // Show number of population in BossHouse
            graphics2D.drawString(String.valueOf(this.getPopulation()), 880, 300);
            // Draw stone
            if (getNumberStone() == 0) {
                if (this.getPopulation() == 0) {
                    setPopulation(this.getPopulation());
                }
            } else if ((getNumberStone() <= 7) && (getNumberStone() >= 1)) {
                graphics2D.drawImage(ImageGame.Stones[getNumberStone()], getX() - 5 + 100, getY() + 30, null);
            } else {
                graphics2D.drawImage(ImageGame.Stones[8], getX() - 5 + 100, getY() + 30, null);
            }
            // Draw the boss if the boss house contain boss
            if (isHasBoss()) {
                graphics2D.drawImage(ImageGame.boss6, getX() + 100, getY(), null);
            }

        } else {
            graphics2D.drawString(String.valueOf(this.getPopulation()), 200, 470);
            if (getNumberStone() == 0) {
                if (this.getPopulation() == 0) {
                    setPopulation(this.getPopulation());
                }
            } else if ((getNumberStone() <= 7) && (getNumberStone() >= 1)) {
                graphics2D.drawImage(ImageGame.Stones[getNumberStone()], getX() - 5, getY() + 25, null);
            } else {
                graphics2D.drawImage(ImageGame.Stones[8], getX() - 5, getY() + 25, null);
            }
            if (isHasBoss()) {
                graphics2D.drawImage(ImageGame.boss0, getX() - 100, getY(), null);
            }
        }
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public void increaseStone() {
        super.increaseStone();
    }
}
