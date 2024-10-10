package put.ec.solution.solvers;

import put.ec.problem.TravellingSalesmanProblem;

public class SolverFactory {

    public Solver createSolver(String name, TravellingSalesmanProblem problem){
        if(name.equals("random")){
            return new RandomSolver(problem);
        } else if (name.equals("nn")) {
            return new NearestNeighbourSolver(problem);
        } else if (name.equals("nnAnywhere")) {
            return new NearestNeighbourAtAnySolver(problem);
        } else if (name.equals("greedyCycle")) {
            return new GreedyCycleSolver(problem);
        }
        System.out.println("Solver name not found");
        return null;
    }
}
