package put.ec.solution.solvers.LocalSearch;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.Solver;
import put.ec.solution.solvers.SolverFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalSearch extends Solver {
    private HeuristicSolver inititialSolver;
    private LocalSearchType type;
    private IntraMovesType movesType;

    public LocalSearch(TravellingSalesmanProblem problem, String heuristicName, LocalSearchType type, IntraMovesType moveType){
        super(problem);
        this.type = type;
        this.movesType = moveType;
        SolverFactory solverFactory = new SolverFactory();
        this.inititialSolver = solverFactory.createHeuristicSolver(heuristicName,problem);
    }

    @Override
    public Solution solve() {
        return null;
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution solution = inititialSolver.solve(startingCityIndex);
        solution.calculateCityLocations();
        boolean improvement;
        do{
            improvement = false;
            double bestCost = 0;
            LocalMove bestMove = null;
            List<LocalMove> moves = getMoves(solution); // get all possible moves

            if(type == LocalSearchType.GREEDY){ // shuffle them if greedy
                Collections.shuffle(moves); //this is Fisher-Yates shuffle
            }

            for(LocalMove move: moves){
                double moveCost = calculateMoveCost(solution,move);

                if (moveCost >= 0){ //ignore non improving cost
                    continue;
                }
                improvement = true;
                if(type==LocalSearchType.GREEDY){ //finish if greedy
                    bestMove = move;
                    break;
                }
                if(moveCost < bestCost){ //steepest
                    bestCost = moveCost;
                    bestMove = move;
                }
            }
            solution.performMove(bestMove);
        }while(improvement);

        return solution;
    }

    public double calculateMoveCost(Solution solution, LocalMove move){
        return 0.0;
    }

    @Override
    public double getCostAtPosition(Solution solution, City newCity, int index) {
        return 0;
    }

    public List<LocalMove> getMoves(Solution solution){
        int n = getProblem().getNumberOfCities();
        List<LocalMove> moves = new ArrayList<>(n*n/2);

        for(int i = 0; i<n; i++){
            City city_i = getProblem().getCity(i);
            for(int j = i; j<n; j++){
                City city_j = getProblem().getCity(j);

                if(solution.isIn(city_i) && solution.isIn(city_j)){
                    moves.add(new IntraMove(
                            solution.getCityIndexInOrder(city_i),
                            solution.getCityIndexInOrder(city_j),
                            this.movesType));
                    continue;
                }
                if(solution.isIn(city_i)){
                    moves.add(new InterMove(
                            solution.getCityIndexInOrder(city_i),
                            city_j.getIndex()));
                    continue;
                }
                if(solution.isIn(city_j)){
                    moves.add(new InterMove(
                            solution.getCityIndexInOrder(city_j),
                            city_i.getIndex()));
                    continue;
                }

                moves.add(new InterMove(city_i.getIndex(),city_j.getIndex()));
            }
        }

        return moves;
    }
}
