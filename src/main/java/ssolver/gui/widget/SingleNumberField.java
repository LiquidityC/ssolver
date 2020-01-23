package ssolver.gui.widget;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SingleNumberField extends TextField {

    public SingleNumberField() {
        super();
        setMaxHeight(50);
        setMinHeight(50);
        setMaxWidth(50);
        setMinWidth(50);
        setAlignment(Pos.CENTER);
        setFont(Font.font("Verdana", FontWeight.BOLD, 16));

        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               if (!newValue.matches("\\d")) {
                   setText("");
               }
            }
        });
    }
}
