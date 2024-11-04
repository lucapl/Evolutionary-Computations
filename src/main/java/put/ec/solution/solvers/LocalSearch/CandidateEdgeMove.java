package put.ec.solution.solvers.LocalSearch;

public class CandidateEdgeMove extends LocalMove{
    private final double moveValue;
    private final IntraMovesType type;

    public CandidateEdgeMove(int city1Index, int city2Index, double moveValue, IntraMovesType type){
        super(city1Index, city2Index);
        this.type = type;
        this.moveValue = moveValue;
    }

    public double getMoveValue() {
        return moveValue;
    }

    public IntraMovesType getType() {
        return type;
    }
}
