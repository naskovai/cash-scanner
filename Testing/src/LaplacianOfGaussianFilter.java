

public class LaplacianOfGaussianFilter extends Filter {
	private double sigma;

	public LaplacianOfGaussianFilter(int kernelSize, double sigma) {
		super(kernelSize);
		this.sigma = sigma;
	}

	@Override
	protected double getKernelElementValue(int x, int y) {
		double kernelElementValue =
				(-1 / (Math.PI * Math.pow(sigma,  4))) *
				(1 - ((x * x + y * y) / (2 * sigma * sigma))) *
				Math.pow(Math.E, -1 * (x * x + y * y) / (2 * sigma * sigma));
		return kernelElementValue;
	}
}
