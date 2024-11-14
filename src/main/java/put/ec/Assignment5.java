package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;

public class Assignment5 extends Experiment{

    public static void main(String[] args){
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv","A"),il.load("instances/TSPB.csv","B")};
        String[] solvers = new String[]{ "lmSearch-Random"};

        String outFolder = "./out/";

        runExperiment(instances,solvers,outFolder);
    }
}
