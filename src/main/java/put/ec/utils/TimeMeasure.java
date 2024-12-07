package put.ec.utils;

public class TimeMeasure {
    private double elapsedTime;
    private long start;
    private long stop;
    private long maxTime;

    public TimeMeasure(){}
    public TimeMeasure(long maxTime){
        this.maxTime = maxTime;
    }

    public void start(){
        start = System.nanoTime();
    }

    public void stop(){
        stop = System.nanoTime();
    }

    public double getElapsedTime(){
        elapsedTime = stop-start;
        return elapsedTime;
    }

    public double getElapsedTime(int scale){
        elapsedTime = (double)(stop-start)* Math.pow(10,scale);
        return elapsedTime;
    }

    public boolean hasFinished(){
        stop();
        return getElapsedTime() >= maxTime;
    }
}
