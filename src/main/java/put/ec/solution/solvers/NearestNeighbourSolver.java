package put.ec.solution.solvers;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearestNeighbourSolver extends Solver {
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
        List<Boolean> inSolution = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),false));

        solution.addCity(getProblem().getCity(startingCityIndex));
        inSolution.set(startingCityIndex,true);

        for(int i = solution.size(); i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
            double bestCost = Double.POSITIVE_INFINITY;

            for(int j=0; j<getProblem().getNumberOfCities(); j++){
                if(inSolution.get(j)){
                    continue;
                }

                City city = getProblem().getCity(j);

                double newCost = getCostAtPosition(solution, city);

                if(newCost<bestCost){
                    bestCost = newCost;
                    bestCity = city;
                }
            }

            inSolution.set(bestCity.getIndex(),  true);
            solution.addCity(bestCity);
        }

        return solution;
    }

    @Override
    public Solution solve() {
        return null;
    }

}
