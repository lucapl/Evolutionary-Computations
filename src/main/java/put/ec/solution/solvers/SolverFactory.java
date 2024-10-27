package put.ec.solution.solvers;

import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Heuristics.*;
import put.ec.solution.solvers.LocalSearch.IntraMovesType;
import put.ec.solution.solvers.LocalSearch.LocalSearch;
import put.ec.solution.solvers.LocalSearch.LocalSearchType;

public class SolverFactory {

    public HeuristicSolver createHeuristicSolver(String name, TravellingSalesmanProblem problem){
        switch (name) {
            case "random" -> {
                return new RandomSolver(problem);
            }
            case "nn" -> {
                return new NearestNeighbourSolver(problem);
            }
            case "nnAnywhere" -> {
                return new NearestNeighbourAtAnySolver(problem);
            }
            case "greedyCycle" -> {
                return new GreedyCycleSolver(problem);
            }
            case "regretHeuristic" -> {
                return new RegretHeuristic(problem);
            }
            case "weightedRegretHeuristic" -> {
                return new WeightedRegretHeuristic(problem, 0.5, 0.5);
            }
        }
        System.out.println("Solver name not found");
        return null;
    }

    public Solver createSolver(String name, TravellingSalesmanProblem problem){
        String bestHeuristic = "weightedRegretHeuristic";

        return switch (name) {
            case "localSearchSteepestNodesHeuristic" -> //not the most elegant, but does not affect anything
                    new LocalSearch(problem, bestHeuristic, LocalSearchType.STEEPEST, IntraMovesType.NODES);
            case "localSearchGreedyNodesHeuristic" ->
                    new LocalSearch(problem, bestHeuristic, LocalSearchType.GREEDY, IntraMovesType.NODES);
            case "localSearchSteepestEdgesHeuristic" ->
                    new LocalSearch(problem, bestHeuristic, LocalSearchType.STEEPEST, IntraMovesType.EDGES);
            case "localSearchGreedyEdgesHeuristic" ->
                    new LocalSearch(problem, bestHeuristic, LocalSearchType.GREEDY, IntraMovesType.EDGES);
            case "localSearchSteepestNodesRandom" ->
                    new LocalSearch(problem, "random", LocalSearchType.STEEPEST, IntraMovesType.NODES);
            case "localSearchGreedyNodesRandom" ->
                    new LocalSearch(problem, "random", LocalSearchType.GREEDY, IntraMovesType.NODES);
            case "localSearchSteepestEdgesRandom" ->
                    new LocalSearch(problem, "random", LocalSearchType.STEEPEST, IntraMovesType.EDGES);
            case "localSearchGreedyEdgesRandom" ->
                    new LocalSearch(problem, "random", LocalSearchType.GREEDY, IntraMovesType.EDGES);
            default ->

                // TODO: make the upper code nice

                    createHeuristicSolver(name, problem);
        };

    }
}
