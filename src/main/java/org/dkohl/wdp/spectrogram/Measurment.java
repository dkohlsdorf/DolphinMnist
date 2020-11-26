package org.dkohl.wdp.spectrogram;

import java.util.Arrays;

public class Measurment {
    private int time[];
    private int frequency[];

    public Measurment() {
        time = new int[]{-1, -1};
        frequency = new int[]{-1, -1};
    }

    public double deltaT() {
        return time[1] - time[0];
    }

    public double deltaF() {
        return frequency[1] - frequency[0];
    }

    public int getStartT() {
        return time[0];
    }

    public int getStopT() {
        return time[1];
    }

    public int getStartF() {
        return frequency[0];
    }

    public int getStopF() {
        return frequency[1];
    }

    public void setStart(int t, int f) {
        time[0] = t;
        frequency[0] = f;
    }

    public void setStop(int t, int f) {
        time[1] = t;
        frequency[1] = f;
    }

    @Override
    public String toString() {
        return "Measurment{" +
                "time=" + Arrays.toString(time) +
                ", frequency=" + Arrays.toString(frequency) +
                ", delta_t=" + deltaT() +
                ", delta_f=" + deltaF() +
                '}';
    }
}
