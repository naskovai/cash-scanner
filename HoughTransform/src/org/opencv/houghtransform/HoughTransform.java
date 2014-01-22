package org.opencv.houghtransform;

import java.util.List;
import java.util.ListIterator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class HoughTransform extends Activity implements CvCameraViewListener2, OnTouchListener, CameraCallback {
    private static final String TAG = "OCVSample::Activity";

    private CameraView mOpenCvCameraView;
    private List<Size> mResolutionList;
    private MenuItem[] mEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mResolutionMenuItems;
    private SubMenu mResolutionMenu;

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
        getOpenCvCameraView().setMaxFrameSize(1280, 720);
        Log.i(TAG, "Height: " + getOpenCvCameraView().getHeight());
        Log.i(TAG, "Width: " + getOpenCvCameraView().getWidth());

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
    public boolean onCreateOptionsMenu(Menu menu) {
        List<String> effects = getOpenCvCameraView().getEffectList();

        if (effects == null) {
            Log.e(TAG, "Color effects are not supported by device!");
            return true;
        }

        mColorEffectsMenu = menu.addSubMenu("Color Effect");
        mEffectMenuItems = new MenuItem[effects.size()];

        int idx = 0;
        ListIterator<String> effectItr = effects.listIterator();
        while(effectItr.hasNext()) {
           String element = effectItr.next();
           mEffectMenuItems[idx] = mColorEffectsMenu.add(1, idx, Menu.NONE, element);
           idx++;
        }

        mResolutionMenu = menu.addSubMenu("Resolution");
        mResolutionList = getOpenCvCameraView().getResolutionList();
        mResolutionMenuItems = new MenuItem[mResolutionList.size()];

        ListIterator<Size> resolutionItr = mResolutionList.listIterator();
        idx = 0;
        while(resolutionItr.hasNext()) {
            Size element = resolutionItr.next();
            mResolutionMenuItems[idx] = mResolutionMenu.add(2, idx, Menu.NONE,
                    Integer.valueOf(element.width).toString() + "x" + Integer.valueOf(element.height).toString());
            idx++;
         }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item.getGroupId() == 1)
        {
            getOpenCvCameraView().setEffect((String) item.getTitle());
            Toast.makeText(this, getOpenCvCameraView().getEffect(), Toast.LENGTH_SHORT).show();
        }
        else if (item.getGroupId() == 2)
        {
            int id = item.getItemId();
            Size resolution = mResolutionList.get(id);
            getOpenCvCameraView().setResolution(resolution);
            resolution = getOpenCvCameraView().getResolution();
            String caption = Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
            Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i(TAG,"onTouch event");
        getOpenCvCameraView().takePicture();
        return false;
    }

    public void onReset(){
    	System.out.println("Reset");
    }
    
	@Override
	public void onPictureTaken(final byte[] compressedImage) {
		//setContentView(R.layout.canvas_surface_view);
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
