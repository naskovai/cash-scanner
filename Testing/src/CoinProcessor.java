import java.util.HashMap;
import org.opencv.core.Mat;

public class CoinProcessor {
	private static CoinProcessor instance = null;

	public static CoinProcessor getInstance() {
	   if(instance == null) {
    	  instance = new CoinProcessor();
      	}
      	return instance;
   	}
	
	private MR8FilterBank filterBank;
	private KMeansFinder kMeansFinder;
	private CoinsManager coinsManager;
	
	private CoinProcessor() {
		filterBank = new MR8FilterBank(17);
		kMeansFinder = new KMeansFinder(5);
		coinsManager = new CoinsManager();
	}
	
	public CoinTypes getCoinType(Mat image) {
		MaxFiltersResponses responses = filterBank.getResponses(image);
		HashMap<Vector, Integer> textons = kMeansFinder.getTextons(responses);
		Histogram histogram = convertToHistogram(textons);
		histogram.normalize();
		
		CoinTypes coinType = coinsManager.getCoinType(histogram);
		return coinType;
	}
	
	public Histogram getHistogram(HashMap<Vector, Integer> means) {
		HashMap<Vector, Integer> textons = kMeansFinder.getTextons(means);
		Histogram histogram = convertToHistogram(textons);
		histogram.normalize();
		return histogram;
	}
	
	public void train(Mat[] image, CoinTypes coinType) {
	}
	
	private Histogram convertToHistogram(HashMap<Vector, Integer> textons) {
		Histogram histogram = new Histogram();
		
		int colorId = 0;
		for(Vector v: textons.keySet()) {
			histogram.put(colorId, (double) textons.get(v));
			colorId++;
		}

		return histogram;
	}
}
