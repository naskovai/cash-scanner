package org.opencv.houghtransform;
import java.util.ArrayList;
import java.util.Set;


public class Vector {
	private ArrayList<Double> vector;
	
	public Vector(int size) {
		initialize(size, (double)0);
	}
	
	private void initialize(int size, double value) {
		vector = new ArrayList<Double>(size);
		while(size > 0) {
			vector.add(value);
			size--;
		}
	}
	
	public double ssd(Vector otherVector) {
		double sum = 0;
		for(int i = 0; i < size(); i++) {
			sum += Math.pow(get(i) - otherVector.get(i), 2);
		}
		return sum;
	}
	
	public double get(int index) {
		return vector.get(index);
	}
	
	public void set(int index, double value) {
		vector.set(index, value);
	}
	
	public void add(int index, double value) {
		vector.set(index, get(index) + value);
	}
	
	public void devide(int index, double value) {
		vector.set(index, get(index) / value);
	}
	
	public int size() {
		return vector.size();
	}
	
	public static Vector getMeanVector(ArrayList<Vector> vectors, int size) {
		Vector meanVector = new Vector(size);

		for (Vector v: vectors) {
			for(int j = 0; j < size; j++) {
				meanVector.add(j, v.get(j));
			}
		}
		
		for (int i = 0; i < size; i++) {
			meanVector.devide(i, vectors.size());
		}
		
		return meanVector;
	}
}
