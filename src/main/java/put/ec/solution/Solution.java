package put.ec.solution;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {
    private List<City> cityOrder;
    private List<Boolean> inSolution;
    private TravellingSalesmanProblem problem;
    private double cost = -1;
    private double edgeLength = -1;
    private int startingCityIndex;

    public Solution(TravellingSalesmanProblem tsp){
        setProblem(tsp);
        cityOrder = new ArrayList<>();
        inSolution = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),false));
    }

    public double getCostBetween(City city1, City city2){
        return getProblem().getCostBetween(city1,city2);
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
            lastCity = city;
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
        this.inSolution.set(city.getIndex(),true);
    }

    public void addCity(City city){
        this.cityOrder.add(city);
        this.inSolution.set(city.getIndex(),true);
    }

    public boolean isIn(City city){
        return this.inSolution.get(city.getIndex());
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

    public int getStartingCityIndex() {
        return startingCityIndex;
    }

    public void setStartingCityIndex(int startingCityIndex) {
        this.startingCityIndex = startingCityIndex;
    }

    public TravellingSalesmanProblem getProblem() {
        return problem;
    }

    public void setProblem(TravellingSalesmanProblem problem) {
        this.problem = problem;
    }
}
