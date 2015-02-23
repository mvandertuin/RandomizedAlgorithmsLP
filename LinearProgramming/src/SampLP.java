import java.util.ArrayList;
import java.util.List;

public class SampLP {
	LPInstance lp;
	
	public SampLP(LPInstance lp){
		this.lp = lp;
	}
	
	public double[] solve() {
		List<Constraint> S = new ArrayList<Constraint>();
		int n = lp.getH().size();
		int d = lp.getC().length;
		if(n < (9*d^2)){
			return new SimplexSolver(lp).solve();
		} 
		List<Constraint> V = new ArrayList<Constraint>();
		V.addAll(lp.getH());
		double[] x;
		while(V.size()>0){
			int r = (int) Math.round(Math.min(d*Math.sqrt(n), (double) (lp.getH().size() - S.size())));
			ArrayList<Constraint> RandS = chooseR(lp.getH(), S, r);
			RandS.addAll(S);
			x = new SampLP(new LPInstance(RandS, lp.getC())).solve();
			V = new ArrayList<Constraint>();
			for(Constraint c: lp.getH()){
				if(!c.check(x))
					V.add(c);
			}
			if(V.size() <= 2 * Math.sqrt(n))
				for(Constraint c: V)
					if(!S.contains(c))
						S.add(c);
		}
		return x;
	}
	
	private static ArrayList<Constraint> chooseR(List<Constraint> H, List<Constraint> S, int rSize){
		ArrayList<Constraint> R = new ArrayList<Constraint>();
		List<Constraint> pool = new ArrayList<Constraint>();
		pool.addAll(H);
		pool.removeAll(S);
		while(R.size() < rSize){
			Constraint c = pool.get((int) Math.random()*pool.size()); 
			R.add(c);
			pool.remove(c);
		}
		return R;
	}
}
