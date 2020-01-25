package ssolver.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ssolver.io.BoardIO;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Pretty "hacky" gui class. Mostly here to get the visual job done. I'm not big on "designing"
 */
public class MainPane extends VBox {

    private Board board;
    private Consumer<int[][]> onSolveClick = null;
    private Button solveBtn, clearBtn;

    public MainPane() {
        build();
    }

    private void build() {
        getChildren().add(buildButtonBar());
        getChildren().add(board = new Board());
    }

    private HBox buildButtonBar() {
        var buttonBar = new HBox();
        buttonBar.setSpacing(10);
        buttonBar.setPadding(new Insets(5, 5, 5, 5));
        buttonBar.setAlignment(Pos.CENTER);

        clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("clear-btn");
        clearBtn.setOnAction(this::clearBoard);
        buttonBar.getChildren().add(clearBtn);

        var openBtn = new Button("Open");
        openBtn.setOnAction(this::loadBoard);
        buttonBar.getChildren().add(openBtn);

        var saveBtn = new Button("Save");
        saveBtn.setOnAction(this::saveBoard);
        buttonBar.getChildren().add(saveBtn);

        solveBtn = new Button("Solve");
        solveBtn.getStyleClass().add("solve-btn");
        solveBtn.setOnAction(this::solve);
        buttonBar.getChildren().add(solveBtn);
        return buttonBar;
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
        if (onSolveClick != null) {
            onSolveClick.accept(board.getNumbers());
        }
    }

    public void enableButtons(boolean b) {
        clearBtn.setDisable(!b);
        solveBtn.setDisable(!b);
    }
}

