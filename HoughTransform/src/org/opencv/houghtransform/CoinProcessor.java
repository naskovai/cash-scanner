package org.opencv.houghtransform;
import java.util.Hashtable;
import java.util.Hashtable;

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
		filterBank = new MR8FilterBank();
		kMeansFinder = new KMeansFinder(10);
		coinsManager = new CoinsManager();
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
	
	private String dump(Hashtable<Vector, Integer> textons) {
		String result = "";
		
		int index = 0;
		for(Vector v: textons.keySet()) {
			String variableName = "vector_" + String.valueOf(index);
			
			result += "Vector " + variableName + " = new Vector(8);\n";
			result += "setVectorValues(" + variableName + ", ";
			
			for(int i = 0; i < 7; i++) {
				result += String.valueOf(v.get(i));
				result += ",";
			}
			result += String.valueOf(v.get(7));
			result += ");\n";
			
			result += "learnedMeans.put(" + variableName + ", " + String.valueOf(textons.get(v)) + ");\n";
			result += "\n";
			
			index++;
		}
		
		return result;
	}
}
