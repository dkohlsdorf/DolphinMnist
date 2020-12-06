package org.dkohl.wdp.spectrogram;

public interface Adjustable {
    public void adjust(double x);
    public double value();
    public String unit();
}

