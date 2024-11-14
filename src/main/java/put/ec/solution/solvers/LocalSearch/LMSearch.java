package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.IntraMovesType;
import put.ec.moves.LocalMove;
import put.ec.moves.sets.OptimizedMoveset;
import put.ec.problem.TravellingSalesmanProblem;

public class LMSearch extends LocalSearch {
    private static final String defaultStart = "random";

    public LMSearch(TravellingSalesmanProblem problem){
        super(problem,defaultStart, LocalSearchType.Steepest,IntraMovesType.Edges, new OptimizedMoveset());
        setName(createName("lmSearch",defaultStart));
    }
}
