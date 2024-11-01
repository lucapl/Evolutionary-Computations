package put.ec.solution.solvers.LocalSearch;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CandidateSearch extends LocalSearch{

    private final IntraMovesType movesType;
    private final int numberOfCandidateMoves;

    public CandidateSearch(TravellingSalesmanProblem problem, String heuristicName, LocalSearchType type, IntraMovesType moveType, int numberOfCandidateMoves) {
        super(problem, heuristicName, type, moveType);
        this.movesType = moveType;
        this.numberOfCandidateMoves = numberOfCandidateMoves;
    }

    @Override
    public List<LocalMove> getMoves(Solution solution){

        int n = getProblem().getNumberOfCities();
        List<LocalMove> moves = new ArrayList<>(n*getProblem().getSolutionLength());


        for(int i = 0; i<n; i++){
            City city_i = getProblem().getCity(i);
            List<LocalMove> potentialMoves = new ArrayList<>(getProblem().getSolutionLength());
            for(int j = i+1; j<n; j++){
                City city_j = getProblem().getCity(j);

                if(solution.isIn(city_i) && solution.isIn(city_j)){
                    potentialMoves.add(new IntraMove(
                            solution.getCityIndexInOrder(city_i),
                            solution.getCityIndexInOrder(city_j),
                            this.movesType));
                    continue;
                }
                if(solution.isIn(city_i)){
                    potentialMoves.add(new InterMove(
                            solution.getCityIndexInOrder(city_i),
                            city_j.getIndex()));
                }
                if(solution.isIn(city_j)){
                    potentialMoves.add(new InterMove(
                            solution.getCityIndexInOrder(city_j),
                            city_i.getIndex()));
                }

            }
            potentialMoves.sort(Comparator.comparingDouble((LocalMove move) -> calculateMoveCost(solution, move)));
            if (potentialMoves.size() > numberOfCandidateMoves) {
                potentialMoves = potentialMoves.subList(0, numberOfCandidateMoves);
            }
            moves.addAll(potentialMoves);

        }
        return moves;
    }
}