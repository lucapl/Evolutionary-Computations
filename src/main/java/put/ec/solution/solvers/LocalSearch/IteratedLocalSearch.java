package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.IntraMove;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IteratedLocalSearch extends LocalSearch {
    private static final String defaultStart = "random";
    private final int startingPoints;
    private boolean validate = false;
    private final Moveset moveset;
    private final HeuristicSolver initialSolver;
    private final int perturbationSize;

    public IteratedLocalSearch(TravellingSalesmanProblem problem, String heuristicName, Moveset moveset, int startingPoints, int perturbationSize) {
        super(problem, defaultStart, LocalSearchType.Steepest, IntraMovesType.Edges, new OptimizedMoveset());
        this.startingPoints = startingPoints;
        this.initialSolver = new SolverFactory().createHeuristicSolver(heuristicName, problem);
        this.moveset = moveset;
        this.perturbationSize = perturbationSize;
        setName(createName("IteratedLocalSearch", defaultStart));
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution currentSolution = initialSolver.solve(startingCityIndex);
        currentSolution = localSearch(currentSolution);

        Solution bestSolution = currentSolution;
        double bestObjectiveValue = currentSolution.getObjectiveFunctionValue();

        for (int iteration = 0; iteration < startingPoints; iteration++) {
            Solution perturbedSolution = perturb(currentSolution);

            perturbedSolution = localSearch(perturbedSolution);

            double perturbedObjectiveValue = perturbedSolution.getObjectiveFunctionValue();
            if (perturbedObjectiveValue < bestObjectiveValue) {
                currentSolution = perturbedSolution;
                bestObjectiveValue = perturbedObjectiveValue;
                bestSolution = perturbedSolution;
            }
        }

        return bestSolution;
    }


    private Solution localSearch(Solution solution) {
        moveset.clear();
        moveset.setSolution(solution);
        solution.calculateCityLocations();
        solution.calculateInSolutions();

        // Record the start time
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 500) {
            double bestCost = Double.POSITIVE_INFINITY;
            LocalMove bestMove = null;

            for (LocalMove move : moveset) {
                double moveCost = move.getMoveCost();
                if (!move.isEvaluated()) {
                    moveCost = calculateMoveCost(solution, move);
                    move.setMoveCost(moveCost);
                    moveset.giveMoveEvaluation(move, moveCost);
                }

                if (moveCost >= 0 || move.getMoveState() == MoveState.NotApplicable) { // Ignore non-improving cost
                    continue;
                }

                // Choose the best move based on the move's cost
                if (moveCost < bestCost) {
                    bestCost = moveCost;
                    bestMove = move;
                }
            }

            if (bestMove != null) {
                solution.performMove(bestMove);
                moveset.setPerformedMove(bestMove);
            }
        }

        return solution;
    }

    private Solution perturb(Solution solution) {
        List<City> cityOrder = new ArrayList<>(solution.getCityOrder());
        Random rand = new Random();

        for (int i = 0; i < perturbationSize; i++) {
            // Randomly select a segment length and start index
            int segmentLength = rand.nextInt(cityOrder.size() / 2) + 1;
            int startIdx = rand.nextInt(cityOrder.size() - segmentLength);

            // Extract the segment and randomly reposition it
            List<City> segment = new ArrayList<>(cityOrder.subList(startIdx, startIdx + segmentLength));
            cityOrder.subList(startIdx, startIdx + segmentLength).clear(); // Remove the segment
            int newPos = rand.nextInt(cityOrder.size()); // Random position for the segment

            // Insert the segment back into the new position
            cityOrder.addAll(newPos, segment);
        }

        solution.setCityOrder(cityOrder);
        return solution;
    }


}
