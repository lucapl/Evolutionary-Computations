package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.SolutionWriter;
import put.ec.solution.solvers.*;
import put.ec.utils.TimeMeasure;

abstract public class Experiment {
    public static void runExperiment(TravellingSalesmanProblem[] instances, String[] solvers,String outFolder){
        SolutionWriter solutionWriter = new SolutionWriter();
        SolverFactory solverFactory = new SolverFactory();
        TimeMeasure timeMeasure = new TimeMeasure();


        for (int instanceIndex = 0; instanceIndex < instances.length; instanceIndex++) {
            TravellingSalesmanProblem problemInstance = instances[instanceIndex];

            for(int solverIndex = 0; solverIndex < solvers.length; solverIndex++) {
                String solverName = solvers[solverIndex];
                Solver solver = solverFactory.createSolver(solverName,problemInstance);

                for (int startingCity = 0; startingCity < problemInstance.getNumberOfCities(); startingCity++) {
                    timeMeasure.start();
                    Solution solution = solver.solve(startingCity);
                    timeMeasure.stop();
                    solution.setStartingCityIndex(startingCity);
                    solutionWriter.writeSolution(solution,solver,timeMeasure,problemInstance,outFolder);
                }
            }
        }
    }
}