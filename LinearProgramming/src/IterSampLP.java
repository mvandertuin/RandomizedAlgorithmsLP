import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IterSampLP {
	LPInstance lp;
	
	public IterSampLP(LPInstance lp){
		this.lp = lp;
	}
	
	public double[] solve() {
		Map<Constraint, Integer> w = new HashMap<Constraint, Integer>(lp.getH().size());
		for(Constraint c: lp.getH())
			w.put(c, 1);
		
		int n = lp.getH().size(); //Number of constraints
		int d = lp.getC().length; //Number of variables
		if(n < (9*d^2)){
			return new SimplexApache(lp).solve();
		} 
		
		List<Constraint> V = new ArrayList<Constraint>(lp.getH());
		double[] x = new double[0];
		while(V.size()>0){
			
			//r = 9d^2
			int r = (int) 9*d^2;
			ArrayList<Constraint> R = chooseR(lp.getH(), w,  r);
			//Recursive call on the instance with constraint set R U S
			x = new SimplexApache(new LPInstance(R, lp.getC())).solve();

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
			hSum = w.get(c);
		return vSum <= (2*hSum)/(9*d-1);
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
