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
		test("C:\\Users\\rumen\\Documents\\GitHub\\cash-scanner\\HoughTransform\\res\\drawable\\drawable\\test9.jpg");
	}
	
	private static void test(String image) {
		Mat img = Highgui.imread(image, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		CoinProcessor p = CoinProcessor.getInstance();
		p.getCoinType(img);
		
		//Imgproc.filter2D(img, img, -1, new GaussianFilter(99,10).getKernel());
	/*	String path = "C:\\Users\\rumen\\Documents\\GitHub\\cash-scanner\\Testing\\res\\";
		for (int i = 0; i < responses.length; i++) {
			//normalizeFilterResponse(responses[i]);
			//normalizeIntensity(responses[i]);
			Highgui.imwrite(path + i + ".jpg", responses[i]);
		}*/
		System.out.println("Success");
	}
	
	/* 
	 private static double l2Norm(Mat image){
		  double norm = 0;
		     for (int i = 0; i < image.cols(); i++){
		      for (int j = 0; j < image.rows(); j++){
		       norm += image.get(j, i)[0] * image.get(j, i)[0];
		      }
		     }
		     
		     return Math.sqrt(norm);
		 }
		 
		 private static void normalizeFilterResponse(Mat filterResponse){
		  double l2norm = l2Norm(filterResponse);
		  
		     for (int i = 0; i < filterResponse.cols(); i++){
		      for (int j = 0; j < filterResponse.rows(); j++){
		       double normalizedIntensity = filterResponse.get(j, i)[0] * Math.log(1 + l2norm / 0.03) / l2norm;
		       filterResponse.put(j, i, normalizedIntensity);
		      }
		     }
		 }
*/
}
