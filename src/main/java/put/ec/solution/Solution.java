package put.ec.solution;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.LocalSearch.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {
    private List<City> cityOrder;
    private List<Boolean> inSolution;
    private List<Integer> cityLocations;
    private TravellingSalesmanProblem problem;
    private double cost = -1;
    private double edgeLength = -1;
    private int startingCityIndex;

    public Solution(TravellingSalesmanProblem tsp){
        setProblem(tsp);
        cityOrder = new ArrayList<>();
        inSolution = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),false));
        cityLocations = new ArrayList<>(Collections.nCopies(getProblem().getNumberOfCities(),-1));
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
//        if(cost < 0){
            cost = calculateSolutionCost();
//        }
        return cost;
    }

    public double getEdgeLength(){
//        if(edgeLength < 0){
            edgeLength = calculateEdgeLength();
//        }
        return edgeLength;
    }

    public double getObjectiveFunctionValue(){
        return getSolutionCost() + getEdgeLength();
    }

    public void setCityOrder(List<City> cityOrder) {
        this.cityOrder = cityOrder;
    }

    public void addCityAt(int index, City city){
        this.cityOrder.add(loopIndex(index),city);
        this.inSolution.set(city.getIndex(),true);
    }

    public void setCityAt(int index, City city){
        this.cityOrder.set(loopIndex(index),city);
        this.cityLocations.set(city.getIndex(),loopIndex(index));
        this.inSolution.set(city.getIndex(),true);
    }

    public void addCity(City city){
        this.cityOrder.add(city);
        this.inSolution.set(city.getIndex(),true);
    }

    public void calculateCityLocations(){
        for(int i = 0; i < cityOrder.size();i++){
            City city = cityOrder.get(i);
            this.cityLocations.set(city.getIndex(),i);
        }
    }

    public void calculateInSolutions(){
        for(int i = 0; i < cityOrder.size();i++){
            City city = cityOrder.get(i);
            this.inSolution.set(city.getIndex(),true);
        }
    }

    public int loopIndex(int index){
        return (index>=0?index%size():size()-1);
    }

    public boolean checkIfNext(int index1, int index2){
        return checkDist(index1,index2,1);
    }

    public boolean checkDist(int index1, int index2, int dist){
        return loopIndex(index1+dist) == loopIndex(index2) || loopIndex(index1) == loopIndex(index2+dist);
    }

    public int getLowerIndex(int index1, int index2){
        return getLowerIndexDist(index1,index2,1);
    }

    public int getLowerIndexDist(int index1, int index2, int dist){
        if(loopIndex(index1+dist) == loopIndex(index2)){
            return index1;
        }
        return index2;
    }

    public int getCityIndexInOrder(City city){
        return cityLocations.get(city.getIndex());
    }

    public City getNeighbour(City city, int k){
        return getCity(getCityIndexInOrder(city)+k);
    }

    public City getNext(City city){
        return getNeighbour(city,1);
    }

    public City getPrevious(City city){
        return getNeighbour(city,-1);
    }

    public boolean isIn(City city){
        return this.inSolution.get(city.getIndex());
    }

    public City getCity(int index){
        return this.cityOrder.get(loopIndex(index));
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

    public void performMove(LocalMove move){
        cost = -1;
        edgeLength = -1;
        if(move instanceof InterMove interMove){
            performInterMove(interMove);
            return;
        }
        performIntraMove((IntraMove) move);
    }

    private void performInterMove(InterMove move){
        int placementIndex = move.getInsideCityIndex();
        City insideCity = getCity(placementIndex);
        City outsideCity = getProblem().getCity(move.getOutsideCityIndex());

        setCityAt(placementIndex,outsideCity);

        this.inSolution.set(insideCity.getIndex(),false);
        this.cityLocations.set(insideCity.getIndex(),-1);
    }

    private void performIntraMove(IntraMove move){
        switch (move.getType()){
            case Edges -> performIntraEdgeSwap(move);
            case Nodes -> performIntraNodeSwap(move);
        }
    }

    private void performIntraNodeSwap(IntraMove move){
        City city1 = getCity(move.getIndex1());
        City city2 = getCity(move.getIndex2());

        setCityAt(move.getIndex1(),city2);
        setCityAt(move.getIndex2(),city1);
    }

    private void performIntraEdgeSwap(IntraMove move){
        int index1 = move.getIndex1();
        int index2 = move.getIndex2();

        int edge1Start = Math.min(index1, index2);
        int edge2Start = Math.max(index1, index2);

        edge1Start += 1;
        while (edge1Start < edge2Start) {
            City temp = getCity(edge1Start);
            setCityAt(edge1Start, getCity(edge2Start));
            setCityAt(edge2Start, temp);
            edge1Start++;
            edge2Start--;
        }
    }

    public void determineMove(CandidateEdgeMove move, List<LocalMove> moves){
        City city_1 = getProblem().getCity(move.getIndex1());
        City city_2 = getProblem().getCity(move.getIndex2());

        if(isIn(city_1) && isIn(city_2)){
            if(move.getType() == IntraMovesType.Nodes){
                moves.add(new IntraMove(getPrevious(city_1), city_2,this,move.getType()));
            }
            moves.add(new IntraMove(move.getType() == IntraMovesType.Nodes?getNext(city_1):city_1, city_2, this, move.getType()));
            return;
        }
        City inCity = null;
        City outCity = null;
        if(isIn(city_1)){
            inCity = city_1;
            outCity = city_2;
        }
        if(isIn(city_2)){
            inCity = city_2;
            outCity = city_1;
        }

        if(inCity != null){
            moves.add(new InterMove(getNext(inCity), outCity, this));
            moves.add(new InterMove(getPrevious(inCity), outCity, this));
        }
    }
}
