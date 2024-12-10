package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Solver;

public class Assignment8 extends Assignment6{
    public static void main(String[] args){
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv","A"),il.load("instances/TSPB.csv","B")};
        String[] solvers = {Solver.createName("localSearch","Greedy","Edges","Random")};
        String outFolder = "./out8/";

        runCustomExperiment(instances,solvers,outFolder, 1000);
    }
}
