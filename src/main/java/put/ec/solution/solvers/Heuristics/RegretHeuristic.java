package put.ec.solution.solvers.Heuristics;

import put.ec.problem.TravellingSalesmanProblem;

public class RegretHeuristic extends WeightedRegretHeuristic{

    public RegretHeuristic(TravellingSalesmanProblem tsp){
        super(tsp,0.0,1.0);
        setName("regretHeuristic");
    }
}
