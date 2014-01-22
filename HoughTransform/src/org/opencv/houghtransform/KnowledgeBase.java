package org.opencv.houghtransform;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Vector;

import org.opencv.core.Mat;

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
}
