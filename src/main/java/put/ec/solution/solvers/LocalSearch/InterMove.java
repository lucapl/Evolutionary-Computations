package put.ec.solution.solvers.LocalSearch;

public class InterMove extends LocalMove{
    public InterMove(int inSolutionIndex,int globalIndex){
        super(inSolutionIndex,globalIndex);
    }

    public int getInsideCityIndex() {
        return getIndex1();
    }

    public int getOutsideCityIndex() {
        return getIndex2();
    }
}
