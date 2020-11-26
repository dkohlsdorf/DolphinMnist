package org.dkohl.wdp.spectrogram;

import java.awt.*;

public class MeasurementPlot {

    private static void cross(Graphics2D context, int x, int y) {
        int s = 3;
        context.drawLine(x-s, y, x+s, y);
        context.drawLine(x, y-s, x, y+s);
    }

    private static void text(SpectrogramParams params, Graphics2D context, int x, int y, double deltaF, double deltaT) {
        context.drawString(String.format("%d [Hz] %.2f[sec]", -1 * params.frequency(deltaF), params.seconds(deltaT)), x + 5, y + 5);
    }

    public static void plot(SpectrogramParams params, Color c, Measurment measurment, Graphics2D context, double scaleX, double scaleY) {
        int startX = (int) (measurment.getStartT() * scaleX);
        int startY = (int) (measurment.getStartF() * scaleY);
        context.setStroke(new BasicStroke(3));
        context.setFont(new Font("TimesRoman", Font.BOLD, 18));
        context.setColor(c);
        cross(context, startX, startY);
        if(measurment.getStopF() >= 0 && measurment.getStopT() >= 0) {
            int stopY = (int) (measurment.getStopF() * scaleY);
            int stopX = (int) (measurment.getStopT()  * scaleX);
            context.drawLine(startX, startY, stopX, stopY);
            cross(context, stopX, stopY);
            text(params, context, stopX, stopY, measurment.deltaF(), measurment.deltaT());
        }
    }

}
