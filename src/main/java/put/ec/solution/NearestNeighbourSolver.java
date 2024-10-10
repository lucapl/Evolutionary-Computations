package put.ec.solution;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearestNeighbourSolver extends Solver{
    public NearestNeighbourSolver(TravellingSalesmanProblem tsp){
        super(tsp);
    }

    @Override
    public double getCostAtPosition(Solution solution, City newCity,int index) throws IllegalArgumentException{
        if (index < 0 || index >= solution.size()){
            throw new IllegalArgumentException("Index out of bounds");
        }

        double cost = newCity.getCost();

        if (solution.isEmpty()){
            return cost;
        }

        City cityInPlace = solution.getCity(index);

        cost += getProblem().getCostBetween(cityInPlace,newCity);

        return cost;
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution solution = new Solution(getProblem());
        List<Boolean> inSolution = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),false));

        solution.addCity(getProblem().getCity(startingCityIndex));
        inSolution.set(startingCityIndex,true);

        for(int i = 0; i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
            int bestIndex = -1;
            double bestCost = Double.POSITIVE_INFINITY;

            for(int j=0; j<getProblem().getNumberOfCities(); j++){
                if(inSolution.get(j)){
                    continue;
                }

                City city = getProblem().getCity(j);

                double newCost = getCostAtPosition(solution, city, i);

                if(newCost<bestCost){
                    bestCost = newCost;
                    bestCity = city;
                    bestIndex = j;
                }
            }

            inSolution.set(bestIndex,true);
            solution.addCity(bestCity);
        }

        return solution;
    }

    @Override
    public Solution solve() {
        return null;
    }

}
