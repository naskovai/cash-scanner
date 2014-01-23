import java.util.HashMap;
import java.util.Hashtable;

import org.opencv.core.Mat;


public class CoinProcessor {
	private MR8FilterBank filterBank;
	private KMeansFinder kMeansFinder;
	
	public class Histogram extends Hashtable<Integer, Double> {
		public Double compare(final Histogram computedHistogram){
			Double chiSq = 0d;
			
			for (int i = 0; i < this.size(); i++){
				Double h1 = this.get(i);
				Double h2 = computedHistogram.get(i);
				chiSq += (h2 - h1) * (h2 - h1) / h1;
			}
			
			return chiSq;
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
	
	public CoinProcessor() {
		filterBank = new MR8FilterBank(17);
		kMeansFinder = new KMeansFinder(5);
	}
	
	public CoinTypes getCoinType(Mat image) {
		MaxFiltersResponses responses = filterBank.getResponses(image);
		HashMap<Vector, Integer> textons = kMeansFinder.getTextons(responses);
		
		return CoinTypes.None;
	}
	
	public void train(Mat[] image, CoinTypes coinType) {
		
	}
}
