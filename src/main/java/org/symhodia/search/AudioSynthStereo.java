package org.symhodia.search;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioSynthStereo {

	protected static final int SAMPLE_RATE = 44100;


	public static byte[] createStereoSinWaveBuffer(double freq, int ms) {
		int samples = (int) ((ms * SAMPLE_RATE) / 1000);
		byte[] output = new byte[samples * 2];
		//
		double period = (double) SAMPLE_RATE / freq;
		for (int i = 0; i < samples; i++) {
			double angle = 2.0 * Math.PI * i / period;
			output[i*2] = 0;
			output[i*2+1] = (byte) (Math.sin(angle) * 127f);
		}

		return output;
	}


	public static void main(String[] args) throws LineUnavailableException {
		final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 2, true, true);
		SourceDataLine line = AudioSystem.getSourceDataLine(af);
		line.open(af, SAMPLE_RATE);
		line.start();

		for (double freq = 400; freq <= 800; ) {
			byte[] toneBuffer = createStereoSinWaveBuffer(freq, 500);
			line.write(toneBuffer, 0, toneBuffer.length);
			freq += 50;
		}

		line.drain();
		line.close();
	}
}
