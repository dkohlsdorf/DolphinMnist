package org.dkohl.wdp.spectrogram;

public class SpectrogramParams implements Adjustable {

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

    public double seconds(double fftbin) {
        return sample((int) fftbin) / 44100.0;
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

    @Override
    public void adjust(double x) {
        setGain(x);
    }

    @Override
    public double value() {
        return getGain();
    }

    @Override
    public String unit() {
        return "alpha";
    }
}
