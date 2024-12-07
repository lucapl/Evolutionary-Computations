package put.ec.moves;

import put.ec.problem.City;
import put.ec.solution.Solution;

import java.util.Arrays;

public class EdgeSwapMove extends IntraMove {
    private City[] edge1;
    private City[] edge2;

    public EdgeSwapMove(City city1, City city2, Solution solution){
        super(city1, city2, solution, IntraMovesType.Edges);
        edge1 = new City[]{city1, solution.getNext(city1)};
        edge2 = new City[]{city2, solution.getNext(city2)};
    }

    public EdgeSwapMove(EdgeSwapMove other, Solution solution){
        this(other.getCity1(),other.getCity2(),solution);
    }

    public City[] getEdge1() {
        return edge1;
    }

    public void invertEdge1(){
        edge1[0] = edge1[1];
        edge1[1] = getCity1();
        setCity1(edge1[0]);
    }

    public City[] getEdge2() {
        return edge2;
    }

    public void invertEdge2(){
        edge2[0] = edge2[1];
        edge2[1] = getCity2();
        setCity2(edge2[0]);
    }

    public void invert(){
        City[] temp = edge1;
        edge1 = edge2;
        edge2 = temp;
        invertEdge2();
        invertEdge1();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        EdgeSwapMove other = (EdgeSwapMove) obj;
        return (Arrays.equals(this.edge1, other.edge1) && Arrays.equals(this.edge2, other.edge2))
                || (Arrays.equals(this.edge1, other.edge2) && Arrays.equals(this.edge2, other.edge1));
    }

    @Override
    public int hashCode() {
        int hashEdge1 = Arrays.hashCode(edge1);
        int hashEdge2 = Arrays.hashCode(edge2);

        return hashEdge1 ^ hashEdge2;
    }
}
