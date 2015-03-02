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
	
	public double value(double[] x){
		double sum = 0;
		for(int i=0;i<c.length;i++){
			sum += c[i] * x[i];
		}
		return sum;
	}
	
//  Unused	
	public static LPInstance generate(int n, int d){
		double[] c =  new double[d];
		for(int i=0;i<d;i++)
			c[i] = 100.0*Math.random() - 50.0;
		ArrayList<Constraint> H = new ArrayList<Constraint>();
		for(int i=0;i<n;i++){
			double[] a =  new double[d];
			for(int j=0;j<d;j++)
				a[j] = 100.0*Math.random() - 50.0;
			H.add(new Constraint(Math.random()*100, a));
		}
		return new LPInstance(H, c);
	}

}
