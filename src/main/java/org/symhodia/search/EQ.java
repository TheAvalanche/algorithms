package org.symhodia.search;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class EQ {

	public static void main(String[] args) {

		int discFreq = 44100;
		double discPer = 1.0 / discFreq;
		double seconds = 0.5;
		double[] xData = new double[(int) (discFreq * seconds)];
		double[] yData = new double[(int) (discFreq * seconds)];

		double sineFreq = 100;

		int i = 0;
		for (double p = 0.0; p < seconds; p = p + discPer) {
			xData[i] = p;
			yData[i] = Math.sin(2 * Math.PI * sineFreq * p);
			i++;
		}


		XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

		highPass(xData, yData, 200, 1);
		highPass2(xData, yData, 200, 1);
/*		highPass2(xData, yData, 400, 1);
		highPass2(xData, yData, 600, 1);
		highPass2(xData, yData, 800, 1);
		highPass2(xData, yData, 1000, 1);*/

		// Show it
		new SwingWrapper(chart).displayChart();

	}

	/* High Pass Filter based on RBJ Cookbook linked above */
	/* Analog Transfer Function for this filter: H(s) = s^2 / (s^2 + s/Q + 1) */

	/* https://ethanwiner.com/eq-dsp.htm */
	private static void highPass(double[] xData, double[] yData, double f0, double Q) {

		double Fs = 44100;

		double w0 = 2 * Math.PI * f0 / Fs;
		double alpha = Math.sin(w0) / (2 * Q);
		double a0 = 1 + alpha;
		double a1 = -2 * Math.cos(w0);
		double a2 = 1 - alpha;
		double b0 = (1 + Math.cos(w0)) / 2;
		double b1 = -(1 + Math.cos(w0));
		double b2 = (1 + Math.cos(w0)) / 2;

		double[] output = new double[yData.length];
		double[] PrevSample = new double[3];

		for (int i = 0; i < yData.length; i++) {
			double y = yData[i];
			PrevSample[2] = PrevSample[1];   /* Slide the samples over one position */
			PrevSample[1] = PrevSample[0];
			PrevSample[0] = y;

			output[i] = ( b0 / a0 * PrevSample[0]) +
					(b1 / a0 * PrevSample[1]) +
					(b2 / a0 * PrevSample[2]) -
					(a1 / a0 * output[(i - 1) > 0 ? (i - 1) : 0]) -
					(a2 / a0 * output[(i - 2) > 0 ? (i - 2) : 0]);
		}

		XYChart chart = QuickChart.getChart("Output Chart " + f0 + " " + Q, "X", "Y", "y(x)", xData, output);

		// Show it
		new SwingWrapper(chart).displayChart();

	}

	/* https://arachnoid.com/BiQuadDesigner/source_files/BiQuadraticFilter.java */
	private static void highPass2(double[] xData, double[] yData, double f0, double Q) {

		double Fs = 44100;

		double omega = 2 * Math.PI * f0 / Fs;
		double sn = Math.sin(omega);
		double cs = Math.cos(omega);
		double alpha = sn / (2 * Q);

		double a0, a1, a2, b0, b1, b2;
		double x1, x2, y, y1, y2;

		x1 = x2 = y1 = y2 = 0;

		b0 = (1 + cs) / 2;
		b1 = -(1 + cs);
		b2 = (1 + cs) / 2;
		a0 = 1 + alpha;
		a1 = -2 * cs;
		a2 = 1 - alpha;

		b0 /= a0;
		b1 /= a0;
		b2 /= a0;
		a1 /= a0;
		a2 /= a0;

		double[] output = new double[yData.length];

		for (int i = 0; i < yData.length; i++) {
			double x = yData[i];

			y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
			x2 = x1;
			x1 = x;
			y2 = y1;
			y1 = y;

			output[i] = y;
		}

		XYChart chart = QuickChart.getChart("Output Chart " + f0 + " " + Q, "X", "Y", "y(x)", xData, output);

		// Show it
		new SwingWrapper(chart).displayChart();

	}


}
