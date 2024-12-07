package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.SolutionWriter;
import put.ec.solution.solvers.LocalSearch.IteratedLocalSearch;
import put.ec.solution.solvers.LocalSearch.MultipleStartLocalSearch;
import put.ec.solution.solvers.Solver;
import put.ec.solution.solvers.SolverFactory;
import put.ec.utils.LoadingBar;
import put.ec.utils.StatKeeper;
import put.ec.utils.TimeMeasure;

import java.util.HashMap;
import java.util.Map;

public class Assignment6 extends Experiment{
    public static void main(String[] args){
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv","A"),il.load("instances/TSPB.csv","B")};
        String[] solvers = { "ils"};
        String outFolder = "./out6/";

        runCustomExperiment(instances,solvers,outFolder, 20);
    }

    public static void runCustomExperiment(TravellingSalesmanProblem[] instances, String[] solvers,String outFolder, int maxRunAmount){
        SolverFactory solverFactory = new SolverFactory();
        TimeMeasure timeMeasure = new TimeMeasure();
        Map<String, StatKeeper> mslsTimes = new HashMap<>();

        for (String solverName : solvers) {
            SolutionWriter solutionWriter = new SolutionWriter();
            for (TravellingSalesmanProblem problemInstance : instances) {
                Solver solver = solverFactory.createSolver(solverName, problemInstance);
                if(solver instanceof IteratedLocalSearch ils){
                    //ils.setTime((int)mslsTimes.get(problemInstance.getName()).getAverage());
                    ils.setTime(2739058509L);
                }
                solutionWriter.newInstance(problemInstance.getName());

                LoadingBar loadingBar = new LoadingBar(maxRunAmount,problemInstance.getName());
                for (int i = 0; i < maxRunAmount; i++) {
                    loadingBar.progress(i);
                    timeMeasure.start();
                    Solution solution = solver.solve(0);
                    timeMeasure.stop();
                    solution.setStartingCityIndex(0);
                    solutionWriter.writeSolution(solution, solver, timeMeasure, problemInstance);
                }

                if(solver instanceof MultipleStartLocalSearch){
                    mslsTimes.put(problemInstance.getName(),solutionWriter.getTimeStat());
                }
                solutionWriter.saveInstanceSolutions();
            }
            solutionWriter.saveSolutionInfo(outFolder);
        }
    }
}
