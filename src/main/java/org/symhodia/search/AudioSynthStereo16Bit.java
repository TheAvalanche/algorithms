package org.symhodia.search;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioSynthStereo16Bit {

	protected static final int SAMPLE_RATE = 44100;

	public static byte[] createStereoSinWaveBuffer16bit(double freq, int ms) {
		int samples = (int) ((ms * SAMPLE_RATE) / 1000);
		byte[] output = new byte[samples * 4];
		//
		double period = (double) SAMPLE_RATE / freq;
		for (int i = 0; i < samples; i++) {
			double angle = 2.0 * Math.PI * i / period;

			short value = (short) (Math.sin(angle) * (Short.MAX_VALUE / 2 - 1));
			output[i*4] = (byte) ((value & 0xFF00) >> 8);
			output[i*4+1] = (byte) (value & 0x00FF);
			output[i*4+2] = (byte) ((value & 0xFF00) >> 8);
			output[i*4+3] = (byte) (value & 0x00FF);
		}

		return output;
	}


	public static void main(String[] args) throws LineUnavailableException {
		final AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 2, true, true);
		SourceDataLine line = AudioSystem.getSourceDataLine(af);
		line.open(af, SAMPLE_RATE);
		line.start();

		for (double freq = 400; freq <= 500; ) {
			byte[] toneBuffer = createStereoSinWaveBuffer16bit(freq, 1000);
			line.write(toneBuffer, 0, toneBuffer.length);
			freq += 50;
		}

		line.drain();
		line.close();
	}
}
