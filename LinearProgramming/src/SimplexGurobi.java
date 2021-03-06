import gurobi.*;

/**
 * Solves the LP by using the Gurobi model. The relaxation is obtained by
 * having continuous variables between 0 and 1 instead of discrete vars
 * Result is stored in solution.
 */
public class SimplexGurobi implements LPSolver {

	LPInstance lp;
	double[] solutionVector;
	double solution;

	public SimplexGurobi(LPInstance lp) {
		this.lp = lp;
	}

	public double[] getSolutionVector() {
		return solutionVector;
	}

	public double getSolution() {
		return solution;
	}


	@Override
	public double[] solve() {
		try {
			GRBEnv env = new GRBEnv("lp.log");
			env.set(GRB.IntParam.OutputFlag, 0);
			GRBModel model = new GRBModel(env);


			// Create variables
			model.addVars(null,null,lp.getC(),null,null);

			// Integrate new variables
			model.update();

			// Set objective: minimize cT x
			GRBLinExpr expr = new GRBLinExpr();
			expr.addTerms(lp.getC(), model.getVars());
			model.setObjective(expr, GRB.MINIMIZE);

			// Add constraints
			int constraints = 0;
			for (Constraint h : lp.getH()) {
				// ArrayList Constraints: A[], b
				GRBLinExpr grbLinExpr = new GRBLinExpr();
				for (int i = 0; i < lp.getC().length; i++) {
					grbLinExpr.addTerm(h.getA()[i], model.getVar(i));
				}
				model.addConstr(grbLinExpr, GRB.LESS_EQUAL, h.getB(), "H"
						+ constraints);
				constraints++;
			}
			// Optimize model
			model.optimize();


			solution = model.get(GRB.DoubleAttr.ObjVal);

			//return solution vector x
			solutionVector = new double[lp.getC().length];
			for(int i = 0; i<lp.getC().length; i++){
				solutionVector[i] = model.getVar(i).get(GRB.DoubleAttr.X);
			}

			// Dispose of model and environment
			model.dispose();
			env.dispose();

			return solutionVector;

		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". "
					+ e.getMessage());
			return new double[0];
		}
	}

	@Override
	public long getSimplexDuration() {
		return 0;
	}

}
