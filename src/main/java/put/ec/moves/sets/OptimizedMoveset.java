package put.ec.moves.sets;

import put.ec.moves.*;
import put.ec.solution.EdgeState;
import put.ec.solution.Solution;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class OptimizedMoveset extends Moveset {
    private final PriorityQueue<LocalMove> improvingMoves;

    public OptimizedMoveset(Solution solution, IntraMovesType movesType){
        super(solution,movesType,false);

        improvingMoves = new PriorityQueue<>(new MoveComparator());
    }
    @Override
    public void giveMoveEvaluation(LocalMove move, double evaluation){
        if(evaluation < 0){
            improvingMoves.add(move);
        }
    }

    @Override
    public List<LocalMove> getMoves() {

        while(!improvingMoves.isEmpty()){
            //improvingMoves.
        }

        if(improvingMoves.isEmpty()){
            return super.getMoves();
        }

        return improvingMoves.stream().toList();
    }

    private List<LocalMove> getNewMoves(){
        return null; //TODO
    }

    public MoveState determineMoveState(LocalMove move){
        if(move instanceof InterMove interMove){
            return determineInterMoveState(interMove);
        }
        return determineIntraMoveState((IntraMove) move);
    }

    private MoveState determineInterMoveState(InterMove move){
        if(getSolution().isIn(move.getCityInside()) && !getSolution().isIn(move.getCityOutside())){
            return MoveState.CurrentlyNotApplicable;
        }
        return MoveState.NotApplicable;
    }

    private MoveState determineIntraMoveState(IntraMove move){
        if(move instanceof EdgeSwapMove edgeSwapMove){
            return determineEdgeSwapMoveState(edgeSwapMove);
        }
        throw new IllegalArgumentException("IntraMove handling not implemented further");
    }

    private  MoveState determineEdgeSwapMoveState(EdgeSwapMove move){
        EdgeState edge1State = getSolution().isEdgeIn(move.getEdge1());
        EdgeState edge2State = getSolution().isEdgeIn(move.getEdge2());

        if(edge1State == EdgeState.NotFound || edge2State == EdgeState.NotFound){
            return MoveState.NotApplicable;
        }

        if((edge1State == EdgeState.CorrectDirection && edge2State == EdgeState.CorrectDirection) ||
            (edge1State == EdgeState.InvertedDirection && edge2State == EdgeState.InvertedDirection)){
            // both correct direction or both inverted
            return MoveState.Applicable;
        }

        return MoveState.CurrentlyNotApplicable;
    }
}

class MoveComparator implements Comparator<LocalMove>{
    @Override
    public int compare(LocalMove o1, LocalMove o2) {
        return Double.compare(o1.getMoveCost(),o2.getMoveCost());
    }
}