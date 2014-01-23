import java.util.ArrayList;
import java.util.HashMap;


public class CoinsManager {
	private double eps = 3;
	private ArrayList<Coin> coins;
	
	public CoinsManager() {
		populateData();
	}
	
	public CoinTypes getCoinType(Histogram histogram) {
		double minDistance = Double.MAX_VALUE;
		CoinTypes coinType = CoinTypes.None;
		for(Coin c: coins) {
			Histogram currentCoinHistogram = c.getHistogram();
			if (currentCoinHistogram.size() == 0)
				continue;
			
			double currentDistance = c.getHistogram().distance(histogram);
			if (currentDistance < eps) {
				if (minDistance > currentDistance) {
					minDistance = currentDistance;
					coinType = c.getType();
				}
			}
		}

		return coinType;
	}
	
	private void populateData() {
		coins = new ArrayList<Coin>();
		
		coins.add(new Coin(CoinTypes.OneFront, getLearnedMeans(CoinTypes.OneFront)));
		coins.add(new Coin(CoinTypes.TwoFront, getLearnedMeans(CoinTypes.TwoFront)));
		coins.add(new Coin(CoinTypes.FiveFront, getLearnedMeans(CoinTypes.FiveFront)));
		coins.add(new Coin(CoinTypes.TenFront, getLearnedMeans(CoinTypes.TenFront)));
		coins.add(new Coin(CoinTypes.TwentyFront, getLearnedMeans(CoinTypes.TwentyFront)));
		coins.add(new Coin(CoinTypes.FiftyFront, getLearnedMeans(CoinTypes.FiftyFront)));
		coins.add(new Coin(CoinTypes.LevFront, getLearnedMeans(CoinTypes.LevFront)));
	}
	
	private HashMap<Vector, Integer> getLearnedMeans(CoinTypes coinType) {
		HashMap<Vector, Integer> learnedMeans = new HashMap<Vector, Integer>();
		
		if (coinType == CoinTypes.OneFront) {
		}
		else if (coinType == CoinTypes.TwoFront) {
		}
		else if (coinType == CoinTypes.FiveFront) {		
		}
		else if (coinType == CoinTypes.TenFront) {
		}
		else if (coinType == CoinTypes.TwentyFront) {
			Vector vector = new Vector(8);
			setVectorValues(vector, 47.78084982537835, 25.624563445867288, 53.61379511059371, 54.792491268917345, 55.78696158323632, 61.86059371362049, 142.1137951105937, 149.67607683352736);
			learnedMeans.put(vector, 3424);
			
			setVectorValues(vector, 48.53469685902118, 159.163623082542, 58.2527392257122, 54.63403944485025, 47.78086194302411, 88.10518626734843, 239.16216216216216, 242.6691015339664);
			learnedMeans.put(vector, 1305);
			
			setVectorValues(vector, 40.622093023255815, 29.291003671970625, 46.4421664626683, 45.880048959608324, 45.78365973072216, 55.62362301101591, 254.79589963280293, 250.65667074663403);
			learnedMeans.put(vector, 3352);
			
			setVectorValues(vector, 38.51219512195122, 33.06380662020906, 42.76894599303136, 43.45317944250871, 43.45209059233449, 51.858885017421606, 48.02003484320557, 62.438371080139376);
			learnedMeans.put(vector, 4601);
			
			setVectorValues(vector, 139.24845388545307, 210.17881150847, 149.88410863135252, 146.41543425652057, 138.66496369991933, 182.43022317827374, 217.96074213498252, 218.2000537778973);
			learnedMeans.put(vector, 3702);
		}
		else if (coinType == CoinTypes.FiftyFront) {
		}
		else if (coinType == CoinTypes.LevFront) {
		}
		
		return learnedMeans;
	}
	
	private void setVectorValues(Vector vector, double... values) {
		for (int i = 0; i < values.length; i++) {
			vector.set(i, values[i]);
		}
	}
}
