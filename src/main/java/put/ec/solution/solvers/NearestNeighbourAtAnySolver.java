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
        setName("nnAnywhere");
    }

    @Override
    public Solution solve(int startingCityIndex) {
        Solution solution = new Solution(getProblem());
        List<Boolean> inSolution = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),false));

        solution.addCity(getProblem().getCity(startingCityIndex));
        inSolution.set(startingCityIndex,true);

        for(int i = solution.size(); i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
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
                        bestPlacingIndex = candidateIndex;
                    }
                }
            }
            inSolution.set(bestCity.getIndex(),true);
            solution.addCityAt(bestPlacingIndex,bestCity);
        }

        return solution;
    }
}
