package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;

import java.util.Arrays;

public class Assignment3 extends Experiment{
    public static void main(String[] args) {
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv","A"),il.load("instances/TSPB.csv","B")};
        String[] solvers = new String[4];

        combineSolverNames(solvers);

        String outFolder = "./out3/";

        runExperiment(instances,solvers,outFolder);
    }

    private static void combineSolverNames(String[] solvers){
        String solverName = "localSearch";
        String[] solverTypes = {"Greedy", "Steepest"};
        String[] intraTypes = {"Nodes"};//, "Edges"};
        String[] startTypes = {"Heuristic", "Random"};
        int i = 0;
        for(String solverType: solverTypes){
            for(String intraType: intraTypes){
                for(String startType: startTypes){
                    solvers[i] = solverName + solverType + intraType + startType;
                    i++;
                }
            }
        }
        System.out.println(Arrays.toString(solvers));
    }
}
