package GameStep;

/**
 * Describe the Step of current player
 */
public class Step {
    public int choose_Id;
    public int direction;
    public int count_eat_number;
    private int winner;

    public Step() {
    }

    public Step(int choose_Id, int direction) {
        this.choose_Id = choose_Id;
        this.direction = direction;
    }

    boolean compareStep(Step step1, Step step2) {
        return step1.choose_Id == step2.choose_Id && step1.direction == step2.direction;
    }

    public int getChoose_Id() {
        return choose_Id;
    }

    public void setChoose_Id(int choose_Id) {
        this.choose_Id = choose_Id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getCount_eat_number() {
        return count_eat_number;
    }

    public void setCount_eat_number(int count_eat_number) {
        this.count_eat_number = count_eat_number;
    }
}
