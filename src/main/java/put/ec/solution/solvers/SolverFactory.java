package put.ec.solution.solvers;

import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Heuristics.*;
import put.ec.solution.solvers.LocalSearch.LocalSearch;
import put.ec.solution.solvers.LocalSearch.LocalSearchType;

public class SolverFactory {

    public HeuristicSolver createHeuristicSolver(String name, TravellingSalesmanProblem problem){
        if(name.equals("random")){
            return new RandomSolver(problem);
        } else if (name.equals("nn")) {
            return new NearestNeighbourSolver(problem);
        } else if (name.equals("nnAnywhere")) {
            return new NearestNeighbourAtAnySolver(problem);
        } else if (name.equals("greedyCycle")) {
            return new GreedyCycleSolver(problem);
        } else if (name.equals("regretHeuristic")) {
            return new RegretHeuristic(problem);
        } else if (name.equals("weightedRegretHeuristic")) {
            return new WeightedRegretHeuristic(problem,0.5,0.5);
        }
        System.out.println("Solver name not found");
        return null;
    }

    public Solver createSolver(String name, TravellingSalesmanProblem problem){
        if(name.equals("localSearchSteepest")){
            return new LocalSearch(problem,"greedyCycle", LocalSearchType.STEEPEST);
        } else if (name.equals("localSearchGreedy")){
            return new LocalSearch(problem,"greedyCycle", LocalSearchType.GREEDY);
        }
        return createHeuristicSolver(name,problem);
    }
}
