

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;

import com.joptimizer.optimizers.LPStandardConverter;
import com.joptimizer.util.MPSParser;

public class MPSReader {

	public static void main(String[] args){
		
		try {			
	        PrintWriter printWriter = new PrintWriter(new File("MIPLIB_test.txt"));
	        printWriter.write("problemName foundObjective \n");

			File folder = new File(new URI(Thread.currentThread().getContextClassLoader().getResource("mps_files/miplib_complete").toString()));

	        File[] listOfFiles = folder.listFiles();
	        for (File file : listOfFiles) {
	            if (file.isFile()) {
	            	try{
	            		System.out.println(file.getName());
	            		MPSParser m = new MPSParser();
	            		LPStandardConverter lps = new LPStandardConverter();
	            		m.parse(file);
	            		lps.toStandardForm(m.getC(), m.getG(), m.getH(), m.getA(), m.getB(), m.getLb(), m.getUb());
	    				LPInstance lpi = transformToLPInstance(lps);
	    				SimplexSolver ss = new SimplexSolver(lpi);
	    				ss.solve();
		                printWriter.write(file.getName() + " " + ss.getSolution() + "\n");
		                printWriter.flush();			

	    			}
	    			catch(OutOfMemoryError e){ 
		                printWriter.write(file.getName() + " " + "heapSpace" + "\n");
		                printWriter.flush();	
	    			}
	            	catch(IllegalArgumentException e){
		                printWriter.write(file.getName() + " " + "tooLarge" + "\n");
		                printWriter.flush();			
	            	}
	            	catch(Exception e){
		                printWriter.write(file.getName() + " " + "someOtherError" + "\n");
		                printWriter.flush();			
	            	}

	            }
	        }
	        printWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static LPInstance transformToLPInstance(LPStandardConverter lps){
		double[] c = lps.getStandardC().toArray();
		ArrayList<Constraint> H = new ArrayList<Constraint>();
		
		for(int i=0; i<lps.getStandardB().size(); i++){
			double[] A = lps.getStandardA().viewRow(i).toArray();
			double b = lps.getStandardB().get(i);
			H.add(new Constraint(b,A));
		}
		
		LPInstance lpi = new LPInstance(H,c);
		
		return lpi;
		
	}
}
