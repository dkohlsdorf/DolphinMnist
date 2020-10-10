package org.dkohl.wdp.spectrogram;

public class Utils {

    public static double[] extremes(double x[]) {
        double minimum = Double.POSITIVE_INFINITY;
        double maximum = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < x.length; i++) {
            if (x[i] < minimum) minimum = x[i];
            if (x[i] >= maximum) maximum = x[i];
        }
        return new double[]{minimum, maximum};
    }

    public static double normalize(double x, double min, double max) {
        return (x - min) / (max - min);
    }

}
