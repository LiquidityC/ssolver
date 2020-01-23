package ssolver.io;

import javafx.stage.FileChooser;
import ssolver.App;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Pretty ugly and unsafe class for reading and writing soduko boards to disk
 * Not the main task, just a quikc IO hack
 */
public class BoardIO {

    public static int[][] readBoard() {
        var chooser = new FileChooser();
        chooser.setTitle("Select a file to load");
        var file = chooser.showOpenDialog(App.getPrimaryStage());
        var numbers = new int[9][9];

        // This whole logic is pretty unsafe and "assuming".
        // But it's not the "big part" of the software so to speak
        try (var reader = new BufferedReader(new FileReader(file))) {
            int col = 0, row = 0;
            int c;
            while ((c = reader.read()) != -1) {
                char ch = (char) c;
                if (ch == '\n') {
                    continue;
                }
                numbers[col][row] = Character.getNumericValue(ch);
                ++col;
                if (col >= 9) {
                    row++;
                    col = 0;
                }
                if (row >= 9) {
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return numbers;
    }

    public static void writeBoard(int[][] numbers) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open sudoku file");
        var file = chooser.showSaveDialog(App.getPrimaryStage());

        try (var writer = new FileWriter(file)) {
            for (var row = 0; row < numbers.length; ++row) {
                for (var col = 0; col < numbers.length; ++col) {
                    writer.append(String.valueOf(numbers[col][row]));
                }
                writer.append('\n');
            }
        } catch (IOException e) {
            // Lazy handling
            e.printStackTrace();
        }
    }
}
