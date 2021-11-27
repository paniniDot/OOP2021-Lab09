package it.unibo.oop.lab.reactivegui02;

/**
 * 
 * Simple Counter.
 */
public class Counter {
    private int current;

    /**
     * 
     * Constructor.
     */
    public Counter() {
        this.current = 0;
    }

    /**
     * 
     * Increments the current value.
     */
    public void increment() {
        this.current++;
    }

    /**
     *
     * @return the current value.
     */
    public int getValue() {
        return this.current;
    }
}
