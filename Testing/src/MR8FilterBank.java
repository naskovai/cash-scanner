import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MR8FilterBank {
	private int filtersKernelSize;
	private GaussianFilter gaussianFilter;
	private LaplacianOfGaussianFilter logFilter;
	private EdgeFilter[] edgeFilters;
	private BarFilter[] barFilters;
	
	public MR8FilterBank() {
		this(3);
	}
	
	public MR8FilterBank(int filtersKernelSize) {
		this.filtersKernelSize = filtersKernelSize;
		initializeFilters();
	}
	
	public Mat[] getResponses(Mat input) {
		Mat[] responses = new Mat[8];
		
		responses[0] = input.clone();
		Imgproc.filter2D(responses[0], responses[0], -1, this.gaussianFilter.getKernel());
		
		responses[1] = input.clone();
		Imgproc.filter2D(responses[1], responses[1], -1, this.logFilter.getKernel());
		
		responses[2] = getMaxResponse(input, this.edgeFilters, 0, 6);
		responses[3] = getMaxResponse(input, this.edgeFilters, 6, 6);
		responses[4] = getMaxResponse(input, this.edgeFilters, 12, 6);
		
		responses[5] = getMaxResponse(input, this.barFilters, 0, 6);
		responses[6] = getMaxResponse(input, this.barFilters, 6, 6);
		responses[7] = getMaxResponse(input, this.barFilters, 12, 6);
		
		return responses;
	}
	
	private Mat getMaxResponse(Mat input, Filter[] filters, int startFilter, int count) {
		Mat maxResponse = null;
		
		for(int i = startFilter; i < startFilter + count; i++) {
			Mat currentEdgeResponse = input.clone();
			Imgproc.filter2D(currentEdgeResponse, currentEdgeResponse, -1, filters[i].getKernel());
			
			if (maxResponse == null) {
				maxResponse = currentEdgeResponse;
			}
			else {/*
				for (int row = 0; row < maxResponse.rows(); row++)
					for (int col = 0; col < maxResponse.cols(); col++) {
						maxResponse.put(row, col, 
								Math.max(maxResponse.get(row, col)[0], currentEdgeResponse.get(row, col)[0]));
					}*/
			}
		}

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

		this.edgeFilters[0] = new EdgeFilter(this.filtersKernelSize, 0.5, 1.5, 0);
		this.edgeFilters[1] = new EdgeFilter(this.filtersKernelSize, 0.5, 1.5, Math.PI / 6);
		this.edgeFilters[2] = new EdgeFilter(this.filtersKernelSize, 0.5, 1.5, 11 * Math.PI / 6);
		this.edgeFilters[3] = new EdgeFilter(this.filtersKernelSize, 0.5, 1.5, Math.PI / 3);
		this.edgeFilters[4] = new EdgeFilter(this.filtersKernelSize, 0.5, 1.5, 5 * Math.PI / 3);
		this.edgeFilters[5] = new EdgeFilter(this.filtersKernelSize, 0.5, 1.5, Math.PI / 2);
		
		this.edgeFilters[6] = new EdgeFilter(this.filtersKernelSize, 1, 3, 0);
		this.edgeFilters[7] = new EdgeFilter(this.filtersKernelSize, 1, 3, Math.PI / 6);
		this.edgeFilters[8] = new EdgeFilter(this.filtersKernelSize, 1, 3, 11 * Math.PI / 6);
		this.edgeFilters[9] = new EdgeFilter(this.filtersKernelSize, 1, 3, Math.PI / 3);
		this.edgeFilters[10] = new EdgeFilter(this.filtersKernelSize, 1, 3, 5 * Math.PI / 3);
		this.edgeFilters[11] = new EdgeFilter(this.filtersKernelSize, 1, 3, Math.PI / 2);
		
		this.edgeFilters[12] = new EdgeFilter(this.filtersKernelSize, 2, 6, 0);
		this.edgeFilters[13] = new EdgeFilter(this.filtersKernelSize, 2, 6, Math.PI / 6);
		this.edgeFilters[14] = new EdgeFilter(this.filtersKernelSize, 2, 6, 11 * Math.PI / 6);
		this.edgeFilters[15] = new EdgeFilter(this.filtersKernelSize, 2, 6, Math.PI / 3);
		this.edgeFilters[16] = new EdgeFilter(this.filtersKernelSize, 2, 6, 5 * Math.PI / 3);
		this.edgeFilters[17] = new EdgeFilter(this.filtersKernelSize, 2, 6, Math.PI / 2);
	}
	
	private void initializeBarFilters() {
		this.barFilters = new BarFilter[18];

		this.barFilters[0] = new BarFilter(this.filtersKernelSize, 0.5, 1.5, 0);
		this.barFilters[1] = new BarFilter(this.filtersKernelSize, 0.5, 1.5, Math.PI / 6);
		this.barFilters[2] = new BarFilter(this.filtersKernelSize, 0.5, 1.5, 11 * Math.PI / 6);
		this.barFilters[3] = new BarFilter(this.filtersKernelSize, 0.5, 1.5, Math.PI / 3);
		this.barFilters[4] = new BarFilter(this.filtersKernelSize, 0.5, 1.5, 5 * Math.PI / 3);
		this.barFilters[5] = new BarFilter(this.filtersKernelSize, 0.5, 1.5, Math.PI / 2);
		
		this.barFilters[6] = new BarFilter(this.filtersKernelSize, 1, 3, 0);
		this.barFilters[7] = new BarFilter(this.filtersKernelSize, 1, 3, Math.PI / 6);
		this.barFilters[8] = new BarFilter(this.filtersKernelSize, 1, 3, 11 * Math.PI / 6);
		this.barFilters[9] = new BarFilter(this.filtersKernelSize, 1, 3, Math.PI / 3);
		this.barFilters[10] = new BarFilter(this.filtersKernelSize, 1, 3, 5 * Math.PI / 3);
		this.barFilters[11] = new BarFilter(this.filtersKernelSize, 1, 3, Math.PI / 2);
		
		this.barFilters[12] = new BarFilter(this.filtersKernelSize, 2, 6, 0);
		this.barFilters[13] = new BarFilter(this.filtersKernelSize, 2, 6, Math.PI / 6);
		this.barFilters[14] = new BarFilter(this.filtersKernelSize, 2, 6, 11 * Math.PI / 6);
		this.barFilters[15] = new BarFilter(this.filtersKernelSize, 2, 6, Math.PI / 3);
		this.barFilters[16] = new BarFilter(this.filtersKernelSize, 2, 6, 5 * Math.PI / 3);
		this.barFilters[17] = new BarFilter(this.filtersKernelSize, 2, 6, Math.PI / 2);
	}
}
