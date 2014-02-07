package org.opencv.houghtransform;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

public class CoinsManager {
	private static final String storageFileName = "res/raw/storage"; 

	private double eps = 0.4;
	private Hashtable<CoinTypes, Coin> coins;
	
	public CoinsManager(Hashtable<CoinTypes, Coin> storage) {
		coins = storage;
	}

	public CoinTypes getCoinType(Histogram histogram) {
		double minDistance = Double.MAX_VALUE;
		CoinTypes coinType = CoinTypes.None;
		for(Coin c: coins.values()) {
			Histogram currentCoinHistogram = c.getHistogram();
			if (currentCoinHistogram.size() == 0)
				continue;
			
			double currentDistance = c.getHistogram().getMinDistance(histogram);
			if (currentDistance < eps) {
				if (minDistance > currentDistance) {
					minDistance = currentDistance;
					coinType = c.getType();
				}
			}
		}

		return coinType;
	}
	
	public void train(CoinTypes coinType, Hashtable<Vector, Integer> means) {
		if (coins.containsKey(coinType)) {
			means.putAll(coins.get(coinType).getMeans());
			coins.remove(coinType);
		}
		Coin coin = new Coin(coinType, means);
		coins.put(coinType, coin);

		store();
	}
	
	private void store() {
		try {
			FileOutputStream fileOutStream = new FileOutputStream(storageFileName);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOutStream);
			outStream.writeObject(coins);
			outStream.close();
			fileOutStream.close();
		}
		catch(IOException i) {
			i.printStackTrace();
		}
	}
}
