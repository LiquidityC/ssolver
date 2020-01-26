package ssolver.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.layout.GridPane;
import ssolver.gui.widget.SingleNumberField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Board extends GridPane implements ChangeListener<String> {

    private static SingleNumberField[][] inputFields = new SingleNumberField[9][9];
    private Runnable boardChangedListener;

    public Board() {
        super();
        build();
    }

    private void build() {
        PseudoClass classRight = PseudoClass.getPseudoClass("right");
        PseudoClass classBottom = PseudoClass.getPseudoClass("bottom");
        for (var i = 0; i < 9; ++i) {
            for (var j = 0; j < 9; ++j) {
                var inputField = new SingleNumberField();
                inputField.getStyleClass().add("cell");
                inputField.pseudoClassStateChanged(classRight, i == 2  || i == 5);
                inputField.pseudoClassStateChanged(classBottom, j == 2  || j == 5);
                inputField.textProperty().addListener(this);
                inputFields[i][j] = inputField;
                add(inputFields[i][j], i, j);
            }
        }
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        validate();
        if (boardChangedListener != null) {
            boardChangedListener.run();
        }
    }

    private List<Integer> getRow(int row) {
        var numbers = new ArrayList<Integer>();
        for (var col = 0; col < 9; ++col) {
            try {
                numbers.add(Integer.valueOf(inputFields[col][row].getText()));
            } catch (NumberFormatException e){
                // Pass
            }
        }
        return numbers;
    }

    private List<Integer> getCol(int col) {
        return Stream.of(inputFields[col]).filter(field -> field.getText().length() > 0).map(field -> Integer.valueOf(field.getText())).collect(Collectors.toList());
    }

    public long getNumberCount() {
        return Stream.of(inputFields)
                .flatMap(Stream::of)
                .map(SingleNumberField::getText)
                .filter(not(String::isEmpty))
                .count();
    }

    /**
     * Returns the numbers in a cell where colIndex and rowIndex are positions of a single entry within
     * that cell.
     * @param colIndex
     * @param rowIndex
     * @return
     */
    private List<Integer> getBox(int colIndex, int rowIndex) {
        colIndex -= colIndex % 3;
        rowIndex -= rowIndex % 3;
        var numbers = new ArrayList<Integer>();
        for (var co = 0; co < 3; co++) {
            for (var ro = 0; ro < 3; ro++){
                try {
                    numbers.add(Integer.parseInt(inputFields[colIndex + co][rowIndex + ro].getText()));
                } catch (NumberFormatException e) {
                    // Pass
                }
            }
        }
        return numbers;
    }

    void validate() {
        // Reset validation
        PseudoClass classError = PseudoClass.getPseudoClass("error");
        Stream.of(inputFields).flatMap(Stream::of).forEach(field -> field.pseudoClassStateChanged(classError, false));

        // Validate rows and cols
        for (var i = 0; i < 9; i++) {
            var numbers = getRow(i);
            var set = numbers.stream().collect(Collectors.toSet());
            if (set.size() != numbers.size()) {
                for (var j = 0; j < 9; j++) {
                    inputFields[j][i].pseudoClassStateChanged(classError, true);
                }
            }
            numbers = getCol(i);
            set = numbers.stream().collect(Collectors.toSet());
            if (set.size() != numbers.size()) {
                for (var field : inputFields[i]) {
                    field.pseudoClassStateChanged(classError, true);
                }
            }
        }

        for (var i = 0; i < 3; ++i) {
            for (var j = 0; j < 3; ++j) {
                var col = i * 3;
                var row = j * 3;
                var numbers = getBox(col, row);
                var set = numbers.stream().collect(Collectors.toSet());
                if (set.size() != numbers.size()) {
                    for (var co = 0; co < 3; co++) {
                        for (var ro = 0; ro < 3; ro++){
                            inputFields[col + co][row + ro].pseudoClassStateChanged(classError, true);
                        }
                    }
                }
            }
        }
    }

    public void clear() {
        Stream.of(inputFields).flatMap(Stream::of).forEach(field -> field.setText(""));
    }

    public int[][] getNumbers() {
        var numbers = new int[9][9];
        for (var col = 0; col < 9; ++col) {
            for (var row = 0; row < 9; ++row) {
                try {
                    numbers[col][row] = Integer.valueOf(inputFields[col][row].getText());
                } catch (NumberFormatException e) {
                    numbers[col][row] = 0;
                }
            }
        }
        return numbers;
    }

    public void setNumbers(int[][] numbers) {
        clear();
        for (var col = 0; col < numbers.length; ++col) {
            for (var row = 0; row < numbers[col].length; ++row) {
                if (numbers[col][row] == 0) {
                    inputFields[col][row].setText("");
                } else {
                    inputFields[col][row].setText(String.valueOf(numbers[col][row]));
                }
            }
        }
    }

    public void setBoardChangedListener(Runnable boardChangedListener) {
        this.boardChangedListener = boardChangedListener;
    }

}
