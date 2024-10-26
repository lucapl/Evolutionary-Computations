package put.ec.solution.solvers.LocalSearch;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.Solver;
import put.ec.solution.solvers.SolverFactory;

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
        Solution initialSolution = inititialSolver.solve(startingCityIndex);


        return initialSolution;
    }

    @Override
    public double getCostAtPosition(Solution solution, City newCity, int index) {
        return 0;
    }

    public List<LocalMove> getMoves(Solution solution){

    }
}
