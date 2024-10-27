package put.ec.problem;

public class City {
    private double x;
    private double y;
    private double cost;
    private int index;

    public City(double x, double y, double cost, int index){
        this.setX(x);
        this.setY(y);
        this.setCost(cost);
        this.setIndex(index);
    }

    public City(String[] cityString, int index){
        this(
                Double.parseDouble(cityString[0]),
                Double.parseDouble(cityString[1]),
                Double.parseDouble(cityString[2]),
                index
        );
    }

    @Override
    public String toString(){
        return "City "+getIndex() + " at: "+getX()+", "+getY()+" Costing: "+getCost();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
