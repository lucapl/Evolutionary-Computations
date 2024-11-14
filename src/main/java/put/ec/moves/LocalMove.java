package put.ec.moves;

import put.ec.moves.sets.Moveset;
import put.ec.problem.City;

abstract public class LocalMove {
    private int index1;
    private int index2;
    private double moveCost;
    private MoveState moveState;

    public LocalMove(int index1,int index2){
        this.setIndex1(index1);
        this.setIndex2(index2);
        this.setMoveState(MoveState.Unknown);
        this.setMoveCost(Double.POSITIVE_INFINITY);
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

    public boolean isEvaluated(){
        return !Double.isInfinite(getMoveCost());
    }
}
