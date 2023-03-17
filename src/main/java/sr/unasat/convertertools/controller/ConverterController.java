package sr.unasat.convertertools.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import sr.unasat.convertertools.Requirements;
import sr.unasat.convertertools.audio.MorsePlayer;
import sr.unasat.convertertools.constants.ApplicationInfo;
import sr.unasat.convertertools.constants.TranslationOption;
import sr.unasat.convertertools.converter.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ConverterController implements Initializable, Requirements {
    @FXML
    public Button clearButton;

    @FXML
    private Label versionLabel;

    @FXML
    private TextArea translationInput;

    @FXML
    private TextArea translationResult;

    @FXML
    private ComboBox<String> translateFrom;

    @FXML
    private ComboBox<String> translateTo;

    @FXML
    private Button playMorseSoundButton;

    @FXML
    private Button stopPlayingMorseSoundButton;

    @FXML
    public Button swapButton;

    @FXML
    private HBox playMorseSoundButtonContainer;

    private String morseCode;

    private Task<Void> morseSoundTask;

    public void showCredits() {
        Alert creditsAlert = new Alert(Alert.AlertType.INFORMATION);
        creditsAlert.setTitle("Credits");
        creditsAlert.setHeaderText("© ConverterTools " + ApplicationInfo.VERSION + " 2023");

        StringBuilder sb = new StringBuilder();
        sb.append("Credits:\n");

        for (String groupMember : groepsleden()) {
            sb.append(" • ").append(groupMember).append("\n");
        }

        creditsAlert.setContentText(sb.toString());
        creditsAlert.show();
    }

    public void showHelp() {
        Alert creditsAlert = new Alert(Alert.AlertType.INFORMATION);
        creditsAlert.setTitle("Help");
        creditsAlert.setHeaderText("How to use the program?");

        String helpText = """
                Instructions:
                %s
                
                Example text:
                    %s

                Example Morse code:
                    %s""".formatted(explain(), exampleString(), exampleMorseCode());

        creditsAlert.setContentText(helpText);
        creditsAlert.setHeight(500);
        creditsAlert.show();
    }

    public void resetTextInputs() {
        clear();
    }

    private static class MorseSoundPlayerExecutor {
        public static void playMorseSound(String morseText, Task<Void> task) {
            MorsePlayer morsePlayer = new MorsePlayer();
            for (String morseChar : morseText.split("")) {
                if (task.isCancelled()) {
                    break;
                }

                morsePlayer.playMorseCharacter(morseChar);
            }
            morsePlayer.stop();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        versionLabel.setText(ApplicationInfo.VERSION);

        translationInput.textProperty()
                .addListener((obs, oldText, newText) -> translate());

        translateFrom.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> translate());

        translateTo.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> translate());

        List<String> translationOptions = Arrays.stream(TranslationOption.values())
                .map(TranslationOption::getOption)
                .toList();

        translateFrom.setItems(
                FXCollections.observableArrayList(translationOptions));

        translateTo.setItems(
                FXCollections.observableArrayList(translationOptions));

        swapButton.setOnAction(actionEvent -> swap());

        stopPlayingMorseSoundButton.setManaged(false);
        stopPlayingMorseSoundButton.setVisible(false);

        playMorseSoundButtonContainer.setVisible(false);

        playMorseSoundButton.setOnAction(actionEvent -> {
            morseSoundTask = new Task<>() {
                @Override
                protected Void call() {
                    MorseSoundPlayerExecutor.playMorseSound(morseCode, this);
                    return null;
                }
            };

            morseSoundTask.setOnSucceeded(workerStateEvent -> {
                stopPlayingMorseSoundButton.setManaged(false);
                stopPlayingMorseSoundButton.setVisible(false);
                playMorseSoundButton.setManaged(true);
                playMorseSoundButton.setVisible(true);
            });

            morseSoundTask.setOnCancelled(workerStateEvent -> {
                stopPlayingMorseSoundButton.setManaged(false);
                stopPlayingMorseSoundButton.setVisible(false);
                playMorseSoundButton.setManaged(true);
                playMorseSoundButton.setVisible(true);
            });

            playMorseSoundButton.setVisible(false);
            playMorseSoundButton.setManaged(false);

            stopPlayingMorseSoundButton.setManaged(true);
            stopPlayingMorseSoundButton.setVisible(true);

            new Thread(morseSoundTask).start();
        });

        stopPlayingMorseSoundButton.setOnAction((actionEvent -> morseSoundTask.cancel()));

        displayMorseCodeSoundButton();

//        Platform.runLater(() -> translationInput.requestFocus());
    }

    private TranslationOption determineTranslationOption(String translationOption) {
        return switch (translationOption.toUpperCase()) {
            case "TEXT" -> TranslationOption.TEXT;
            case "MORSE" -> TranslationOption.MORSE_CODE;
            case "BASE64" -> TranslationOption.BASE64;
            default -> null;
        };
    }

    private Converter determineConverter(TranslationOption sourceLanguage, TranslationOption targetLanguage) {
        Converter converter;

        if (sourceLanguage == TranslationOption.TEXT && targetLanguage == TranslationOption.MORSE_CODE) {
            converter = new TextToMorseConverter();
        } else if (sourceLanguage == TranslationOption.MORSE_CODE && targetLanguage == TranslationOption.TEXT) {
            converter = new MorseToTextConverter();
        } else if (sourceLanguage == TranslationOption.MORSE_CODE && targetLanguage == TranslationOption.BASE64) {
            converter = new MorseToBase64Converter();
        } else if (sourceLanguage == TranslationOption.TEXT && targetLanguage == TranslationOption.BASE64) {
            converter = new TextToBase64Converter();
        } else if (sourceLanguage == TranslationOption.BASE64 && targetLanguage == TranslationOption.TEXT) {
            converter = new Base64ToTextConverter();
        } else if (sourceLanguage == TranslationOption.BASE64 && targetLanguage == TranslationOption.MORSE_CODE) {
            converter = new Base64ToMorseConverter();
        } else {
            converter = null;
        }

        return converter;
    }

    private void displayMorseCodeSoundButton() {
        TranslationOption sourceLanguage = determineTranslationOption(translateFrom.getValue());
        TranslationOption targetLanguage = determineTranslationOption(translateTo.getValue());

        playMorseSoundButtonContainer.setVisible(
                (sourceLanguage == TranslationOption.MORSE_CODE || targetLanguage == TranslationOption.MORSE_CODE) &&
                        (!translationInput.getText().isEmpty() || !translationResult.getText().isEmpty()));
    }

    private void detectMorseCodeTranslationOption() {
        TranslationOption sourceLanguage = determineTranslationOption(translateFrom.getValue());
        TranslationOption targetLanguage = determineTranslationOption(translateTo.getValue());

        if (sourceLanguage == TranslationOption.MORSE_CODE) {
            morseCode = translationInput.getText();
        } else if (targetLanguage == TranslationOption.MORSE_CODE) {
            morseCode = translationResult.getText();
        }
    }

    public void translate() {
        resetTranslationResult();

        TranslationOption sourceLanguage = determineTranslationOption(translateFrom.getValue());
        TranslationOption targetLanguage = determineTranslationOption(translateTo.getValue());

        displayMorseCodeSoundButton();

        if (sourceLanguage == targetLanguage) {
            translationResult.setText(translationInput.getText());
            return;
        }

        Converter converter = determineConverter(sourceLanguage, targetLanguage);

        if (converter != null) {
            try {
                String convertedValue = converter.convert(translationInput.getText());
                translationResult.setText(convertedValue);

                detectMorseCodeTranslationOption();
            } catch (Exception e) {
                errorTranslationResult(e.getMessage());
            }
        } else {
            errorTranslationResult("Converter not implemented.");
        }
    }

    private void errorTranslationResult(String message) {
        translationResult.setStyle("-fx-text-fill: red");
        translationResult.setText(message);
    }

    private void resetTranslationResult() {
        translationResult.setStyle("");
        translationResult.setText("");
        morseCode = "";
    }

    // Methods implemented from the Requirements interface set by the teacher
    @Override
    public String[] groepsleden() {
        return new String[] {
                "Rajiv Ramsing (SE/1121/046)",
                "Girish Oemrawsingh (SE/1121/038)",
                "Rohan Sitlapersad (SE/1121/052)",
                "Rochelle Pawirodimedjo (SE/1121/040)",
                "Shwasti Gopie (SE/1121/020)"
        };
    }

    @Override
    public String abs2morse(char inputChar) {
        Converter textToMorseConverter = new TextToMorseConverter();
        return textToMorseConverter.convert(String.valueOf(inputChar));
    }

    @Override
    public char morse2abc(String inputString) {
        Converter morseToTextConverter = new MorseToTextConverter();
        return morseToTextConverter.convert(inputString).charAt(0);
    }

    @Override
    public void convert() {
        translate();
    }

    @Override
    public void swap() {
        String translateFromValue = translateFrom.getValue();
        String translateToValue = translateTo.getValue();

        String translationInputValue = translationInput.getText();
        String translationResultValue = translationResult.getText();

        translateFrom.setValue(translateToValue);
        translateTo.setValue(translateFromValue);

        translationInput.setText(translationResultValue);
        translationResult.setText(translationInputValue);

        translate();
    }

    @Override
    public void clear() {
        resetTranslationResult();
        translationInput.setText("");
        translate();
    }

    @Override
    public String exampleMorseCode() {
        return "... --- ...";
    }

    @Override
    public String exampleString() {
        return "sos";
    }

    @Override
    public String explain() {
        return """
                1) Use the dropdown menus to select the source and target language to translate to.
                2) Then start by typing in the text box and you will see a live translation / decode as you type
                
                If either the source or target language was set to Morse code and some text was entered, then a button will
                appear with the option to play the morse code sound.""";
    }
}
