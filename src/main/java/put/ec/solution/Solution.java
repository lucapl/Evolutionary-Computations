package put.ec.solution;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.utils.Distance;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<City> cityOrder;
    private TravellingSalesmanProblem problem;
    private double cost = -1;

    public Solution(TravellingSalesmanProblem tsp){
        problem = tsp;
        cityOrder = new ArrayList<>();
    }

    public double getCostBetween(City city1, City city2){
        return problem.getCostBetween(city1,city2);
    }

    public double getNewCityCost(City newCity,int index) throws IllegalArgumentException{
        if (index < 0 || index >= cityOrder.size()+1){
            throw new IllegalArgumentException("Index out of bounds");
        }

        double cost = newCity.getCost();

        if (cityOrder.isEmpty()){
            return cost;
        }

        City cityInPlace = cityOrder.get(index);

        cost += getCostBetween(cityInPlace,newCity);

        if (index < cityOrder.size()-1){
            City nextCity = cityOrder.get(index+1);
            cost += getCostBetween(newCity,nextCity);
        }

        return cost;
    }

    public double calculateSolutionCost(){
        City lastCity = null;
        double cost = 0;
        for(City city: cityOrder){
            cost += city.getCost();
            if(lastCity == null){
                lastCity = city;
                continue;
            }
            cost += getCostBetween(lastCity,city);
        }
        return cost;
    }

    public double getSolutionCost(){
        if(cost >= 0){
            return cost;
        }
        cost = calculateSolutionCost();
        return cost;
    }

    public void setCityOrder(List<City> cityOrder) {
        this.cityOrder = cityOrder;
    }
}
