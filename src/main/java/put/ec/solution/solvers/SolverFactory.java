package put.ec.solution.solvers;

import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Heuristics.*;
import put.ec.solution.solvers.LocalSearch.CandidateSearch;
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
        System.err.println("Solver name not found");
        return null;
    }

    public Solver createSolver(String name, TravellingSalesmanProblem problem){
        String bestHeuristic = "weightedRegretHeuristic";
        int numberOfCandidateMoves = 10;

        return switch (name) {
            case "localSearchSteepestNodesHeuristic" -> //not the most elegant, but does not affect anything
                    new LocalSearch(problem, bestHeuristic, LocalSearchType.Steepest, IntraMovesType.Nodes);
            case "localSearchGreedyNodesHeuristic" ->
                    new LocalSearch(problem, bestHeuristic, LocalSearchType.Greedy, IntraMovesType.Nodes);
            case "localSearchSteepestEdgesHeuristic" ->
                    new LocalSearch(problem, bestHeuristic, LocalSearchType.Steepest, IntraMovesType.Edges);
            case "localSearchGreedyEdgesHeuristic" ->
                    new LocalSearch(problem, bestHeuristic, LocalSearchType.Greedy, IntraMovesType.Edges);
            case "localSearchSteepestNodesRandom" ->
                    new LocalSearch(problem, "random", LocalSearchType.Steepest, IntraMovesType.Nodes);
            case "localSearchGreedyNodesRandom" ->
                    new LocalSearch(problem, "random", LocalSearchType.Greedy, IntraMovesType.Nodes);
            case "localSearchSteepestEdgesRandom" ->
                    new LocalSearch(problem, "random", LocalSearchType.Steepest, IntraMovesType.Edges);
            case "localSearchGreedyEdgesRandom" ->
                    new LocalSearch(problem, "random", LocalSearchType.Greedy, IntraMovesType.Edges);
            case "candidateSearchSteepestNodesRandom" ->
                    new CandidateSearch(problem, "random", LocalSearchType.Steepest, IntraMovesType.Nodes, numberOfCandidateMoves);
            case "candidateSearchGreedyNodesRandom" ->
                    new CandidateSearch(problem, "random", LocalSearchType.Greedy, IntraMovesType.Nodes, numberOfCandidateMoves);
            case "candidateSearchSteepestEdgesRandom" ->
                    new CandidateSearch(problem, "random", LocalSearchType.Steepest, IntraMovesType.Edges, numberOfCandidateMoves);
            case "candidateSearchGreedyEdgesRandom" ->
                    new CandidateSearch(problem, "random", LocalSearchType.Greedy, IntraMovesType.Edges, numberOfCandidateMoves);
            default ->

                // TODO: make the upper code nice

                    createHeuristicSolver(name, problem);
        };

    }
}
