package org.opencv.houghtransform;


public class GaussianFilter extends Filter {
	private double sigma;

	public GaussianFilter(int kernelSize, double sigma) {
		super(kernelSize);
		this.sigma = sigma;
	}

	@Override
	protected double getKernelElementValue(int x, int y) {
		double kernelElementValue =
				(1 / (2 * Math.PI * sigma * sigma)) *
				Math.pow(Math.E, -1 * (x * x + y * y) / (2 * sigma * sigma));
		return kernelElementValue;
	}
}
