package org.opencv.houghtransform;
import org.opencv.core.Mat;


public class MaxFiltersResponses {
	private int responseMatrixSize;
	private Mat[] responses;
	private Vector[] vectors;
	
	public MaxFiltersResponses(int responseMatrixSize, Mat[] responses) {
		this.responseMatrixSize = responseMatrixSize;
		this.responses = responses;
		initializeVectors();
	}
	
	public int getResponseMatrixSize() {
		return responseMatrixSize;
	}
	
	private void initializeVectors() {
		int vectorsCount = responseMatrixSize * responseMatrixSize;
		vectors = new Vector[vectorsCount];
		
		for (int row = 0; row < responseMatrixSize; row++) {
			for (int col = 0; col < responseMatrixSize; col++) {
				ensureVectorInitializedAt(row, col);
			}
		}
	}
	
	public Vector[] getAllVectors() {
		return vectors;
	}
	
	public Vector getVectorAt(Point point) {
		int position = getVectorPosition(point);
		ensureVectorInitializedAt(point);
		return vectors[position];
	}
	
	private void ensureVectorInitializedAt(Point point) {
		ensureVectorInitializedAt(point.getX(), point.getY());
	}
	
	private void ensureVectorInitializedAt(int row, int col) {
		int position = getVectorPosition(row, col);
		if (vectors[position] == null) {
			Vector newVector = new Vector(responses.length);
			for(int i = 0; i < responses.length; i++) {
				newVector.set(i, responses[i].get(row, col)[0]);
			}
			vectors[position] = newVector;
		}
	}
	
	private int getVectorPosition(Point point) {
		int position = getVectorPosition(point.getX(), point.getY());
		return position;
	}
	
	private int getVectorPosition(int row, int col) {
		int position = row * responseMatrixSize + col;
		return position;
	}
}
