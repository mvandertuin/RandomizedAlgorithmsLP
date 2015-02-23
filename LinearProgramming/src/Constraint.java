
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
	
	public boolean check(double[] x){
		int sum = 0;
		for(int i=0;i<x.length;i++){
			sum += A[i] * x[i];
		}
		return sum <= b;
	}
	
	public String toString() {
		String result = "";
		for(int i=0;i<A.length;i++){
			if(i!=0)
				result += " +";
			result += A[i]+"x"+i;
		}
		result += " = "+b;
		return result;
	}
	
}
