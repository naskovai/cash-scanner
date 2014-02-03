package org.opencv.houghtransform;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.houghtransform.ImageProcessResultReceiver.Receiver;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class HoughTransform extends Activity implements CvCameraViewListener2,
		OnTouchListener, CameraCallback, Receiver {
	private static final String TAG = "OCVSample::Activity";

	private CameraView mOpenCvCameraView;
	private ImageProcessResultReceiver mResultReceiver;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");
				getOpenCvCameraView().enableView();
				getOpenCvCameraView().setOnTouchListener(HoughTransform.this);
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	public HoughTransform() {
		Log.i(TAG, "Instantiated new " + this.getClass());
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		mResultReceiver = new ImageProcessResultReceiver(new Handler());
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.houghtransform_surface_view);

		setOpenCvCameraView((CameraView) findViewById(R.id.houghtransform_activity_java_surface_view));

		getOpenCvCameraView().setVisibility(SurfaceView.VISIBLE);
		getOpenCvCameraView().setCvCameraViewListener(this);

	}

	@Override
	public void onPause() {
		super.onPause();
		mResultReceiver.setReceiver(null);
		if (getOpenCvCameraView() != null)
			getOpenCvCameraView().disableView();
	}

	@Override
	public void onResume() {
		super.onResume();
		mResultReceiver.setReceiver(this);
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this,
				mLoaderCallback);
	}

	public void onDestroy() {
		super.onDestroy();
		if (getOpenCvCameraView() != null)
			getOpenCvCameraView().disableView();
	}

	public void onCameraViewStarted(int width, int height) {
	}

	public void onCameraViewStopped() {
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		return inputFrame.rgba();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.i(TAG, "onTouch event");
		getOpenCvCameraView().takePicture();
		return false;
	}

	@Override
	public void onPictureTaken(final byte[] compressedImage) {
		DisplayMetrics displayMetrics = getBaseContext().getResources()
				.getDisplayMetrics();
		Intent imageProcessor = new Intent(this, ImageProcessor.class);
		mResultReceiver.setReceiver(this);

		imageProcessor.putExtra("image", compressedImage);
		imageProcessor.putExtra("displayHeight", displayMetrics.heightPixels);
		imageProcessor.putExtra("displayWidth", displayMetrics.widthPixels);
		imageProcessor.putExtra("resultReceiver", mResultReceiver);
		startService(imageProcessor);
	}

	public CameraView getOpenCvCameraView() {
		return mOpenCvCameraView;
	}

	public void setOpenCvCameraView(CameraView mOpenCvCameraView) {
		this.mOpenCvCameraView = mOpenCvCameraView;
	}

	@SuppressLint("NewApi")
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		System.out.println("RESULT: " + resultCode);
		Bitmap processedImage = (Bitmap) resultData
				.getParcelable("resultImage");

		Button reset = new Button(this);

		reset.setText("Try again");
		reset.setOnClickListener(new ResetClickListener(this));

		LinearLayout ll = (LinearLayout) new CanvasView(this, processedImage);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		String[] coinList = new String[CoinTypes.values().length];
		coinList[0] = "1 Stotinka: " + resultData.getInt("1", 0);
		coinList[1] = "2 Stotinki: " + resultData.getInt("2", 0);
		coinList[2] = "5 Stotinki: " + resultData.getInt("5", 0);
		coinList[3] = "10 Stotinki: " + resultData.getInt("10", 0);
		coinList[4] = "20 Stotinki: " + resultData.getInt("20", 0);
		coinList[5] = "50 Stotinki: " + resultData.getInt("50", 0);
		coinList[6] = "1 Lev: " + resultData.getInt("100", 0);

		for (int i = 0; i < coinList.length; i++) {
			TextView coinString = new TextView(this);
			coinString.setText(coinList[i]);
			coinString.setScaleX(0.6f);
			coinString.setScaleY(0.6f);
			LayoutParams lp1 = new LayoutParams(100, 120);
			ll.addView(coinString, lp1);
		}
		
		ll.addView(reset, lp);
		
		setContentView(ll);
	}
}
