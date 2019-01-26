package code;

/**
 * Class representing an element of the knapsack.
 *
 * @author Doga Poyraz Tahan
 */
public class Item {
    /** Unique identifier. */
    private final int id;

    /** The value this element adds to the knapsack. */
    private final int value;

    /** The weight of the element. */
    public final int weight;

    /**
     * Constructor for the class. It sets the parameters.
     *
     * @param id The unique identifier.
     * @param value The value of the element.
     * @param weight The weight of the element.
     */

    public Item(int id, int value, int weight) {
        this.id = id;
        this.value = value;
        this.weight = weight;
    }

    public int getId() {return id;}
    public int getWeight() {return weight;}
    public int getValue() {return value;}

    public double getRatio() {return (double)value/(double)weight;}
}
