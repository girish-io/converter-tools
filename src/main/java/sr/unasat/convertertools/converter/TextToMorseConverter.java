package sr.unasat.convertertools.converter;

import sr.unasat.convertertools.converter.constants.Morse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TextToMorseConverter implements Converter {
    @Override
    public String convert(String text) {
        if (text.isEmpty()) return "";

        text = text
                .replaceAll("\\s", " ")
                .toUpperCase();

        List<String> translatedParts = Arrays.stream(text.split(""))
                .map(Morse.getAlphabet()::get)
                .filter(Objects::nonNull)
                .toList();

        return String.join(" ", translatedParts);
    }
}
