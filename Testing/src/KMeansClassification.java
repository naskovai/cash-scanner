import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Mat;


public class KMeansClassification {
	private int k;

	public KMeansClassification(int k) {
		this.k = k;
	}
	
	public HashMap<Vector, List<Vector>> getTextons(Mat[] filterRespones) {
		HashMap<Vector, List<Vector>> textons = new HashMap<Vector, List<Vector>>();
		seed(textons, filterRespones[0].rows(), filterRespones[0].cols());
		
		while(true) {
			
		}
		
		return textons;
	}
	
	private Vector[] getVectors(Mat[] filterRespones) {
		Vector[] vectors = new Vector[k];
	}

	private void seed(HashMap<Vector, List<Vector>> textons, int rows, int cols) {
		textons.put(new Vector(8, new Point(0, 0)), new ArrayList<Vector>());
		textons.put(new Vector(8, new Point(0, cols)), new ArrayList<Vector>());
		textons.put(new Vector(8, new Point(rows, 0)), new ArrayList<Vector>());
		textons.put(new Vector(8, new Point(rows, cols)), new ArrayList<Vector>());
		textons.put(new Vector(8, new Point(rows / 2, cols / 2)), new ArrayList<Vector>());
	}
	
	private void classify(HashMap<Vector, List<Vector>> textons, Mat[] filterRespones) {
		for ()
	}
}
