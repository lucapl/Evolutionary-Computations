package put.ec;

import put.ec.instance.InstanceLoader;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.SolutionWriter;
import put.ec.solution.solvers.*;

public class Main {
    public static void main(String[] args) {
        InstanceLoader il = new InstanceLoader();

        TravellingSalesmanProblem[] instances = {il.load("instances/TSPA.csv"),il.load("instances/TSPB.csv")};
        String[] instanceNames = {"A","B"};
        String[] solvers = {"random","nn","nnAnywhere","greedyCycle"};



        SolutionWriter solutionWriter = new SolutionWriter();
        SolverFactory solverFactory = new SolverFactory();

        for(int solverIndex = 0; solverIndex < solvers.length; solverIndex++) {
            String solverName = solvers[solverIndex];

            for (int instanceIndex = 0; instanceIndex < instances.length; instanceIndex++) {
                TravellingSalesmanProblem problemInstance = instances[instanceIndex];
                String instanceName = instanceNames[instanceIndex];
                Solver currentSolver = solverFactory.createSolver(solverName,problemInstance);

                for (int startingCity = 0; startingCity < problemInstance.getNumberOfCities(); startingCity++) {
                    Solution solution = currentSolver.solve(startingCity);
                    solutionWriter.writeSolution(solution,"./out/", instanceName+"_"+startingCity+"_"+solverName, instanceName,solverName);
                }
            }
        }
    }
}