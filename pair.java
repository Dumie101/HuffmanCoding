//package D101;

public class Pair implements Comparable<Pair> {
    // declare all required fields
    private char value;
    private double prob;

    public Pair(char v, double p) {
        value = v;
        prob = p;
    }

    public void setValue(char v) {
        value = v;
    }

    public void setProb(double p) {
        prob = p;
    }

    public char getValue() {
        return value;
    }

    public double getProb() {
        return prob;
    }


    @Override
    public int compareTo(Pair p) {
        return Double.compare(this.getProb(), p.getProb());
    }


    @Override
    public String toString() {
        return value + " : " + prob;
    }
}
