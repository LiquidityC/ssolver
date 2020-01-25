package ssolver.solver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A sudoku board representation that offers methods to solve the puzzle
 *
 * It's a bit of a patch-as-you-go write up. But methods have been kept under 30 loc and the flow should be
 * pretty readable although method ordering could probably be better. Not that that matters much with a modern IDE.
 */
public class Board {

    private Cell[][] cells = new Cell[9][9];

    public Board() {
        for (var i = 0; i < 9; ++i) {
            for (var j = 0; j < 9; ++j) {
                var cell = new Cell(i, j);
                cell.setValueChangeListener(this::cellChanged);
                cells[i][j] = cell;
            }
        }
    }

    private void cellChanged(int col, int row, int value) {
        if (value > 0) {
            reduceOptionsInCol(col, value);
            reduceOptionsInRow(row, value);
            reduceOptionsInBox(col, row, value);
        }
    }

    public void setNumbers(int[][] numbers) {
        for (var col = 0; col < 9; ++col) {
            for (var row = 0; row < 9; ++row) {
                cells[col][row].setValue(numbers[col][row]);
            }
        }
    }

    public int[][] getNumbers() {
        var numbers = new int[9][9];
        for (var col = 0; col < 9; ++col) {
            for (var row = 0; row < 9; ++row) {
                numbers[col][row] = cells[col][row].getValue();
            }
        }
        return numbers;
    }

    /**
     * Checks all cells and sets the value for every cell that only
     * has one possible choice.
     * @return true if anything was changed
     */
    public boolean setObviousChoices() {
        boolean changes = false;
        for (var col = 0; col < 9; col++) {
            for (var row = 0; row < 9; row++) {
                changes = cells[col][row].checkForGiven() | changes;
            }
        }
        return changes;
    }

    /**
     * Check if an option for a cell within a substructure (row, col, box)
     * is unique. If any unique is found it's applied.
     * @return true or false depending on if any changes were made
     */
    public boolean checkUniqueOptions() {
        boolean changes = false;

        // Check cols
        for (var col = 0; col < 9; col++) {
            var cells = getCellsForCol(col);
            var uniques = uniqueOptionsFor(cells);
            changes = applyUniqueOptionsFor(cells, uniques) | changes;
        }

        // Check rows
        for (var row = 0; row < 9; row++) {
            var cells = getCellsForRow(row);
            var uniques = uniqueOptionsFor(cells);
            changes = applyUniqueOptionsFor(cells, uniques) | changes;
        }

        // Check boxes
        for (var col = 0; col < 3; col++) {
            for (var row = 0; row < 3; row++) {
                var cells = getCellsForBox(col * 3, row * 3);
                var uniques = uniqueOptionsFor(cells);
                changes = applyUniqueOptionsFor(cells, uniques) | changes;
            }
        }

        return changes;
    }

    /**
     * Find unique options for the given list of cells
     * @param cells the list of cells
     * @return a list of integers that are unique options among the cells
     */
    private List<Integer> uniqueOptionsFor(List<Cell> cells) {
        var hitMap = cells.stream()
                .filter(cell -> cell.getValue() <= 0)
                .flatMap(cell -> cell.getOptions().stream())
                .collect(Collectors.groupingBy(val -> val, Collectors.counting()));

        return hitMap.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private boolean applyUniqueOptionsFor(List<Cell> cells, List<Integer> uniques) {
        for (var cell : cells) {
            for (var number : uniques) {
                if (cell.getOptions().contains(number)) {
                    cell.setValue(number);
                    return true;
                }
            }
        }
        return false;
    }

    private List<Cell> getCellsForCol(int col) {
        return Arrays.asList(cells[col]);
    }

    private List<Cell> getCellsForRow(int row) {
        return Stream.of(cells).map(col -> col[row]).collect(Collectors.toList());
    }

    private List<Cell> getCellsForBox(int col, int row) {
        col -= col % 3;
        row -= row % 3;

        var cellList = new ArrayList<Cell>();
        for (var ci = 0; ci < 3; ci++) {
            for (var ri = 0; ri < 3; ri++) {
                cellList.add(cells[col + ci][row + ri]);
            }
        }
        return cellList;
    }

    private void reduceOptionsInCol(int col, int value) {
        Stream.of(cells[col]).forEach(cell -> cell.getOptions().remove(value));
    }

    private void reduceOptionsInRow(int row, int value) {
        Stream.of(cells).map(col -> col[row]).forEach(cell -> cell.getOptions().remove(value));
    }

    private void reduceOptionsInBox(int col, int row, int value) {
        col -= col % 3;
        row -= row % 3;

        for (var ci = 0; ci < 3; ci++) {
            for (var ri = 0; ri < 3; ri++) {
                cells[col + ci][row + ri].getOptions().remove(value);
            }
        }
    }
}
