
public interface LPSolver {
	/**
	 * Runs the algorithm
	 * @return The resulting vector x with the optimal solution
	 */
	public double[] solve();

	/**
	 * Returns the time required by performing the Simplex algorithm (which is called by the solver)  
	 * @return The time in nanoseconds
	 */
	public long getSimplexDuration();
}
