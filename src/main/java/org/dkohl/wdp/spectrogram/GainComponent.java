package org.dkohl.wdp.spectrogram;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class GainComponent extends JComponent implements ChangeListener {
    // TODO brightness here
    private JSlider brightnessSlider;
    private JLabel  brightnessLabel;

    private JSlider gainSlider;
    private JLabel  gain;
    private SpectrogramParams params;
    private SpectrogramComponent component;

    public GainComponent(SpectrogramComponent component, SpectrogramParams params) {
        this.component = component;
        this.params = params;
        setBackground(Color.WHITE);
        gainSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(params.getGain() * 100));
        gainSlider.setPreferredSize(new Dimension(50, 10));
        gainSlider.addChangeListener(this);
        gain = new JLabel(gain());
        setLayout(new BoxLayout(this, 0));
        add(gainSlider);
        add(gain);
    }

    private String gain() {
        return String.format("%.2f [alpha]" , params.getGain());
    }

    public void update() {
        gain.setText(gain());
        component.repaint();
        component.gainChange();
        repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int newVal = source.getValue();
            params.setGain(newVal / 100.0);
            update();
        }
    }

}
