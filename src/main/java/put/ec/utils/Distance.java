package put.ec.utils;

import put.ec.problem.City;

public class Distance {

    public static double Euclidean(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1-x2),2.0) + Math.pow((y1-y2),2.0));
    }

    public static double Euclidean(City city1, City city2){
        return Euclidean(city1.getX(),city1.getY(), city2.getX(), city2.getY());
    }
}
