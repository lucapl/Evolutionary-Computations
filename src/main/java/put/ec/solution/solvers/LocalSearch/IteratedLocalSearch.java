package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.LocalMove;
import put.ec.moves.MoveState;
import put.ec.moves.sets.Moveset;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.SolverFactory;
import put.ec.utils.TimeMeasure;

import java.util.*;

public class IteratedLocalSearch extends LMSearch {
    private static final String defaultStart = "random";
    private final int perturbationSize;
    private final Moveset moveset;
    private final HeuristicSolver initialSolver;
    private long time;
    private final boolean validate = false;

    public IteratedLocalSearch(TravellingSalesmanProblem problem, String heuristicName, Moveset moveset, long time, int perturbationSize) {
        super(problem);
        this.perturbationSize = perturbationSize;
        this.initialSolver = new SolverFactory().createHeuristicSolver(heuristicName, problem);
        this.moveset = moveset;
        this.setTime(time);
        setName(createName("IteratedLocalSearch", defaultStart));
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution x = initialSolver.solve(startingCityIndex);
        x = localSearch(x);

        Solution bestSolution = x;
        double bestObjectiveValue = x.getObjectiveFunctionValue();

        iterations = 0;

        // Iterated Local Search process
        TimeMeasure timeMeasure = new TimeMeasure(getTime());
        timeMeasure.start();
        while (!timeMeasure.hasFinished()) {
            iterations++;
            Solution currentSolution = perturb(bestSolution);
            currentSolution = super.solve(currentSolution);

            // Update the best solution if `y` is better
            double currentObjectiveValue = currentSolution.getObjectiveFunctionValue();
            if (currentObjectiveValue < bestObjectiveValue) {
                bestSolution = currentSolution;
                bestObjectiveValue = currentObjectiveValue;
            }
        }

        return bestSolution;
    }

    public Solution localSearch(Solution solution) {
        return super.solve(solution);
    }

    private Solution perturb(Solution originalSolution) {

        List<City> cityOrder = new ArrayList<>(originalSolution.getCityOrder());
        Solution perturbedSolution = new Solution(getProblem());
        perturbedSolution.setCityOrder(cityOrder);
        Random rand = new Random();

        int numberOfCities = perturbedSolution.getProblem().getNumberOfCities();

        for (int i = 0; i < perturbationSize; i++) {
            int removeIndex = rand.nextInt(cityOrder.size());
            cityOrder.remove(removeIndex);

            City newCity;
            do {
                int randomIndex = rand.nextInt(numberOfCities);
                newCity = perturbedSolution.getProblem().getCity(randomIndex);
            } while (cityOrder.contains(newCity));

            cityOrder.add(newCity);
        }

        perturbedSolution.setCityOrder(cityOrder);

        return perturbedSolution;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
