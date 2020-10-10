package org.dkohl.wdp.io;

import java.io.File;
import java.io.IOException;

public class AudioReader {

	public static double[] audioData(String file) throws IOException, WavFileException {
		WavFile wavFile = WavFile.openWavFile(new File(file));
		int numChannels = wavFile.getNumChannels();
		long frames = wavFile.getNumFrames();
		double data[] = new double[(int) frames];
		double[] buffer = new double[100 * numChannels];
		int framesRead = 0;
		int t = 0;
		do {
			framesRead = wavFile.readFrames(buffer, 100);
			// Average channels
			for (int s = 0; s < numChannels * framesRead; s += numChannels) {
				for (int c = 0; c < numChannels; c++) {
					data[t] += buffer[s + c];
				}
				data[t] /= numChannels;
				t++;
			}
		} while (framesRead != 0);
		wavFile.close();
		return data;
	}

}
