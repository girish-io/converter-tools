package sr.unasat.convertertools.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class TonePlayer {
    private final float sampleRate;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;
    private static final int SAMPLE_SIZE_IN_BITS = 8;

    private final SourceDataLine sdl;

    public TonePlayer(float sampleRate, int channels) throws LineUnavailableException {
        this.sampleRate = sampleRate;

        AudioFormat af = new AudioFormat(
                sampleRate,
                SAMPLE_SIZE_IN_BITS,
                channels,
                SIGNED,
                BIG_ENDIAN);

        sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
    }

    public void tone(int hz, int msecs, double volume) {
        byte[] buf = new byte[1];

        for (int i = 0; i < msecs * 8; i++) {
            double angle = i / (sampleRate / hz) * 2.0 * Math.PI;

            buf[0] = (byte) (Math.sin(angle) * 127.0 * volume);
            sdl.write(buf, 0, 1);
        }
    }

    public void stop() {
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
}
