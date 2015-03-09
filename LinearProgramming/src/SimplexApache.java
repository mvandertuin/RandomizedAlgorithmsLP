import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;


public class SimplexApache {
	LPInstance lp;
	public long count;
	LinearObjectiveFunction f;
	Collection<LinearConstraint> constraints;
	
	public SimplexApache(LPInstance lp){
		this.lp = lp;
		this.count  = 0;
		// describe the optimization problem
		f = new LinearObjectiveFunction(lp.getC(), 0);
		constraints = new ArrayList<LinearConstraint>();
		for(Constraint c: lp.getH())
			constraints.add(new LinearConstraint(c.getA(), Relationship.LEQ, c.getB()));

	
	}
	
	public double[] solve() {
		// create and run the solver
		org.apache.commons.math.optimization.linear.SimplexSolver solver = new org.apache.commons.math.optimization.linear.SimplexSolver();
		RealPointValuePair solution = null;
		try {
			solution = solver.optimize(f, constraints, GoalType.MINIMIZE, true);
		} catch (OptimizationException e) {
			e.printStackTrace();
		}

		// get the solution
		double min = solution.getValue();
		count = solver.count;
		return solution.getPoint();
	}
}
