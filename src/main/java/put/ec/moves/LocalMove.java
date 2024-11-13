package put.ec.moves;

abstract public class LocalMove {
    private int index1;
    private int index2;

    public LocalMove(int index1,int index2){
        this.setIndex1(index1);
        this.setIndex2(index2);
    }

    public int getIndex1() {
        return index1;
    }

    public void setIndex1(int index1) {
        this.index1 = index1;
    }

    public int getIndex2() {
        return index2;
    }

    public void setIndex2(int index2) {
        this.index2 = index2;
    }
}
