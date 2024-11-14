package put.ec.moves;

import put.ec.problem.City;
import put.ec.solution.Solution;

abstract public class DetailedMove extends  LocalMove{
    private City city1;
    private City city2;
    private Solution solution;


    public DetailedMove(City city1, City city2, Solution solution){
        this(-1,-1,city1,city2,solution);
    }

    public DetailedMove(int i,int j,City city1, City city2, Solution solution){
        super(i,j);
        this.setCity1(city1);
        this.setCity2(city2);
        this.setSolution(solution);
    }

    public City getCity1() {
        return city1;
    }

    public void setCity1(City city1) {
        this.city1 = city1;
    }

    public City getCity2() {
        return city2;
    }

    public void setCity2(City city2) {
        this.city2 = city2;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}
