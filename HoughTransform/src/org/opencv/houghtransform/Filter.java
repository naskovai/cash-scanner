package org.opencv.houghtransform;


import org.opencv.core.CvType;
import org.opencv.core.Mat;

public abstract class Filter {
	protected int kernelSize;
	protected Mat kernel;
	
	public Filter(int kernelSize) {
		this.kernelSize = kernelSize;
	}
	
	public int getKernelSize() {
		return this.kernelSize;
	}
	
	public Mat getKernel() {
		if (this.kernel == null) {
			this.kernel = computeKernel(this.kernelSize);
		}
		return this.kernel;
	}

	protected Mat computeKernel(int size) {
		Mat kernel = new Mat(size, size, CvType.CV_32F);
		Point center = getCenter(size);
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				double kernelElementValue =
						getKernelElementValue(Math.abs(i - center.getX()), Math.abs(j - center.getY()));
				kernel.put(i, j, kernelElementValue);
			}	
		return normalize(kernel, size);
	}
	
	private Mat normalize(Mat kernel, int size) {
		double normalizationValue = 1 / sum(kernel, size); 
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				double kernelElement = kernel.get(i, j)[0];
				kernel.put(i, j, kernelElement * normalizationValue);
			}
		return kernel;
	}
	
	private double sum(Mat kernel, int size) {
		double totalSum = 0;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				totalSum += kernel.get(i, j)[0];
			}
		return totalSum;
	}
	
	private Point getCenter(int kernelSize) {
		int middle = kernelSize >> 1; 
		return new Point(middle, middle);
	}
	
	protected abstract double getKernelElementValue(int x, int y);
}
