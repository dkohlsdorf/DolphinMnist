package org.dkohl.wdp.spectrogram;

import java.awt.*;

public class GrayscaleImager {

    public static void plot(Spectrogram spectrogram, Graphics2D context, double scaleX, double scaleY) {
        int t = spectrogram.getTime();
        int d = spectrogram.getBins();
        double[] extremes = spectrogram.extremes();
        double min = extremes[0];
        double max = extremes[1];

        for(int i = 0; i < t; i++) {
            for(int j = 0; j < d; j++) {
                float norm = 1.0f - (float) Utils.normalize(spectrogram.at(i, j), min, max);
                context.setColor(new Color(norm, norm, norm, 1));
                int x = (int) (i * scaleX);
                int y = (int) (j * scaleY);
                int w = (int) Math.max(scaleX, 1);
                int h = (int) Math.max(scaleY, 1);
                context.fillRect(x, y, w + 1, h + 1);
            }
        }

        int nFreq   = 44100 / 2;
        int binSize = nFreq / d;
        int kHz     = 1000 / binSize;
        for(int j = 1; j < d; j+= kHz) {
            context.setColor(Color.RED);
            context.drawString(String.format("%d [kHz]", (j * binSize) / 1000), 10, (int) ((d - j) * scaleY));
        }
    }

}
