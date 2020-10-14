package org.dkohl.wdp.spectrogram;

import org.dkohl.wdp.io.AudioWritingUpdate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InfoComponent extends JLabel implements AudioWritingUpdate {

    private ArrayList<Annotation> annotations;
    private HashMap<Integer, Labels> mapping;
    private AudioStream stream;
    private int width;
    private int position;
    private int percentageDone;
    private String filename;

    public InfoComponent(ArrayList<Annotation> annotations, AudioStream stream, int width) {
        this.annotations = annotations;
        this.stream = stream;
        this.mapping = new HashMap<>();
        this.mapping.put(1, Labels.BP_FAST);
        this.mapping.put(2, Labels.BP_MED);
        this.mapping.put(3, Labels.EC_FAST);
        this.mapping.put(4, Labels.EC_MED);
        this.mapping.put(5, Labels.EC_SLOW);
        this.mapping.put(6, Labels.WSTL_UP);
        this.mapping.put(7, Labels.WSTL_DOWN);
        this.mapping.put(8, Labels.WSTL_CONV);
        this.mapping.put(9, Labels.WSTL_CONC);
        this.mapping.put(0, Labels.NOISE);
        this.width = width;
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
        builder.append("<li> [s]: save </li>");
        builder.append("</ul>");

        builder.append("<h2> Annotations </h2> <ul>");
        HashMap<Labels, Integer> counts = Annotation.counts(annotations);
        for(int i = 0; i < 10; i++) {
            Labels l = mapping.get(i);
            int count = 0;
            if(counts.containsKey(l)) {
                count = counts.get(l);
            }
            builder.append(String.format("<li> [%d]: %s (%d) </li>", i, l.name(), count));
        }
        builder.append("</ul>");

        builder.append("<h2> Current </h2> <ul>");
        builder.append(String.format("<li> Start: %s</li>", stream.format(position)));
        builder.append(String.format("<li> Stop:  %s</li>", stream.format(position + width)));
        builder.append(String.format("<li> File:  %s</li>", stream.currentFileName()));
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
