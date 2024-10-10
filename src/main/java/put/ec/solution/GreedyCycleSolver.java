package put.ec.solution;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;

import java.util.List;

public class GreedyCycleSolver extends NearestNeighbourAtAnySolver {

    public GreedyCycleSolver(TravellingSalesmanProblem problem){
        super(problem);
    }

    @Override
    public double getCostAtPosition(Solution solution, City city, int index) {
        double cost = super.getCostAtPosition(solution,city,index);

        if(solution.size()>2){
            cost += solution.getCostBetween(city,solution.getCity((index+1)%solution.size()));
        }
        return cost;
    }
}
