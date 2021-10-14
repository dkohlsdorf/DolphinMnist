package org.dkohl.wdp.spectrogram;

import org.dkohl.wdp.io.Audio;

import java.util.Arrays;

public class AudioReader {

    private double[] audio;
    private String[] files;
    private int windowSize;

    private int currentStart;
    private int currentFile;

    public AudioReader(String[] files, int windowSize) {
        this.windowSize = windowSize;
        this.currentFile = 0;
        this.files = files;
        this.currentStart = 0;
        try {
            audio = Audio.read(files[currentFile]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double[] getData() {
        return Arrays.copyOfRange(audio, currentStart, Math.min(audio.length, currentStart + windowSize));
    }

    public boolean bwd() {
        if(currentStart <= 0) {
            if(currentFile > 0) {
                currentFile -= 1;
                try {
                    audio = Audio.read(files[currentFile]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currentStart  = audio.length - windowSize;
                currentStart  = Math.max(0, currentStart);
                return true;
            }
            return false;
        } else {
            currentStart -= windowSize;
            currentStart  = Math.max(0, currentStart);
            return true;
        }
    }

    public boolean fwd() {
        if(currentStart + windowSize >= audio.length) {
            if(currentFile + 1 < files.length) {
                currentFile += 1;
                currentStart = 0;
                try {
                    audio = Audio.read(files[currentFile]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        } else {
            currentStart += windowSize;
            return true;
        }
    }

    public boolean inWindow(Annotation annotation) {
        int offset = currentStart;
        int viewEnd = offset + windowSize;
        boolean sameFile = annotation.getFile().equals(files[currentFile]);
        boolean inRange  = annotation.getStart() >= offset && annotation.getStop() <= viewEnd;
        return sameFile && inRange;
    }

    public int[] spectrogramRange(Annotation annotation, SpectrogramParams params) {
        assert inWindow(annotation);
        int offset = currentStart;
        int start = params.fftSample((int) annotation.getStart() - offset);
        int stop  = params.fftSample((int) annotation.getStop()  - offset);
        return new int[]{start, stop};
    }

    public String currentFileName() {
        String[] cmp = files[currentFile].split("/");
        return cmp[cmp.length - 1];
    }

    public String currentFile() {
        return files[currentFile];
    }

    public String format(int x) {
        int offset       = currentStart;
        double s         = (offset + x) / 44100.0;
        int hours        = (int) s / 3600;
        double remainder = s - hours * 3600;
        int mins         = (int) remainder / 60;
        remainder        = remainder - mins * 60.0;
        double secs      = remainder;
        return String.format("%d:%d:%f", hours, mins, secs);
    }

    public int getCurrentOffset() {
        return currentStart;
    }

    public int sample(int window) {
        int offset = currentStart;
        return window + offset;
    }

    public Annotation getAnnotation(String label, int start, int stop) {
        return new Annotation(sample(start), sample(stop), label, files[currentFile]);
    }
    
    public double[] current(int start, int stop) {
        int offset = currentStart;
        return Arrays.copyOfRange(audio, offset + start, offset + stop);
    }



}
