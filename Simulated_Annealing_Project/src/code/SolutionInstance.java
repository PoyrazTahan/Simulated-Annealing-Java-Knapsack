package code;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SolutionInstance<E> implements Serializable, Cloneable{
    public int numberOfItems;
    public boolean[] solution;
    public int fitness;
    public int neighboorhoodStructure;
    public int cap;
    ArrayList<E> itemList;
    public int totalWeight;


    public SolutionInstance(){

    }

    public SolutionInstance(int numberOfItems, int sizeOfTheNeighbourhood, int capacity, ArrayList<E> items, boolean... b){
        this.numberOfItems = numberOfItems;
        solution = new boolean[numberOfItems];
        if (b == null)
           for (int i = 0; i< numberOfItems; i++)
               solution[i] = false;
        else
            solution = b;

        neighboorhoodStructure = sizeOfTheNeighbourhood;
        cap = capacity;
        itemList = items;

        fitness = calculatetotalProfit();
        totalWeight = calculateWeight();
    }

    public SolutionInstance(SolutionInstance s){
        this(s.numberOfItems, s.neighboorhoodStructure, s.cap, s.itemList, s.solution);
    }

    @Override
    public Object clone(){
        try{
            return (SolutionInstance) super.clone();
        } catch (CloneNotSupportedException e) {
            return new SolutionInstance<E>(this.numberOfItems, this.neighboorhoodStructure, this.cap, this.itemList, this.solution);
        }

    }

    public SolutionInstance deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (SolutionInstance) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    public SolutionInstance<E> findNeighbour(){
        Random r = new Random();
        int howManyToChange = r.nextInt(neighboorhoodStructure)+1;
        int randItem = r.nextInt(numberOfItems);




        ArrayList<Integer> nonTakenItemsID = getNontakenItems();
        boolean[] solutionCopy = new boolean[solution.length];
        for(int i = 0 ; i<this.solution.length; i++){
            solutionCopy[i] = this.solution[i]; // not to do a shallow copy
        }
        SolutionInstance<E> neighbor =  new SolutionInstance<E>(numberOfItems, neighboorhoodStructure, cap, itemList, solutionCopy);


        while(howManyToChange-- !=0){

            randItem = r.nextInt(nonTakenItemsID.size());

            neighbor.solution[nonTakenItemsID.get(randItem)] = true;
            neighbor.calculateWeight();

            if (neighbor.isOverWeight()){// if you are over-weight drop item
                ArrayList<Integer> takenItemsID = neighbor.getTakenItems();
                do {
                    randItem = r.nextInt(takenItemsID.size());
                    // !!! BUG: to improve make sure it is not the item you selected !!!
                    neighbor.solution[takenItemsID.get(randItem)] = false;
                    neighbor.calculateWeight();
                }while (neighbor.isOverWeight()); // until you are not over-weight
            }
        }



        return neighbor;
    }



    public boolean isOverWeight(){
        return (totalWeight > cap);
    }

    public int calculatetotalProfit(){
        int totalProfit = 0;
        for(int i = 0; i<numberOfItems;i++){
            if (solution[i]) { // if item i is taken
                totalProfit += ((Item)itemList.get(i)).getValue();
            }
        }
        return totalProfit;
    }

    public int calculateWeight(){
        int sum=0;
        for(int i = 0; i<numberOfItems;i++){
            if (solution[i]) { // if item i is taken
                sum += ((Item)itemList.get(i)).getWeight();
            }
        }
        totalWeight = sum;
        return sum;
    }


    /*  -----UTILITY FUNCTIONS------ */
    public ArrayList<Integer> getTakenItems(){
        ArrayList<Integer> a = new ArrayList<Integer>();
        for(int i = 0; i < solution.length; i++)
            if (solution[i] == true)
                a.add(i);

        return a;
    }

    public ArrayList<Integer> getNontakenItems(){
        ArrayList<Integer> a = new ArrayList<Integer>();
        for(int i = 0; i < solution.length; i++)
            if (solution[i] == false)
                a.add(i);

        return a;
    }





}
