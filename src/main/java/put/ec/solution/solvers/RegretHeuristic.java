package put.ec.solution.solvers;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegretHeuristic extends WeightedRegretHeuristic{

    public RegretHeuristic(TravellingSalesmanProblem tsp){
        super(tsp,0.0,1.0);
        setName("regretHeuristic");
    }
}
