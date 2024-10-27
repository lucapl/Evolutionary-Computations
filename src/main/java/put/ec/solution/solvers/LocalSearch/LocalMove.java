package put.ec.solution.solvers.LocalSearch;

abstract public class LocalMove {
    private int index1;
    private int index2;

    public LocalMove(int index1,int index2){
        this.index1 = index1;
        this.index2 = index2;
    }
}
