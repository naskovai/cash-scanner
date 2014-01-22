package org.opencv.houghtransform;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class HoughTransform extends Activity implements CvCameraViewListener2, OnTouchListener, CameraCallback {
    private static final String TAG = "OCVSample::Activity";

    private CameraView mOpenCvCameraView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    getOpenCvCameraView().enableView();
                    getOpenCvCameraView().setOnTouchListener(HoughTransform.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.houghtransform_surface_view);

        setOpenCvCameraView((CameraView) findViewById(R.id.houghtransform_activity_java_surface_view));

        getOpenCvCameraView().setVisibility(SurfaceView.VISIBLE);
        getOpenCvCameraView().setCvCameraViewListener(this);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (getOpenCvCameraView() != null)
            getOpenCvCameraView().disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
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
        Log.i(TAG,"onTouch event");
        getOpenCvCameraView().takePicture();
        return false;
    }
    
	@Override
	public void onPictureTaken(final byte[] compressedImage) {
		Button reset = new Button(this);
		reset.setText("Try again");
		reset.setOnClickListener(new ResetClickListener(this));
		
		LinearLayout ll = (LinearLayout) new CanvasView(this, compressedImage);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setContentView(ll);
		
		ll.addView(reset, lp);
	}

	public CameraView getOpenCvCameraView() {
		return mOpenCvCameraView;
	}

	public void setOpenCvCameraView(CameraView mOpenCvCameraView) {
		this.mOpenCvCameraView = mOpenCvCameraView;
	}
}
