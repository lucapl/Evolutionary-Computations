package put.ec.problem;

import put.ec.utils.Distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TravellingSalesmanProblem {
    private List<City> cities;
    private String name;
    private ArrayList<ArrayList<Double>> distanceMatrix;
    private final int initialSize = 200;

    public TravellingSalesmanProblem(String name){
        cities = new ArrayList<>(initialSize);
        createDistanceMatrix();
        this.name = name;
    }

    private void createDistanceMatrix(){
        distanceMatrix = new ArrayList<>(initialSize);
        for(int i = 0; i<initialSize; i++){
            distanceMatrix.add(new ArrayList<>(Collections.nCopies(initialSize,-1.0)));
        }
    }

    public void calculateDistanceMatrix(){
        for(int city1Index = 0; city1Index < getNumberOfCities(); city1Index++){
            City city1 = cities.get(city1Index);
            for(int city2Index = city1Index+1; city2Index < getNumberOfCities(); city2Index++){
                City city2 = cities.get(city2Index);
                getCostBetween(city1,city2);
            }
        }
    }

    public void addCity(City city){
        cities.add(city);
    }

    public City getCity(int index){
        return cities.get(index);
    }

    public int getNumberOfCities(){
        return cities.size();
    }

    public int getSolutionLength(){
        return (int)Math.ceil((double)cities.size()/2);
    }

    public double getCostBetween(City city1, City city2){
        double value = distanceMatrix.get(city1.getIndex()).get(city2.getIndex());
        if (value == -1.0){
            value = Math.round(Distance.Euclidean(city1,city2));
            distanceMatrix.get(city1.getIndex()).set(city2.getIndex(),value);
        }
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
