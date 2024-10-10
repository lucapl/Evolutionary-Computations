package put.ec.solution;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;

import java.util.List;

public abstract class Solver {
    private TravellingSalesmanProblem problem;

    public Solver(TravellingSalesmanProblem problem){
        this.problem = problem;
    }

    abstract public Solution solve();
    abstract public Solution solve(int startingCityIndex);
    abstract public double getCostAtPosition(Solution solution, City newCity,int index);

    public TravellingSalesmanProblem getProblem(){
        return problem;
    }
}
