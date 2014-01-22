

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
