package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.IntraMovesType;
import put.ec.moves.LocalMove;
import put.ec.moves.MoveState;
import put.ec.moves.sets.Moveset;
import put.ec.moves.sets.OptimizedMoveset;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.SolverFactory;

public class MultipleStartLocalSearch extends LocalSearch {
    private static final String defaultStart = "random";
    private final int startingPoints;
    private final Moveset moveset;
    private final HeuristicSolver initialSolver;
    private final boolean validate = false;

    public MultipleStartLocalSearch(TravellingSalesmanProblem problem, String heuristicName, Moveset moveset, int startingPoints) {
        super(problem, defaultStart, LocalSearchType.Steepest, IntraMovesType.Edges, new OptimizedMoveset());
        this.startingPoints = startingPoints;
        this.initialSolver = new SolverFactory().createHeuristicSolver(heuristicName, problem);
        this.moveset = moveset;
        setName(createName("MultipleStartLocalSearch", defaultStart));
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution bestSolution = null;
        double bestObjectiveValue = Double.POSITIVE_INFINITY;

        for (int i = 0; i < startingPoints; i++) {
            Solution solution = initialSolver.solve(startingCityIndex);
            solution = localSearch(solution);

            double currentObjectiveValue = solution.getObjectiveFunctionValue();
            if (currentObjectiveValue < bestObjectiveValue) {
                bestObjectiveValue = currentObjectiveValue;
                bestSolution = solution;
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
}
