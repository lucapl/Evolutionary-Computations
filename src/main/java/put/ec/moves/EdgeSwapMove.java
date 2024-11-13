package put.ec.moves;

import put.ec.problem.City;
import put.ec.solution.Solution;

public class EdgeSwapMove extends IntraMove {
    private final City[] edge1;
    private final City[] edge2;

    public EdgeSwapMove(City city1, City city2, Solution solution){
        super(city1, city2, solution, IntraMovesType.Edges);
        edge1 = new City[]{city1, solution.getNext(city1)};
        edge2 = new City[]{city2, solution.getNext(city2)};
    }

    public City[] getEdge1() {
        return edge1;
    }

    public City[] getEdge2() {
        return edge2;
    }
}
