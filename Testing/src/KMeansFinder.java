import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class KMeansFinder {
	private int k;

	public KMeansFinder(int k) {
		this.k = k;
	}
	
	public int getK() {
		return k;
	}
	
	public HashMap<Vector, Integer> getTextons(MaxFiltersResponses filtersResponses) {
		HashMap<Vector, Integer> frequenceByVector = convertToFrequenceByVector(filtersResponses.getAllVectors());
		return getTextons(frequenceByVector);
	}
	
	private HashMap<Vector, Integer> convertToFrequenceByVector(Vector[] vectors) {
		HashMap<Vector, Integer> frequenceByVector = new HashMap<>(vectors.length);
		for(int i = 0; i < vectors.length; i++) {
			frequenceByVector.put(vectors[i], 1);
		}
		return frequenceByVector;
	}
	
	public HashMap<Vector, Integer> getTextons(HashMap<Vector, Integer> means) {
		HashMap<Vector, ArrayList<Vector>> textons = new HashMap<Vector, ArrayList<Vector>>();
		seed(textons, means.keySet());
		
		int iterations = 10;
		while(iterations > 0) {
			iterations--;
			
			classify(textons, means.keySet());
			optimize(textons);
		}
		classify(textons, means.keySet());
		
		HashMap<Vector, Integer> frequenceByTexton = convertToFrequenceByTexton(textons);
		return frequenceByTexton;
	}
	
	private void seed(HashMap<Vector, ArrayList<Vector>> textons, Set<Vector> vectors) {
		int count = 0;
		for(Vector v: vectors) {
			if (count == k)
				break;
			
			textons.put(v, new ArrayList<Vector>());
			
			count++;
		}
	}
	
	private void classify(HashMap<Vector, ArrayList<Vector>> textons, Set<Vector> vectors) {
		for(Vector v: vectors) {
			Vector center = getClusterCenter(textons.keySet(), v);
			textons.get(center).add(v);
		}
	}
	
	private Vector getClusterCenter(Set<Vector> centers, Vector vector) {
		Vector center = null;
		double minSsd = Double.MAX_VALUE;
		for(Vector currCenter: centers) {
			double currSsd = vector.ssd(currCenter);
			if (minSsd > currSsd) {
				minSsd = currSsd;
				center = currCenter;
			}
		}
		return center;
	}
	
	private void optimize(HashMap<Vector, ArrayList<Vector>> textons) {
		ArrayList<Vector> newCenters = new ArrayList<Vector>();
		for(Vector oldCenter: textons.keySet()) {
			newCenters.add(Vector.getMeanVector(textons.get(oldCenter), oldCenter.size()));
		}
		
		textons.clear();
		for(Vector newCenter: newCenters) {
			textons.put(newCenter, new ArrayList<Vector>());
		}
	}
	
	private HashMap<Vector, Integer> convertToFrequenceByTexton(HashMap<Vector, ArrayList<Vector>> textons) {
		HashMap<Vector, Integer> frequenceByTexton = new HashMap<Vector, Integer>();
		for(Vector key: textons.keySet()) {
			frequenceByTexton.put(key, textons.get(key).size());
		}
		return frequenceByTexton;
	}
}
