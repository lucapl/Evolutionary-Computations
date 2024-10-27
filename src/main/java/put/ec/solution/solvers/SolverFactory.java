package put.ec.solution.solvers;

import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Heuristics.*;
import put.ec.solution.solvers.LocalSearch.IntraMovesType;
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
        if(name.equals("localSearchSteepestNodesHeuristic")){//not the most elegant, but does not affect anything
            return new LocalSearch(problem,"greedyCycle", LocalSearchType.STEEPEST, IntraMovesType.NODES);
        } else if (name.equals("localSearchGreedyNodesHeuristic")){
            return new LocalSearch(problem,"greedyCycle", LocalSearchType.GREEDY, IntraMovesType.NODES);
        } else if (name.equals("localSearchSteepestEdgesHeuristic")){
            return new LocalSearch(problem,"greedyCycle", LocalSearchType.STEEPEST, IntraMovesType.EDGES);
        } else if (name.equals("localSearchGreedyEdgesHeuristic")){
            return new LocalSearch(problem,"greedyCycle", LocalSearchType.GREEDY, IntraMovesType.EDGES);
        } else if (name.equals("localSearchSteepestNodesRandom")) {
            return new LocalSearch(problem, "random", LocalSearchType.STEEPEST, IntraMovesType.NODES);
        } else if (name.equals("localSearchGreedyNodesRandom")){
            return new LocalSearch(problem,"random", LocalSearchType.GREEDY, IntraMovesType.NODES);
        } else if (name.equals("localSearchSteepestEdgesRandom")){
            return new LocalSearch(problem,"random", LocalSearchType.STEEPEST, IntraMovesType.EDGES);
        } else if (name.equals("localSearchGreedyEdgesRandom")){
            return new LocalSearch(problem,"random", LocalSearchType.GREEDY, IntraMovesType.EDGES);
        }

        // TODO: make the upper code nice

        return createHeuristicSolver(name,problem);
    }
}
