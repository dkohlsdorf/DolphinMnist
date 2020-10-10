package org.dkohl.wdp.spectrogram;

public class SpectrogramParams {

    private int fftWin, fftStep;

    public SpectrogramParams(int fftWin, int fftStep) {
        this.fftWin = fftWin;
        this.fftStep = fftStep;
    }

    public int fftBins() {
        return fftWin / 2;
    }

    public int len(int audioSamples) {
        return (audioSamples - fftWin) / fftStep + 1;
    }

    public int sample(int fftSample) {
        return (fftSample * fftStep) + fftWin;
    }

    public int getFftWin() {
        return fftWin;
    }

    public int getFftStep() {
        return fftStep;
    }

}
