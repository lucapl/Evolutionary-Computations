package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;

public class Assignment1 extends Experiment{
    public static void main(String[] args) {
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv","A"),il.load("instances/TSPB.csv","B")};
        String[] solvers = {"random","nn","nnAnywhere","greedyCycle"};
        String outFolder = "./out1/";

        runExperiment(instances,solvers,outFolder);
    }
}
