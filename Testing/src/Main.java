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
		
		//String image = "./res/20_4.jpg";
		String image = "C:\\Users\\rumen\\Documents\\GitHub\\cash-scanner\\training\\100\\2.jpg";
		Mat img = Highgui.imread(image, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		test(img);
		//train(img, CoinTypes.LevFront);

		System.out.println("Success");
	}
	
	private static void test(Mat img) {
		CoinProcessor p = CoinProcessor.getInstance();
		CoinTypes type = p.getCoinType(img);
		
		System.out.println(type.toString());
		
		//Imgproc.filter2D(img, img, -1, new GaussianFilter(99,10).getKernel());
/*		
		MR8FilterBank filterBank = new MR8FilterBank();
		Mat[] responses = filterBank.getResponses(img).responses;
		
		String path = "./res/";
		for (int i = 0; i < responses.length; i++) {
			//normalizeFilterResponse(responses[i]);
			//normalizeIntensity(responses[i]);
			Highgui.imwrite(path + i + ".jpg", responses[i]);
		}*/
	}
	
	private static void train(Mat img, CoinTypes type) {
		CoinProcessor.getInstance().train(img, type);
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
