package org.dkohl.wdp.spectrogram;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class AdjustableComponent extends JComponent implements ChangeListener {

    private JSlider slider;
    private JLabel  label;
    private Adjustable adjustable;
    private SpectrogramComponent component;

    public AdjustableComponent(SpectrogramComponent component, Adjustable adjustable) {
        this.component = component;
        this.adjustable = adjustable;
        setBackground(Color.WHITE);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(adjustable.value() * 100));
        slider.setPreferredSize(new Dimension(50, 10));
        slider.addChangeListener(this);
        label = new JLabel(format());
        setLayout(new BoxLayout(this, 0));
        add(slider);
        add(label);
    }

    private String format() {
        return String.format("%.2f [%s]" , adjustable.value(), adjustable.unit());
    }

    public void update() {
        label.setText(format());
        component.repaint();
        component.gainChange();
        repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int newVal = source.getValue();
            adjustable.adjust(newVal / 100.0);
            update();
        }
    }

}
