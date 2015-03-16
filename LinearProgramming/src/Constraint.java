import java.util.Arrays;

/**
 * A constraint is a matrix row of Ax <= b, eg a1*x1 - a2*x2 + a3*x3 <= b
 */
public class Constraint {
	private static final double epsilon = 1e-6;
	double b;
	double[] A;

	/**
	 * Constructor for a constraint
	 * @param b The constraint should be less than or equal to b
	 * @param a The vector of coefficients of x
	 */
	public Constraint(double b, double[] a) {
		super();
		this.b = b;
		A = a;
	}

	/**
	 * Get the value of b
	 * @return b
	 */
	public double getB() {
		return b;
	}


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Constraint){
			Constraint other = (Constraint) obj;
			if(Arrays.equals(other.getA(),this.getA())){
				if(other.getB() == this.getB()){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Set the value of b to another value
	 * @param b the value
	 */
	public void setB(double b) {
		this.b = b;
	}

	/**
	 * Get the vector of coefficients a
	 * @return the vector of coefficients a
	 */
	public double[] getA() {
		return A;
	}

	/**
	 * Set the vector a to another value
	 * @param a the vector
	 */
	public void setA(double[] a) {
		A = a;
	}

	/**
	 * Check whether the constraint is feasible for a certain vector x (considering some epsilon) 
	 * @param x the vector x
	 * @return true if feasible
	 */
	public boolean check(double[] x){
		double sum = 0;
		for(int i=0;i<x.length;i++){
			sum += A[i] * x[i];
		}
		return sum <= b+epsilon;
	}

	@Override
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
