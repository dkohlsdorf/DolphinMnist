package org.dkohl.wdp.spectrogram;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

    public static String[] files(ArrayList<Annotation> annotations) {
        HashSet<String> files = new HashSet<>();
        for(Annotation annotation : annotations) {
            files.add(annotation.getFile());
        }
        String[] result = new String[files.size()];
        int i = 0;
        for(String f : files) {
            result[i] = f;
            i += 1;
        }
        return result;
    }

    public static ArrayList<Annotation> fromFile(String file) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line = in.readLine();
        line = in.readLine();
        ArrayList<Annotation> annotations = new ArrayList<>();
        while(line != null) {
            String cmp[] = line.trim().split(",");
            if(cmp.length == 5) {
                Labels label = Labels.valueOf(cmp[1].trim());
                String path = cmp[2];
                long start = Long.parseLong(cmp[3]);
                long stop = Long.parseLong(cmp[4]);
                annotations.add(new Annotation(start, stop, label, path));
            }
            line = in.readLine();
        }
        return annotations;
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
            if(annotation.getFile().equals(stream.currentFile())) {
                int[] window = stream.spectrogramRange(annotation, params);
                if (window[0] == start && window[1] == stop) {
                    return annotation;
                }
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

    public boolean in(int t) {
        return t >= start && t <= stop;
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
