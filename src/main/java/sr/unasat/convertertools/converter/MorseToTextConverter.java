package sr.unasat.convertertools.converter;

import sr.unasat.convertertools.converter.constants.Morse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MorseToTextConverter implements Converter {
    @Override
    public String convert(String text) {
        if (text.isEmpty()) return "";

        text = text
                .replaceAll("\\s", " ")
                .toUpperCase();

        List<String> translatedParts = Arrays.stream(text.split(" "))
                .map(Morse.getInvertedAlphabet()::get)
                .filter(Objects::nonNull)
                .toList();

        return String.join("", translatedParts);
    }
}
