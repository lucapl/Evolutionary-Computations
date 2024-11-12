package put.ec.solution.solvers.LocalSearch;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.*;
import java.util.function.Consumer;

public class Moveset implements Iterable<LocalMove>{
    protected List<LocalMove> moves;
    protected LocalMove previousMove = null;
    private final Solution solution;
    private final TravellingSalesmanProblem problem;
    private final IntraMovesType movesType;
    private final boolean shuffle;

    public Moveset(Solution solution, IntraMovesType movesType, boolean shuffle){
        this.solution = solution;
        this.problem = solution.getProblem();
        this.movesType = movesType;
        this.shuffle = shuffle;
    }

    public List<LocalMove> getMoves(){
        // this is probably the bottleneck
        int n = problem.getNumberOfCities();
        moves = new ArrayList<>(problem.getNumberOfCities()*problem.getSolutionLength()); // max capacity

        for(int i = 0; i<n; i++){
            City city_i = problem.getCity(i);
            for(int j = i+1; j<n; j++){
                City city_j = problem.getCity(j);

                if(solution.isIn(city_i) && solution.isIn(city_j)){
                    moves.add(new IntraMove(city_i, city_j, solution, this.movesType));
                    continue;
                }
                if(solution.isIn(city_i)){
                    moves.add(new InterMove(city_i, city_j, solution));
                    continue;
                }
                if(solution.isIn(city_j)){
                    moves.add(new InterMove(city_j, city_i, solution));
                }
            }
        }

        return moves;
    }

    public void setPerformedMove(LocalMove move){
        previousMove = move;
    }

    @Override
    public Iterator<LocalMove> iterator() {
        getMoves();
        if(shuffle){
            Collections.shuffle(moves); //this is Fisher-Yates shuffle
        }
        return moves.iterator();
    }
}
