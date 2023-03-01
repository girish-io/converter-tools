package sr.unasat.convertertools.converter.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

final public class Morse {
    public static Map<String, String> getAlphabet() {
        final Map<String, String> charToMorse = new HashMap<>();

        // Alphabet
        charToMorse.put("A", ".-");
        charToMorse.put("B", "-...");
        charToMorse.put("C", "-.-.");
        charToMorse.put("D", "-..");
        charToMorse.put("E", ".");
        charToMorse.put("F", "..-.");
        charToMorse.put("G", "--.");
        charToMorse.put("H", "....");
        charToMorse.put("I", "..");
        charToMorse.put("J", ".---");
        charToMorse.put("K", "-.-");
        charToMorse.put("L", ".-..");
        charToMorse.put("M", "--");
        charToMorse.put("N", "-.");
        charToMorse.put("O", "---");
        charToMorse.put("P", ".--.");
        charToMorse.put("Q", "--.-");
        charToMorse.put("R", ".-.");
        charToMorse.put("S", "...");
        charToMorse.put("T", "-");
        charToMorse.put("U", "..-");
        charToMorse.put("V", "...-");
        charToMorse.put("W", ".--");
        charToMorse.put("X", "-..-");
        charToMorse.put("Y", "-.--");
        charToMorse.put("Z", "--..");

        // Digits
        charToMorse.put("1", ".----");
        charToMorse.put("2", "..---");
        charToMorse.put("3", "...--");
        charToMorse.put("4", "....-");
        charToMorse.put("5", ".....");
        charToMorse.put("6", "-....");
        charToMorse.put("7", "--...");
        charToMorse.put("8", "---..");
        charToMorse.put("9", "----.");
        charToMorse.put("0", "-----");

        // Punctuation
        charToMorse.put(" ", "/");
        charToMorse.put(".", ".-.-.-");
        charToMorse.put(",", "--..--");
        charToMorse.put("?", "..--..");
        charToMorse.put(";", "-.-.-.");
        charToMorse.put(":", "---...");
        charToMorse.put("-", "-....-");
        charToMorse.put("/", "-..-.");
        charToMorse.put("'", ".----.");
        charToMorse.put("\"", ".-..-.");
        charToMorse.put("!", "-.-.--");

        // Special characters
        charToMorse.put("_", "..--.-");
        charToMorse.put("+", ".-.-.");
        charToMorse.put("=", "-...-");
        charToMorse.put("(", "-.--.");
        charToMorse.put(")", "-.--.-");

        return charToMorse;
    }

    public static Map<String, String> getInvertedAlphabet() {
        return getAlphabet().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (v1, v2) -> v1));
    }
}
