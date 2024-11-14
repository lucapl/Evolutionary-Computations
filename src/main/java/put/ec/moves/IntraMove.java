package put.ec.moves;

import put.ec.problem.City;
import put.ec.solution.Solution;

public class IntraMove extends DetailedMove {
    private final IntraMovesType type;

    public IntraMove(City city1, City city2, Solution solution, IntraMovesType type){
        super(solution.getCityIndexInOrder(city1), solution.getCityIndexInOrder(city2),city1,city2,solution);
        this.type = type;
    }

    public IntraMovesType getType() {
        return type;
    }

    @Override
    public int getIndex1() {
        return getSolution().getCityIndexInOrder(getCity1());
    }

    @Override
    public int getIndex2() {
        return getSolution().getCityIndexInOrder(getCity2());
    }
}
