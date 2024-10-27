package put.ec.solution.solvers.Heuristics;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Solver;

import java.util.List;

abstract public class HeuristicSolver extends Solver {
    public HeuristicSolver(TravellingSalesmanProblem problem) {
        super(problem);
    }
    abstract public double getCostAtPosition(Solution solution, City newCity, int index);
}
