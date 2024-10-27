package put.ec.solution.solvers.Heuristics;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

public class GreedyCycleSolver extends NearestNeighbourAtAnySolver {

    public GreedyCycleSolver(TravellingSalesmanProblem problem){
        super(problem);
        setName("greedyCycle");
    }

    @Override
    public double getCostAtPosition(Solution solution, City newCity, int index) {
        if (index < 0 || index > solution.size()){
            throw new IllegalArgumentException("Index out of bounds");
        }

        if (solution.size()<2){
            return super.getCostAtPosition(solution,newCity,index);
        }

        double cost = newCity.getCost();

        if (solution.isEmpty()){
            return cost;
        }

        int previousIndex = (index-1);
        //previousIndex = previousIndex>=0?previousIndex:solution.size()-1;
        City previousCity = solution.getCity(previousIndex);

        City nextCity = solution.getCity(index);

        cost += getProblem().getCostBetween(previousCity,newCity);
        cost += getProblem().getCostBetween(nextCity,newCity);

        cost -= getProblem().getCostBetween(previousCity,nextCity);

        return cost;
    }
}
