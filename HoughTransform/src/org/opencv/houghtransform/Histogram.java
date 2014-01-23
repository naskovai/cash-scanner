package org.opencv.houghtransform;
import java.util.Hashtable;

public class Histogram extends Hashtable<Integer, Double> {
		public Double distance(final Histogram computedHistogram){
			Double chiSq = 0d;
			
			for (int i = 0; i < this.size(); i++){
				Double h1 = this.get(i);
				Double h2 = computedHistogram.get(i);
				chiSq += ((h2 - h1) * (h2 - h1)) / (h1 + h2);
			}
			
			return chiSq;
		}
		
		public void normalize(){
			Double area = 0d;
			
			for (int i = 0; i < this.size(); i++){
				area += this.get(i);
			}
			
			for (int i = 0; i < this.size(); i++){
				this.put(i, this.get(i) / area);
			}
		}
	}