package put.ec.solution.solvers.Heuristics;

import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Solver;

import java.util.List;

abstract public class HeuristicSolver extends Solver {
    public HeuristicSolver(TravellingSalesmanProblem problem) {
        super(problem);
    }
}
