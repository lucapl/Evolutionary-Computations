package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;

public class Assignment7 extends Assignment6{
    public static void main(String[] args){
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv","A"),il.load("instances/TSPB.csv","B")};
        String[] solvers = {"lns","lns_withLs"};
        String outFolder = "./out7/";

        runCustomExperiment(instances,solvers,outFolder, 20);
    }
}
