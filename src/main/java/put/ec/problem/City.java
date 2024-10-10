package put.ec.problem;

public class City {
    private double x;
    private double y;
    private double cost;

    public City(double x, double y, double cost){
        this.setX(x);
        this.setY(y);
        this.setCost(cost);
    }

    public City(String[] cityString){
        this(
                Double.parseDouble(cityString[0]),
                Double.parseDouble(cityString[1]),
                Double.parseDouble(cityString[2])
        );
    }

    @Override
    public String toString(){
        return "City at: "+getX()+", "+getY()+" Costing: "+getCost();
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
}
