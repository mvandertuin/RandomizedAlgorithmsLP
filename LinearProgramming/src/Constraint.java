
public class Constraint {
	double b;
	double[] A;
	
	public Constraint(double b, double[] a) {
		super();
		this.b = b;
		A = a;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double[] getA() {
		return A;
	}
	public void setA(double[] a) {
		A = a;
	}
	
	
}
