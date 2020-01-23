package ssolver.solver.model;

import java.util.HashSet;
import java.util.Set;

public class Cell {

    private Set<Integer> options;
    private int value;

    public Cell() {
        this(0);
    }

    public Cell(int value) {
        this.value = value;
        options = new HashSet<>();
        if (value == 0) {
            for (var i = 1; i < 10; ++i) {
                options.add(i);
            }
        }
    }

    public boolean checkForGiven() {
        if (value > 0) {
            return false;
        }
        if (options.size() == 1) {
            value = options.iterator().next();
            return true;
        }
        return false;
    }

    public Set<Integer> getOptions() {
        return options;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value > 0) {
            this.value = value;
            options.clear();
        }
    }
}
