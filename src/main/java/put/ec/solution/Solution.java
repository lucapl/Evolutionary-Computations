package put.ec.solution;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<City> cityOrder;
    private TravellingSalesmanProblem problem;
    private double cost = -1;
    private double edgeLength = -1;

    public Solution(TravellingSalesmanProblem tsp){
        problem = tsp;
        cityOrder = new ArrayList<>();
    }

    public double getCostBetween(City city1, City city2){
        return problem.getCostBetween(city1,city2);
    }

    public double calculateSolutionCost(){
        double cost = 0;
        for(City city: cityOrder){
            cost += city.getCost();
        }
        return cost;
    }

    public double calculateEdgeLength(){
        City lastCity = cityOrder.getLast();
        double cost = 0;
        for(City city: cityOrder){
            if(lastCity == null){
                lastCity = city;
                continue;
            }
            cost += getCostBetween(lastCity,city);
        }
        return cost;
    }

    public double getSolutionCost(){
        if(cost < 0){
            cost = calculateSolutionCost();
        }
        return cost;
    }

    public double getEdgeLength(){
        if(edgeLength < 0){
            edgeLength = calculateEdgeLength();
        }
        return edgeLength;
    }

    public double getObjectiveFunctionValue(){
        return getSolutionCost() + getEdgeLength();
    }

    public void setCityOrder(List<City> cityOrder) {
        this.cityOrder = cityOrder;
    }

    public void addCityAt(int index, City city){
        this.cityOrder.add(index,city);
    }

    public void addCity(City city){
        this.cityOrder.add(city);
    }

    public City getCity(int index){
        return this.cityOrder.get(index);
    }

    public City getLast(){
        return this.cityOrder.getLast();
    }

    public City getFirst(){
        return this.cityOrder.getFirst();
    }


    public List<City> getCityOrder(){
        return cityOrder;
    }

    public int size(){
        return cityOrder.size();
    }

    public boolean isEmpty(){
        return cityOrder.isEmpty();
    }

    @Override
    public String toString(){
        return "Solution\n\tCost: "+getSolutionCost()+"\n\tEdge Length: "+ getEdgeLength()+"\n\tObjective function: "+getObjectiveFunctionValue();
    }
}
