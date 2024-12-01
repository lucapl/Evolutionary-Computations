package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.sets.Moveset;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.Heuristics.WeightedRegretHeuristic;
import put.ec.solution.solvers.SolverFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LargeNeighborhoodSearch extends IteratedLocalSearch{
    private static final String defaultStart = "random";
    private final HeuristicSolver initialSolver;
    private final WeightedRegretHeuristic repairSolver;
    private final int time;

    public LargeNeighborhoodSearch(TravellingSalesmanProblem problem, String heuristicName, Moveset moveset, int time) {
        super(problem, heuristicName, moveset, time, 200);
        this.initialSolver = new SolverFactory().createHeuristicSolver(heuristicName, problem);
        this.repairSolver = new WeightedRegretHeuristic(problem, 0.5, 0.5);
        this.time = time;
        setName(createName("LargeNeighborhoodSearchLocal", defaultStart));
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution x = initialSolver.solve(startingCityIndex);
        x = localSearch(x);

        Solution bestSolution = x;
        double bestObjectiveValue = x.getObjectiveFunctionValue();

        // Time-based termination
        long startTime = System.currentTimeMillis();
        long endTime = startTime + time;

        int iterations = 0;

        // Iterated Local Search process
        while (System.currentTimeMillis() < endTime) {
            iterations++;
            Solution y = destroy(bestSolution);
            y = repair(y);
            y = localSearch(y);

            // Update the best solution if `y` is better
            double currentObjectiveValue = y.getObjectiveFunctionValue();
            if (currentObjectiveValue < bestObjectiveValue) {
                bestSolution = y;
                bestObjectiveValue = currentObjectiveValue;
                x = y; // Update `x` to the new best solution
            }
        }
        System.out.println(iterations);

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
