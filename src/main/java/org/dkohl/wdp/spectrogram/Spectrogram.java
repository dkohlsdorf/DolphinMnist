package org.dkohl.wdp.spectrogram;

import org.jtransforms.fft.DoubleFFT_1D;

public class Spectrogram {

    final double EPS = 10e-6;
    final double DYN_RANGE_OFFSET = 2;
    final double DYN_RANGE_EXP    = 0.5;

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
                double mag = Math.sqrt(re * re + im * im) ;
                set(t, j, mag);
            }
            t += 1;
        }
    }

    public Spectrogram pcen(SpectrogramParams params) {
        Spectrogram pcen   = new Spectrogram(time, bins);
        Spectrogram smoothed = new Spectrogram(time, bins);
        // https://librosa.org/doc/main/generated/librosa.pcen.html
        double smoothing = (Math.sqrt(1 + 4 * Math.pow(time,2)) - 1) / (2 * Math.pow(time,2));
        for(int t = 1; t < time; t++) {
            for(int d = 0; d < bins; d++) {
                double smooth  = smoothing * at(t, d) + (1 - smoothing) * smoothed.at(t - 1, d);
                double scaler  = Math.pow(EPS + smooth, params.getGain());
                double pcenVal = Math.pow(at(t, d) / scaler + DYN_RANGE_OFFSET, DYN_RANGE_EXP);
                pcenVal       -= Math.pow(DYN_RANGE_OFFSET, DYN_RANGE_EXP);
                smoothed.set(t, d, smooth);
                pcen.set(t, d, pcenVal);
            }
        }
        return pcen;
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
