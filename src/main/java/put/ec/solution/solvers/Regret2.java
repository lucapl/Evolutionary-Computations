package put.ec.solution.solvers;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Regret2 extends NearestNeighbourSolver{

    public Regret2(TravellingSalesmanProblem tsp){
        super(tsp);
        setName("regret2");
    }

    public Solution solve(int startingCityIndex) {
        Solution solution = new Solution(getProblem());
        List<Boolean> inSolution = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),false));

        solution.addCity(getProblem().getCity(startingCityIndex));
        inSolution.set(startingCityIndex,true);

        for(int i = solution.size(); i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
            int bestPlacingIndex = -1;
            double bestCost = Double.POSITIVE_INFINITY;
            double secondBestCost = Double.POSITIVE_INFINITY;
            double bestRegret = -1;

            for(int currentCityIndex=0; currentCityIndex < getProblem().getNumberOfCities(); currentCityIndex++){

                if(inSolution.get(currentCityIndex)){
                    continue;
                }

                City city = getProblem().getCity(currentCityIndex);
                for(int candidateIndex = 0; candidateIndex <= solution.size(); candidateIndex++){

                    double newCost = getCostAtPosition(solution, city, candidateIndex);

                    if(newCost<bestCost){
                        secondBestCost = bestCost;
                        bestCost = newCost;
                        bestCity = city;
                        bestPlacingIndex = candidateIndex;
                    } else if (newCost<secondBestCost) {
                        secondBestCost = newCost;
                    }
                }
                double regret = secondBestCost - bestCost;
                if (regret > bestRegret){
                    bestRegret = regret;
                    bestCity = city;
                }
            }
            inSolution.set(bestCity.getIndex(),true);
            solution.addCityAt(bestPlacingIndex,bestCity);
        }

        return solution;
    }
}
