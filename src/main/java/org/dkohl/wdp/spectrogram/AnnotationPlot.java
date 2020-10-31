package org.dkohl.wdp.spectrogram;

import java.awt.*;
import java.util.ArrayList;

public class AnnotationPlot {

    static final int BAR_HEIGHT = 25;

    private SpectrogramParams params;
    private ArrayList<Annotation> annotations;
    private AudioStream stream;

    public AnnotationPlot(SpectrogramParams params, ArrayList<Annotation> annotations, AudioStream stream) {
        this.params = params;
        this.annotations = annotations;
        this.stream = stream;
    }

    public void plot(Graphics2D g2d, double scaleW) {
        for(Annotation annotation : annotations) {
            if(stream.inWindow(annotation)) {
                int[] window = stream.spectrogramRange(annotation, params);
                Color c = AnnotationColor.getColor(annotation.getAnnotation().ordinal(), 1.0f);
                g2d.setColor(c);
                int w = (int) (window[1] - window[0]);
                g2d.fillRect((int) (window[0] * scaleW), 0, (int) (w * scaleW), BAR_HEIGHT);
            }
        }
    }

}
