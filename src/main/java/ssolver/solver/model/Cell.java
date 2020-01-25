package ssolver.solver.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Cell {

    private Set<Integer> options;
    private int value;
    private CellValueChangeListener valueChangeListener;
    private int col, row;

    public Cell(int col, int row) {
        this(col, row, 0);
    }

    public Cell(int col, int row, int value) {
        this.col = col;
        this.row = row;
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
            setValue(options.iterator().next());
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
        if (valueChangeListener != null) {
            valueChangeListener.valueChanged(col, row, value);
        }
    }

    public void setValueChangeListener(CellValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public interface CellValueChangeListener {
        public void valueChanged(int row, int col, int value);
    }
}
