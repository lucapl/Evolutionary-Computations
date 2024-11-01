package put.ec.solution.solvers.LocalSearch;

import put.ec.problem.TravellingSalesmanProblem;

public class CandidateSearch extends LocalSearch{

    public CandidateSearch(TravellingSalesmanProblem problem, String heuristicName, LocalSearchType type, IntraMovesType moveType) {
        super(problem, heuristicName, type, moveType);
    }
}
