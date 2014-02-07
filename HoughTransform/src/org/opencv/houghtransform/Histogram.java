package org.opencv.houghtransform;
import java.util.Hashtable;
import java.util.Set;

public class Histogram {
	private Hashtable<Vector, Double> textons;
	private Vector[] h1Keys;
	
	public Histogram(Hashtable<Vector, Integer> means) {
		this.textons = new Hashtable<Vector, Double>();
		this.h1Keys = new Vector[means.keySet().size()];

		int index = 0;
		for(Vector key: means.keySet()) {
			this.textons.put(key, (double)means.get(key));

			this.h1Keys[index] = key;
			index++;
		}
		
		this.normalize();
	}
	
	public int size() {
		return textons.size();
	}
	
	public Double getMinDistance(final Histogram otherHistogram) {
		double[] h2 = new double[otherHistogram.size()];
		double sum = 0d;

		for(Vector vector: otherHistogram.keySet()) {
			Integer closestTextonIndex = getClosestTextonIndex(vector);
			h2[closestTextonIndex] += otherHistogram.get(vector);
			sum += h2[closestTextonIndex];
		}
		
		for (int i = 0; i < h2.length; i++) {
			h2[i] /= sum;
		}

		double chiSquareDistance = getChiSquareDistance(h2);
		return chiSquareDistance;
	}
	
	private Integer getClosestTextonIndex(Vector vector) {
		Double minSsd = Double.MAX_VALUE;
		int minId = -1;

		for (int i = 0; i < h1Keys.length; i++) {
			double currentSsd = vector.ssd(h1Keys[i]);
			if (minSsd > currentSsd) {
				minSsd = currentSsd;
				minId = i;
			}
		}
		
		return minId;
	}
	
	private double getChiSquareDistance(final double[] h2){
		double chiSquare = 0d;
			
		for (int i = 0; i < this.size(); i++){
			chiSquare += getChiSquareDistance(this.get(h1Keys[i]), h2[i]);
		}

		return chiSquare;
	}

	private double getChiSquareDistance(Double h1, Double h2) {
		Double chiSquare = ((h2 - h1) * (h2 - h1)) / (h1 + h2);
		return chiSquare;
	}
	
	public void normalize(){
		Double area = 0d;
		
		for (Vector key: this.textons.keySet()){
			area += this.get(key);
		}
		
		for (Vector key: this.textons.keySet()){
			textons.put(key, this.get(key) / area);
		}
	}
	
	public Double get(Vector key) {
		return this.textons.get(key);
	}
	
	public Set<Vector> keySet() {
		return this.textons.keySet();
	}
}