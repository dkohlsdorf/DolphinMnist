package org.dkohl.wdp.spectrogram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Annotation {

    private long start;
    private long stop;
    private Labels annotation;
    private String file;

    public Annotation(long start, long stop, Labels annotation, String file) {
        this.start = start;
        this.stop = stop;
        this.annotation = annotation;
        this.file = file;
    }

    public static HashMap<Labels, Integer> counts(ArrayList<Annotation> annotations) {
        HashMap<Labels, Integer> counts = new HashMap<>();
        for(Annotation annotation : annotations) {
            Labels l = annotation.getAnnotation();
            if(!counts.containsKey(l)) {
                counts.put(annotation.getAnnotation(), 1);
            } else {
                int count = counts.get(l);
                counts.put(l, count + 1);
            }
        }
        return counts;
    }

    public static Annotation findAnnotation(ArrayList<Annotation> annotations, SpectrogramParams params, AudioStream stream, int start, int stop) {
        for(Annotation annotation : annotations) {
            int[] window = stream.spectrogramRange(annotation, params);
            if(window[0] == start && window[1] == stop) {
                return annotation;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "start=" + start +
                ", stop=" + stop +
                ", annotation=" + annotation +
                ", file='" + file + '\'' +
                '}';
    }

    public long getStart() {
        return start;
    }

    public void setAnnotation(Labels annotation) {
        this.annotation = annotation;
    }

    public long getStop() {
        return stop;
    }

    public Labels getAnnotation() {
        return annotation;
    }

    public String getFile() {
        return file;
    }

}
