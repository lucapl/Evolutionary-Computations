package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;

public class Assignment9 extends Assignment6{
    public static void main(String[] args){
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv","A"),il.load("instances/TSPB.csv","B")};
        String[] solvers = {"hybridEA"};
        String outFolder = "./out/";

        runCustomExperiment(instances,solvers,outFolder, 20);
    }
}
