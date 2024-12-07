package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.SolutionWriter;
import put.ec.solution.solvers.*;
import put.ec.utils.LoadingBar;
import put.ec.utils.TimeMeasure;

abstract public class Experiment {
    public static void runExperiment(TravellingSalesmanProblem[] instances, String[] solvers,String outFolder){
       runExperimentCustomAmount(instances,solvers,outFolder,200);
    }
    public static void runExperimentCustomAmount(TravellingSalesmanProblem[] instances, String[] solvers,String outFolder, int maxRunAmount){
        SolverFactory solverFactory = new SolverFactory();
        TimeMeasure timeMeasure = new TimeMeasure();

        for (String solverName : solvers) {
            SolutionWriter solutionWriter = new SolutionWriter();
            for (TravellingSalesmanProblem problemInstance : instances) {
                Solver solver = solverFactory.createSolver(solverName, problemInstance);
                solutionWriter.newInstance(problemInstance.getName());

                LoadingBar loadingBar = new LoadingBar(maxRunAmount,problemInstance.getName());
                for (int i = 0; i < maxRunAmount; i++) {
                    loadingBar.progress(i);
                    timeMeasure.start();
                    Solution solution = solver.solve(i);
                    timeMeasure.stop();
                    solution.setStartingCityIndex(i);
                    solutionWriter.writeSolution(solution, solver, timeMeasure, problemInstance);
                }
                solutionWriter.saveInstanceSolutions();
            }
            solutionWriter.saveSolutionInfo(outFolder);
        }
    }
}