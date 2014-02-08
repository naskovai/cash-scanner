package org.opencv.houghtransform;

import java.util.ArrayList;
import java.util.Hashtable;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;

public class ImageProcessor extends IntentService {
	
	public ImageProcessor() {
		super("ImageProcessor");
		
	}

	public ImageProcessor(String name) {
		super(name);
	}

	private class HoughCircle {
		Mat pixelMatrix;
		Point center;
		int radius;

		public HoughCircle(Mat pixelMatrix, Point center, int radius) {
			this.pixelMatrix = pixelMatrix;
			this.center = center;
			this.radius = radius;
		}

		public Mat getPixelMatrix() {
			return pixelMatrix;
		}

		public Point getCenter() {
			return center;
		}

		public int getRadius() {
			return radius;
		}
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		// Gets data from the incoming Intent
		byte[] compressedImage = workIntent.getByteArrayExtra("image");
		int screenWidth = workIntent.getIntExtra("displayWidth", 0);
		int screenHeight = workIntent.getIntExtra("displayHeight", 0);
		ResultReceiver resultReceiver = workIntent
				.getParcelableExtra("resultReceiver");
		
		Bitmap workingBitmap = null;
		Mat colorImage = new Mat();
		Mat workingImage = new Mat();
		Mat sobelImage = new Mat();

		// Load the image
		// workingBitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.test7);
		
		workingBitmap = BitmapFactory.decodeByteArray(compressedImage, 0,
				compressedImage.length);

		// Scale the image
		workingBitmap = scaleBitmap(workingBitmap, screenHeight, screenWidth);

		// Convert to pixel matrix
		Utils.bitmapToMat(workingBitmap, colorImage);

		// Convert to gray scale
		Imgproc.cvtColor(colorImage, workingImage, Imgproc.COLOR_BGRA2GRAY);

		// Add Gaussian blur to reduce noise
		Imgproc.GaussianBlur(workingImage, workingImage, new Size(7, 7), 0, 0);

		// Edge detection
		sobelImage = sobelFilter(workingImage);

		// Obtain an array with circles using Hough Transform algorithm
		Mat circles = getCircles(sobelImage);
		ArrayList<HoughCircle> houghCircles = getCirclesMatrices(workingImage, circles);

		// Process the coin candidates using MR8 Filter bank, K-means clustering
		// and histogram comparison
		int[] coinValues = getCoinValues(houghCircles);

		// Draw circles with specific color according to the value of the coin.
		// No circle if not coin.
		drawCircles(colorImage, coinValues, houghCircles);

		// Map coin values to labels (will use them in GUI)
		Hashtable<String, Integer> coinMap = mapCoinValues(coinValues);

		// Convert back to a bitmap suitable for drawing
		Utils.matToBitmap(colorImage, workingBitmap);

		// Send the processed image to activity
		Bundle b = new Bundle();
		b.putParcelable("resultImage", workingBitmap);
		b.putInt("1", coinMap.get("1"));
		b.putInt("2", coinMap.get("2"));
		b.putInt("5", coinMap.get("5"));
		b.putInt("10", coinMap.get("10"));
		b.putInt("20", coinMap.get("20"));
		b.putInt("50", coinMap.get("50"));
		b.putInt("100", coinMap.get("100"));

		resultReceiver.send(1337, b);
	}

	private Bitmap scaleBitmap(Bitmap originalBitmap, int displayHeight,
			int displayWidth) {
		double ratio = Math.min(
				displayWidth / (double) originalBitmap.getWidth(),
				displayHeight / (double) originalBitmap.getHeight());
		return Bitmap.createScaledBitmap(originalBitmap,
				(int) (originalBitmap.getWidth() * ratio),
				(int) (originalBitmap.getHeight() * ratio), false);
	}

	private Mat sobelFilter(Mat image) {
		Mat grad_x = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
		Mat grad_y = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
		Mat abs_grad_x = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
		Mat abs_grad_y = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
		Mat sobelImage = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);

		Imgproc.Sobel(image, grad_x, CvType.CV_16S, 1, 0, 3, 1, 0,
				Imgproc.BORDER_DEFAULT);
		Imgproc.Sobel(image, grad_y, CvType.CV_16S, 0, 1, 3, 1, 0,
				Imgproc.BORDER_DEFAULT);

