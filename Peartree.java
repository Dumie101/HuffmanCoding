//package D101;

import java.util.LinkedList;
import java.util.Queue;

public class Peartree implements Comparable<Peartree>{
    // declare all required fields
    private Pair data;
    private Peartree parent;
    private Peartree left;
    private Peartree right;

    /**
     Constructor 1
     No arg constructor
     */
    public Peartree(){
        parent = left = right = null;
        data = null;
    }

    /**
     Constructor 2
     Pair data given
     */
    public Peartree(Pair p){
        parent = left = right = null;
        data = p;
    }

    public void setData(Pair data){
        this.data = data;
    }

    public void setLeft(Peartree pt){
        left = pt;
    }

    public void setRight(Peartree pt){
        right = pt;
    }

    public void setParent(Peartree pt){
        parent = pt;
    }

    public Pair getData(){
        return data;
    }

    public Peartree getParent(){
        return parent;
    }

    public Peartree getLeft(){
        return left;
    }

    public Peartree getRight(){
        return right;
    }

    public void attachLeft(Peartree pt){
        if (pt==null)
            return;
        else if (left!=null || pt.getParent()!=null){
            System.out.println("Can't attach");
            return;
        }
        else{
            pt.setParent(this);
            this.setLeft(pt);
        }
    }

    public void attachRight(Peartree pt){
        if (pt==null)
            return;
        else if (right!=null || pt.getParent()!=null){
            System.out.println("Can't attach");
            return;
        }
        else{
            pt.setParent(this);
            this.setRight(pt);
        }
    }

    // this method simplifies implementation of the Huffman Coding algorithm
    public double getProb(){
        return data.getProb();
    }

    // this method simplifies implementation of the Huffman Coding algorithm
    public char getValue(){
        return data.getValue();
    }

    /**
     The compareTo method overrides the compareTo method of the Comparable
     interface.
     */
    @Override
    public int compareTo(Peartree pt){
        return Double.compare(this.getProb(), pt.getProb());
    }

    // this method uses a modified BFS to print the data associated with all
    // nodes/trees in level order
    public static void levelorder(Peartree pt){
        if(pt==null)
            return;
        // initialize Queue to store references to Peartrees and
        // preload agenda
        Queue<Peartree> q = new LinkedList<Peartree>();
        q.add(pt);

        while(!q.isEmpty()){
            Peartree tmp = q.remove();
            System.out.print(tmp.getData() +" ");
            if(tmp.getLeft()!=null)
                q.add(tmp.getLeft());
            if(tmp.getRight()!=null)
                q.add(tmp.getRight());
        }
    }
}
