package put.ec.solution.solvers.LocalSearch;

public class IntraMove extends LocalMove{
    private IntraMovesType type;
    public IntraMove(int inSolutionIndex1,int inSolutionIndex2,IntraMovesType type){
        super(inSolutionIndex1,inSolutionIndex2);
        this.type = type;
    }
}
