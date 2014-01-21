package org.opencv.houghtransform;

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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.LinearLayout;

public class CanvasView extends LinearLayout {

	private Bitmap bmpOut, myBitmap;
	private Paint p = new Paint();
	
	public CanvasView(Context context, byte[] compressedImage) {
		super(context);
		init(compressedImage);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Render the output
		System.out.println("Check Draw");
		canvas.drawBitmap(bmpOut, 0, 0, p);

	}
	
	private void init(byte[] compressedImage) {
		
		Mat mImg = new Mat();
//		myBitmap = BitmapFactory.decodeResource(getResources(),
//				R.drawable.test3);
		setWillNotDraw(false);
		myBitmap = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);
		bmpOut = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(),
				Bitmap.Config.ARGB_8888);

		Utils.bitmapToMat(myBitmap, mImg);

		// Convert to gray scale
		Mat mGray = new Mat(mImg.rows(), mImg.cols(), CvType.CV_8UC1);
		Imgproc.cvtColor(mImg, mGray, Imgproc.COLOR_BGRA2GRAY);

		Mat mCanny = new Mat(mGray.rows(), mGray.cols(), CvType.CV_8UC1);

		// Add Gaussian blur to reduce noise
		Imgproc.GaussianBlur(mGray, mGray, new Size(7, 7), 0, 0);

		// Edge detection
		Imgproc.Canny(mGray, mCanny, 125d, 250d, 3, false);

		// Obtain an array with circles using Hough Transform algorithm
		Mat circles = new Mat();
		Imgproc.HoughCircles(mGray, circles, Imgproc.CV_HOUGH_GRADIENT, 1d,
				(double) mGray.height() / 10, 250d, 100d, 10, 400);

		if (circles.rows() == 1) {
			System.out.println("circleImage.cols(): " + circles.cols());
			for (int i = 0; i < circles.cols(); i++) {
				Core.circle(mImg,
						new Point(circles.get(0, i)[0], circles.get(0, i)[1]),
						(int) circles.get(0, i)[2], new Scalar(255d, 0d, 0d), 2);
			}
		}

		Mat[] coins = getCirclesMatrices(mImg, circles);

		// Convert back to a bitmap suitable for drawing
		Utils.matToBitmap(mImg, bmpOut);
	}

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

				outMatrices[i] = originalImage.submat(new Range(top, bottom),
						new Range(left, right));
			}
		}

		return outMatrices;
	}

}