package put.ec.solution.solvers;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearestNeighbourAtAnySolver extends NearestNeighbourSolver {

    public NearestNeighbourAtAnySolver(TravellingSalesmanProblem problem){
        super(problem);
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

    @Override
    public Solution solve(int startingCityIndex) {
        Solution solution = new Solution(getProblem());
        List<Boolean> inSolution = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),false));

        solution.addCity(getProblem().getCity(startingCityIndex));
        inSolution.set(startingCityIndex,true);

        for(int i = 0; i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
            int bestCityIndex = -1;
            int bestPlacingIndex = -1;
            double bestCost = Double.POSITIVE_INFINITY;

            for(int candidateIndex = 0; candidateIndex <= solution.size(); candidateIndex++){
                for(int currentCityIndex=0; currentCityIndex < getProblem().getNumberOfCities(); currentCityIndex++){
                    if(inSolution.get(currentCityIndex)){
                        continue;
                    }

                    City city = getProblem().getCity(currentCityIndex);

                    double newCost = getCostAtPosition(solution, city, candidateIndex);

                    if(newCost<bestCost){
                        bestCost = newCost;
                        bestCity = city;
                        bestCityIndex = currentCityIndex;
                        bestPlacingIndex = candidateIndex;
                    }
                }
            }
            inSolution.set(bestCityIndex,true);
            solution.addCityAt(bestPlacingIndex,bestCity);
        }

        return solution;
    }
}
