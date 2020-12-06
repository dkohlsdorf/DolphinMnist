package org.dkohl.wdp.spectrogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class SpectrogramComponent extends JComponent {
    private SpectrogramParams params;
    private Spectrogram spectrogram;
    private Spectrogram pcen;
    private Measurment measurment;
    private ArrayList<Measurment> measurments;
    private GrayscaleImager grayImager;

    private int position;
    private int width;
    private AnnotationPlot annotationPlot;
    private RegionImager regionImager;

    public SpectrogramComponent(Spectrogram spectrogram,
                                SpectrogramParams params,
                                RegionImager regionImager,
                                AnnotationPlot annotationPlot,
                                GrayscaleImager grayImager,
                                int width) {
        this.grayImager = grayImager;
        this.spectrogram = spectrogram;
        this.position = 0;
        this.width = width;
        this.annotationPlot = annotationPlot;
        this.regionImager = regionImager;
        this.params = params;
        setMeasurments(new ArrayList<Measurment>());
        gainChange();
    }

    public void gainChange() {
       pcen = spectrogram.pcen(params);
    }

    public void setSpectrogram(Spectrogram spectrogram) {
        this.spectrogram = spectrogram;
        this.pcen = spectrogram.pcen(params);
    }

    public void setMeasurments(ArrayList<Measurment> measurments) {
        this.measurments = measurments;
    }

    public void setMeasurment(Measurment measurment) {
        this.measurment = measurment;
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
        double scaleX = scale(pcen.getTime(), dim.width);
        double scaleY = scale(pcen.getBins(), dim.height);
        grayImager.plot(pcen, g2d, scaleX, scaleY);
        regionImager.plot(g2d, position, width, scaleX,dim.height, new Color(0.0f, 1.0f, 0.0f, 0.2f), Color.RED);
        annotationPlot.plot(g2d, scaleX);
        if(measurment != null) {
            MeasurementPlot.plot(params, Color.RED, measurment, g2d, scaleX, scaleY);
            for(Measurment m : measurments) {
                MeasurementPlot.plot(params, Color.ORANGE, m, g2d, scaleX, scaleY);
            }
        }

    }
}
