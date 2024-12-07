package put.ec.utils;

public class TimeMeasure {
    private long elapsedTime;
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

    public long getElapsedTime(){
        elapsedTime = stop-start;
        return elapsedTime;
    }

    public double getElapsedTime(int scale){
        return (double)getElapsedTime()* Math.pow(10,scale);
    }

    public boolean hasFinished(){
        stop();
        return getElapsedTime() >= maxTime;
    }
}
