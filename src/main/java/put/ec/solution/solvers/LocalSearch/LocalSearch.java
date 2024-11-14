package put.ec.solution.solvers.LocalSearch;

import put.ec.moves.*;
import put.ec.moves.sets.Moveset;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.Solver;
import put.ec.solution.solvers.SolverFactory;

public class LocalSearch extends Solver {
    private final HeuristicSolver initialSolver;
    private final LocalSearchType type;
    private final IntraMovesType movesType;
    private final Moveset moveset;
    private boolean validate = false;

    public LocalSearch(TravellingSalesmanProblem problem, String heuristicName, LocalSearchType type, IntraMovesType moveType){
        this(problem,heuristicName,type,moveType,new Moveset(moveType,type==LocalSearchType.Greedy));
    }

    public LocalSearch(TravellingSalesmanProblem problem, String heuristicName, LocalSearchType type, IntraMovesType moveType, Moveset moveset){
        super(problem);
        this.type = type;
        this.movesType = moveType;
        this.initialSolver = new SolverFactory().createHeuristicSolver(heuristicName,problem);
        this.moveset = moveset;

        if(validate){
            System.out.println("Validation is on for LocalSearch, turn off for efficiency");
        }

        setName(createName("localSearch",type.name(),moveType.name(),simplifyHeuristicName(heuristicName)));
    }

    protected String simplifyHeuristicName(String heuristicName){
        if(heuristicName.equals("random")){
            return "Random";
        }
        return "Heuristic";
    }

    @Override
    public Solution solve() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution solution = initialSolver.solve(startingCityIndex);
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
        }while(improvement);

        return solution;
    }

    public double calculateMoveCost(Solution solution, LocalMove move){
        if(move instanceof InterMove interMove){
            return calculateInterMoveCost(solution,interMove);
        }
        return calculateIntraMoveCost(solution,(IntraMove) move);
    }

    private double calculateInterMoveCost(Solution solution, InterMove move){
        double cost = 0;

        int placementIndex = move.getInsideCityIndex();
        City previousCity = solution.getCity(placementIndex-1);
        City insideCity = solution.getCity(placementIndex);
        City nextCity = solution.getCity(placementIndex+1);

        City outsideCity = getProblem().getCity(move.getOutsideCityIndex());

        cost += outsideCity.getCost();
        cost -= insideCity.getCost();

        cost += getProblem().getCostBetween(outsideCity,nextCity);
        cost += getProblem().getCostBetween(outsideCity,previousCity);

        cost -= getProblem().getCostBetween(insideCity,nextCity);
        cost -= getProblem().getCostBetween(insideCity,previousCity);

        return cost;
    }

    private double calculateIntraMoveCost(Solution solution, IntraMove move){
        switch (move.getType()){
            case Nodes: return calculateIntraNodesMoveCost(solution,move);
            case Edges: return calculateIntraEdgesMoveCost(solution,move);
        }
        throw new IllegalArgumentException("Unknown move type");
    }

    private double calculateIntraNodesMoveCost(Solution solution, IntraMove move){
        double cost = 0;

        int city1Index = move.getIndex1();
        int city2Index = move.getIndex2();

        City prevCity1 = solution.getCity(city1Index-1);
        City city1 = solution.getCity(city1Index);
        City nextCity1 = solution.getCity(city1Index+1);

        City prevCity2 = solution.getCity(city2Index-1);
        City city2 = solution.getCity(city2Index);
        City nextCity2 = solution.getCity(city2Index+1);

        if(city2 != prevCity1) {
            cost -= getProblem().getCostBetween(city1, prevCity1);
        }
        if(city2 != nextCity1) {
            cost -= getProblem().getCostBetween(city1, nextCity1);
        }

        if(city1 != prevCity2){
            cost -= getProblem().getCostBetween(city2,prevCity2);
        }
        if(city1 != nextCity2){
            cost -= getProblem().getCostBetween(city2,nextCity2);
        }

        cost += getProblem().getCostBetween(city1,prevCity2);
        cost += getProblem().getCostBetween(city1,nextCity2);
        cost += getProblem().getCostBetween(city2,prevCity1);
        cost += getProblem().getCostBetween(city2,nextCity1);

        return cost;
    }

    private double calculateIntraEdgesMoveCost(Solution solution, IntraMove move){
        double cost = 0;
        int edge1Start = move.getIndex1();
        int edge2Start = move.getIndex2();

        City city1 = solution.getCity(edge1Start);
        City city1Next = solution.getCity(edge1Start+1);

        City city2 = solution.getCity(edge2Start);
        City city2Next = solution.getCity(edge2Start+1);

        cost -= getProblem().getCostBetween(city1,city1Next);
        cost -= getProblem().getCostBetween(city2,city2Next);

        cost += getProblem().getCostBetween(city1Next,city2Next);
        cost += getProblem().getCostBetween(city1,city2);

        return cost;
    }

    public LocalSearchType getType() {
        return type;
    }

    public IntraMovesType getMovesType() {
        return movesType;
    }
}
