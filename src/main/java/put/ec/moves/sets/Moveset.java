package put.ec.moves.sets;

import put.ec.moves.*;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.*;

public class Moveset implements Iterable<LocalMove>{
    protected List<LocalMove> moves;
    protected LocalMove previousMove = null;
    private Solution solution;
    protected TravellingSalesmanProblem problem;
    private final IntraMovesType movesType;
    private final boolean shuffle;

    public Moveset(Solution solution, IntraMovesType movesType, boolean shuffle){
        this.solution = solution;
        if(solution != null){
            this.problem = solution.getProblem();
        }
        this.movesType = movesType;
        this.shuffle = shuffle;
        this.moves = new ArrayList<>();
    }

    public Moveset(IntraMovesType movesType, boolean shuffle){
        this(null,movesType,shuffle);
    }

    public List<LocalMove> getMoves(){
        // this is probably the bottleneck
        int n = problem.getNumberOfCities();
        moves = new ArrayList<>(problem.getNumberOfCities()*problem.getSolutionLength()); // max capacity

        for(int i = 0; i<n; i++){
            City city_i = problem.getCity(i);
            for(int j = i+1; j<n; j++){
                City city_j = problem.getCity(j);
                LocalMove move = createMove(city_i,city_j);
                if(move == null){
                    continue;
                }
                moves.add(move);
            }
        }

        return moves;
    }

    public LocalMove createMove(City city_i, City city_j){
        if(getSolution().isIn(city_i) && getSolution().isIn(city_j)){
            return createIntraMove(city_i,city_j, getSolution());
        }
        if(getSolution().isIn(city_i)){
            return new InterMove(city_i, city_j, getSolution());
        }
        if(getSolution().isIn(city_j)){
            return new InterMove(city_j, city_i, getSolution());
        }
        return null;
    }

    private IntraMove createIntraMove(City city1, City city2, Solution solution){
        return switch (movesType){
            case Edges -> new EdgeSwapMove(city1,city2,solution);
            case Nodes -> new IntraMove(city1,city2,solution,movesType);
        };
    }

    public void setPerformedMove(LocalMove move){
        previousMove = move;
    }
    public void giveMoveEvaluation(LocalMove move, double evaluation){
        return;
    }

    @Override
    public Iterator<LocalMove> iterator() {
        getMoves();
        if(shuffle){
            Collections.shuffle(moves); //this is Fisher-Yates shuffle
        }
        return moves.iterator();
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution){
        this.solution = solution;
        this.problem = solution.getProblem();
    }

    public void clear(){
        moves.clear();
        solution = null;
        problem = null;
        previousMove = null;
    }
}
