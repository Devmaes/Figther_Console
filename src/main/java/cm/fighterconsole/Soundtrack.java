package cm.fighterconsole;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Random;

/** Original procedural arcade-style loop generated in memory; no licensed audio or audio file needed. */
public final class Soundtrack implements AutoCloseable {
    private Clip clip;
    private boolean enabled = true;

    public boolean enabled() { return enabled; }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) stop();
    }

    public void start() {
        if (!enabled || clip != null) return;
        try {
            int frequency = 22050;
            int seconds = 8;
            int samples = frequency * seconds;
            byte[] bytes = new byte[samples * 2];
            double[] notes = {110, 130.81, 146.83, 164.81, 110, 146.83, 196.0, 164.81};
            Random noise = new Random(42);
            for (int i = 0; i < samples; i++) {
                double time = i / (double) frequency;
                int beat = (int) (time * 2.0);
                double phase = (time * 2.0) % 1.0;
                double envelope = Math.exp(-phase * 10);
                double bass = Math.sin(2 * Math.PI * notes[beat % notes.length] * time) * 0.12;
                double pulse = Math.sin(2 * Math.PI * (65 - 30 * phase) * time) * envelope * 0.17;
                double snare = (noise.nextDouble() * 2 - 1) * Math.exp(-phase * 19) * (beat % 2 == 1 ? 0.09 : 0.025);
                double sample = Math.max(-1, Math.min(1, bass + pulse + snare));
                short pcm = (short) (sample * Short.MAX_VALUE);
                bytes[2 * i] = (byte) (pcm & 0xff);
                bytes[2 * i + 1] = (byte) ((pcm >> 8) & 0xff);
            }
            AudioFormat format = new AudioFormat(frequency, 16, 1, true, false);
            clip = AudioSystem.getClip();
            clip.open(format, bytes, 0, bytes.length);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception | LinkageError unavailable) {
            // Headless terminals and soundless systems remain fully playable.
            stop();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    @Override public void close() { stop(); }
}
