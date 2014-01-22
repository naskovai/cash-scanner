
public class Vector {
	private Point point;
	private double[] values;
	
	public Vector(int size, Point point) {
		this.point = point;
		this.values = new double[size];
	}
	
	public double getAt(int pos) {
		return values[pos];
	}
	
	public void setAt(int pos, double value) {
		values[pos] = value;
	}
}
