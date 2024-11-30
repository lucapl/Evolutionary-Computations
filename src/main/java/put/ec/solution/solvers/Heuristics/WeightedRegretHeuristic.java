package put.ec.solution.solvers.Heuristics;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.*;

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

        solution.addCity(getProblem().getCity(startingCityIndex));

        for(int i = solution.size(); i<getProblem().getSolutionLength(); i++){
            City bestCity = null;
            int bestRegretPlacingIndex = -1;
            double objective = Double.NEGATIVE_INFINITY;

            for(City city: getProblem().getCities()){
                double bestCost = Double.POSITIVE_INFINITY;
                double secondBestCost = Double.POSITIVE_INFINITY;
                int bestPlacingIndex = -1;

                if(solution.isIn(city)){
                    continue;
                }

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

    public Solution solve_solution(Solution solution) {
        // Create a set to track cities already in the solution
        Set<City> citiesInSolution = new HashSet<>(solution.getCityOrder());

        // Iterate until the solution reaches the required size
        while (solution.size() < getProblem().getSolutionLength()) {
            City bestCity = null;
            int bestRegretPlacingIndex = -1;
            double bestValue = Double.NEGATIVE_INFINITY;

            // Evaluate each city not in the solution
            for (City city : getProblem().getCities()) {
                if (citiesInSolution.contains(city)) {
                    continue; // Skip cities already in the solution
                }

                double bestCost = Double.POSITIVE_INFINITY;
                double secondBestCost = Double.POSITIVE_INFINITY;
                int bestPlacingIndex = -1;

                // Evaluate all possible positions to insert the city
                for (int candidateIndex = 0; candidateIndex <= solution.size(); candidateIndex++) {
                    double newCost = getCostAtPosition(solution, city, candidateIndex);

                    // Update best and second-best costs for regret calculation
                    if (newCost < bestCost) {
                        secondBestCost = bestCost;
                        bestCost = newCost;
                        bestPlacingIndex = candidateIndex;
                    } else if (newCost < secondBestCost) {
                        secondBestCost = newCost;
                    }
                }

                // Calculate regret and the objective value for this city
                double regret = secondBestCost - bestCost;
                double value = getWeighted(regret, bestCost);

                // Update the best city and position if the current city has a better value
                if (value > bestValue) {
                    bestValue = value;
                    bestCity = city;
                    bestRegretPlacingIndex = bestPlacingIndex;
                }
            }

            // Add the best city to the solution at the identified position
            if (bestCity != null) {
                solution.addCityAt(bestRegretPlacingIndex, bestCity);
                citiesInSolution.add(bestCity); // Track this city as added
            } else {
                System.err.println("No valid city found to add. This should not happen.");
                break; // Exit to prevent an infinite loop
            }
        }

        // Final validation to ensure the solution is complete and unique
        if (solution.size() != getProblem().getSolutionLength()) {
            System.err.println("The solution is incomplete.");
        }

        if (new HashSet<>(solution.getCityOrder()).size() != solution.getCityOrder().size()) {
            System.err.println("Duplicate cities detected in the final solution!");
        }

        return solution;
    }

}
