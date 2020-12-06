package org.dkohl.wdp.spectrogram;

import java.awt.*;

public class GrayscaleImager implements Adjustable {
    public static final float GAIN = 2f;

    private float brightness;

    public GrayscaleImager() {
        this.brightness = 0.75f;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public void plot(Spectrogram spectrogram, Graphics2D context, double scaleX, double scaleY) {
        int t = spectrogram.getTime();
        int d = spectrogram.getBins();
        double[] extremes = spectrogram.extremes();
        double min = extremes[0];
        double max = extremes[1];

        for(int i = 0; i < t; i++) {
            for(int j = 0; j < d; j++) {
                float val   = (float) Utils.normalize(spectrogram.at(i, j), min, max);
                float norm  = 1.0f - Math.min(GAIN * val, 1.0f);
                float color = brightness * norm;
                Color heatmap = new Color(color, color, color);
                context.setColor(heatmap);
                int x = (int) (i * scaleX);
                int y = (int) (j * scaleY);
                int w = (int) Math.max(scaleX, 1);
                int h = (int) Math.max(scaleY, 1);
                context.fillRect(x, y, w + 1, h + 1);
            }
        }
    }

    @Override
    public void adjust(double x) {
        setBrightness((float) x);
    }

    @Override
    public double value() {
        return getBrightness();
    }

    @Override
    public String unit() {
        return "lumen";
    }
}
