package put.ec.moves;

import put.ec.problem.City;
import put.ec.solution.Solution;

import java.util.Arrays;

public class InterMove extends DetailedMove {
    private City[] neighbours;
    public InterMove(City cityInside, City cityOutside, Solution solution){
        super(solution.getCityIndexInOrder(cityInside),cityOutside.getIndex(),cityInside,cityOutside,solution);
        neighbours = new City[]{solution.getPrevious(cityInside),solution.getNext(cityInside)};
    }

    public City getNext(){
        return neighbours[1];
    }

    public City getPrevious(){
        return neighbours[0];
    }

    public int getInsideCityIndex() {
        return getIndex1();
    }

    public int getOutsideCityIndex() {
        return getIndex2();
    }

    public City getCityInside(){
        return getCity1();
    }

    public City getCityOutside(){
        return getCity2();
    }

    @Override
    public int getIndex1() {
        return getSolution().getCityIndexInOrder(getCityInside());
    }

    @Override
    public int getIndex2() {
        return getCityOutside().getIndex();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        InterMove other = (InterMove) obj;
        return (getCityInside() == other.getCityInside() && getCityOutside() == other.getCityOutside());
    }

    @Override
    public int hashCode() {
        return getCityInside().hashCode() ^ getCityOutside().hashCode();
    }
}
