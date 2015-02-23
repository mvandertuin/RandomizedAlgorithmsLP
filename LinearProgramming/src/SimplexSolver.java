import gurobi.*;

import java.util.HashMap;

public class SimplexSolver {

	LPInstance lp;
	
	public SimplexSolver(LPInstance lp){
		this.lp = lp;
	}
	
    /**
     * Solves the LP by using the Gurobi model.
     * The relaxation is obtained by having continuous variables between 0 and 1 instead of discrete vars
     * Result is stored in solution.
     */
    @Override
    public void solve() {
        try {
            GRBEnv env = new GRBEnv("lp.log");
            GRBModel model = new GRBModel(env);

            // Create variables

            //HashMap<Vertex, GRBVar> GRBvars = new HashMap<>();

            for (int i = 0; i<lp.getC().length; i++) {
                model.addVar(0.0, 1.0, 1.0, GRB.CONTINUOUS, Integer.toString(i));
                //GRBvars.put(vertex, v);
            }


            // Integrate new variables
            model.update();

            // Set objective: minimize cT x

            GRBLinExpr expr = new GRBLinExpr();
            for (GRBVar grbVar : GRBvars.values()) {
                expr.addTerm(1.0, grbVar);
            }
            model.setObjective(expr, GRB.MINIMIZE);

            // Add constraints from edges
            for (Edge edge : graph.getEdges()) {
                GRBLinExpr grbLinExpr = new GRBLinExpr();
                grbLinExpr.addTerm(1.0, GRBvars.get(edge.getA()));
                grbLinExpr.addTerm(1.0, GRBvars.get(edge.getB()));
                model.addConstr(grbLinExpr, GRB.GREATER_EQUAL, 1.0, edge.toString());

            }
            // Optimize model

            model.optimize();

            GRBVar[] fvars = model.getVars();
            double[] x = model.get(GRB.DoubleAttr.X, fvars);
            String[] vnames = model.get(GRB.StringAttr.VarName, fvars);

            for (int j = 0; j < fvars.length; j++) {
                if (x[j] >= 0.5) {
                    solution.add(graph.getVertex(Integer.parseInt(vnames[j])));
                }
            }

            // Dispose of model and environment

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

}
