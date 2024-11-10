package put.ec.utils;

import org.json.simple.JSONObject;

public class StatKeeper {
    private final String name;
    private double sum;
    private double max;
    private double min;
    private int count;

    public StatKeeper(String name){
        this.name = name;
        clear();
    }

    public void add(double value){
        sum += value;
        setMax(value);
        setMin(value);
        count++;
    }

    public double getAverage(){
        return sum/count;
    }

    @Override
    public String toString(){
        return getName() + ": "+ getAverage()+" ("+ getMin() +"-"+ getMax() +")";
    }

    public JSONObject toJSONObject(){
//        JSONObject jsonObject = new JSONObject();

        JSONObject stats = new JSONObject();
        stats.put("min",getMin());
        stats.put("max",getMax());
        stats.put("avg",getAverage());

        return stats;
    }

    public void clear(){
        sum = 0;
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
        count = 0;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = Math.max(max, getMax());
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = Math.min(min, getMin());
    }

    public String getName() {
        return name;
    }
}
