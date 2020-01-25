package ssolver.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ssolver.io.BoardIO;

import java.util.function.Consumer;

/**
 * Pretty "hacky" gui class. Mostly here to get the visual job done. I'm not big on "designing"
 * Pretty new to JavaFX to, so some of the solutions and flows are probably a bit "swingy"
 */
public class MainPane extends VBox {

    private Board board;
    private Consumer<int[][]> onSolveClick = null;
    private Button solveBtn, clearBtn, saveBtn;

    public MainPane() {
        build();
    }

    private void build() {
        getChildren().add(buildButtonBar());
        board = new Board();
        board.setBoardChangedListener(this::boardModified);
        getChildren().add(board);
    }

    private HBox buildButtonBar() {
        var buttonBar = new HBox();
        buttonBar.setSpacing(10);
        buttonBar.setPadding(new Insets(5, 5, 5, 5));
        buttonBar.setAlignment(Pos.CENTER);

        clearBtn = new Button("Clear");
        clearBtn.setDisable(true);
        clearBtn.getStyleClass().add("clear-btn");
        clearBtn.setOnAction(this::clearBoard);
        buttonBar.getChildren().add(clearBtn);

        var openBtn = new Button("Open");
        openBtn.setOnAction(this::loadBoard);
        buttonBar.getChildren().add(openBtn);

        saveBtn = new Button("Save");
        saveBtn.setDisable(true);
        saveBtn.setOnAction(this::saveBoard);
        buttonBar.getChildren().add(saveBtn);

        solveBtn = new Button("Solve");
        solveBtn.setDisable(true);
        solveBtn.getStyleClass().add("solve-btn");
        solveBtn.setOnAction(this::solve);
        buttonBar.getChildren().add(solveBtn);
        return buttonBar;
    }

    private void boardModified() {
        long numberCount = board.getNumberCount();
        clearBtn.setDisable(true);
        saveBtn.setDisable(true);
        solveBtn.setDisable(true);
        if (numberCount > 0) {
            clearBtn.setDisable(false);
            saveBtn.setDisable(false);
        }

        // A puzzle can't have a unique solution with less than 17
        // starting clues according to something I googled :D
        if (numberCount > 16 && numberCount < 81) {
            solveBtn.setDisable(false);
        }
    }

    public void clearBoard(ActionEvent event) {
        if (board != null) {
            board.clear();
        }
    }

    public void saveBoard(ActionEvent event) {
        if (board == null) {
            return;
        }
        BoardIO.writeBoard(board.getNumbers());
    }

    public void loadBoard(ActionEvent event) {
        if (board == null) {
            return;
        }
        board.setNumbers(BoardIO.readBoard());
    }

    public void setSolveClickListener(Consumer<int[][]> onSolveClick) {
        this.onSolveClick = onSolveClick;
    }

    public void setBoard(int[][] numbers) {
        if (board != null) {
            board.setNumbers(numbers);
        }
    }

    public void solve(ActionEvent event) {
        clearBtn.setDisable(true);
        solveBtn.setDisable(true);
        if (onSolveClick != null) {
            onSolveClick.accept(board.getNumbers());
        }
    }
}

