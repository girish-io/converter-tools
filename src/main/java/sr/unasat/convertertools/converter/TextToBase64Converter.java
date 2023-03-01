package sr.unasat.convertertools.converter;

import java.util.Base64;

public class TextToBase64Converter implements Converter {
    @Override
    public String convert(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }
}
