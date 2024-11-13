package put.ec.moves;

import put.ec.problem.City;
import put.ec.solution.Solution;

public class IntraMove extends LocalMove {
    private final IntraMovesType type;

    public IntraMove(City city1, City city2, Solution solution, IntraMovesType type){
        super(solution.getCityIndexInOrder(city1), solution.getCityIndexInOrder(city2));
        this.type = type;
    }

    public IntraMovesType getType() {
        return type;
    }
}
