package org.dkohl.wdp.spectrogram;

import javax.swing.*;
import java.awt.*;

public class SpectrogramComponent extends JComponent {

    private Spectrogram spectrogram;
    private int position;
    private int width;

    public SpectrogramComponent(Spectrogram spectrogram, int width) {
        this.spectrogram = spectrogram;
        this.position = 0;
        this.width = width;
    }

    public void setSpectrogram(Spectrogram spectrogram) {
        this.spectrogram = spectrogram;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private double scale(int max, int value) {
        return value / (double) max;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension dim = getSize();
        Graphics2D g2d = (Graphics2D) g;
        double scaleX = scale(spectrogram.getTime(), dim.width);
        double scaleY = scale(spectrogram.getBins(), dim.height);
        GrayscaleImager.plot(spectrogram, g2d, scaleX, scaleY);
        RegionImager.plot(g2d, position, width, scaleX,dim.height, new Color(0.0f, 1.0f, 0.0f, 0.2f), Color.RED);
    }
}
