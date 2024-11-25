package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.IntraMovesType;
import put.ec.moves.LocalMove;
import put.ec.moves.MoveState;
import put.ec.moves.sets.Moveset;
import put.ec.moves.sets.OptimizedMoveset;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.SolverFactory;

import java.util.*;

public class IteratedLocalSearch extends LocalSearch {
    private static final String defaultStart = "random";
    private final int perturbationSize;
    private final Moveset moveset;
    private final HeuristicSolver initialSolver;
    private final int time;
    private final boolean validate = false;

    public IteratedLocalSearch(TravellingSalesmanProblem problem, String heuristicName, Moveset moveset, int time, int perturbationSize) {
        super(problem, defaultStart, LocalSearchType.Steepest, IntraMovesType.Edges, new OptimizedMoveset());
        this.perturbationSize = perturbationSize;
        this.initialSolver = new SolverFactory().createHeuristicSolver(heuristicName, problem);
        this.moveset = moveset;
        this.time = time;
        setName(createName("IteratedLocalSearch", defaultStart));
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

        // Iterated Local Search process
        while (System.currentTimeMillis() < endTime) {
            Solution y = perturb(bestSolution);
            y = localSearch(y);

            // Update the best solution if `y` is better
            double currentObjectiveValue = y.getObjectiveFunctionValue();
            if (currentObjectiveValue < bestObjectiveValue) {
                bestSolution = y;
                bestObjectiveValue = currentObjectiveValue;
                x = y; // Update `x` to the new best solution
            }
        }

        return bestSolution;
    }

    public Solution localSearch(Solution solution) {
        moveset.clear();
        moveset.setSolution(solution);
        solution.calculateCityLocations();
        solution.calculateInSolutions();

        boolean improvement;
        double prevobj = Double.POSITIVE_INFINITY;
        if (validate) {
            prevobj = solution.getObjectiveFunctionValue();
        }
        iterations = 0;
        double cost_past = solution.getObjectiveFunctionValue();
        double new_cost = 0;
        do{
            improvement = false;
            double bestCost = Double.POSITIVE_INFINITY;
            LocalMove bestMove = null;

            for(LocalMove move: moveset){
                double moveCost = move.getMoveCost();
                if(!move.isEvaluated()){
                    moveCost = calculateMoveCost(solution,move);
                    move.setMoveCost(moveCost);
                    moveset.giveMoveEvaluation(move,moveCost);
                }

                if (moveCost >= 0 || move.getMoveState() == MoveState.NotApplicable){ //ignore non improving cost
                    continue;
                }
                improvement = true;
                if(getType() ==LocalSearchType.Greedy){ //finish if greedy
                    bestCost = moveCost;
                    bestMove = move;
                    break;
                }
                if(moveCost < bestCost){ //steepest
                    bestCost = moveCost;
                    bestMove = move;
                }
            }

            if(bestMove != null){
                if(validate){
                    prevobj = solution.getObjectiveFunctionValue();
                }
                solution.performMove(bestMove);
                // some movesets may use this info
                moveset.setPerformedMove(bestMove);

                if(validate){
                    double curobj = solution.getObjectiveFunctionValue();

                    if (curobj-prevobj != bestCost){
                        System.err.println("Change in cost not equal to expected! \nDifference: "+((curobj-prevobj)-bestCost) +"\n New cost: "+curobj);
                    }
                }
            }
            iterations++;
            new_cost = solution.getObjectiveFunctionValue();
            if (new_cost == cost_past){
                break;
            }
            else{
                cost_past = new_cost;
            }
        }while(improvement);

        return solution;
    }

    private Solution perturb(Solution originalSolution) {

        List<City> cityOrder = new ArrayList<>(originalSolution.getCityOrder());
        Solution perturbedSolution = new Solution(getProblem());
        perturbedSolution.setCityOrder(cityOrder);
        Random rand = new Random();

        int numberOfCities = perturbedSolution.getProblem().getNumberOfCities();

        // Step 3: Apply perturbation to the copied city order
        for (int i = 0; i < perturbationSize; i++) {
            // Remove one random city from the current solution
            int removeIndex = rand.nextInt(cityOrder.size());
            cityOrder.remove(removeIndex);

            // Add a city that is not currently in the solution
            City newCity;
            do {
                int randomIndex = rand.nextInt(numberOfCities);
                newCity = perturbedSolution.getProblem().getCity(randomIndex);
            } while (cityOrder.contains(newCity));

            cityOrder.add(newCity);
        }

        // Step 4: Set the perturbed city order to the copied solution
        perturbedSolution.setCityOrder(cityOrder);

        return perturbedSolution;
    }

}
