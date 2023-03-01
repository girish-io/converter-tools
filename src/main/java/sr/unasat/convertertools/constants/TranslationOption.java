package sr.unasat.convertertools.constants;

public enum TranslationOption {

    TEXT("Text"),
    MORSE_CODE("Morse"),
    BASE64("Base64");

    private final String option;

    TranslationOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
