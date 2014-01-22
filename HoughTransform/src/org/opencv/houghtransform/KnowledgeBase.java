package org.opencv.houghtransform;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import android.content.Context;

public class KnowledgeBase {

	public class Texton { 
		//TODO: ???		
	}
	
	public class CoinType {
		private int i_type;
		private Mat i_histogram;
		private Vector<Texton> i_textonList;
		
		public CoinType(int type){
			i_type = type;
		}

		public Mat getHistogram() {
			return i_histogram;
		}

		public void setHistogram(Mat i_histogram) {
			this.i_histogram = i_histogram;
		}

		public Vector<Texton> getTextonList() {
			return i_textonList;
		}

		public void setTextonList(Vector<Texton> i_textonList) {
			this.i_textonList = i_textonList;
		}
	}

	private Vector<CoinType> i_classList;
	private String i_databasePath;
	private Context i_context;
	@SuppressWarnings("unchecked")
	public KnowledgeBase(Context context, String fileName){
		FileInputStream fis;
		ObjectInputStream is;
		try {
			fis = context.openFileInput(fileName);
			is = new ObjectInputStream(fis);
			i_classList = (Vector<CoinType>) is.readObject();
			i_databasePath = fileName;
			i_context = context;
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	 
	public Vector<CoinType> getClassList(){
		return i_classList;
	}
	
	public void setClassList(Vector<CoinType> classList){
		i_classList = classList;
	}
	
	public void serializeClassList(){
		FileOutputStream fos;
		try {
			fos = i_context.openFileOutput(i_databasePath, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
//////////////////////////////////////////////////////////////////////////////////	
//  CURRENTLY A MYSTERY. MIGHT BE USEFUL LATER  //////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//	
//	private double l2Norm(Mat image){
//		double norm = 0;
//	    for (int i = 0; i < image.cols(); i++){
//	    	for (int j = 0; j < image.rows(); j++){
//		    	norm += image.get(j, i)[0] * image.get(j, i)[0];
//		    }
//	    }
//	    
//	    return Math.sqrt(norm);
//	}
//	
//	private void normalizeFilterResponse(Mat filterResponse){
//		double l2norm = l2Norm(filterResponse);
//		
//	    for (int i = 0; i < filterResponse.cols(); i++){
//	    	for (int j = 0; j < filterResponse.rows(); j++){
//	    		double normalizedIntensity = filterResponse.get(j, i)[0] * Math.log(1 + l2norm / 0.03) / l2norm;
//	    		filterResponse.put(j, i, normalizedIntensity);
//		    }
//	    }
//	}
//////////////////////////////////////////////////////////////////////////////////	
	
	private void histogramTest(Mat firstImage, Mat secondImage){
		List<Mat> l1 = new Vector<Mat>();
		l1.add(firstImage);
		
		List<Mat> l2 = new Vector<Mat>();
		l2.add(secondImage);
		
		MatOfInt channels = new MatOfInt(0);
        Mat hist1 = new Mat();
        Mat hist2 = new Mat();
        MatOfInt histSize = new MatOfInt(256);
        MatOfFloat ranges = new MatOfFloat(0f, 255f);
		Imgproc.calcHist(l1, channels, new Mat(), hist1, histSize, ranges);
		Imgproc.calcHist(l2, channels, new Mat(), hist2, histSize, ranges);
		System.out.println(Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CHISQR));
	}
}
