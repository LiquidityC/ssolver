package ssolver.solver.model;

public class Board {

    private Cell[][] cells = new Cell[9][9];

    public Board() {
        for (var i = 0; i < 9; ++i) {
            for (var j = 0; j < 9; ++j) {
                cells[i][j] = new Cell();
            }
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

    public boolean isComplete() {
        for (var col = 0; col < 9; ++col) {
            for (var row = 0; row < 9; ++row) {
                if (cells[col][row].getValue() ==  0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean setObviousChoices() {
        boolean changes = false;
        for (var col = 0; col < 9; col++) {
            for (var row = 0; row < 9; row++) {
                changes = cells[col][row].checkForGiven() | changes;
            }
        }
        return changes;
    }

    public void reduceAllOptions() {
        for (var col = 0; col < 9; col++) {
            for (var row = 0; row < 9; row++) {
                var value = cells[col][row].getValue();
                if (value > 0) {
                    reduceRow(row, value);
                    reduceCol(col, value);
                    reduceBox(col, row, value);
                }
            }
        }
    }

    public void reduceCol(int col, int value) {
        for (var cell : cells[col]) {
            cell.getOptions().remove(value);
        }
    }

    public void reduceRow(int row, int value) {
        for (int col = 0; col < 9; ++col) {
            cells[col][row].getOptions().remove(value);
        }
    }

    public void reduceBox(int col, int row, int value) {
        col -= col % 3;
        row -= row % 3;

        for (var ci = 0; ci < 3; ci++) {
            for (var ri = 0; ri < 3; ri++) {
                cells[col + ci][row + ri].getOptions().remove(value);
            }
        }
    }
}
