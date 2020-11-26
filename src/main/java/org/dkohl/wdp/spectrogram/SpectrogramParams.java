package org.dkohl.wdp.spectrogram;

import org.dkohl.wdp.io.Properties;

public class SpectrogramParams {

    private int fftWin, fftStep;
    private double gain;

    public SpectrogramParams(int fftWin, int fftStep, double gain) {
        this.fftWin = fftWin;
        this.fftStep = fftStep;
        this.gain = gain;
    }

    public int frequency(double fftBin) {
        return (int) (fftBin * (44100.0 / fftWin));
    }

    public int fftBins() {
        return fftWin / 2;
    }

    public int len(int audioSamples) {
        return (audioSamples - fftWin) / fftStep + 1;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public int sample(int fftSample) {
        return (fftSample * fftStep) + fftWin;
    }

    public int fftSample(int sample) {
        return (sample - fftWin) / fftStep;
    }

    public int getFftWin() {
        return fftWin;
    }

    public int getFftStep() {
        return fftStep;
    }

}
