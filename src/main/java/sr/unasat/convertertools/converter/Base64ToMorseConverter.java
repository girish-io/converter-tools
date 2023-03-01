package sr.unasat.convertertools.converter;

public class Base64ToMorseConverter implements Converter {
    @Override
    public String convert(String text) {
        Converter base64ToTextConverter = new Base64ToTextConverter();
        Converter textToMorseConverter = new TextToMorseConverter();

        return textToMorseConverter.convert(
                base64ToTextConverter.convert(text));
    }
}
