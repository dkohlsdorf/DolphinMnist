package org.dkohl.wdp.spectrogram;

import javax.swing.*;
import java.awt.*;

public class AudioComponent extends JComponent {

    private double[] audio;

    public AudioComponent(double[] audio) {
        this.audio = audio;
    }

    public void setAudio(double[] audio) {
        this.audio = audio;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension dim = getSize();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,dim.width, dim.height);
        g2d.setColor(Color.BLACK);
        LinePlot.plot(g2d, audio, dim.width, dim.height);
    }
}
