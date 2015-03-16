import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Linear Programming instance  
 *
 */
public class LPInstance {

	ArrayList<Constraint> H;
	double[] c;

	/**
	 * Constructor of LPInstance
	 * @param h The list of constraints
	 * @param c Objective vector
	 */
	public LPInstance(ArrayList<Constraint> h, double[] c) {
		super();
		H = h;
		this.c = c;
	}

	/**
	 * Get the list of constraints
	 * @return The list of constraints
	 */
	public ArrayList<Constraint> getH() {
		return H;
	}

	/**
	 * Set the list of constraints
	 * @param h The list of constraints
	 */
	public void setH(ArrayList<Constraint> h) {
		H = h;
	}

	/**
	 * Get the objective vector
	 * @return The objective vector c
	 */
	public double[] getC() {
		return c;
	}

	/**
	 * Set the objective vector
	 * @param c The objective vector c
	 */
	public void setC(double[] c) {
		this.c = c;
	}

	/**
	 * Return the objective z value for a given vector x 
	 * @param x The vector x
	 * @return The objective value (sum of cTx)
	 */
	public double value(double[] x){
		double sum = 0;
		for(int i=0;i<c.length;i++){
			sum += c[i] * x[i];
		}
		return sum;
	}


	/**
	 * Generate an lp instance with given n and d
	 * @param n The number of constraints
	 * @param d The dimension
	 * @return A randomly generated lp instance
	 */
	public static LPInstance generate(int n, int d){
		double[] c =  new double[d];
		for(int i=0;i<d;i++)
			c[i] = 100.0*Math.random() - 50.0;
		ArrayList<Constraint> H = new ArrayList<Constraint>();
		for(int i=0;i<n;i++){
			double[] a =  new double[d];
			for(int j=0;j<d;j++)
				a[j] = 100.0*Math.random() - 50.0;
			H.add(new Constraint(Math.random()*100, a));
		}
		return new LPInstance(H, c);
	}

	/**
	 * Save LPInstance to a file
	 * The format of the file:
	 * constraints x
	 * variables i
	 * (foreach constraint)
	 * 		a1 a2 a3 ..
	 * 		b
	 * (foreach variable)
	 * 		v1
	 * @param f The file where the LP Instance should be saved to
	 */
	public void save(File f){
		try {
			PrintWriter printWriter = new PrintWriter(f);
			printWriter.write("constraints "+H.size()+"\n");
			printWriter.write("variables "+c.length+"\n");

			for(int j = 0; j<H.size();j++){
				String constraint = "";
				Constraint c = H.get(j);
				double[] a = c.getA();
				for(int k = 0; k<a.length;k++){
					constraint += a[k]+ " ";
				}
				constraint += "\n"+c.getB()+"\n";
				printWriter.write(constraint);
			}

			String variables ="";
			for(int i = 0; i<c.length;i++){
				variables += c[i]+"\n"; 
			}
			printWriter.write(variables);
			printWriter.flush();			
			printWriter.close();			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Read a file which contains a LPInstance (written to file by method save(f))
	 * @param f The file which should be read
	 * @return The LPInstance which was written into the file
	 */
	public static LPInstance read(File f){
		try{
			Scanner sc = new Scanner(f);
			sc.next(); //the word "constraints"
			int constraints = sc.nextInt();
			sc.nextLine();
			sc.next(); //the word "variables"
			int variables = sc.nextInt();
			sc.nextLine();

			ArrayList<Constraint> H = new ArrayList<Constraint>();
			for(int i = 0; i<constraints;i++){
				double[] a = new double[variables];
				for(int k=0; k<variables;k++){
					a[k] = Double.parseDouble(sc.next());
				}
				sc.nextLine();
				double b = Double.parseDouble(sc.next());
				sc.nextLine();
				H.add(new Constraint(b,a));
			}
			double[] c = new double[variables];
			for(int i = 0; i<variables;i++){
				c[i] = Double.parseDouble(sc.next());
				sc.nextLine();
			}
			sc.close();
			return new LPInstance(H,c);
		} catch(Exception e){
			System.out.println("Something has gone wrong");
			return null;
		}
	}
}
