package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.RandomSolver;

public class Main {
    public static void main(String[] args) {
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem tspA = il.load("instances/TSPA.csv");
        TravellingSalesmanProblem tspB = il.load("instances/TSPB.csv");

        RandomSolver instanceASolver = new RandomSolver(tspA);
        instanceASolver.solve();
    }
}