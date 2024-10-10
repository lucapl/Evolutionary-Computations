package put.ec.solution;

import put.ec.problem.TravellingSalesmanProblem;

public abstract class Solver {
    private TravellingSalesmanProblem problem;

    public Solver(TravellingSalesmanProblem problem){
        this.problem = problem;
    }

    abstract public Solution solve();

    public TravellingSalesmanProblem getProblem(){
        return problem;
    }
}
