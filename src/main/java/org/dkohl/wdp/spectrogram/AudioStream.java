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

    public boolean inWindow(Annotation annotation) {
        int offset = currentOffset - windowSize;
        int viewEnd = offset + windowSize;
        boolean sameFile = annotation.getFile().equals(files[currentFile]);
        boolean inRange  = annotation.getStart() >= offset && annotation.getStop() <= viewEnd;
        return sameFile && inRange;
    }

    public int[] spectrogramRange(Annotation annotation, SpectrogramParams params) {
        assert inWindow(annotation);
        int offset = currentOffset - windowSize;
        int start = params.fftSample((int) annotation.getStart() - offset);
        int stop  = params.fftSample((int) annotation.getStop()  - offset);
        return new int[]{start, stop};
    }

    private boolean currentDone() {
        return audio == null || currentOffset >= audio.length;
    }

    private boolean done() {
        return currentFile + 1 >= files.length;
    }

    public double[] next() throws Exception {
        if(currentDone()) {
            if(done()) return null;
            currentOffset = 0;
            currentFile++;
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

    public String currentFile() {
        return files[currentFile];
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

    public int getCurrentOffset() {
        return currentOffset - windowSize;
    }

    public int sample(int window) {
        int offset = currentOffset - windowSize;
        return window + offset;
    }

    public Annotation getAnnotation(Labels label, int start, int stop) {
        return new Annotation(sample(start), sample(stop), label, files[currentFile]);
    }

    public double[] current(int start, int stop) {
        int offset = currentOffset - windowSize;
        return Arrays.copyOfRange(audio, offset + start, offset + stop);
    }

}
