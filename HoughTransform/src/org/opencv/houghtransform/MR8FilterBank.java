package org.opencv.houghtransform;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MR8FilterBank {
	private int filtersKernelSize = 13;
	private int edgeFiltersKernelSize = 9;
	private int barFiltersKernelSize = 5;

	private GaussianFilter gaussianFilter;
	private LaplacianOfGaussianFilter logFilter;
	private EdgeFilter[] edgeFilters;
	private BarFilter[] barFilters;
	
	public MR8FilterBank() {
		initializeFilters();
	}
	
	public MaxFiltersResponses getResponses(Mat input) {
		Mat image = preProcess(input);
		Mat[] responses = new Mat[8];
	
		responses[0] = image.clone();
		Imgproc.filter2D(responses[0], responses[0], -1, this.gaussianFilter.getKernel());
	
		responses[1] = image.clone();
		Imgproc.filter2D(responses[1], responses[1], -1, this.logFilter.getKernel());
		
		responses[2] = getMaxResponse(image, this.edgeFilters, 0, 6);
		responses[3] = getMaxResponse(image, this.edgeFilters, 6, 6);
		responses[4] = getMaxResponse(image, this.edgeFilters, 12, 6);
		
		responses[5] = getMaxResponse(image, this.barFilters, 0, 6);
		responses[6] = getMaxResponse(image, this.barFilters, 6, 6);
		responses[7] = getMaxResponse(image, this.barFilters, 12, 6);
		
		MaxFiltersResponses maxFiltersResponses = new MaxFiltersResponses(image.cols(), responses);
		return maxFiltersResponses;
	}
	
	private Mat preProcess(Mat input) {
		Mat image = input.clone();
		Imgproc.resize(image, image, new Size(128, 128));
		normalizeIntensity(image);
		//zeroMeanAndStandartDeviationNormalization(image);
		return image;
	}
/*	
	private void zeroMeanAndStandartDeviationNormalization(Mat image) {
		for (int column = 0; column < image.cols(); column++) {
			double standardDeviation = getStandardDeviation(image, column);
			for (int row = 0; row < image.rows(); row++) {
				image.put(row, column, image.get(row, column)[0] / standardDeviation);
			}
		}
	}
	
	private double getStandardDeviation(Mat image, int column) {
		double mean = getMean(image, column);
		
		double standardDeviation = 0;
		for (int row = 0; row < image.rows(); row++) {
			standardDeviation += Math.pow(image.get(row, column)[0] - mean, 2);
		}
		standardDeviation /= image.rows();
		standardDeviation = Math.sqrt(standardDeviation);
		return standardDeviation;
	}
	
	private double getMean(Mat image, int column) {
		double mean = 0;
		for (int row = 0; row < image.rows(); row++) {
			mean += image.get(row, column)[0];
		}
		mean /= (image.rows());
		return mean;
	}
*/	
	private void normalizeIntensity(Mat image) {
		int maxInt = 0;
		int minInt = 255;
 
		for (int i = 0; i < image.rows(); i++) {
			for(int j = 0; j < image.cols(); j++) {
				if (image.get(i, j)[0] > maxInt) {
					maxInt = (int) image.get(i, j)[0];
				}
				if (image.get(i, j)[0] < minInt) {
					minInt = (int) image.get(i, j)[0];
				}
			}
		}
  
		for (int i = 0; i < image.rows(); i++) {
			for(int j = 0; j < image.cols(); j++) {
				double normalizedValue = (image.get(i, j)[0] - minInt) * 255 / (maxInt - minInt);
				image.put(i, j, normalizedValue);
			}
		}
	}
	
	private Mat getMaxResponse(Mat input, Filter[] filters, int startFilter, int count) {
		Mat maxResponse = null;
		
		//double L[][] = new double[input.rows()][input.cols()];

		for(int i = startFilter; i < startFilter + count; i++) {
			Mat currentResponse = input.clone();
			Imgproc.filter2D(currentResponse, currentResponse, -1, filters[i].getKernel());
			
			if (maxResponse == null) {
				maxResponse = currentResponse;
			}
			else {
				for (int row = 0; row < maxResponse.rows(); row++)
					for (int col = 0; col < maxResponse.cols(); col++) {
						maxResponse.put(row, col, 
							Math.max(maxResponse.get(row, col)[0], currentResponse.get(row, col)[0]));
						
						//L[row][col] += Math.pow(currentResponse.get(row, col)[0], 2);
					}
			}
		}
/*	
		for (int row = 0; row < maxResponse.rows(); row++)
			for (int col = 0; col < maxResponse.cols(); col++) {
				L[row][col] = Math.sqrt(L[row][col]);
			}
		
		// Normalize the Max Response
		for (int row = 0; row < maxResponse.rows(); row++)
			for (int col = 0; col < maxResponse.cols(); col++) {
				maxResponse.put(row, col, 
					maxResponse.get(row, col)[0] * (Math.log(1 + L[row][col] / 0.03)) / L[row][col]);
			}
*/
		return maxResponse; 
	}
	
	private void initializeFilters() {
		this.gaussianFilter = new GaussianFilter(this.filtersKernelSize, 3);
		this.logFilter = new LaplacianOfGaussianFilter(this.filtersKernelSize, 3);
		
		initializeEdgeFilters();
		initializeBarFilters();
	}
	
	private void initializeEdgeFilters() {
		this.edgeFilters = new EdgeFilter[18];

		this.edgeFilters[0] = new EdgeFilter(this.edgeFiltersKernelSize, 0.5, 1.5, 0);
		this.edgeFilters[1] = new EdgeFilter(this.edgeFiltersKernelSize, 0.5, 1.5, Math.PI / 6);
		this.edgeFilters[2] = new EdgeFilter(this.edgeFiltersKernelSize, 0.5, 1.5, 11 * Math.PI / 6);
		this.edgeFilters[3] = new EdgeFilter(this.edgeFiltersKernelSize, 0.5, 1.5, Math.PI / 3);
		this.edgeFilters[4] = new EdgeFilter(this.edgeFiltersKernelSize, 0.5, 1.5, 5 * Math.PI / 3);
		this.edgeFilters[5] = new EdgeFilter(this.edgeFiltersKernelSize, 0.5, 1.5, Math.PI / 2);
		
		this.edgeFilters[6] = new EdgeFilter(this.edgeFiltersKernelSize, 1, 3, 0);
		this.edgeFilters[7] = new EdgeFilter(this.edgeFiltersKernelSize, 1, 3, Math.PI / 6);
		this.edgeFilters[8] = new EdgeFilter(this.edgeFiltersKernelSize, 1, 3, 11 * Math.PI / 6);
		this.edgeFilters[9] = new EdgeFilter(this.edgeFiltersKernelSize, 1, 3, Math.PI / 3);
		this.edgeFilters[10] = new EdgeFilter(this.edgeFiltersKernelSize, 1, 3, 5 * Math.PI / 3);
		this.edgeFilters[11] = new EdgeFilter(this.edgeFiltersKernelSize, 1, 3, Math.PI / 2);
		
		this.edgeFilters[12] = new EdgeFilter(this.edgeFiltersKernelSize, 2, 6, 0);
		this.edgeFilters[13] = new EdgeFilter(this.edgeFiltersKernelSize, 2, 6, Math.PI / 6);
		this.edgeFilters[14] = new EdgeFilter(this.edgeFiltersKernelSize, 2, 6, 11 * Math.PI / 6);
		this.edgeFilters[15] = new EdgeFilter(this.edgeFiltersKernelSize, 2, 6, Math.PI / 3);
		this.edgeFilters[16] = new EdgeFilter(this.edgeFiltersKernelSize, 2, 6, 5 * Math.PI / 3);
		this.edgeFilters[17] = new EdgeFilter(this.edgeFiltersKernelSize, 2, 6, Math.PI / 2);
	}
	
	private void initializeBarFilters() {
		this.barFilters = new BarFilter[18];

		this.barFilters[0] = new BarFilter(this.barFiltersKernelSize, 0.5, 1.5, 0);
		this.barFilters[1] = new BarFilter(this.barFiltersKernelSize, 0.5, 1.5, Math.PI / 6);
		this.barFilters[2] = new BarFilter(this.barFiltersKernelSize, 0.5, 1.5, 11 * Math.PI / 6);
		this.barFilters[3] = new BarFilter(this.barFiltersKernelSize, 0.5, 1.5, Math.PI / 3);
		this.barFilters[4] = new BarFilter(this.barFiltersKernelSize, 0.5, 1.5, 5 * Math.PI / 3);
		this.barFilters[5] = new BarFilter(this.barFiltersKernelSize, 0.5, 1.5, Math.PI / 2);
		
		this.barFilters[6] = new BarFilter(this.barFiltersKernelSize, 1, 3, 0);
		this.barFilters[7] = new BarFilter(this.barFiltersKernelSize, 1, 3, Math.PI / 6);
		this.barFilters[8] = new BarFilter(this.barFiltersKernelSize, 1, 3, 11 * Math.PI / 6);
		this.barFilters[9] = new BarFilter(this.barFiltersKernelSize, 1, 3, Math.PI / 3);
		this.barFilters[10] = new BarFilter(this.barFiltersKernelSize, 1, 3, 5 * Math.PI / 3);
		this.barFilters[11] = new BarFilter(this.barFiltersKernelSize, 1, 3, Math.PI / 2);
		
		this.barFilters[12] = new BarFilter(this.barFiltersKernelSize, 2, 6, 0);
		this.barFilters[13] = new BarFilter(this.barFiltersKernelSize, 2, 6, Math.PI / 6);
		this.barFilters[14] = new BarFilter(this.barFiltersKernelSize, 2, 6, 11 * Math.PI / 6);
		this.barFilters[15] = new BarFilter(this.barFiltersKernelSize, 2, 6, Math.PI / 3);
		this.barFilters[16] = new BarFilter(this.barFiltersKernelSize, 2, 6, 5 * Math.PI / 3);
		this.barFilters[17] = new BarFilter(this.barFiltersKernelSize, 2, 6, Math.PI / 2);
	}
}
