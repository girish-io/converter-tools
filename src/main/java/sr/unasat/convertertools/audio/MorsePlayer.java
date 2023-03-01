package sr.unasat.convertertools.audio;

import javax.sound.sampled.LineUnavailableException;

public class MorsePlayer {
    private static final int MORSE_TONE_FREQ = 641;
    private static final float SAMPLE_RATE = 8000f;
    private static final int CHANNELS = 1;
    private static final double VOLUME = 1.0;

    private static final int SHORT_TONE_MS = 100;
    private static final int LONG_TONE_MS = 200;

    private final TonePlayer tonePlayer;

    public MorsePlayer() {
        try {
            tonePlayer = new TonePlayer(SAMPLE_RATE, CHANNELS);
        } catch (LineUnavailableException e) {
            throw new RuntimeException("Failed to load SDL.");
        }
    }

    public void playMorseCharacter(String character) {
        if (character.equals(".")) {
            tonePlayer.tone(MORSE_TONE_FREQ, SHORT_TONE_MS, VOLUME);
            toneInterval();
        } else if (character.equals("-")) {
            tonePlayer.tone(MORSE_TONE_FREQ, LONG_TONE_MS, VOLUME);
            toneInterval();
        } else {
            toneSpace();
        }
    }

    public void toneInterval() {
        tonePlayer.tone(0, 250, VOLUME);
    }

    public void toneSpace() {
        tonePlayer.tone(0, 500, VOLUME);
    }

    public void stop() {
        tonePlayer.stop();
    }
}
