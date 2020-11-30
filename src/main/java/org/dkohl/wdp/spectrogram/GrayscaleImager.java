package org.dkohl.wdp.spectrogram;

import java.awt.*;

public class GrayscaleImager {
    // Move that to a slider
    public static float GAIN = 2.5f;
    public static float BRIGHTNESS = 0.8f;

    public static void plot(Spectrogram spectrogram, Graphics2D context, double scaleX, double scaleY) {
        int t = spectrogram.getTime();
        int d = spectrogram.getBins();
        double[] extremes = spectrogram.extremes();
        double min = extremes[0];
        double max = extremes[1];

        for(int i = 0; i < t; i++) {
            for(int j = 0; j < d; j++) {
                float val  = (float) Utils.normalize(spectrogram.at(i, j), min, max);
                float norm = 1.0f - Math.min(GAIN * val, 1.0f);
                Color heatmap = new Color(BRIGHTNESS * norm, BRIGHTNESS * norm, BRIGHTNESS * norm);
                context.setColor(heatmap);
                int x = (int) (i * scaleX);
                int y = (int) (j * scaleY);
                int w = (int) Math.max(scaleX, 1);
                int h = (int) Math.max(scaleY, 1);
                context.fillRect(x, y, w + 1, h + 1);
            }
        }
    }

}
