package org.dkohl.wdp.io;
/******************************************************************************
 *  Compilation:  javac StdAudio.java
 *  Execution:    java StdAudio
 *  Dependencies: none
 *
 *  Simple library for reading, writing, and manipulating .wav files.
 *
 *  https://introcs.cs.princeton.edu/java/15inout/StdAudio.java
 *
 *  Limitations
 *  -----------
 *    - Assumes the audio is monaural, little endian, with sampling rate
 *      of 44,100
 *    - check when reading .wav files from a .jar file ?
 *
 ******************************************************************************/

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *  <i>Standard audio</i>. This class provides a basic capability for
 *  creating, reading, and saving audio.
 *  <p>
 *  The audio format uses a sampling rate of 44,100 Hz, 16-bit, monaural.
 *
 *  <p>
 *  For additional documentation, see <a href="https://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class StdAudio {

    public static final int SAMPLE_RATE = 44100;

    private static final int BYTES_PER_SAMPLE = 2;       // 16-bit audio
    private static final int BITS_PER_SAMPLE = 16;       // 16-bit audio
    private static final double MAX_16_BIT = 32768;
    private static final int SAMPLE_BUFFER_SIZE = 4096;

    private static final int MONO   = 1;
    private static final int STEREO = 2;
    private static final boolean LITTLE_ENDIAN = false;
    private static final boolean BIG_ENDIAN    = true;
    private static final boolean SIGNED        = true;
    private static final boolean UNSIGNED      = false;


    private static SourceDataLine line;   // to play the sound
    private static byte[] buffer;         // our internal buffer
    private static int bufferSize = 0;    // number of samples currently in internal buffer

    private StdAudio() { }

    static { init(); }

    private static void init() {
        try {
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, MONO, SIGNED, LITTLE_ENDIAN);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);
            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE/3];
        }
        catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }
        line.start();
    }

    public static void close() {
        line.drain();
        line.stop();
    }

    public static void play(double sample) {
        if (Double.isNaN(sample)) throw new IllegalArgumentException("sample is NaN");
        if (sample < -1.0) sample = -1.0;
        if (sample > +1.0) sample = +1.0;
        short s = (short) (MAX_16_BIT * sample);
        if (sample == 1.0) s = Short.MAX_VALUE;   // special case since 32768 not a short
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8);   // little endian
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }

    public static void play(double[] samples) {
        if (samples == null) throw new IllegalArgumentException("argument to play() is null");
        for (int i = 0; i < samples.length; i++) {
            play(samples[i]);
        }
    }

}