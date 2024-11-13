package put.ec.moves;

import put.ec.problem.City;
import put.ec.solution.Solution;

public class InterMove extends LocalMove {
    public InterMove(City cityInside, City cityOutside, Solution solution){
        super(solution.getCityIndexInOrder(cityInside),cityOutside.getIndex(),cityInside,cityOutside);
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
}
