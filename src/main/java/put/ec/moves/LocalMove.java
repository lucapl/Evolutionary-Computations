package put.ec.moves;

import put.ec.problem.City;

abstract public class LocalMove {
    private int index1;
    private int index2;
    private City city1;
    private City city2;
    private double moveCost;
    private MoveState moveState;

    public LocalMove(int index1,int index2, City city1, City city2){
        this.setIndex1(index1);
        this.setIndex2(index2);
        this.setCity1(city1);
        this.setCity2(city2);
        this.setMoveState(MoveState.Unknown);
        this.setMoveCost(Double.POSITIVE_INFINITY);
    }

    public boolean isEvaluated(){
        return getMoveCost() == Double.POSITIVE_INFINITY;
    }

    public int getIndex1() {
        return index1;
    }

    public void setIndex1(int index1) {
        this.index1 = index1;
    }

    public int getIndex2() {
        return index2;
    }

    public void setIndex2(int index2) {
        this.index2 = index2;
    }

    public City getCity1() {
        return city1;
    }

    public void setCity1(City city1) {
        this.city1 = city1;
    }

    public City getCity2() {
        return city2;
    }

    public void setCity2(City city2) {
        this.city2 = city2;
    }

    public MoveState getMoveState() {
        return moveState;
    }

    public void setMoveState(MoveState moveState) {
        this.moveState = moveState;
    }

    public double getMoveCost() {
        return moveCost;
    }

    public void setMoveCost(double moveCost) {
        this.moveCost = moveCost;
    }
}
