package org.dkohl.wdp.spectrogram;

import javax.swing.*;
import java.awt.*;

public class FrequencyComponent extends JComponent {
    private int d;

    public FrequencyComponent(int dim) {
        this.d = dim;
    }

    private double scale(int max, int value) {
        return value / (double) max;
    }

    @Override
    protected void paintComponent(Graphics context) {
        Dimension dim = getSize();
        double scaleY = scale(d, dim.height);
        int nFreq   = 44100 / 2;
        int binSize = nFreq / d;
        int kHz     = 1000 / binSize;
        for(int j = 1; j < d; j+= kHz) {
            context.setColor(Color.RED);
            context.drawString(String.format("%d [kHz]", (j * binSize) / 1000), 10, (int) ((d - j) * scaleY));
        }
    }
}
