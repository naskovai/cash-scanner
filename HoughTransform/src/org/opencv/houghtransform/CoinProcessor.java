package org.opencv.houghtransform;
import java.util.Hashtable;

import org.opencv.core.Mat;

public class CoinProcessor {
	private static CoinProcessor instance = null;

	public static CoinProcessor getInstance(Hashtable<CoinTypes, Coin> storage) {
	   if(instance == null) {
    	  instance = new CoinProcessor(storage);
      	}
      	return instance;
   	}
	
	private MR8FilterBank filterBank;
	private KMeansFinder kMeansFinder;
	private CoinsManager coinsManager;
	
	private CoinProcessor(Hashtable<CoinTypes, Coin> storage) {
		filterBank = new MR8FilterBank();
		kMeansFinder = new KMeansFinder(10);
		coinsManager = new CoinsManager(storage);
	}
	
	public CoinTypes getCoinType(Mat image) {
		Hashtable<Vector, Integer> textons = getTextons(image);
		Histogram histogram = new Histogram(textons);		
		CoinTypes coinType = coinsManager.getCoinType(histogram);
		return coinType;
	}
	
	public Histogram getHistogram(Hashtable<Vector, Integer> means) {
		Hashtable<Vector, Integer> textons = kMeansFinder.getTextons(means);
		Histogram histogram = new Histogram(textons);
		return histogram;
	}
	
	public void train(Mat image, CoinTypes coinType) {
		Hashtable<Vector, Integer> textons = getTextons(image);
		coinsManager.train(coinType, textons);
	}
	
	public Hashtable<Vector, Integer> getTextons(Mat image) {
		MaxFiltersResponses responses = filterBank.getResponses(image);
		Hashtable<Vector, Integer> textons = kMeansFinder.getTextons(responses);
		return textons;
	}
}
