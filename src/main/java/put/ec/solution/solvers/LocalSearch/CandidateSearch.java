package put.ec.solution.solvers.LocalSearch;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.SolverFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CandidateSearch extends LocalSearch{
    private final int numberOfCandidateMoves;
    private final List<CandidateEdgeMove> candidateMoves;

    public CandidateSearch(TravellingSalesmanProblem problem, String heuristicName, IntraMovesType moveType, int numberOfCandidateMoves) {
        super(problem, heuristicName, LocalSearchType.Steepest, moveType);
        this.numberOfCandidateMoves = numberOfCandidateMoves;
        SolverFactory solverFactory = new SolverFactory();

        candidateMoves = getCandidateMoves();

        setName(createName("candidateSearch",getType().name(),moveType.name(),simplifyHeuristicName(heuristicName)));
    }

    public List<CandidateEdgeMove> getCandidateMoves(){

        int n = getProblem().getNumberOfCities();
        List<CandidateEdgeMove> moves = new ArrayList<>(n*getProblem().getSolutionLength());


        for(int i = 0; i<n; i++){
            City city_i = getProblem().getCity(i);
            List<CandidateEdgeMove> potentialMoves = new ArrayList<>(getProblem().getNumberOfCities());
            for(int j = i+1; j<n; j++){
                City city_j = getProblem().getCity(j);

                double edgeValue = city_j.getCost() + getProblem().getCostBetween(city_i,city_j);

                potentialMoves.add(new CandidateEdgeMove(i,j,edgeValue,getMovesType()));
            }
            potentialMoves.sort(Comparator.comparingDouble(CandidateEdgeMove::getMoveValue));
            if (potentialMoves.size() > numberOfCandidateMoves) {
                potentialMoves = potentialMoves.subList(0, numberOfCandidateMoves);
            }
            moves.addAll(potentialMoves);

        }
        return moves;
    }

// TODO: transform the search into a moveset; add it as a moveset to local search

//    @Override
//    public List<LocalMove> getMoves(Solution solution) {
//        List<LocalMove> localMoves = new ArrayList<>(getProblem().getNumberOfCities()*getProblem().getSolutionLength());
//        for(CandidateEdgeMove candidateEdgeMove: candidateMoves){
//            solution.determineMove(candidateEdgeMove,localMoves);
//        }
//        return localMoves;
//    }
}
