package org.opencv.houghtransform;

public interface CameraCallback {
	public void onPictureTaken(final byte[] compressedImage);
}
