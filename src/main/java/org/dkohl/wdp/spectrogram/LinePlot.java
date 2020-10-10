package org.dkohl.wdp.spectrogram;

import java.awt.*;

public class LinePlot {

    private static double[] compress(double[] raw, int width) {
        double compressed[] = new double[width];
        int bucketSize = raw.length / width;
        for(int i = 0; i < raw.length; i++) {
            int idx = i / bucketSize;
            if (idx >= compressed.length) idx = compressed.length - 1;
            if (Math.abs(compressed[idx]) < Math.abs(raw[i])) {
                compressed[idx] = raw[i];
            }
        }
        return compressed;
    }

    public static void plot(Graphics2D g2d, double[] raw, int width, int height) {
        double[] compressed = compress(raw, width);
        double[] extremes = Utils.extremes(compressed);
        double min = extremes[0];
        double max = extremes[1];
        for(int i = 1; i < compressed.length; i++) {
            int h1 = (int) (Utils.normalize(compressed[i - 1], min, max) * height);
            int h2 = (int) (Utils.normalize(compressed[i], min, max) * height);
            g2d.drawLine((i - 1), h1, i, h2);
        }
    }

}
