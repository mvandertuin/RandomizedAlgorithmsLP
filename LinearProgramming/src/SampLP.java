import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the SampLP algorithm by Clarkson
 */
public class SampLP implements LPSolver {
	LPInstance lp;
	private long simplexDuration;

	/**
	 * Constructor of SampLP
	 * @param lp Instance wich should be run
	 */
	public SampLP(LPInstance lp){
		this.lp = lp;
		this.simplexDuration = 0;
	}

	@Override
	public long getSimplexDuration(){
		return simplexDuration;
	}


	@Override
	public double[] solve() {
		List<Constraint> S = new ArrayList<Constraint>();
		int n = lp.getH().size(); //Number of constraints
		int d = lp.getC().length; //Number of variables

		if(n < (9* Math.pow(d,2))){
			SimplexApache sa = new SimplexApache(lp);
			long start = System.nanoTime();
			double[] ret = sa.solve();
			simplexDuration = simplexDuration + (System.nanoTime() - start);
			return ret;
		} 

		List<Constraint> V = new ArrayList<Constraint>(lp.getH());
		double[] x = new double[0];
		while(V.size()>0){
			//r = min{ d sqrt(n), |H\S|}
			ArrayList<Constraint> HminS = new ArrayList<Constraint>(lp.getH());
			HminS.removeAll(S);
			int r = (int) Math.round(Math.min(d*Math.sqrt(n), (double) HminS.size()));
			ArrayList<Constraint> copyHminS = new ArrayList<Constraint>(HminS);
			ArrayList<Constraint> RandS = chooseR(copyHminS, r);

			//RandS = RandS \cup S
			for(Constraint c: S){
				if(!RandS.contains(c))
					RandS.add(c);
			}

			//Recursive call on the instance with constraint set R U S
			SampLP slp =  new SampLP(new LPInstance(RandS, lp.getC()));
			x = slp.solve();
			simplexDuration += slp.simplexDuration;

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

	/**
	 * Choose rSize constraints randomly from a set
	 * @param pool The set from which constraints should be picked (generally H\S)
	 * @param rSize The number of constraints
	 * @return The list of randomly selected constraints
	 */
	private static ArrayList<Constraint> chooseR(List<Constraint> pool, int rSize){
		ArrayList<Constraint> R = new ArrayList<Constraint>();
		while(R.size() < rSize){
			int r = (int) (Math.random()*pool.size());
			Constraint c = pool.get(r); 
			R.add(c);
			pool.remove(c);
		}
		return R;
	}
}
