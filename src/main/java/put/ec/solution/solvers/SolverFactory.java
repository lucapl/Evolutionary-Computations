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

    private SolverData separateLocalSearchName(String name){
        String bestHeuristic = "weightedRegretHeuristic";

        String[] names = name.split(Solver.separator);

        return new SolverData(names[0],LocalSearchType.valueOf(names[1]),IntraMovesType.valueOf(names[2]),names[3].equals("Heuristic")?bestHeuristic:"random");
    }

    public Solver createSolver(String name, TravellingSalesmanProblem problem){
        int numberOfCandidateMoves = 10;
        SolverData solverData = separateLocalSearchName(name);

        return switch(solverData.name){
            case "localSearch" -> new LocalSearch(problem, solverData.initialHeuristic, solverData.searchType, solverData.movesType);
            case "candidateSearch" -> new CandidateSearch(problem, solverData.initialHeuristic, solverData.movesType, numberOfCandidateMoves);
            default -> createHeuristicSolver(name, problem);
        };

//        return switch (name) {
//            case "localSearchSteepestNodesHeuristic" -> //not the most elegant, but does not affect anything
//                    new LocalSearch(problem, bestHeuristic, LocalSearchType.Steepest, IntraMovesType.Nodes);
//            case "localSearchGreedyNodesHeuristic" ->
//                    new LocalSearch(problem, bestHeuristic, LocalSearchType.Greedy, IntraMovesType.Nodes);
//            case "localSearchSteepestEdgesHeuristic" ->
//                    new LocalSearch(problem, bestHeuristic, LocalSearchType.Steepest, IntraMovesType.Edges);
//            case "localSearchGreedyEdgesHeuristic" ->
//                    new LocalSearch(problem, bestHeuristic, LocalSearchType.Greedy, IntraMovesType.Edges);
//            case "localSearchSteepestNodesRandom" ->
//                    new LocalSearch(problem, "random", LocalSearchType.Steepest, IntraMovesType.Nodes);
//            case "localSearchGreedyNodesRandom" ->
//                    new LocalSearch(problem, "random", LocalSearchType.Greedy, IntraMovesType.Nodes);
//            case "localSearchSteepestEdgesRandom" ->
//                    new LocalSearch(problem, "random", LocalSearchType.Steepest, IntraMovesType.Edges);
//            case "localSearchGreedyEdgesRandom" ->
//                    new LocalSearch(problem, "random", LocalSearchType.Greedy, IntraMovesType.Edges);
//            case "candidateSearchNodesRandom" ->
//                    new CandidateSearch(problem, "random", IntraMovesType.Nodes, numberOfCandidateMoves);
//            case "candidateSearchEdgesRandom" ->
//                    new CandidateSearch(problem, "random", IntraMovesType.Edges, numberOfCandidateMoves);
//            default ->
//
//                // TODO: make the upper code nice
//
//                    createHeuristicSolver(name, problem);
//        };

    }
}

class SolverData{
    public String name;
    public LocalSearchType searchType;
    public IntraMovesType movesType;
    public String initialHeuristic;

    public SolverData(String name, LocalSearchType searchType, IntraMovesType movesType, String initialHeuristic){
        this.name = name;
        this.searchType = searchType;
        this.movesType = movesType;
        this.initialHeuristic = initialHeuristic;
    }
}