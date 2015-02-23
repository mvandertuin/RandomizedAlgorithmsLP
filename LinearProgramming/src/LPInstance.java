import java.util.ArrayList;


public class LPInstance {
	
	ArrayList<Constraint> H;
	double[] c;
	
	public LPInstance(ArrayList<Constraint> h, double[] c) {
		super();
		H = h;
		this.c = c;
	}
	public ArrayList<Constraint> getH() {
		return H;
	}
	public void setH(ArrayList<Constraint> h) {
		H = h;
	}
	public double[] getC() {
		return c;
	}
	public void setC(double[] c) {
		this.c = c;
	}
	
	
	

}
