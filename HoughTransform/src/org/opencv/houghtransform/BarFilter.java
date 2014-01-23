package org.opencv.houghtransform;


public class BarFilter extends Filter {
	private double sigmaX;
	private double sigmaY;
	private double tita;

	public BarFilter(int kernelSize, double sigmaX, double sigmaY, double tita) {
		super(kernelSize);
		this.sigmaX = sigmaX;
		this.sigmaY = sigmaY;
		this.tita = tita;
	}

	@Override
	protected double getKernelElementValue(int x, int y) {
		double X = calcX(x, y);
		double Y = calcY(x, y);
		double kernelElementValue =
				-1 *
				((X * X) / Math.pow(sigmaX, 4) - 1 / Math.pow(sigmaX, 2)) *
				((1 / (sigmaX * Math.sqrt(2 * Math.PI))) * Math.pow(Math.E, (-1 * X * X) / (2 * sigmaX * sigmaX))) *
				((1 / (sigmaY * Math.sqrt(2 * Math.PI))) * Math.pow(Math.E, (-1 * Y * Y) / (2 * sigmaY * sigmaY)));
		return kernelElementValue;
	}
	
	private double calcX(int x, int y) {
		double X = x * Math.cos(tita) + y * Math.sin(tita);
		return X;
	}
	
	private double calcY(int x, int y) {
		double Y = -x * Math.cos(tita) + y * Math.cos(tita);
		return Y;
	}
}
