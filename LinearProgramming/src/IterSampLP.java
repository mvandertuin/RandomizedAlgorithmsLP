import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IterSampLP implements LPSolver {
	LPInstance lp;
	private long simplexDuration;
	
	public IterSampLP(LPInstance lp){
		this.lp = lp;
		this.simplexDuration = 0;
	}
	
	public long getSimplexDuration(){
		return simplexDuration;
	}
	
	public double[] solve() {
		Map<Constraint, Integer> w = new HashMap<Constraint, Integer>(lp.getH().size());
		for(Constraint c: lp.getH())
			w.put(c, 1);
				
		int n = lp.getH().size(); //Number of constraints
		int d = lp.getC().length; //Number of variables
		if(n < (9*Math.pow(d,2) )){
			SimplexApache sa = new SimplexApache(lp);
			long start = System.nanoTime();
			double[] ret = sa.solve();
			simplexDuration += sa.getSimplexDuration() + (System.nanoTime() - start);
			return ret;
		} 
		// V -> H
		List<Constraint> V = new ArrayList<Constraint>(lp.getH());
		double[] x = new double[0];
		while(V.size()>0){
			
			//r = 9d^2
			int r = 9* (int)Math.pow(d, 2);
			ArrayList<Constraint> R = chooseR(lp.getH(), w,  r);
			//Recursive call on the instance with constraint set R U S
			SimplexApache sa = new SimplexApache(new LPInstance(R, lp.getC()));
			long start = System.nanoTime();
			x = sa.solve();
			this.simplexDuration += sa.getSimplexDuration() + (System.nanoTime() - start);

			//V <- {all vertices in H that are violated by the values of x}
			V.clear();
			for(Constraint c: lp.getH()){
				if(!c.check(x))
					V.add(c);
			}
			
			if(weightConstraint(V, lp.getH(), w, d))
				for(Constraint c: V)
					w.put(c, 2*w.get(c));
		}
		return x;
	}
	
	private static boolean weightConstraint(List<Constraint> V, List<Constraint> H, Map<Constraint, Integer> w, int d){
		int vSum = 0, hSum = 0;
		for(Constraint c: V)
			vSum += w.get(c);
		for(Constraint c: H)
			hSum += w.get(c);
		int res = (2*hSum)/(9*d-1);
		return vSum <= res;
	}
	
	//Choose rSize constraints randomly from the set H
	private static ArrayList<Constraint> chooseR(List<Constraint> H, Map<Constraint, Integer> w, int rSize){
		ArrayList<Constraint> R = new ArrayList<Constraint>();
		List<Constraint> pool = new ArrayList<Constraint>(H);
		while(R.size() < rSize){
			int wSum = 0;
			for(Constraint c: pool)
				wSum += w.get(c);
			int pick = (int) (Math.random()*wSum);
			
			int current = 0;
			for(Constraint c: pool){
				if(pick>=current && pick < current + w.get(c)){
					R.add(c);
					pool.remove(c);
					break;
				}
				current += w.get(c);
			}				
		}
		return R;
	}
}
