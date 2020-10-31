package org.dkohl.wdp.spectrogram;

import java.awt.*;

public class AnnotationColor {
    public static final String[] COLORS = new String[]{
            "#006400",
            "#3737fa",
            "#b03060",
            "#ff4500",
            "#ffff00",
            "#00ff00",
            "#00ffff",
            "#ff00ff",
            "#6495ed",
            "#ffdab9"
    };

    public static Color getColor(int colorId, float alpha) {
        Color source = Color.decode(COLORS[colorId]);
        return new Color(source.getRed(), source.getGreen(), source.getBlue(), (int) (255 * alpha));
    }
}
