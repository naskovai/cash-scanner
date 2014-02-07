package org.opencv.houghtransform;
import java.util.Hashtable;

public class Coin implements java.io.Serializable {
	private CoinTypes type;
	private Hashtable<Vector, Integer> means;

	private transient Histogram textonsHistogram;
	
	public Coin(CoinTypes type, Hashtable<Vector, Integer> means) {
		this.type = type;
		this.means = means;
	}

	public Histogram getHistogram() {
		if (textonsHistogram == null) {
			this.textonsHistogram = CoinProcessor.getInstance(null).getHistogram(this.means);
		}
		return textonsHistogram;
	}
	
	public CoinTypes getType() {
		return type;
	}
	
	public Hashtable<Vector, Integer> getMeans() {
		return this.means;
	}
}
