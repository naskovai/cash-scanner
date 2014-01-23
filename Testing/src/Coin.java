import java.util.HashMap;


public class Coin {
	private CoinTypes type;
	private HashMap<Vector, Integer> means;
	private Histogram textonsHistogram;
	
	public Coin(CoinTypes type, HashMap<Vector, Integer> means) {
		this.type = type;
		this.means = means;
	}

	public Histogram getHistogram() {
		if (textonsHistogram == null) {
			this.textonsHistogram = CoinProcessor.getInstance().getHistogram(this.means);
		}
		return textonsHistogram;
	}
	
	public CoinTypes getType() {
		return type;
	}
}
