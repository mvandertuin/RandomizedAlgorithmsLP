import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;

/**
 * Runs the Simplex implementation of Apache 
 */
public class SimplexApache implements LPSolver {
	LPInstance lp;
	private long simplexDuration;
	LinearObjectiveFunction f;
	Collection<LinearConstraint> constraints;

	/**
	 * The constructor of SimplexApache converts the LPInstance into a LinearObjectiveFunction and a collection of constraints
	 * @param lp the LPInstance
	 */
	public SimplexApache(LPInstance lp){
		this.lp = lp;
		this.simplexDuration  = 0;
		// describe the optimization problem
		f = new LinearObjectiveFunction(lp.getC(), 0);
		constraints = new ArrayList<LinearConstraint>();
		for(Constraint c: lp.getH())
			constraints.add(new LinearConstraint(c.getA(), Relationship.LEQ, c.getB()));
	}

	@Override
	public long getSimplexDuration(){
		return simplexDuration;
	}

	@Override
	public double[] solve() {
		// create and run the solver
		org.apache.commons.math.optimization.linear.SimplexSolver solver = new org.apache.commons.math.optimization.linear.SimplexSolver();
		RealPointValuePair solution = null;
		try {
			solution = solver.optimize(f, constraints, GoalType.MINIMIZE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//You can get the solution value with 
		// double min = solution.getValue();
		simplexDuration = solver.count;
		//return the solution vector
		return solution.getPoint();
	}
}
