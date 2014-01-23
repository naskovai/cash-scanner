package org.opencv.houghtransform;

import java.util.Hashtable;
import java.util.Random;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.houghtransform.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CanvasView extends LinearLayout {

	private Bitmap bmpOut, myBitmap;
	private Paint p = new Paint();
	private HoughTransform context;

	public CanvasView(HoughTransform context, byte[] compressedImage) {
		super(context);
		this.context = context;
		init(compressedImage);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Render the output
		System.out.println("Check Draw");
		canvas.drawBitmap(bmpOut, 0, 0, p);

	}

	@SuppressLint("NewApi")
	private void init(byte[] compressedImage) {

		Mat mImg = new Mat();
		setWillNotDraw(false);
//		 myBitmap = BitmapFactory.decodeResource(getResources(),
//		 R.drawable.test7);
//
//		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
//		android.graphics.Point size = new android.graphics.Point();
//		display.getSize(size);
		myBitmap = BitmapFactory.decodeByteArray(compressedImage, 0,
				compressedImage.length);
		//myBitmap = Bitmap.createScaledBitmap(myBitmap, 533, 300, false);
		bmpOut = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(),
				Bitmap.Config.ARGB_8888);

		Utils.bitmapToMat(myBitmap, mImg);

		// Convert to gray scale
		Mat mGray = new Mat(mImg.rows(), mImg.cols(), CvType.CV_8UC1);
		Imgproc.cvtColor(mImg, mGray, Imgproc.COLOR_BGRA2GRAY);

		// Add Gaussian blur to reduce noise
		Imgproc.GaussianBlur(mGray, mGray, new Size(7, 7), 0, 0);

		// Edge detection
		Mat grad_x = new Mat(mGray.rows(), mGray.cols(), CvType.CV_8UC1);
		Mat grad_y = new Mat(mGray.rows(), mGray.cols(), CvType.CV_8UC1);
		Mat abs_grad_x = new Mat(mGray.rows(), mGray.cols(), CvType.CV_8UC1);
		Mat abs_grad_y = new Mat(mGray.rows(), mGray.cols(), CvType.CV_8UC1);
		Mat sobelImage = new Mat(mGray.rows(), mGray.cols(), CvType.CV_8UC1);

		Imgproc.Sobel(mGray, grad_x, -1, 1, 0, 3, 1, 0, Imgproc.BORDER_DEFAULT);
		Imgproc.Sobel(mGray, grad_y, -1, 0, 1, 3, 1, 0, Imgproc.BORDER_DEFAULT);

		Core.convertScaleAbs(grad_x, abs_grad_x);
		Core.convertScaleAbs(grad_y, abs_grad_y);
		Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, sobelImage);

		// Obtain an array with circles using Hough Transform algorithm
		Mat circles = new Mat();
		Imgproc.HoughCircles(sobelImage, circles, Imgproc.CV_HOUGH_GRADIENT,
				1d, (double) mGray.height() / 5, 175d, 20d, 50,
				mGray.height() / 3);

		if (circles.rows() == 1) {
			System.out.println("circleImage.cols(): " + circles.cols());
			for (int i = 0; i < circles.cols(); i++) {
				Core.circle(mImg,
						new Point(circles.get(0, i)[0], circles.get(0, i)[1]),
						(int) circles.get(0, i)[2], new Scalar(255d, 0d, 0d), 2);
			}
		}

		Mat[] coins = getCirclesMatrices(mGray, circles);
		getCoinValues(coins);
		// Convert back to a bitmap suitable for drawing
		Utils.matToBitmap(mImg, bmpOut);
	}

	@SuppressLint("NewApi")
	private void getCoinValues(Mat[] coins) {

		Hashtable<String, Integer> coinMap = new Hashtable<String, Integer>();
		coinMap.put("1", 0);
		coinMap.put("2", 0);
		coinMap.put("5", 0);
		coinMap.put("10", 0);
		coinMap.put("20", 0);
		coinMap.put("50", 0);
		coinMap.put("100", 0);

		for (int i = 0; i < coins.length; i++) {
			switch (CoinProcessor.getInstance().getCoinType(coins[i])) {
			case OneFront:
				coinMap.put("1", coinMap.get("1") + 1);
				break;
			case FiftyFront:
				coinMap.put("50", coinMap.get("50") + 1);
				break;
			case FiveFront:
				coinMap.put("5", coinMap.get("5") + 1);
				break;
			case LevFront:
				coinMap.put("100", coinMap.get("100") + 1);
				break;
			case TenFront:
				coinMap.put("10", coinMap.get("10") + 1);
				break;
			case TwentyFront:
				coinMap.put("20", coinMap.get("20") + 1);
				break;
			case TwoFront:
				coinMap.put("2", coinMap.get("2") + 1);
				break;
			default:
				System.out.println("Not a coin");
				break;
			}
		}

		String[] coinList = new String[CoinTypes.values().length];
		coinList[0] = "1 Stotinka: " + coinMap.get("1");
		coinList[1] = "2 Stotinki: " + coinMap.put("2", 0);
		coinList[2] = "5 Stotinki: " + coinMap.put("5", 0);
		coinList[3] = "10 Stotinki: " + coinMap.put("10", 0);
		coinList[4] = "20 Stotinki: " + coinMap.put("20", 0);
		coinList[5] = "50 Stotinki: " + coinMap.put("50", 0);
		coinList[6] = "1 Lev: " + coinMap.put("100", 0);

		for (int i = 0; i < coinList.length; i++) {
			TextView coinString = new TextView(context);
			coinString.setText(coinList[i]);
			coinString.setScaleX(0.5f);
			coinString.setScaleY(0.5f);
			LayoutParams lp = new LayoutParams(100, 120);
			this.addView(coinString, lp);
		}
	}

//	private enum CoinTypes {
//		None, OneFront, TwoFront, FiveFront, TenFront, TwentyFront, FiftyFront, LevFront
//	}
//
//	public CoinTypes getCoinType(Mat image) {
//		Random random = new Random();
//		return CoinTypes.values()[random.nextInt(CoinTypes.values().length)];
//	}

	private Mat[] getCirclesMatrices(final Mat originalImage, final Mat circles) {
		int radius = 0;
		int left = 0;
		int right = 0;
		int top = 0;
		int bottom = 0;

		Mat[] outMatrices = new Mat[circles.cols()];
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
					outMatrices[i] = originalImage.submat(
							new Range(top, bottom), new Range(left, right));
				} else {
					outMatrices[i] = originalImage.submat(new Range(0, 0),
							new Range(0, 0));
				}
			}
		}

		return outMatrices;
	}

}