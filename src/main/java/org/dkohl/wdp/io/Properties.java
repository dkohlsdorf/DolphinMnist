package org.dkohl.wdp.io;

public class Properties {

    private int fftWin;
    private int fftStep;
    private int buffer;
    private int mnistWin;
    private int mnistStep;
    private int mnistWinDFT;
    private int mnistStepDFT;

    public Properties(int fftWin, int fftStep, int buffer, int mnistWin, int mnistStep) {
        this.fftWin = fftWin;
        this.fftStep = fftStep;
        this.buffer = buffer;
        this.mnistWin = mnistWin;
        this.mnistStep = mnistStep;
        this.mnistWinDFT = mnistWin / fftStep;
        this.mnistStepDFT = mnistStep / fftStep;

    }

    public static Properties defaultProperties() {
        int fftWin    = 512;
        int fftStep   = 128;
        int buffer    = 10 * 44100;
        int mnistWin  = 44100 / 8;
        int mnistStep = 44100 / 32;
        return new Properties(fftWin, fftStep, buffer, mnistWin, mnistStep);
    }

    public int getFftWin() {
        return fftWin;
    }

    public int getFftStep() {
        return fftStep;
    }

    public int getBuffer() {
        return buffer;
    }

    public int getMnistWin() {
        return mnistWin;
    }

    public int getMnistStep() {
        return mnistStep;
    }

    public int getMnistStepDFT() { return mnistStepDFT; }

    public int getMnistWinDFT() { return mnistWinDFT; }

}
