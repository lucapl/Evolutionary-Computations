package put.ec.moves.sets;

import put.ec.moves.*;
import put.ec.problem.City;
import put.ec.solution.EdgeState;
import put.ec.solution.Solution;

import java.util.*;

public class OptimizedMoveset extends Moveset {
    private final PriorityQueue<LocalMove> improvingMoves;
    private final Set<LocalMove> improvingMovesTracker;
    private final boolean validate = false;

    public OptimizedMoveset(Solution solution, IntraMovesType movesType){
        super(solution,movesType,false);

        if(validate){
            System.out.println("Validation is on for OptimizedMoveset, turn off for efficiency");
        }
        improvingMoves = new PriorityQueue<>();
        improvingMovesTracker = new HashSet<>();
    }

    public OptimizedMoveset(IntraMovesType movesType){
        this(null,movesType);
    }

    public OptimizedMoveset(){
        this(IntraMovesType.Edges);
    }


    @Override
    public void giveMoveEvaluation(LocalMove move, double evaluation){
        if(evaluation < 0){
            add(move);
        }
    }

    @Override
    public List<LocalMove> getMoves() {
//
//        List<LocalMove> improvingMovesCopy = new LinkedList<>(improvingMoves);
//        double prevObj = Double.POSITIVE_INFINITY;
//        for(LocalMove localMove: improvingMovesCopy){
//            //MoveState moveState = determineMoveState(localMove);
//            localMove.setMoveState(moveState);
//            switch (moveState){
//                case Applicable:
//                    if(validate){
//                        prevObj = getSolution().getObjectiveFunctionValue();
//                    }
//                    getSolution().performMove(localMove);
//                    if(validate){
//                        double curobj = getSolution().getObjectiveFunctionValue();
//
//                        if (curobj-prevObj != localMove.getMoveCost()){
//                            System.err.println("Change in cost not equal to expected! \nDifference: "+((curobj-prevObj)-localMove.getMoveCost()) +"\n New cost: "+curobj);
//                        }
//                    }
//                    remove(localMove);
//                    break;
//                case NotApplicable:
//                    remove(localMove);
//                    break;
//            }
//        }
//
//        return getNewMoves();
        return null;
    }

    private List<LocalMove> getNewMoves(){
        moves = new LinkedList<>();

        for(int i = 0; i<problem.getNumberOfCities(); i++){
            City city_i = problem.getCity(i);
            for(int j = i+1; j<problem.getNumberOfCities(); j++){
                City city_j = problem.getCity(j);
                LocalMove move = createMove(city_i,city_j);
                if(move==null || isInImprovingMoves(move)){
                    continue;
                }
                if(move instanceof EdgeSwapMove edgeSwapMove){
                    EdgeSwapMove moveCopy = new EdgeSwapMove(edgeSwapMove,getSolution());
                    moveCopy.invertEdge2();
                    if(isInImprovingMoves(edgeSwapMove)){continue;}
                    moveCopy.invertEdge1();
                    if(isInImprovingMoves(edgeSwapMove)){continue;}
                    moveCopy.invertEdge2();
                    if(isInImprovingMoves(edgeSwapMove)){continue;}
                }
                moves.add(move);
            }
        }
        return moves;
    }

    public void add(LocalMove move){
        improvingMoves.add(move);
        improvingMovesTracker.add(move);
    }

    public void remove(LocalMove move){
        improvingMoves.remove(move);
        improvingMovesTracker.remove(move);
    }

    public boolean isInImprovingMoves(LocalMove move){
        return improvingMovesTracker.contains(move);
    }



    @Override
    public void clear() {
        super.clear();
        improvingMoves.clear();
        improvingMovesTracker.clear();
    }
}

