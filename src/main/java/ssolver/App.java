/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ssolver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ssolver.gui.MainPane;
import ssolver.solver.Solver;

import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class App extends Application {

    private static Stage primaryStage;
    private MainPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("ssolver");

        root = new MainPane();
        root.setSolveClickListener(this::solve);

        // Create the grid
        var scene = new Scene(root);

        URL styleRes = getClass().getResource("style.css");
        if (styleRes != null) {
            scene.getStylesheets().add(styleRes.toExternalForm());
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void solve(int[][] numbers) {
        Solver s = new Solver(numbers);
        s.setChangeObserver((int[][] newNumbers) -> {
            Platform.runLater(() -> {
                root.setBoard(newNumbers);
            });
        });
        s.setDoneObserver(() -> {
            root.enableButtons(true);
        });
        root.enableButtons(false);
        s.run();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
