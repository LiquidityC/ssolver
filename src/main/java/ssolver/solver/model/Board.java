package ssolver.solver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {

    private Cell[][] cells = new Cell[9][9];

    public Board() {
        for (var i = 0; i < 9; ++i) {
            for (var j = 0; j < 9; ++j) {
                var cell = new Cell();
                cell.setValueChangeListener(this::cellChanged);
                cells[i][j] = cell;
            }
        }
    }

    private void cellChanged(int value) {
        if (value > 0) {
            reduceAllOptions();
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

    public boolean checkUniqueOptions() {
        boolean changes = false;
        for (var col = 0; col < 9; col++) {
            var cells = getCellsForCol(col);
            var uniques = uniqueOptionsFor(cells);
            for (var cell : cells) {
                for (var option : uniques) {
                    if (cell.getOptions().contains(option)) {
                        cell.setValue(option);
                        changes = true;
                    }
                }
            }
        }
        for (var row = 0; row < 9; row++) {
            var cells = getCellsForRow(row);
            var uniques = uniqueOptionsFor(cells);
            for (var cell : cells) {
                for (var option : uniques) {
                    if (cell.getOptions().contains(option)) {
                        cell.setValue(option);
                        changes = true;
                    }
                }
            }
        }

        for (var col = 0; col < 3; col++) {
            for (var row = 0; row < 3; row++) {
                var cells = getCellsForBox(col * 3, row * 3);
                var uniques = uniqueOptionsFor(cells);
                for (var cell : cells) {
                    for (var option : uniques) {
                        if (cell.getOptions().contains(option)) {
                            cell.setValue(option);
                            changes = true;
                        }
                    }
                }
            }
        }

        return changes;
    }

    private List<Integer> uniqueOptionsFor(List<Cell> cells) {
        var hitMap = new HashMap<Integer, Integer>();
        for (var cell : cells) {
            if (cell.getValue() > 0) {
                continue;
            }
            for (var option : cell.getOptions()) {
                Integer count = hitMap.getOrDefault(option, 0);
                hitMap.put(option, count + 1);
            }
        }

        return hitMap.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Cell> getCellsForCol(int col) {
        return Arrays.asList(cells[col]);
    }

    public List<Cell> getCellsForRow(int row) {
        var cellList = new ArrayList<Cell>();
        for (var col = 0; col < 9; col++) {
            cellList.add(cells[col][row]);
        }
        return cellList;
    }

    public List<Cell> getCellsForBox(int col, int row) {
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
