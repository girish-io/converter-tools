package sr.unasat.convertertools;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sr.unasat.convertertools.converter.*;
import sr.unasat.convertertools.exception.Base64Exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestConverter {
    @Test
    @DisplayName("Text to morse")
    public void textToMorse() {
        Converter textToMorseConverter = new TextToMorseConverter();

        String expected = ".... . .-.. .-.. --- --..-- / .-- --- .-. .-.. -.. -.-.--";
        String actual = textToMorseConverter.convert("Hello, world!");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Text to base64")
    public void textToBase64() {
        Converter textToBase64Converter = new TextToBase64Converter();

        String expected = "SGVsbG8sIHdvcmxkIQ==";
        String actual = textToBase64Converter.convert("Hello, world!");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Morse to text")
    public void morseToText() {
        Converter morseToTextConverter = new MorseToTextConverter();

        String expected = "HELLO, WORLD!";
        String actual = morseToTextConverter.convert(".... . .-.. .-.. --- --..-- / .-- --- .-. .-.. -.. -.-.--");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Morse to base64")
    public void morseToBase64() {
        Converter morseToBase64Converter = new MorseToBase64Converter();

        String expected = "SEVMTE8sIFdPUkxEIQ==";
        String actual = morseToBase64Converter.convert(".... . .-.. .-.. --- --..-- / .-- --- .-. .-.. -.. -.-.--");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Base64 to text")
    public void base64ToText() {
        Converter base64ToTextConverter = new Base64ToTextConverter();

        String expected = "Hello, world!";
        String actual = base64ToTextConverter.convert("SGVsbG8sIHdvcmxkIQ==");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Base64 to morse")
    public void base64ToMorse() {
        Converter base64ToMorseConverter = new Base64ToMorseConverter();

        String expected = ".... . .-.. .-.. --- --..-- / .-- --- .-. .-.. -.. -.-.--";
        String actual = base64ToMorseConverter.convert("SEVMTE8sIFdPUkxEIQ==");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Base64 decode error")
    public void base64DecodeError() {
        Converter base64ToTextConverter = new Base64ToTextConverter();

        assertThrows(Base64Exception.class, () -> base64ToTextConverter.convert("k"));
    }
}
