package org.dkohl.wdp.spectrogram;

import org.dkohl.wdp.io.Audio;

import java.util.Arrays;

public class AudioStream {

    private double[] audio;
    private String[] files;
    private int windowSize;

    private int currentOffset;
    private int currentFile;

    public AudioStream(String[] files, int windowSize) {
        this.files = files;
        this.windowSize = windowSize;
        currentFile = -1;
    }

    private boolean currentDone() {
        return audio == null || currentOffset >= audio.length;
    }

    private boolean done() {
        return currentFile >= files.length;
    }

    public double[] next() throws Exception {
        if(currentDone()) {
            currentOffset = 0;
            currentFile++;
            if(done()) return null;
            audio = Audio.read(files[currentFile]);
        }
        int until = currentOffset + windowSize;
        if (until > audio.length) until = audio.length;
        double[] window = Arrays.copyOfRange(audio, currentOffset, until);
        currentOffset += windowSize;
        return window;
    }

    public String currentFileName() {
        String[] cmp = files[currentFile].split("/");
        return cmp[cmp.length - 1];
    }

    public String format(int x) {
        int offset       = currentOffset - windowSize;
        double s         = (offset + x) / 44100.0;
        int hours        = (int) s / 3600;
        double remainder = s - hours * 3600;
        int mins         = (int) remainder / 60;
        remainder        = remainder - mins * 60.0;
        double secs      = remainder;
        return String.format("%d:%d:%f", hours, mins, secs);
    }

    public Annotation getAnnotation(Labels label, int start, int stop) {
        int offset = currentOffset - windowSize;
        return new Annotation(offset + start, offset + stop, label, files[currentFile]);
    }

}
