package sr.unasat.convertertools.converter;

import sr.unasat.convertertools.exception.Base64Exception;

import java.util.Base64;

public class Base64ToTextConverter implements Converter {
    @Override
    public String convert(String text) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(text);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            throw new Base64Exception("Invalid base64 text found.");
        }
    }
}
