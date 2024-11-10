package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.SolutionWriter;
import put.ec.solution.solvers.*;
import put.ec.utils.TimeMeasure;

abstract public class Experiment {
    public static void runExperiment(TravellingSalesmanProblem[] instances, String[] solvers,String outFolder){
        SolverFactory solverFactory = new SolverFactory();
        TimeMeasure timeMeasure = new TimeMeasure();

        for (String solverName : solvers) {
            SolutionWriter solutionWriter = new SolutionWriter();
            for (TravellingSalesmanProblem problemInstance : instances) {
                Solver solver = solverFactory.createSolver(solverName, problemInstance);
                solutionWriter.newInstance(problemInstance.getName());

                for (int startingCity = 0; startingCity < problemInstance.getNumberOfCities(); startingCity++) {
                    timeMeasure.start();
                    Solution solution = solver.solve(startingCity);
                    timeMeasure.stop();
                    solution.setStartingCityIndex(startingCity);
                    solutionWriter.writeSolution(solution, solver, timeMeasure, problemInstance);
                }
                solutionWriter.saveInstanceSolutions();
            }
            solutionWriter.saveSolutionInfo(outFolder);
        }
    }
}