		Core.convertScaleAbs(grad_x, abs_grad_x);
		Core.convertScaleAbs(grad_y, abs_grad_y);
		Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, sobelImage);
		return sobelImage;
	}

	private Mat getCircles(Mat image) {
		Mat circles = new Mat();
		Imgproc.HoughCircles(image, circles, Imgproc.CV_HOUGH_GRADIENT, 1d,
				(double) image.height() / 10, 175d, 20d, 30, image.height() / 3);
		return circles;
	}

	private int[] getCoinValues(ArrayList<HoughCircle> coins) {
		int[] coinValues = new int[coins.size()];

		for (int i = 0; i < coins.size(); i++) {
			System.out.println("Processing coin candidate: " + i);
			System.out.println("coins[i].getPixelMatrix().rows():"
					+ coins.get(i).getPixelMatrix().rows());
			System.out.println("coins[i].getPixelMatrix().cols():"
					+ coins.get(i).getPixelMatrix().cols());
			switch (CoinProcessor.getInstance().getCoinType(
					coins.get(i).getPixelMatrix())) {
			case OneFront:
				coinValues[i] = 1;
				break;
			case FiftyFront:
				coinValues[i] = 50;
				break;
			case FiveFront:
				coinValues[i] = 5;
				break;
			case LevFront:
				coinValues[i] = 100;
				break;
			case TenFront:
				coinValues[i] = 10;
				break;
			case TwentyFront:
				coinValues[i] = 20;
				break;
			case TwoFront:
				coinValues[i] = 2;
				break;
			default:
				coinValues[i] = -1;
				break;
			}
		}

		return coinValues;
	}

	private void drawCircles(Mat image, int[] coinValues, ArrayList<HoughCircle> circles) {
		for (int i = 0; i < circles.size(); i++) {
			switch (coinValues[i]) {
			case 1:
				Core.circle(image, circles.get(i).getCenter(),
						(int) circles.get(i).getRadius(), new Scalar(100d, 0d, 0d),
						2);
				break;
			case 2:
				Core.circle(image, circles.get(i).getCenter(),
						(int) circles.get(i).getRadius(), new Scalar(200d, 0d, 0d),
						2);
				break;
			case 5:
				Core.circle(image, circles.get(i).getCenter(),
						(int) circles.get(i).getRadius(), new Scalar(0d, 100d, 0d),
						2);
				break;
			case 10:
				Core.circle(image, circles.get(i).getCenter(),
						(int) circles.get(i).getRadius(), new Scalar(0d, 200d, 0d),
						2);
				break;
			case 20:
				Core.circle(image, circles.get(i).getCenter(),
						(int) circles.get(i).getRadius(), new Scalar(0d, 0d, 100d),
						2);
				break;
			case 50:
				Core.circle(image, circles.get(i).getCenter(),
						(int) circles.get(i).getRadius(), new Scalar(0d, 0d, 200d),
						2);
				break;
			case 100:
				Core.circle(image, circles.get(i).getCenter(), (int) circles.get(i)
						.getRadius(), new Scalar(255d, 255d, 255d), 2);
				break;
			default:
				break;
			}
		}
	}

	private ArrayList<HoughCircle> getCirclesMatrices(final Mat originalImage,
			final Mat circles) {
		int radius = 0;
		int left = 0;
		int right = 0;
		int top = 0;
		int bottom = 0;

		ArrayList<HoughCircle> outCircles = new ArrayList<HoughCircle>();
		Mat outMatrix = new Mat();
		System.out.println("circles.cols(): " + circles.cols());
		if (circles.rows() == 1) {
			for (int i = 0; i < circles.cols(); i++) {
				radius = (int) circles.get(0, i)[2];
				left = (int) circles.get(0, i)[0] - radius;
				right = (int) circles.get(0, i)[0] + radius;
				top = (int) circles.get(0, i)[1] - radius;
				bottom = (int) circles.get(0, i)[1] + radius;

				if (top >= 0 && bottom <= originalImage.rows() && left >= 0
						&& right <= originalImage.cols()) {
					outMatrix = originalImage.submat(new Range(top, bottom),
							new Range(left, right));

					outCircles.add(new HoughCircle(outMatrix, new Point(circles
							.get(0, i)[0], circles.get(0, i)[1]), radius));
				}
			}
		}

		return outCircles;
	}

	private Hashtable<String, Integer> mapCoinValues(int[] coinValues) {
		Hashtable<String, Integer> coinMap = new Hashtable<String, Integer>();

		coinMap.put("1", 0);
		coinMap.put("2", 0);
		coinMap.put("5", 0);
		coinMap.put("10", 0);
		coinMap.put("20", 0);
		coinMap.put("50", 0);
		coinMap.put("100", 0);

		for (int i = 0; i < coinValues.length; i++) {
			switch (coinValues[i]) {
			case 1:
				coinMap.put("1", coinMap.get("1") + 1);
				break;
			case 2:
				coinMap.put("2", coinMap.get("2") + 1);
				break;
			case 5:
				coinMap.put("5", coinMap.get("5") + 1);
				break;
			case 10:
				coinMap.put("10", coinMap.get("10") + 1);
				break;
			case 20:
				coinMap.put("20", coinMap.get("20") + 1);
				break;
			case 50:
				coinMap.put("50", coinMap.get("50") + 1);
				break;
			case 100:
				coinMap.put("100", coinMap.get("100") + 1);
				break;
			default:
				System.out.println("Not a coin");
				break;
			}
		}
		return coinMap;
	}
}
