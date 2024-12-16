package put.ec.solution.solvers;

import put.ec.moves.sets.Moveset;
import put.ec.moves.sets.OptimizedMoveset;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Evolutionary.HybridEA;
import put.ec.solution.solvers.Heuristics.*;
import put.ec.solution.solvers.LocalSearch.*;
import put.ec.moves.IntraMovesType;

public class SolverFactory {
    private final String bestHeuristic = "weightedRegretHeuristic";
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

    private SolverData getSolverType(String name){
        String[] names = name.split(Solver.separator);
        SolverData solverData = new SolverData();
        solverData.name = names[0];
        return solverData;
    }

    private SolverData separateLocalSearchName(String name){
        String[] names = name.split(Solver.separator);

        return new SolverData(names[0],LocalSearchType.valueOf(names[1]),IntraMovesType.valueOf(names[2]),names[3].equals("Heuristic")?bestHeuristic:"random");
    }

    private SolverData separateCandidateSearchName(String name){
        String[] names = name.split(Solver.separator);

        return new SolverData(names[0],LocalSearchType.Steepest,IntraMovesType.valueOf(names[1]),names[2].equals("Heuristic")?bestHeuristic:"random");
    }

    public Solver createSolver(String name, TravellingSalesmanProblem problem) {
        int numberOfCandidateMoves = 10;
        SolverData solverData = getSolverType(name);

        return switch (solverData.name) {
            case "localSearch" -> {
                solverData = separateLocalSearchName(name);
                yield new LocalSearch(problem, solverData.initialHeuristic, solverData.searchType, solverData.movesType);
            }
            case "candidateSearch" -> {
                solverData = separateCandidateSearchName(name);
                yield new CandidateSearch(problem, solverData.initialHeuristic, solverData.movesType, numberOfCandidateMoves);
            }
            case "msls" -> {
                yield new MultipleStartLocalSearch(problem, "random", new OptimizedMoveset(), 200);
            }
            case "ils" -> {
                yield new IteratedLocalSearch(problem, "random", new OptimizedMoveset(), 10000, 5);
            }
            case "lns" -> {
                yield new LargeNeighborhoodSearch(problem, "random", new OptimizedMoveset(), 1000,false);
            }
            case "lns_withLs" -> {
                yield new LargeNeighborhoodSearch(problem, "random", new OptimizedMoveset(), 1000,true);
            }
            case "lmSearch" -> new LMSearch(problem);
            case "hybridEA" -> new HybridEA(problem,20,true,1000);
            default -> createHeuristicSolver(name, problem);
        };
    }
}

// TODO make this the default way to create solvers instead of strings
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
    public SolverData(){
    }
}