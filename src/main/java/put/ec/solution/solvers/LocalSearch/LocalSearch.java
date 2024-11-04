package put.ec.solution.solvers.LocalSearch;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Heuristics.HeuristicSolver;
import put.ec.solution.solvers.Solver;
import put.ec.solution.solvers.SolverFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalSearch extends Solver {
    private final HeuristicSolver initialSolver;
    private final LocalSearchType type;
    private final IntraMovesType movesType;
    private boolean validate = true;

    public LocalSearch(TravellingSalesmanProblem problem, String heuristicName, LocalSearchType type, IntraMovesType moveType){
        super(problem);
        this.type = type;
        this.movesType = moveType;
        SolverFactory solverFactory = new SolverFactory();
        this.initialSolver = solverFactory.createHeuristicSolver(heuristicName,problem);

        setName("localSearch"+type.name()+moveType.name()+simplifyHeuristicName(heuristicName));
    }

    protected String simplifyHeuristicName(String heuristicName){
        if(heuristicName.equals("random")){
            return "Random";
        }
        return "Heuristic";
    }

    @Override
    public Solution solve() {
        return null;
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution solution = initialSolver.solve(startingCityIndex);
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
            List<LocalMove> moves = getMoves(solution); // get all possible moves

            if(type == LocalSearchType.Greedy){ // shuffle them if greedy
                Collections.shuffle(moves); //this is Fisher-Yates shuffle
            }

            for(LocalMove move: moves){
                double moveCost = calculateMoveCost(solution,move);

                if (moveCost >= 0){ //ignore non improving cost
                    continue;
                }
                improvement = true;
                if(type==LocalSearchType.Greedy){ //finish if greedy
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
                solution.performMove(bestMove);

                if(validate){
                    double curobj = solution.getObjectiveFunctionValue();

                    if (curobj-prevobj != bestCost){
                        System.err.println("Change in cost not equal to expected! \nDifference: "+((curobj-prevobj)-bestCost));
                    }
                    prevobj = curobj;
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


    public List<LocalMove> getMoves(Solution solution){
        // this is probably the bottleneck
        int n = getProblem().getNumberOfCities();
        List<LocalMove> moves = new ArrayList<>(getProblem().getNumberOfCities()*getProblem().getSolutionLength()); // max capacity

        for(int i = 0; i<n; i++){
            City city_i = getProblem().getCity(i);
            for(int j = i+1; j<n; j++){
                City city_j = getProblem().getCity(j);

                if(solution.isIn(city_i) && solution.isIn(city_j)){
                    moves.add(new IntraMove(
                            solution.getCityIndexInOrder(city_i),
                            solution.getCityIndexInOrder(city_j),
                            this.movesType));
                    continue;
                }
                if(solution.isIn(city_i)){
                    moves.add(new InterMove(
                            solution.getCityIndexInOrder(city_i),
                            city_j.getIndex()));
                }
                if(solution.isIn(city_j)){
                    moves.add(new InterMove(
                            solution.getCityIndexInOrder(city_j),
                            city_i.getIndex()));
                }
            }
        }

        return moves;
    }
}
