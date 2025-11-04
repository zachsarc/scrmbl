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
import java.util.Map;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

    // Active phrase based encryptions
    private Map<Byte, Byte> encMapHB;
    private Map<Byte, Byte> decMapHB;
    private String currentPassphraseHB; // <- remember what user entered

    // Add this enum under your existing Mode enum
    private enum Flavor { SCRAMBLED, OVEREASY, HARDBOILED }

    // Which flavor radio is selected?
    private Flavor getSelectedFlavor() {
        if (scrambledRadio.isSelected())  return Flavor.SCRAMBLED;
        if (overeasyRadio.isSelected())   return Flavor.OVEREASY;
        if (hardboiledRadio.isSelected()) return Flavor.HARDBOILED;
        return null;
    }

    // Simple status helper
    private void setOkStatus()  { if (statusLight != null) statusLight.setFill(Color.LIMEGREEN); }
    private void setWarnStatus(){ if (statusLight != null) statusLight.setFill(Color.ORANGE); }
    private void setErrorStatus(){ if (statusLight != null) statusLight.setFill(Color.RED); }



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
        try {
            String in = inputText.getText();
            if (in == null || in.isEmpty()) {
                outputText.setText("Enter some text first.");
                setWarnStatus();
                return;
            }

            boolean decryptMode = isDecryptMode();

            // Require the user to have hit Retrieve Key first
            if (encMapHB == null || decMapHB == null) {
                outputText.setText("Use Retrieve Key to enter your passphrase first.");
                setWarnStatus();
                return;
            }

            if (!hardboiledRadio.isSelected()) {
                outputText.setText("Select Hard-Boiled for the HashMap cipher.");
                setWarnStatus();
                return;
            }

            if (decryptMode) {
                byte[] cipher = java.util.Base64.getDecoder().decode(in.trim());
                byte[] plain  = decryptBytesHB(cipher); // uses decMapHB
                outputText.setText(new String(plain, java.nio.charset.StandardCharsets.UTF_8));
            } else {
                byte[] plain  = in.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                byte[] cipher = encryptBytesHB(plain);  // uses encMapHB
                outputText.setText(java.util.Base64.getEncoder().encodeToString(cipher));
            }

            setOkStatus();
        } catch (IllegalArgumentException badB64) {
            outputText.setText("Input is not valid Base64 for decryption.");
            setErrorStatus();
        } catch (Exception ex) {
            outputText.setText("Error: " + ex.getMessage());
            setErrorStatus();
            ex.printStackTrace();
        }
    }





    public void doEncrypt() {
        String userText = inputText.getText();
    }


    private String encryptHardBoiled(String plain, String passphrase) {
        try {
            Map<Byte, Byte> enc = SubstitutionMap.generateMapFromPassphrase(passphrase);
            byte[] plainBytes = plain.getBytes(StandardCharsets.UTF_8);
            byte[] cipher = new byte[plainBytes.length];
            for (int i = 0; i < plainBytes.length; i++) {
                cipher[i] = enc.get(plainBytes[i]);
            }
            return Base64.getEncoder().encodeToString(cipher);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    private String decryptHardBoiled(String cipher, String passphrase) {
        try {
            Map<Byte, Byte> enc = SubstitutionMap.generateMapFromPassphrase(passphrase);
            Map<Byte, Byte> dec = SubstitutionMap.invertMap(enc);
            byte[] cipherBytes = Base64.getDecoder().decode(cipher);
            byte[] plain = new byte[cipherBytes.length];
            for (int i = 0; i < cipherBytes.length; i++) {
                plain[i] = dec.get(cipherBytes[i]);
            }
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }


    // Graph-based (Over-Easy)
    private String encryptOverEasy(String plain, String key) {
        // TODO: construct cycle graph / pseudo-random walk from key
        return "[OE encrypt stub] " + plain;
    }
    private String decryptOverEasy(String cipher, String key) {
        // TODO: walk inverse path
        return "[OE decrypt stub] " + cipher;
    }

    // Tree-based (Scrambled)
    private String encryptScrambled(String plain, String key) {
        // TODO: build shuffled BST from key insertion order
        return "[SC encrypt stub] " + plain;
    }
    private String decryptScrambled(String cipher, String key) {
        // TODO: reverse traversal using the same BST
        return "[SC decrypt stub] " + cipher;
    }

    private String encrypt(String plain, Flavor f, String key) {
        switch (f) {
            case HARDBOILED: return encryptHardBoiled(plain, key);
            case OVEREASY:   return encryptOverEasy(plain, key);
            case SCRAMBLED:  return encryptScrambled(plain, key);
            default:         return plain;
        }
    }
    private String decrypt(String cipher, Flavor f, String key) {
        switch (f) {
            case HARDBOILED: return decryptHardBoiled(cipher, key);
            case OVEREASY:   return decryptOverEasy(cipher, key);
            case SCRAMBLED:  return decryptScrambled(cipher, key);
            default:         return cipher;
        }
    }

    private void setMapsFromPassphrase(String passphrase) throws Exception {
        encMapHB = SubstitutionMap.generateMapFromPassphrase(passphrase);
        decMapHB = SubstitutionMap.invertMap(encMapHB);
    }

    private boolean ensureMapsPresentForMode(boolean decrypt) {
        boolean ok = (decrypt ? decMapHB != null : encMapHB != null);
        if (!ok) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setHeaderText("Key required");
            a.setContentText("Use Retrieve Key to set the passphrase before you " + (decrypt ? "decrypt." : "encrypt."));
            a.showAndWait();
        }
        return ok;
    }

    private byte[] encryptBytesHB(byte[] plain) {
        byte[] out = new byte[plain.length];
        for (int i = 0; i < plain.length; i++) {
            // encMapHB contains all 256 keys, so get() will not be null
            out[i] = encMapHB.get(plain[i]);
        }
        return out;
    }

    private byte[] decryptBytesHB(byte[] cipher) {
        byte[] out = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i++) {
            out[i] = decMapHB.get(cipher[i]);
        }
        return out;
    }

    @FXML
    public void onRetrieveKey() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Retrieve Key");
        dlg.setHeaderText("Enter passphrase");
        dlg.setContentText("Passphrase:");

        dlg.showAndWait().ifPresent(passphrase -> {
            try {
                if (passphrase.trim().isEmpty()) {
                    outputText.setText("Passphrase cannot be empty.");
                    setWarnStatus();
                    return;
                }
                currentPassphraseHB = passphrase; // remember it
                encMapHB = SubstitutionMap.generateMapFromPassphrase(passphrase);
                decMapHB = SubstitutionMap.invertMap(encMapHB);
                outputText.setText("Key ready. You can now encrypt or decrypt.");
                setOkStatus();
            } catch (Exception ex) {
                outputText.setText("Error generating key: " + ex.getMessage());
                setErrorStatus();
                ex.printStackTrace();
            }
        });
    }



}
