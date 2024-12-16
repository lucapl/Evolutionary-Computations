package put.ec.solution.solvers.Evolutionary;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.EdgeState;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.WeightedRegretHeuristic;
import put.ec.solution.solvers.LocalSearch.IteratedLocalSearch;
import put.ec.solution.solvers.LocalSearch.LMSearch;
import put.ec.solution.solvers.LocalSearch.LocalSearch;
import put.ec.solution.solvers.Solver;
import put.ec.solution.solvers.SolverFactory;
import put.ec.solution.solvers.TimedSolver;
import put.ec.utils.TimeMeasure;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class HybridEA extends EvolutionaryAlgorithm implements TimedSolver {
    private LMSearch localSearch;
    private long time;
    private WeightedRegretHeuristic heuristic;

    public HybridEA(TravellingSalesmanProblem problem, int popSize, boolean steadyState, long time){
        super(problem, popSize, 0, 0.0, 1.0, steadyState);

        setTime(time);
        SolverFactory solverFactory = new SolverFactory();
        localSearch = (LMSearch) solverFactory.createSolver("lmSearch",problem);
        heuristic = (WeightedRegretHeuristic) solverFactory.createSolver("weightedRegretHeuristic",problem);
        setName("HybridEA");
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public Solution solve() {
        iterations = 0;
        initializePopulation();

        // Iterated Local Search process
        TimeMeasure timeMeasure = new TimeMeasure(time);
        timeMeasure.start();
        while (!timeMeasure.hasFinished()) {
            iterations++;
            Solution[] solutions = getTwoRandomSolutions();
            Solution parent1 = solutions[0];
            Solution parent2 = solutions[1];
            Solution newSolution = crossover(parent1, parent2);

            //newSolution = localSearch.solve(newSolution);

            // Update the best solution if `y` is better
            double currentObjectiveValue = newSolution.getObjectiveFunctionValue();
            if (currentObjectiveValue > getWorstSolutionInPop().getObjectiveFunctionValue() || !isInPop(newSolution)) {
                addToPopulation(newSolution);
            }
        }

        return findBest();
    }

    public void initializePopulation(){
        clear();
        for(int i = 0; i<getPopSize(); i++){
            Solution solution;
            do{
                solution = localSearch.solve();
            } while (isInPop(solution));
            add(solution);
        }
        setWorstSolutionInPop(findWorst());
    }

    @Override
    public Solution solve(int startingCityIndex) {
        return solve();
    }

    @Override
    public Solution crossover(Solution parent1, Solution parent2) {
        List<City> cityOrder = new ArrayList<>(parent1.getCityOrder());
        Solution child = new Solution(getProblem());
        child.setCityOrder(cityOrder);

        for(City city: parent1.getCityOrder()){
            if(!parent2.isIn(city)){
                cityOrder.remove(city);
                continue;
            }
            City next = parent1.getNext(city);
            City[] edge = new City[]{city, next};
            if(parent2.isEdgeIn(edge) == EdgeState.NotFound){
                cityOrder.remove(next);
            }
        }

        if(cityOrder.isEmpty()){
            System.err.println("what");
        }

        child.calculateCityLocations();
        child.calculateInSolutions();
        child.getObjectiveFunctionValue();
        return heuristic.solve_solution(child);
    }

    @Override
    public Solution mutation(Solution solution) {
        return null;
    }
}
