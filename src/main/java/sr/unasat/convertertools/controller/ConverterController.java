package sr.unasat.convertertools.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import sr.unasat.convertertools.audio.MorsePlayer;
import sr.unasat.convertertools.constants.TranslationOption;
import sr.unasat.convertertools.converter.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ConverterController implements Initializable {
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
    private HBox playMorseSoundButtonContainer;

    private String morseCode;

    private Task<Void> morseSoundTask;

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
}
