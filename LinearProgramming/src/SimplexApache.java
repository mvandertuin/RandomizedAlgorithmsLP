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
	
	public SimplexApache(LPInstance lp){
		this.lp = lp;
	}
	
	public double[] solve() {
		// describe the optimization problem
		LinearObjectiveFunction f = new LinearObjectiveFunction(lp.getC(), 0);
		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		for(Constraint c: lp.getH())
			constraints.add(new LinearConstraint(c.getA(), Relationship.LEQ, c.getB()));
		//for(int i=0;i<lp.getC().length;i++){
		//	double[] vars = new double[lp.getC().length];
		//	vars[i] = 1;
		//	constraints.add(new LinearConstraint(vars, Relationship.GEQ, 0));
		//}
		
		// create and run the solver
		RealPointValuePair solution = null;
		try {
			solution = new org.apache.commons.math.optimization.linear.SimplexSolver().optimize(f, constraints, GoalType.MINIMIZE, true);
		} catch (OptimizationException e) {
			e.printStackTrace();
		}

		// get the solution
		double min = solution.getValue();
		return solution.getPoint();
	}
}
