import java.util.ArrayList;


public class SimplexSolverTest {

	public static void main(String[] args) {
		// Wikipedia example
		double[] A1 = {3.0,2.0,1.0};
		double b1 = 10;
		Constraint h1 = new Constraint(b1,A1);
		
		double[] A2 = {2.0,5.0,3.0};
		double b2 = 15;
		Constraint h2 = new Constraint(b2,A2);
				
		ArrayList<Constraint> H = new ArrayList<Constraint>();
		H.add(h1); H.add(h2);
		
		double[] c = {-2,-3,-4};
		
		LPInstance lp = new LPInstance(H,c);
		new SimplexSolver(lp).solve();
		
	}

}
