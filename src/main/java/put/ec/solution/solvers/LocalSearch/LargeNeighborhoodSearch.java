package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.sets.Moveset;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.Heuristics.WeightedRegretHeuristic;
import put.ec.solution.solvers.SolverFactory;
import put.ec.utils.TimeMeasure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LargeNeighborhoodSearch extends IteratedLocalSearch{
    private static final String defaultStart = "random";
    private final HeuristicSolver initialSolver;
    private final WeightedRegretHeuristic repairSolver;
    private final boolean withLocalSearch;

    public LargeNeighborhoodSearch(TravellingSalesmanProblem problem, String heuristicName, Moveset moveset, int time, boolean withLocalSearch) {
        super(problem, heuristicName, moveset, time, 200);
        this.initialSolver = new SolverFactory().createHeuristicSolver(heuristicName, problem);
        this.repairSolver = new WeightedRegretHeuristic(problem, 0.5, 0.5);
        this.withLocalSearch = withLocalSearch;
        setName(createName("LargeNeighborhoodSearchLocal", defaultStart,withLocalSearch?"withLS":"withoutLS"));
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution bestSolution = localSearch(initialSolver.solve(startingCityIndex));
        double bestObjectiveValue = bestSolution.getObjectiveFunctionValue();

        iterations = 0;

        // Iterated Local Search process
        // Time base termination
        TimeMeasure timeMeasure = new TimeMeasure(getTime());
        timeMeasure.start();
        while (!timeMeasure.hasFinished()) {
            iterations++;
            Solution currentSolution = destroy(bestSolution);
            currentSolution = repair(currentSolution);
            if(withLocalSearch){
                currentSolution = localSearch(currentSolution);
            }

            // Update the best solution if `y` is better
            double currentObjectiveValue = currentSolution.getObjectiveFunctionValue();
            if (currentObjectiveValue < bestObjectiveValue) {
                bestSolution = currentSolution;
                bestObjectiveValue = currentObjectiveValue;
            }
        }

        return bestSolution;
    }

    private Solution destroy(Solution originalSolution){
        List<City> cityOrder = new ArrayList<>(originalSolution.getCityOrder());
        Solution perturbedSolution = new Solution(getProblem());
        perturbedSolution.setCityOrder(cityOrder);
        Random rand = new Random();
        int chunkSize = 20 + rand.nextInt(11);

        for (int i = 0; i < chunkSize; i++) {
            int removeIndex = rand.nextInt(cityOrder.size());
            cityOrder.remove(removeIndex);
        }

        perturbedSolution.setCityOrder(cityOrder);

        return perturbedSolution;
    }

    private Solution repair(Solution originalSolution){
        List<City> cityOrder = new ArrayList<>(originalSolution.getCityOrder());
        Solution perturbedSolution = new Solution(getProblem());
        perturbedSolution.setCityOrder(cityOrder);

        perturbedSolution = repairSolver.solve_solution(perturbedSolution);

        return perturbedSolution;
    }
}
