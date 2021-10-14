package org.dkohl.wdp.spectrogram;

import java.awt.*;
import java.util.ArrayList;

public class AnnotationPlot {

    static final int BAR_HEIGHT = 25;

    private SpectrogramParams params;
    private ArrayList<Annotation> annotations;
    private AudioReader stream;
    private KeyMap keyMap;

    public AnnotationPlot(SpectrogramParams params, ArrayList<Annotation> annotations, AudioReader stream, KeyMap keyMap) {
        this.params = params;
        this.annotations = annotations;
        this.stream = stream;
        this.keyMap = keyMap;
    }

    public void plot(Graphics2D g2d, double scaleW) {
        for(Annotation annotation : annotations) {
            if(stream.inWindow(annotation)) {
                int[] window = stream.spectrogramRange(annotation, params);
                int i = keyMap.getKey(annotation.getAnnotation()) - 48;
                Color c = AnnotationColor.getColor(i, 1.0f);
                g2d.setColor(c);
                int w = (int) (window[1] - window[0]);
                g2d.fillRect((int) (window[0] * scaleW), 0, (int) (w * scaleW), BAR_HEIGHT);
            }
        }
    }

}
