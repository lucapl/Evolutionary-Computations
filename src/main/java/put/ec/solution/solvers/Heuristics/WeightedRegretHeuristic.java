package put.ec.solution.solvers.Heuristics;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightedRegretHeuristic extends GreedyCycleSolver{
    private double costWeight;
    private double regretWeight;

    public WeightedRegretHeuristic(TravellingSalesmanProblem tsp, double costWeight, double regretWeight) {
        super(tsp);
        setName("weightedRegretHeuristic");
        setWeights(costWeight,regretWeight);
    }

    public Solution solve(int startingCityIndex) {
        Solution solution = new Solution(getProblem());
        List<Boolean> inSolution = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),false));

        solution.addCity(getProblem().getCity(startingCityIndex));
        inSolution.set(startingCityIndex,true);

        for(int i = solution.size(); i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
            int bestRegretPlacingIndex = -1;
            double objective = Double.NEGATIVE_INFINITY;

            for(int currentCityIndex=0; currentCityIndex < getProblem().getNumberOfCities(); currentCityIndex++){
                double bestCost = Double.POSITIVE_INFINITY;
                double secondBestCost = Double.POSITIVE_INFINITY;
                int bestPlacingIndex = -1;

                if(inSolution.get(currentCityIndex)){
                    continue;
                }

                City city = getProblem().getCity(currentCityIndex);
                for(int candidateIndex = 0; candidateIndex <= solution.size(); candidateIndex++){

                    double newCost = getCostAtPosition(solution, city, candidateIndex);

                    if(newCost<bestCost){
                        secondBestCost = bestCost;
                        bestCost = newCost;
                        bestPlacingIndex = candidateIndex;
                    } else if (newCost<secondBestCost) {
                        secondBestCost = newCost;
                    }
                }
                double regret = secondBestCost - bestCost;
                double value = getWeighted(regret,bestCost);

                if (value > objective){
                    objective = value;
                    bestCity = city;
                    bestRegretPlacingIndex = bestPlacingIndex;
                }
            }
            inSolution.set(bestCity.getIndex(),true);
            solution.addCityAt(bestRegretPlacingIndex,bestCity);
        }

        return solution;
    }

    public double getWeighted(double regret,double cost){
        return getCostWeight()*(-cost) + getRegretWeight()*(regret);
    }

    public double getCostWeight() {
        return costWeight;
    }

    public void setWeights(double costWeight, double regretWeight) {
        double sum = costWeight + regretWeight;
        this.costWeight = costWeight/sum;
        this.regretWeight = regretWeight/sum;
    }

    public double getRegretWeight() {
        return regretWeight;
    }
}
