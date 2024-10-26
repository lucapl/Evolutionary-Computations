package put.ec.solution.solvers.Heuristics;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearestNeighbourSolver extends HeuristicSolver{
    public NearestNeighbourSolver(TravellingSalesmanProblem tsp){
        super(tsp);
        setName("nn");
    }

    @Override
    public double getCostAtPosition(Solution solution, City newCity, int index) throws IllegalArgumentException{
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

        cost += getProblem().getCostBetween(previousCity,newCity);

        return cost;
    }

    private double getCostAtPosition(Solution solution, City newCity) throws IllegalArgumentException{
        //
        return getCostAtPosition(solution,newCity,solution.size());
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution solution = new Solution(getProblem());

        solution.addCity(getProblem().getCity(startingCityIndex));

        for(int i = solution.size(); i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
            double bestCost = Double.POSITIVE_INFINITY;

            for(City city: getProblem().getCities()){
                if(solution.isIn(city)){
                    continue;
                }

                double newCost = getCostAtPosition(solution, city);

                if(newCost<bestCost){
                    bestCost = newCost;
                    bestCity = city;
                }
            }

            solution.addCity(bestCity);
        }

        return solution;
    }

    @Override
    public Solution solve() {
        return null;
    }

}
