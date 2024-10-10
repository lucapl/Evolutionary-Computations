package put.ec.solution;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearestNeighbourAtAnySolver extends NearestNeighbourSolver{

    public NearestNeighbourAtAnySolver(TravellingSalesmanProblem problem){
        super(problem);
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
            int bestPlacingIndex = -1;
            double bestCost = Double.POSITIVE_INFINITY;

            for(int k = 0; k<solution.size();k++){
                for(int j=0; j<getProblem().getNumberOfCities(); j++){
                    if(inSolution.get(j)){
                        continue;
                    }

                    City city = getProblem().getCity(j);

                    double newCost = getCostAtPosition(solution, city, k);

                    if(newCost<bestCost){
                        bestCost = newCost;
                        bestCity = city;
                        bestIndex = j;
                        bestPlacingIndex = k;
                    }
                }
            }
            inSolution.set(bestIndex,true);
            solution.addCityAt(bestPlacingIndex,bestCity);
        }

        return solution;
    }
}
