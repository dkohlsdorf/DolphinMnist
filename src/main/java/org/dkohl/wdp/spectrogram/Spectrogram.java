package org.dkohl.wdp.spectrogram;

import org.jtransforms.fft.DoubleFFT_1D;

public class Spectrogram {
    private double flat[];
    private int time;
    private int bins;

    public Spectrogram(int time, int bins) {
        this.time = time;
        this.bins = bins;
        this.flat = new double[time * bins];
    }

    public Spectrogram(double[] audio, SpectrogramParams params) {
        this.time = params.len(audio.length);
        this.bins = params.fftBins();
        this.flat = new double[time * bins];

        double hamming[] = new double[params.getFftWin()];
        for (int i = 0; i < params.getFftWin(); i++) {
            hamming[i] = 0.54 + 0.46 * Math.cos((2 * Math.PI * i) / params.getFftWin());
        }
        int t = 0;
        for(int i = params.getFftWin(); i < audio.length; i+= params.getFftStep()) {
            double[] window = new double[params.getFftWin()];
            for(int j = 0; j < params.fftBins(); j++) {
                window[j] = hamming[j] * audio[i - params.getFftWin() + j];
            }
            DoubleFFT_1D fft = new DoubleFFT_1D(params.getFftWin());
            fft.realForward(window);
            for(int j = 0; j < params.fftBins(); j++) {
                double re  = window[params.getFftWin() - (2*j) - 1];
                double im  = window[params.getFftWin() - (2*j+1) - 1];
                double mag = Math.sqrt(re * re + im * im);
                set(t, j, mag);
            }
            t += 1;
        }
    }

    public double[] extremes() {
        return Utils.extremes(flat);
    }

    public double at(int t, int f) {
        return flat[t * bins + f];
    }

    public void set(int t, int f, double power) {
        flat[t * bins + f] = power;
    }

    public int getBins() {
        return bins;
    }

    public int getTime() {
        return time;
    }
}
