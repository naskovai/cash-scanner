import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class CoinsManager {
	private static final String storageFileName = "storage"; 

	private double eps = 1;
	private Hashtable<CoinTypes, Coin> coins;
	
	public CoinsManager() {
		load();
		if (coins == null) {
			coins = new Hashtable<CoinTypes, Coin>();
		}
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
	
	@SuppressWarnings("unchecked")
	private void load() {
		try {
			ensureStorageFileCreated();
			FileInputStream fileInStream = new FileInputStream(storageFileName);
			ObjectInputStream inStream = new ObjectInputStream(fileInStream);
			coins = (Hashtable<CoinTypes, Coin>)inStream.readObject();
			inStream.close();
			fileInStream.close();
		}
		catch(EOFException eofEx) {
		}
		catch(IOException i) {
			i.printStackTrace();
			return;
		}
		catch(ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}
	}
	
	private void ensureStorageFileCreated() {
		try {
			new File(storageFileName).createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
