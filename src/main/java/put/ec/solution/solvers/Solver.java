package put.ec.solution.solvers;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

public abstract class Solver {
    private final TravellingSalesmanProblem problem;
    private String name;
    public final static String separator = "-";

    public static String createName(String... strings){
        return String.join(separator,strings);
    }

    public int iterations = -1;

    public Solver(TravellingSalesmanProblem problem){
        this.problem = problem;
    }

    abstract public Solution solve();
    abstract public Solution solve(int startingCityIndex);

    public TravellingSalesmanProblem getProblem(){
        return problem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
