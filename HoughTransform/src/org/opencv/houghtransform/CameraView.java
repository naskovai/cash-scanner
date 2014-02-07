package org.opencv.houghtransform;

import java.util.List;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;

public class CameraView extends JavaCameraView implements PictureCallback {

	private static final String TAG = "CameraView";
	private CameraCallback returnInterface;

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		returnInterface = (CameraCallback) context;
	}

	public List<String> getEffectList() {
		return mCamera.getParameters().getSupportedColorEffects();
	}

	public boolean isEffectSupported() {
		return (mCamera.getParameters().getColorEffect() != null);
	}

	public String getEffect() {
		return mCamera.getParameters().getColorEffect();
	}

	public void setEffect(String effect) {
		Camera.Parameters params = mCamera.getParameters();
		params.setColorEffect(effect);
		mCamera.setParameters(params);
	}

	public List<Size> getResolutionList() {
		return mCamera.getParameters().getSupportedPreviewSizes();
	}

	public void setResolution(Size resolution) {
		disconnectCamera();
		mMaxHeight = resolution.height;
		mMaxWidth = resolution.width;
		connectCamera(getWidth(), getHeight());
	}

	public Size getResolution() {
		return mCamera.getParameters().getPreviewSize();
	}

	public void takePicture() {
		Log.i(TAG, "Taking picture");

		Camera.Parameters params = mCamera.getParameters();
		List<Camera.Size> sizes = params.getSupportedPictureSizes();
		int maxHeight = 0;
		int maxWidth = 0;

		for (Camera.Size size : sizes) {
			if (size.height > maxHeight) {
				maxHeight = size.height;
				maxWidth = size.width;
			}
		}
		params.setPictureSize(maxWidth, maxHeight);
		mCamera.setParameters(params);
		
		// Postview and jpeg are sent in the same buffers if the queue is not
		// empty when performing a capture.
		// Clear up buffers to avoid mCamera.takePicture to be stuck because of
		// a memory issue
		mCamera.setPreviewCallback(null);

		// PictureCallback is implemented by the current class
		mCamera.takePicture(null, null, this);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.i(TAG, "Saving a bitmap to file. Size: " + data.length);
		// The camera preview was automatically stopped. Start it again.
		// mCamera.startPreview();
		// mCamera.setPreviewCallback(this);

		returnInterface.onPictureTaken(data);

	}
}
