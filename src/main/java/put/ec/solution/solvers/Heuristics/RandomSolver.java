package put.ec.solution.solvers.Heuristics;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomSolver extends HeuristicSolver{

    public RandomSolver(TravellingSalesmanProblem tsp){
        super(tsp);
        setName("random");
    }

    @Override
    public Solution solve(){
        Solution solution = new Solution(getProblem());

        List<City> cityOrder = new ArrayList<>();
        List<Integer> cityIndexes = new ArrayList<>();
        for (int i = 0; i<getProblem().getNumberOfCities();i++){
            cityIndexes.add(i);
        }
        Collections.shuffle(cityIndexes);

        for(int i = 0; i< getProblem().getSolutionLength();i++){
            cityOrder.add(getProblem().getCity(cityIndexes.get(i)));
        }
        solution.setCityOrder(cityOrder);

        return solution;
    }

    @Override
    public Solution solve(int startingCityIndex){
        return solve();
    }

    @Override
    public double getCostAtPosition(Solution solution, City city, int index) {
        return 0;
    }
}
