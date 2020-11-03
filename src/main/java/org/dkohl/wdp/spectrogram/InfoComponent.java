package org.dkohl.wdp.spectrogram;

import org.dkohl.wdp.io.AudioWritingUpdate;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InfoComponent extends JLabel implements AudioWritingUpdate {

    private ArrayList<Annotation> annotations;
    private AudioReader stream;
    private SpectrogramParams params;
    private int width;
    private int position;
    private int percentageDone;
    private String filename;

    public InfoComponent(ArrayList<Annotation> annotations, AudioReader stream, SpectrogramParams params, int width) {
        this.annotations = annotations;
        this.stream = stream;
        this.width = width;
        this.params = params;
        refresh(0);
    }

    public void refresh(int position) {
        this.position = position;
        update();
    }

    @Override
    public void progress(int percentageDone, String filename) {
        this.percentageDone = percentageDone;
        this.filename = filename;
        update();
    }

    private void update() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body bgcolor=\"white\">");
        builder.append("<h2> Controls </h2> <ul>");
        builder.append("<li> [a]: back </li>");
        builder.append("<li> [d]: forward </li>");
        builder.append("<li> [f]: fast forward </li>");
        builder.append("<li> [b]: fast backwards </li>");
        builder.append("<li> [s]: save </li>");
        builder.append("<li> [p]: play window </li>");
        builder.append("<li> [x]: delete </li>");
        builder.append("</ul>");

        builder.append("<h2> Annotations </h2> <ul>");
        HashMap<Labels, Integer> counts = Annotation.counts(annotations);

        for(Labels l : Labels.values()) {
            int count = 0;
            if(counts.containsKey(l)) {
                count = counts.get(l);
            }
            builder.append(String.format("<li> <p style=\"background-color:%s;\"> [%d]: %s (%d) </p></li>", AnnotationColor.COLORS[l.ordinal()], l.ordinal(), l.name(), count));
        }
        builder.append("</ul>");

        Annotation match = Annotation.findAnnotation(annotations, params, stream, position, position + width);
        builder.append("<h2> Current </h2> <ul>");
        builder.append(String.format("<li> Start: %s</li>", stream.format(params.sample(position))));
        builder.append(String.format("<li> Stop:  %s</li>", stream.format(params.sample(position + width))));
        builder.append(String.format("<li> File:  %s</li>", stream.currentFileName()));
        if(match != null) {
            builder.append(String.format("<li> Label:  %s</li>", match.getAnnotation().name()));
        }
        builder.append("</ul>");

        if(filename != null) {
            String cmp[] = filename.split("/");
            builder.append("<h2> Writing </h2> <ul>");
            builder.append(String.format("<li> Filename: %s </li>", cmp[cmp.length - 1]));
            builder.append(String.format("<li> Percentage Done: %d %%</li>", percentageDone));
        }
        builder.append("</body></html>");
        setText(builder.toString());
    }

}
