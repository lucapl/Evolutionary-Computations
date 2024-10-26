package put.ec.solution.solvers.Heuristics;

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

        solution.addCity(getProblem().getCity(startingCityIndex));

        for(int i = solution.size(); i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
            int bestPlacingIndex = -1;
            double bestCost = Double.POSITIVE_INFINITY;

            for(int candidateIndex = 0; candidateIndex <= solution.size(); candidateIndex++){
                for(City city: getProblem().getCities()){
                    if(solution.isIn(city)){
                        continue;
                    }

                    double newCost = getCostAtPosition(solution, city, candidateIndex);

                    if(newCost<bestCost){
                        bestCost = newCost;
                        bestCity = city;
                        bestPlacingIndex = candidateIndex;
                    }
                }
            }
            solution.addCityAt(bestPlacingIndex,bestCity);
        }

        return solution;
    }
}
