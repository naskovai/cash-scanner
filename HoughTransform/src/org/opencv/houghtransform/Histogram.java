package org.opencv.houghtransform;
import java.util.Hashtable;

public class Histogram extends Hashtable<Integer, Double> {
	private double minDistance;
	
	private Double distance(final Histogram computedHistogram){
		Double chiSq = 0d;
			
		for (int i = 0; i < this.size(); i++){
			chiSq += getChiSquareElement(i, computedHistogram);
		}
			
		return chiSq;
	}

	private double getChiSquareElement(int i, final Histogram computedHistogram) {
		Double h1 = this.get(i);
		Double h2 = computedHistogram.get(i);
		Double chiSquare = ((h2 - h1) * (h2 - h1)) / (h1 + h2);
		return chiSquare;
	}

	public Double getMinDistance(final Histogram computedHistogram) {
		minDistance = Double.MAX_VALUE;
	
		int[] indexes = new int[this.size()];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
		}

		permute(indexes, indexes.length - 1, computedHistogram);
		
		return minDistance;
	}

	private void permute(int[] indexes, int n, final Histogram computedHistogram) {
		if (n == 0) {
			minDistance = Math.min(minDistance, computeDistance(indexes, computedHistogram));
			return;
        }
        for (int i = 0; i <= n; i++) {
            swap(indexes, i, n - 1);
            permute(indexes, n - 1, computedHistogram);
            swap(indexes, i, n - 1);
        }
	}
	
	private Double computeDistance(int[] indexes, final Histogram computedHistogram) {
		Double distance = 0.0;
		for (int i = 0; i < indexes.length; i++) {
			distance += getChiSquareElement(indexes[i], computedHistogram);
		}
		return distance;
	}
		 
	private static void swap(int[] indexes, int i, int j) {
		int index;	
		index = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = index;
	}

	public void normalize(){
		Double area = 0d;
		
		for (int i = 0; i < this.size(); i++){
			area += this.get(i);
		}
		
		for (int i = 0; i < this.size(); i++){
			this.put(i, this.get(i) / area);
		}
	}
}