package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.GreedyCycleSolver;
import put.ec.solution.NearestNeighbourAtAnySolver;
import put.ec.solution.NearestNeighbourSolver;
import put.ec.solution.Solver;

public class Main {
    public static void main(String[] args) {
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem tspA = il.load("instances/TSPA.csv");
        TravellingSalesmanProblem tspB = il.load("instances/TSPB.csv");

        int index = 10;

        Solver instanceASolver = new NearestNeighbourSolver(tspA);
        System.out.println(instanceASolver.solve(index));
        instanceASolver = new NearestNeighbourAtAnySolver(tspA);
        System.out.println(instanceASolver.solve(index));
        instanceASolver = new GreedyCycleSolver(tspA);
        System.out.println(instanceASolver.solve(index));
    }
}