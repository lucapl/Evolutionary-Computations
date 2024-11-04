package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;

public class Assignment4 extends Experiment {
    public static void main(String[] args) {
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv","A"),il.load("instances/TSPB.csv","B")};
        String[] solvers = new String[2];

        combineSolverNames(solvers);

        String outFolder = "./out4/";

        runExperiment(instances,solvers,outFolder);
    }

    private static void combineSolverNames(String[] solvers){
        String solverName = "candidateSearch";
        String[] intraTypes = {"Nodes", "Edges"};
        int i = 0;
        for(String intraType: intraTypes){
            solvers[i] = solverName + intraType + "Random";
            i++;
        }
    }
}
