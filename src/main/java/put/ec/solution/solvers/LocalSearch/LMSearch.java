package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.*;
import put.ec.moves.sets.Moveset;
import put.ec.moves.sets.OptimizedMoveset;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.EdgeState;
import put.ec.solution.Solution;

import java.util.*;

public class LMSearch extends LocalSearch {
    private static final String defaultStart = "random";
    private Solution solution;

    public LMSearch(TravellingSalesmanProblem problem){
        super(problem,defaultStart, LocalSearchType.Steepest,IntraMovesType.Edges, new OptimizedMoveset());
        setName(createName("lmSearch",defaultStart));
    }

    public Solution solve(Solution solution){
        solution.calculateInSolutions();
        solution.calculateCityLocations();
        setSolution(solution);
        PriorityQueue<LocalMove> lmMoves = new PriorityQueue<>(new MoveComparator());
        Queue<LocalMove> newMoves = initializeMoves(solution);
        Queue<LocalMove> checkedLm = new LinkedList<>();

        boolean moveFound;

        do{
            moveFound = false;
            evaluateNewMoves(solution,newMoves,lmMoves);
            while(!lmMoves.isEmpty() && !moveFound){
                LocalMove move = lmMoves.remove();

                MoveState moveState = determineMoveState(move);
                move.setMoveState(moveState);
                switch (moveState){
                    case Applicable:
                        getSolution().performMove(move);
                        addNewMoves(move,newMoves);
                        moveFound = true;
                        break;
                    case NotApplicable:
                        break;
                    case CurrentlyNotApplicable:
                        checkedLm.add(move);
                        break;
                }
            }
            lmMoves.addAll(checkedLm);
            checkedLm.clear();
        }while(moveFound);

        return solution;
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution solution = initialSolve(startingCityIndex);
        return solve(solution);
    }

    /**
     * This method should be invoked after performing the move on the solution
     * @param move to be performed
     * @param newMoves queue where new moves are added
     */
    public void addNewMoves(LocalMove move, Queue<LocalMove> newMoves){
        Moveset moveset = new Moveset(getSolution(),getMovesType(),false);
        City[] cities = null;
        if(move instanceof EdgeSwapMove edgeSwapMove){
            City[] edge1 = edgeSwapMove.getEdge1();
            City[] edge2 = edgeSwapMove.getEdge2();
            cities = new City[]{edge1[0],edge1[1],edge2[0],edge2[1]};
        }else if(move instanceof InterMove interMove){
            cities = new City[]{interMove.getCityInside(), interMove.getCityOutside(), interMove.getNext(), interMove.getPrevious()};
        }
        for(int i = 0; i<getProblem().getNumberOfCities(); i++) {
            City city_i = getProblem().getCity(i);

            for(City city_j: cities){
                if(city_i.equals(city_j)){
                    continue;
                }
                LocalMove newMove = moveset.createMove(city_i,city_j);
                if(newMove != null){
                    newMoves.add(newMove);
                }
            }
        }
    }

    public void evaluateNewMoves(Solution solution, Queue<LocalMove> newMoves, PriorityQueue<LocalMove> lmMoves){
        while(!newMoves.isEmpty()){
            LocalMove move = newMoves.remove();

            double moveCost = calculateMoveCost(solution,move);
            move.setMoveCost(moveCost);

            if (moveCost < 0){
                lmMoves.add(move);
            }
        }
    }

    public Queue<LocalMove> initializeMoves(Solution solution){
        int n = getProblem().getNumberOfCities();
        Queue<LocalMove> moves = new LinkedList<>();
        Moveset moveset = new Moveset(solution,getMovesType(),false);

        for(int i = 0; i<n; i++){
            City city_i = getProblem().getCity(i);
            for(int j = i+1; j<n; j++){
                City city_j = getProblem().getCity(j);
                LocalMove move = moveset.createMove(city_i,city_j);
                if(move == null){
                    continue;
                }
                moves.add(move);
            }
        }

        return moves;
    }

    public MoveState determineMoveState(LocalMove move){
        if(move instanceof InterMove interMove){
            return determineInterMoveState(interMove);
        }
        return determineIntraMoveState((IntraMove) move);
    }

    private MoveState determineInterMoveState(InterMove move){
        if(!getSolution().isIn(move.getCityInside()) || getSolution().isIn(move.getCityOutside())){
            return MoveState.NotApplicable;
        }

        City cityInside = move.getCityInside();
        if(getSolution().getNext(cityInside)==move.getNext() && getSolution().getPrevious(cityInside) == move.getPrevious()){
            return MoveState.Applicable;
        }

        if(getSolution().getNext(cityInside)==move.getPrevious() && getSolution().getPrevious(cityInside) == move.getNext()){
            return MoveState.Applicable;
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

        if((edge1State == EdgeState.CorrectDirection && edge2State == EdgeState.CorrectDirection)){
            // both correct direction or both inverted
            return MoveState.Applicable;
        }

        if (edge1State == EdgeState.InvertedDirection && edge2State == EdgeState.InvertedDirection){
            move.invertEdge1();
            move.invertEdge2();
            return MoveState.Applicable;
        }

        return MoveState.CurrentlyNotApplicable;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}

class MoveComparator implements Comparator<LocalMove> {
    @Override
    public int compare(LocalMove o1, LocalMove o2) {
        return Double.compare(o1.getMoveCost(),o2.getMoveCost());
    }
}