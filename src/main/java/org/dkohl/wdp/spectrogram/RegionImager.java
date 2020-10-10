package org.dkohl.wdp.spectrogram;

import java.awt.*;

public class RegionImager {

    public static void plot(Graphics2D g2d, int start, int w, double scaleW, int imageHeight, Color fill, Color border) {
        g2d.setColor(fill);
        g2d.fillRect((int) (start * scaleW), 0, (int) (w * scaleW), imageHeight);
        g2d.setColor(border);
        g2d.drawRect((int) (start * scaleW), 0, (int) (w * scaleW), imageHeight);
    }

}
