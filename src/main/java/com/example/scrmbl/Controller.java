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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

public class Controller {

    @FXML private RadioButton scrambledRadio, overeasyRadio, hardboiledRadio;
    @FXML private Button runButton, retrieveKeyButton;
    @FXML private TextArea inputText, outputText;
    @FXML private Circle statusLight; // optional in FXML
    @FXML private Label modeLabel;
    @FXML private ToggleButton modeToggle;
    @FXML private ToggleGroup modeGroup;

    private enum Mode { ENCRYPT, DECRYPT }
    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.ENCRYPT);


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

        // Toggle behavior: not selected = ENCRYPT, selected = DECRYPT
        modeToggle.selectedProperty().addListener((obs, oldSel, sel) -> {
            mode.set(sel ? Mode.DECRYPT : Mode.ENCRYPT);
        });

        // Keep the UI texts in sync with state
        mode.addListener((obs, oldM, newM) -> {
            boolean decrypt = newM == Mode.DECRYPT;
            modeToggle.setSelected(decrypt); // keeps toggle and state consistent
            modeLabel.setText("Mode: " + (decrypt ? "Decrypt" : "Encrypt"));
            modeToggle.setText(decrypt ? "Switch to Encrypt" : "Switch to Decrypt");
        });

        // Initialize label/toggle text once
        mode.set(Mode.ENCRYPT);
    }

    private boolean isDecryptMode() {
        return mode.get() == Mode.DECRYPT;
    }


    @FXML
    public void onScrmbl() {
        /*
        if (isDecryptMode()) {
            doDecrypt(); // your existing method
        } else {
            doEncrypt(); // your existing method
        }
         */
        //asd
    }

    @FXML
    public void onRetrieveKey() {
        /*
        if (isDecryptMode()) {
            retrieveKeyForCiphertext(); // your existing or new helper
        } else {
            retrieveKeyForPlaintext();  // your existing or new helper
        }
         */
    }
}
