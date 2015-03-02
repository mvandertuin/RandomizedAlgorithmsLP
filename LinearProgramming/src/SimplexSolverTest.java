
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SimplexSolverTest {

	public static void main(String[] args) {
//		// Wikipedia example
//		double[] A1 = {3.0,2.0,1.0};
//		double b1 = 10;
//		Constraint h1 = new Constraint(b1,A1);
//		
//		double[] A2 = {2.0,5.0,3.0};
//		double b2 = 15;
//		Constraint h2 = new Constraint(b2,A2);
//		
//		ArrayList<Constraint> H = new ArrayList<Constraint>();
//		//for(int i = 0 ; i<50; i++){
//		//	H.add(new Constraint(b2,A2));
//		//}
//		
//		H.add(h1); H.add(h2);
//		
//		double[] c = {-2,-3,-4};
		
		List<LPInstance> instances = new ArrayList<LPInstance>();
		for(int d = 2;d<8;d++){
			int n = 10*d^3;
			for(int i=0;i<4;i++){
				LPInstance lps = LPInstance.generate(n, d);
				try {
					new SimplexSolver(lps).solve();
					instances.add(lps);
				} catch(Exception e){}
			}
		}
		System.out.println("****************************");
		for(LPInstance lp: instances){
			double[] x;
			x = new SimplexApache(lp).solve();
			System.out.println("AP: "+Arrays.toString(x));
			x = new SampLP(lp).solve();
			System.out.println("Sa: "+Arrays.toString(x));
			x = new IterSampLP(lp).solve();
			System.out.println("IS: "+Arrays.toString(x));
		}
		
		
		
	}

}
