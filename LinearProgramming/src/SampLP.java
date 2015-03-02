import java.util.ArrayList;
import java.util.List;

public class SampLP {
	LPInstance lp;
	
	public SampLP(LPInstance lp){
		this.lp = lp;
	}
	
	public double[] solve() {
		List<Constraint> S = new ArrayList<Constraint>();
		
		int n = lp.getH().size(); //Number of constraints
		int d = lp.getC().length; //Number of variables
		if(n < (9*d^2)){
			return new SimplexSolver(lp).solve();
		} 
		List<Constraint> V = new ArrayList<Constraint>(lp.getH());
		double[] x = new double[0];
		while(V.size()>0){
			//r = min{ d sqrt(n), |H\S|}
			ArrayList<Constraint> HminS = new ArrayList<Constraint>(lp.getH());
			HminS.removeAll(S);
			int r = (int) Math.round(Math.min(d*Math.sqrt(n), (double) HminS.size()));
			
			ArrayList<Constraint> RandS = chooseR(lp.getH(), S, r);
			for(Constraint c: S){
				if(!RandS.contains(c))
					RandS.add(c);
			}
			//Recursive call on the instance with constraint set R U S
			x = new SampLP(new LPInstance(RandS, lp.getC())).solve();

			//V <- {all vertices in H that are violated by the values of x}
			V.clear();
			for(Constraint c: HminS){
				if(!c.check(x))
					V.add(c);
			}
			//if |V| <= 2sqrt(n) then S <- S U V
			if(V.size() <= 2 * Math.sqrt(n))
				for(Constraint c: V)
					if(!S.contains(c))
						S.add(c);
		}
		return x;
	}
	
	//Choose rSize constraints randomly from the set H\S
	private static ArrayList<Constraint> chooseR(List<Constraint> H, List<Constraint> S, int rSize){
		ArrayList<Constraint> R = new ArrayList<Constraint>();
		List<Constraint> pool = new ArrayList<Constraint>(H);
		pool.removeAll(S);
		while(R.size() < rSize){
			Constraint c = pool.get((int) Math.random()*pool.size()); 
			R.add(c);
			pool.remove(c);
		}
		return R;
	}
}
