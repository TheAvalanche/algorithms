package org.symhodia.search;

import javax.sound.sampled.*;

public class AudioSynthMonoDelay {

	protected static final int SAMPLE_RATE = 16000;


	public static float[] createMonoSinWaveBuffer(double freq, int ms) {
		int samples = (ms * SAMPLE_RATE) / 1000;
		float[] output = new float[samples];
		//
		double period = (double) SAMPLE_RATE / freq;
		for (int i = 0; i < output.length; i++) {
			double angle = 2.0 * Math.PI * i / period;
			output[i] = (float) (0.5f * Math.sin(angle));
		}

		return output;
	}


	public static void main(String[] args) throws LineUnavailableException {
		final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
		SourceDataLine line = AudioSystem.getSourceDataLine(af);
		line.open(af, SAMPLE_RATE);
		line.start();
		Delay delay = new Delay();
		for (double freq = 440; freq <= 880; freq = freq * 1.059463094359) {
			float[] input = createMonoSinWaveBuffer(freq, 1000);
			float[] processed = delay.processBlock(input);
			byte[] result = new byte[processed.length];
			for (int i = 0; i < processed.length; i++) {
				float n = processed[i];
				result[i] = (byte) (n * 127f);
			}
			line.write(result, 0, input.length);
		}

		line.drain();
		line.close();
	}

	public static class Delay {

		private float level = 0.2f;
		private int lengthMs = 1000;
		private float feedback = 0.75f;

		private float[] delayBuffer = new float[2 * SAMPLE_RATE];

		private int delayWritePosition = 0;
		private int delayReadPosition = (delayWritePosition - (lengthMs * SAMPLE_RATE / 1000) + delayBuffer.length) % delayBuffer.length;


		public float[] processBlock(float[] monoInput) {
			for (int i = 0; i < monoInput.length; i++) {

				float in = monoInput[i];
				float dry = in;// * (1 - level);
				float wet = delayBuffer[delayReadPosition] * level;

				delayBuffer[delayWritePosition] = (float) Math.tanh(monoInput[i] + delayBuffer[delayReadPosition] * feedback);

				if (++delayReadPosition >= delayBuffer.length)
					delayReadPosition = 0;
				if (++delayWritePosition >= delayBuffer.length)
					delayWritePosition = 0;

				monoInput[i] = dry + wet;
			}
			return monoInput;
		}
	}
}
