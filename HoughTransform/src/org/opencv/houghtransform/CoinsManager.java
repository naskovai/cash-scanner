package org.opencv.houghtransform;
import java.util.ArrayList;
import java.util.Hashtable;


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
	
	private Hashtable<Vector, Integer> getLearnedMeans(CoinTypes coinType) {
		Hashtable<Vector, Integer> learnedMeans = new Hashtable<Vector, Integer>();
		
		if (coinType == CoinTypes.OneFront) {
		}
		else if (coinType == CoinTypes.TwoFront) {
		}
		else if (coinType == CoinTypes.FiveFront) {		
		}
		else if (coinType == CoinTypes.TenFront) {
		}
		else if (coinType == CoinTypes.TwentyFront) {
			train20_1(learnedMeans);
			train20_2(learnedMeans);
			train20_3(learnedMeans);
			train20_4(learnedMeans);
			train20_5(learnedMeans);
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
	
	//////////////// HARDCODED LEARNING MODELS ///////////////////
	
	private void train20_1(Hashtable<Vector, Integer> learnedMeans) {
		Vector vector_0 = new Vector(8);
		setVectorValues(vector_0, 134.4076593600403,211.56487780297303,145.56991685563113,141.81582262534644,133.75535399344923,179.88737717309147,218.79994960947343,219.20786092214664);
		learnedMeans.put(vector_0, 3968);

		Vector vector_1 = new Vector(8);
		setVectorValues(vector_1, 39.864145658263304,33.333333333333336,44.074929971988794,44.34173669467787,44.45168067226891,54.04481792717087,255.0,66.25420168067227);
		learnedMeans.put(vector_1, 1428);

		Vector vector_2 = new Vector(8);
		setVectorValues(vector_2, 42.04078082721299,31.207769617317357,46.51430228063394,47.50811751063007,47.93080788558176,55.85195206803247,53.34228836490143,68.8475067645922);
		learnedMeans.put(vector_2, 5193);

		Vector vector_3 = new Vector(8);
		setVectorValues(vector_3, 41.60244428468727,54.47567697100408,47.7275341480949,46.675772825305536,45.37910376228133,59.784567457464654,254.8006230529595,252.9722022525761);
		learnedMeans.put(vector_3, 4173);

		Vector vector_4 = new Vector(8);
		setVectorValues(vector_4, 46.68841463414634,36.00670731707317,54.27682926829268,55.04939024390244,55.01890243902439,62.15426829268293,65.64634146341463,236.17865853658537);
		learnedMeans.put(vector_4, 1622);
	}
	
	private void train20_2(Hashtable<Vector, Integer> learnedMeans) {
		Vector vector_0 = new Vector(8);
		setVectorValues(vector_0, 114.165062560154,217.16145332050047,124.20548604427334,120.71679499518768,113.361645813282,152.4143407122233,230.93527430221366,234.61693936477383);
		learnedMeans.put(vector_0, 4160);

		Vector vector_1 = new Vector(8);
		setVectorValues(vector_1, 123.66797940018176,58.085125719478945,129.4447137231142,130.6095122690094,130.87428052105423,143.18963950318084,164.95243865495306,191.7325053014238);
		learnedMeans.put(vector_1, 3315);

		Vector vector_2 = new Vector(8);
		setVectorValues(vector_2, 72.84295105916728,44.72936449963477,77.86303871439007,78.12125639152666,78.0262965668371,92.8718042366691,255.0,181.44119795471147);
		learnedMeans.put(vector_2, 2755);

		Vector vector_3 = new Vector(8);
		setVectorValues(vector_3, 193.00937081659973,230.88219544846052,201.15261044176708,198.70281124497993,192.94065149486838,222.9723337795627,238.83132530120483,241.30031236055333);
		learnedMeans.put(vector_3, 2245);

		Vector vector_4 = new Vector(8);
		setVectorValues(vector_4, 67.3178824721378,29.494427558257346,72.28470111448834,74.21023302938197,74.95997973657548,82.01646403242148,79.21023302938197,111.70770010131712);
		learnedMeans.put(vector_4, 3909);
	}

	private void train20_3(Hashtable<Vector, Integer> learnedMeans) {
		Vector vector_0 = new Vector(8);
		setVectorValues(vector_0, 194.1334944936879,230.6999731399409,200.0163846360462,198.33763094278808,194.41015310233684,219.77437550362612,235.87724952994895,238.39242546333602);
		learnedMeans.put(vector_0, 3708);

		Vector vector_1 = new Vector(8);
		setVectorValues(vector_1, 115.60595854922279,211.2,125.00699481865286,121.77020725388601,114.91528497409327,151.11062176165802,229.47538860103626,232.53108808290156);
		learnedMeans.put(vector_1, 3860);

		Vector vector_2 = new Vector(8);
		setVectorValues(vector_2, 139.75233806719777,61.921718046414966,145.56494631104954,146.89158295808798,147.40803602355388,155.5732594388639,187.60789747142363,208.45375822653273);
		learnedMeans.put(vector_2, 2877);

		Vector vector_3 = new Vector(8);
		setVectorValues(vector_3, 79.39948253557569,36.9627425614489,84.26235446313066,85.89573091849935,86.62897800776197,95.06934023285899,90.95472186287192,124.86778783958603);
		learnedMeans.put(vector_3, 3881);

		Vector vector_4 = new Vector(8);
		setVectorValues(vector_4, 75.79062957540263,37.03855539287457,81.56271351878965,81.98243045387994,82.25915080527086,95.87994143484627,255.0,185.96486090775988);
		learnedMeans.put(vector_4, 2058);
	}
	
	private void train20_4(Hashtable<Vector, Integer> learnedMeans) {
		Vector vector_0 = new Vector(8);
		setVectorValues(vector_0, 203.4578967792006,232.79937912301125,213.32945285215368,210.63057819169578,203.51920838183935,233.60651920838183,242.96585176561894,243.6988746604579);
		learnedMeans.put(vector_0, 2618);

		Vector vector_1 = new Vector(8);
		setVectorValues(vector_1, 73.42133594323465,30.236114509420112,79.19500856373868,81.72253486665035,82.73672620504037,84.88500122339124,94.22999755321752,123.10276486420358);
		learnedMeans.put(vector_1, 4003);

		Vector vector_2 = new Vector(8);
		setVectorValues(vector_2, 123.51234715895468,229.18340925437545,134.53440421961162,130.83960680891872,122.48933109566052,161.57875809158475,227.88036442100216,229.2409494126109);
		learnedMeans.put(vector_2, 4192);

		Vector vector_3 = new Vector(8);
		setVectorValues(vector_3, 83.36746522411129,62.966383307573416,90.50772797527048,90.20131375579598,89.43740340030912,100.09119010819165,254.93972179289025,226.5112055641422);
		learnedMeans.put(vector_3, 2626);

		Vector vector_4 = new Vector(8);
		setVectorValues(vector_4, 132.8757176629517,50.963188112124286,139.56636271529888,141.82708544410673,142.78520770010132,146.87504221546774,177.5292131036812,195.87301587301587);
		learnedMeans.put(vector_4, 2945);
	}
	
	private void train20_5(Hashtable<Vector, Integer> learnedMeans) {
		Vector vector_0 = new Vector(8);
		setVectorValues(vector_0, 81.82354609929078,44.33531914893617,88.96368794326241,89.01191489361702,88.6158865248227,97.32652482269503,255.0,212.81843971631207);
		learnedMeans.put(vector_0, 3528);

		Vector vector_1 = new Vector(8);
		setVectorValues(vector_1, 110.49714896650036,51.869208838203846,117.18888096935139,119.75374198146828,120.57305773342837,125.96044191019244,128.82038488952244,175.32216678545973);
		learnedMeans.put(vector_1, 2858);

		Vector vector_2 = new Vector(8);
		setVectorValues(vector_2, 183.03709090909092,225.65854545454545,193.21272727272728,190.43963636363637,182.97854545454547,216.09163636363635,235.44254545454547,235.38036363636363);
		learnedMeans.put(vector_2, 2750);

		Vector vector_3 = new Vector(8);
		setVectorValues(vector_3, 59.476478679504815,22.741953232462173,64.91086657496561,67.38019257221458,68.50756533700138,69.63053645116919,74.50756533700138,106.17028885832187);
		learnedMeans.put(vector_3, 3583);

		Vector vector_4 = new Vector(8);
		setVectorValues(vector_4, 108.2824427480916,226.97982551799345,120.32388222464559,116.13522355507088,106.96973827699018,148.11041439476554,232.62295528898582,232.2429116684842);
		learnedMeans.put(vector_4, 3665);
	}
}
