package org.dkohl.wdp.io;

import org.dkohl.wdp.spectrogram.Annotation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Audio {

	public static double[] read(String file) throws IOException, WavFileException {
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

	public static void write(String outputWav, String outputCsv, List<Annotation> annotations, AudioWritingUpdate update) throws IOException, WavFileException {
		if(annotations.size() > 0) {
			int length = (int) (annotations.get(0).getStop() - annotations.get(0).getStart());
			int nFrames = length * annotations.size();
			int offset = 0;
			WavFile out = WavFile.newWavFile(new File(outputWav), 1, nFrames, 16, 44100);
			FileWriter csvWriter = new FileWriter(new File(outputCsv));
			csvWriter.write("offset, annotation, source_file, source_start, source_stop\n");
			double audio[] = null;
			String currentFilename = null;
			int i = 0;
			for (Annotation annotation : annotations) {
				System.out.println("Writing: " + annotation);
				if(audio == null || !annotation.getFile().equals(currentFilename)) {
					audio = read(annotation.getFile());
					currentFilename = annotation.getFile();
				}
				double snippet[] = Arrays.copyOfRange(audio, (int) annotation.getStart(), (int) annotation.getStop());
				out.writeFrames(snippet, snippet.length);
				csvWriter.write(String.format("%d,%s,%s,%d,%d\n",
						offset,
						annotation.getAnnotation(),
						annotation.getFile(),
						annotation.getStart(),
						annotation.getStop()
				));
				offset += length;
				update.progress((int) (i / (double) annotations.size()) * 100, outputWav);
				i += 1;
			}
			csvWriter.close();
			out.close();
			update.progress(100, outputWav);
			System.out.println("Done Writing");
		}
	}
}
