

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;

import com.joptimizer.optimizers.LPStandardConverter;
import com.joptimizer.util.MPSParser;

/**
 * Reads an MPS (Mathematical Programming System) file and converts it into an LPInstance 
 * Next run it on Gurobi so all objectives are known
 *
 */
public class MPSReader {

	public static void main(String[] args){	
		runGurobi("mps_files/miplib_complete");
	}

	/**
	 * Read a MPS instance by using the LPStandardConverter from JOptimizer
	 * Next convert it into an LPInstance
	 * @param file The file of the MPS instance
	 * @return An LPInstance of the file
	 */
	private static LPInstance read(File file){
		try{
			MPSParser m = new MPSParser();
			LPStandardConverter lps = new LPStandardConverter();
			m.parse(file);
			lps.toStandardForm(m.getC(), m.getG(), m.getH(), m.getA(), m.getB(), m.getLb(), m.getUb());
			LPInstance lpi = transformToLPInstance(lps);
			return lpi;
		}
		catch(OutOfMemoryError e){ 
			System.out.println(file.getName() + " " + "heapSpace" + "\n");
		}
		catch(IllegalArgumentException e){
			System.out.println(file.getName() + " " + "tooLarge" + "\n");
		}
		catch(Exception e){
			System.out.println(file.getName() + " " + "someOtherError" + "\n");
		}
		return null;
	}

	/**
	 * Run Gurobi on all mps files in a given folder and save the results in MPS_test.txt
	 * @param foldername
	 */
	private static void runGurobi(String foldername){
		try {			
			PrintWriter printWriter = new PrintWriter(new File("MPS_test.txt"));
			printWriter.write("problemName foundObjective \n");

			File folder = new File(new URI(Thread.currentThread().getContextClassLoader().getResource(foldername).toString()));

			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					LPInstance lpi = read(file);
					if(lpi != null){
						SimplexGurobi ss = new SimplexGurobi(lpi);
						ss.solve();
						printWriter.write(file.getName() + " " + ss.getSolution() + "\n");
					}
					else{
						printWriter.write(file.getName() + " error\n");
					}
					printWriter.flush();
				}
			}
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Converts the LPStandardConverter object into an LPInstance (which can be used for the LPSolvers)
	 * @param lps The LPStandardConverter instance
	 * @return The transformed instance (with identical values)
	 */
	public static LPInstance transformToLPInstance(LPStandardConverter lps){
		double[] c = lps.getStandardC().toArray();
		ArrayList<Constraint> H = new ArrayList<Constraint>();

		for(int i=0; i<lps.getStandardB().size(); i++){
			double[] A = lps.getStandardA().viewRow(i).toArray();
			double b = lps.getStandardB().get(i);
			H.add(new Constraint(b,A));
		}

		return new LPInstance(H,c);

	}
}
