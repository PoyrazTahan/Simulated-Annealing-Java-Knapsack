package code;


import java.io.FileNotFoundException;
import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;


public class main {

    private static int numberOfItems = 100;
    private static int knapsakCap = 995;
    private static double alpha = 0.9999;
    private static int neighborhoodStructure =4;

    public static ArrayList<Item> items = new ArrayList<Item>();

    private static final double INITIAL_TEMPERATURE = 2000000; // For 24 Item: 2000000
    private static final double FINAL_TEMPERATURE = 0.005;

    //--------INITIALIZING PART METHODS--------//
    private static void readItems() throws FileNotFoundException {

        String fileName = "Instance_" + numberOfItems + "_" + knapsakCap + ".txt" ;



        Scanner input;
        //open x file and get x coordinates of cities and create city objects and set x and index values
        input = new Scanner(new File("./"  +fileName));

        input.useDelimiter("\t|\r");

        int counter = 1;
        // char c = input.next().charAt(0);
        while (input.hasNext()) {
            int profit = input.nextInt();
            int weight = input.nextInt();

            Item i = new Item(counter, profit, weight);

            counter++;
            items.add(i);
        }

    }



    public static boolean isNeighbourAccapted(SolutionInstance current, SolutionInstance neighbor, double temperature){

        int delta = current.fitness - neighbor.fitness;
        if  (delta<0)
            return true;
        return Math.random() < Math.exp(-1*delta/temperature);
    }

    public static void main(String [ ] args)
    {
        /* READ ITEMS */
        try{
            readItems();
        }catch (FileNotFoundException e){

            e.printStackTrace();
        }
        /* Finish Reading Items */
        /* -------------------- */
        double temperature = INITIAL_TEMPERATURE;
        SolutionInstance<Item> current = new SolutionInstance<>(numberOfItems,1,knapsakCap, items, null);
        ArrayList<Integer> solutionKey = current.getTakenItems();
        int bestFitness = current.fitness;

        int counter = 0;
        while (temperature > FINAL_TEMPERATURE){
            counter++;
            //boolean[] b = current.findNeighbour().solution;
            SolutionInstance<Item> neighbor = new SolutionInstance<>
                    (numberOfItems,neighborhoodStructure ,knapsakCap, items, current.findNeighbour().solution);
            if (isNeighbourAccapted(current, neighbor, temperature))
                current = neighbor;


            temperature *= alpha;
            // temperature = INITIAL_TEMPERATURE/Math.log(counter+1);
            // temperature = Math.exp(-1*alpha*counter)*INITIAL_TEMPERATURE;
            // temperature =
            /*
            Literature on SA galore with alteratives:
            T_(k+1)= α T_k [Dosso,Oldenburg, 1991]
            T_(k+1)= T_0/log⁡(k+1) [Geman and Geman]
            T_k= e^(-αk).T_0
            T_k=(T_1/T_0 ) ^k ,(T_1/T_(0 ) )=0.9 [Kirptrick, 1983] ??
             */
            if (bestFitness < current.fitness){
                bestFitness = current.fitness;
                solutionKey = current.getTakenItems();
            }

            System.out.println(String.format("%3d", counter) + ": " + String.format("T= %3.1f", temperature) + ": " +
                    String.format("Value= %10d", current.fitness) + ": " + String.format("Weight= %10d", current.totalWeight) + ": " + current.getTakenItems());



        }

        System.out.println("---------------------------------\n\n");
        System.out.println(String.format("%10d", current.fitness) + ": " + current.getTakenItems());
        System.out.println(String.format("%10d", bestFitness) + ": " + solutionKey);



    }


}
