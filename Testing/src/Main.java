import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		test("C:\\Users\\rumen\\Documents\\GitHub\\cash-scanner\\HoughTransform\\res\\drawable\\hope.png");
	}
	
	private static void test(String image) {
		Mat img = Highgui.imread(image, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Imgproc.resize(img, img, new Size(128, 128));
		normalizeIntensity(img);
		
		MR8FilterBank filter = new MR8FilterBank(17);
		
		Mat[] responses = filter.getResponses(img);
		
		//Imgproc.filter2D(img, img, -1, new GaussianFilter(99,10).getKernel());
		String path = "C:\\Users\\rumen\\Documents\\GitHub\\cash-scanner\\Testing\\res\\";
		for (int i = 0; i < responses.length; i++) {
			Highgui.imwrite(path + i + ".jpg", responses[i]);
		}
		System.out.println("Success");
	}
	
	 private static void normalizeIntensity(Mat image){
		  int maxInt = 0;
		  int minInt = 255;
		  
		  for (int i = 0; i < image.cols(); i++){
		   for(int j = 0; j < image.rows(); j++){
		    if (image.get(j, i)[0] > maxInt)
		     maxInt = (int) image.get(j, i)[0];
		    
		    if (image.get(j, i)[0] < minInt)
		     minInt = (int) image.get(j, i)[0];
		   }
		  }
		  
		  for (int i = 0; i < image.cols(); i++){
		   for(int j = 0; j < image.rows(); j++){
		    double normalizedValue = (image.get(j, i)[0] - minInt) * 255 / (maxInt - minInt);
		    image.put(j, i, normalizedValue);
		   }
		  }
		 }

}
