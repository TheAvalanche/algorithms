package org.symhodia.search;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.Random;

public class SinDFT {

	public static void main(String[] args) {

		int discFreq = 1024;
		double discPer = 1.0 / discFreq;
		int seconds = 1;
		double[] xData = new double[discFreq * seconds];
		double[] yData = new double[discFreq * seconds];

		double sineFreq = 100;

		Random r = new Random();
		
		int i = 0;
		for (double p = 0.0; p < seconds; p = p + discPer) {
			xData[i] = p;
			yData[i] = Math.sin(2 * Math.PI * sineFreq * p) + 0.25 * Math.sin(2 * Math.PI * sineFreq * 3 * p) + (r.nextInt(100 + 100 + 1) - 100) / 100.0;
			i++;
		}


		XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

		// Show it
		new SwingWrapper(chart).displayChart();

		DFTComplex(yData);
		FFTComplex(yData);
	}

	public static void DFT(double[] data) {
		int N = data.length;

		double[] real = new double[N / 2];
		double[] im = new double[N / 2];
		double[] arg = new double[N / 2];
		double[] hz = new double[N / 2];

		for (int k = 0; k < N / 2; k++) {
			real[k] = 0.0;
			im[k] = 0.0;
			for (int n = 0; n < N; n++) {
				real[k] += data[n] * Math.cos(2 * Math.PI * k * n / N);
				im[k] -= data[n] * Math.sin(2 * Math.PI * k * n / N);
			}

			arg[k] = Math.sqrt(real[k] * real[k] + im[k] * im[k]) / (N / 2);
			hz[k] = k;
		}

		XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", hz, arg);

		// Show it
		new SwingWrapper(chart).displayChart();

	}

	public static void DFTComplex(double[] data) {
		int N = data.length;

		Complex[] fft = new Complex[N / 2];
		double[] arg = new double[N / 2];
		double[] hz = new double[N / 2];

		for (int k = 0; k < N / 2; k++) {
			fft[k] = new Complex(0.0, 0.0);
			for (int n = 0; n < N; n++) {
				fft[k].re += data[n] * Math.cos(2 * Math.PI * k * n / N);
				fft[k].im -= data[n] * Math.sin(2 * Math.PI * k * n / N);
			}
			
			arg[k] = fft[k].arg() / (N / 2);
			hz[k] = k;
		}

		XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", hz, arg);

		// Show it
		new SwingWrapper(chart).displayChart();

	}
	
	public static void FFTComplex(double[] data) {
		int N = data.length;

		Complex[] fft = FFT(data);
		double[] arg = new double[N / 2];
		double[] hz = new double[N / 2];

		for (int k = 0; k < N / 2; k++) {
			arg[k] = fft[k].arg() / (N / 2);
			hz[k] = k;
		}
		
		XYChart chart2 = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", hz, arg);

		// Show it
		new SwingWrapper(chart2).displayChart();
	}

	public static Complex[] FFT(double[] data) {
		int n = data.length;
		if (n == 1) {
			return new Complex[] { new Complex(data[0], 0.0) };
		}

		double[] even = new double[n / 2];
		double[] odd = new double[n / 2];

		for (int i = 0; i < n / 2; i++) {
			even[i] = data[2 * i];
			odd[i] = data[2 * i + 1];
		}

		Complex[] fftEven = FFT(even);
		Complex[] fftOdd = FFT(odd);
		
		Complex[] fftCombined = new Complex[n];

		for (int k = 0; k < n/2; k++) {
			double kth = -2 * k * Math.PI / n;
			Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
			Complex exp = wk.mult(fftOdd[k]);
			fftCombined[k]       = fftEven[k].plus(exp);
			fftCombined[k + n/2] = fftEven[k].minus(exp);
		}
		return fftCombined;
	}

	static class Complex {
		public double re;
		public double im;

		public Complex() {
			this(0, 0);
		}

		public Complex(double r, double i) {
			re = r;
			im = i;
		}

		public Complex plus(Complex b) {
			return new Complex(this.re + b.re, this.im + b.im);
		}

		public Complex minus(Complex b) {
			return new Complex(this.re - b.re, this.im - b.im);
		}

		public Complex mult(Complex b) {
			return new Complex(this.re * b.re - this.im * b.im,
					this.re * b.im + this.im * b.re);
		}
		
		public double arg() {
			return Math.sqrt(re * re + im * im);
		}

		@Override
		public String toString() {
			return String.format("(%f,%f)", re, im);
		}
	}

}
