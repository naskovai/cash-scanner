import java.util.HashMap;

import org.opencv.core.Mat;


public class CoinProcessor {
	private MR8FilterBank filterBank;
	private KMeansFinder kMeansFinder;
	
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
