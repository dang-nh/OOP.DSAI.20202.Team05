package BoardComponent;

import java.awt.*;


/**
 * The abstract class for ChessHouse and BossHouse
 */
public abstract class House {
    // Coordinate
    private int x, y;
    private int houseID;
    private int numberStone;
    private Shape shape;

    public House() {
    }

    public House(int numberStone) {
        this.numberStone = numberStone;
    }

    public House(int houseID, int numberStone) {
        this.houseID = houseID;
        this.numberStone = numberStone;
    }

    public void drawHouse(Graphics2D graphics2D) {
    }

    public void increaseStone() {
        this.numberStone++;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getNumberStone() {
        return numberStone;
    }

    public void setNumberStone(int numberStone) {
        this.numberStone = numberStone;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
