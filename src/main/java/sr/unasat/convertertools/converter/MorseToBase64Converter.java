package sr.unasat.convertertools.converter;

public class MorseToBase64Converter implements Converter {
    @Override
    public String convert(String text) {
        Converter morseToTextConverter = new MorseToTextConverter();
        Converter textToBase64Converter = new TextToBase64Converter();

        return textToBase64Converter.convert(
                morseToTextConverter.convert(text));
    }
}
