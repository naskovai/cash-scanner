package org.opencv.houghtransform;

import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

public class ResetClickListener implements OnClickListener {

	private HoughTransform mActivity;
	
	public ResetClickListener(HoughTransform mainActivity){
		mActivity = mainActivity;
	}
	
	@Override
	public void onClick(View arg0) {
		mActivity.setContentView(R.layout.houghtransform_surface_view);
		mActivity.setOpenCvCameraView((CameraView) mActivity.findViewById(R.id.houghtransform_activity_java_surface_view));
		mActivity.getOpenCvCameraView().setVisibility(SurfaceView.VISIBLE);
		mActivity.getOpenCvCameraView().setCvCameraViewListener(mActivity);
		mActivity.onResume();
	}

}
