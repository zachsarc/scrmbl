package com.example.scrmbl;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.ToggleGroup;

public class Controller {

    @FXML private RadioButton scrambledRadio, overeasyRadio, hardboiledRadio;
    @FXML private Button runButton, retrieveKeyButton;
    @FXML private TextArea inputText, outputText;
    @FXML private Circle statusLight; // optional in FXML

    private ToggleGroup modeGroup;

    @FXML
    private void initialize() {
        // 1) Make radios mutually exclusive
        modeGroup = new ToggleGroup();
        scrambledRadio.setToggleGroup(modeGroup);
        overeasyRadio.setToggleGroup(modeGroup);
        hardboiledRadio.setToggleGroup(modeGroup);

        // 2) Enable buttons only when text present AND a mode selected
        BooleanBinding inputEmpty = Bindings.createBooleanBinding(
                () -> inputText.getText() == null || inputText.getText().trim().isEmpty(),
                inputText.textProperty()
        );
        BooleanBinding noModeSelected = Bindings.isNull(modeGroup.selectedToggleProperty());
        BooleanBinding notReady = inputEmpty.or(noModeSelected);

        runButton.disableProperty().bind(notReady);
        retrieveKeyButton.disableProperty().bind(notReady);

        // 3) Status light (guard null if you remove it from FXML)
        if (statusLight != null) {
            statusLight.setFill(Color.GRAY);
            notReady.addListener((obs, oldV, isNotReady) ->
                    statusLight.setFill(isNotReady ? Color.GRAY : Color.LIMEGREEN));
        }
    }

    @FXML
    private void onScrmbl() {
        // TODO: implement
    }

    @FXML
    private void onRetrieveKey() {
        // TODO: implement
    }
}
