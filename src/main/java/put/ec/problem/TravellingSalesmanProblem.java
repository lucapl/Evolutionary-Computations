package put.ec.problem;

import put.ec.utils.Distance;

import java.util.ArrayList;
import java.util.List;

public class TravellingSalesmanProblem {
    private List<City> cities;

    public TravellingSalesmanProblem(){
        cities = new ArrayList<>();
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
}
