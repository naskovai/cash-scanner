package org.opencv.houghtransform;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.LinearLayout;

public class CanvasView extends LinearLayout {

	private Bitmap bmpOut;
	private Paint p = new Paint();

	public CanvasView(HoughTransform context, Bitmap image) {
		super(context);
		setWillNotDraw(false);
		bmpOut = image;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Render the output
		System.out.println("Check Draw");
		canvas.drawBitmap(bmpOut, 0, 0, p);

	}
}