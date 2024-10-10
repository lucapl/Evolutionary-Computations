package put.ec.solution.solvers;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

public class GreedyCycleSolver extends NearestNeighbourAtAnySolver {

    public GreedyCycleSolver(TravellingSalesmanProblem problem){
        super(problem);
    }

    @Override
    public double getCostAtPosition(Solution solution, City newCity, int index) {
        if (index < 0 || index > solution.size()){
            throw new IllegalArgumentException("Index out of bounds");
        }

        double cost = newCity.getCost();

        if (solution.isEmpty()){
            return cost;
        }

        int previousIndex = (index-1);
        previousIndex = previousIndex>=0?previousIndex:solution.size()-1;
        City previousCity = solution.getCity(previousIndex);

        int nextIndex = (index+1)%solution.size();
        City nextCity = solution.getCity(nextIndex);

        cost += getProblem().getCostBetween(previousCity,newCity);
        cost += getProblem().getCostBetween(nextCity,newCity);

        cost -= getProblem().getCostBetween(previousCity,nextCity);

        return cost;

//        if(solution.size()>2){
//            cost += solution.getCostBetween(newCity,solution.getCity(index));
//        }
    }
}
