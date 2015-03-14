
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.joptimizer.optimizers.LPStandardConverter;
import com.joptimizer.util.MPSParser;


public class SimplexSolverTest {
	private static final int repetitions = 100;
	
	public static void main(String[] args) {
		//generateInstances();
		evaluateInstances();	
		
	}
	
	public static void evaluateInstances(){
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new File("LPInstance_test.csv"));
		} catch (FileNotFoundException e1) {}
		File folder = new File("lpinstances");
		printWriter.write("Instance,n,d,Apache total, Apache Simplex, SampLP total, SampLP Simplex, IterSampLP total, IterSampLP Simplex\n");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
            	try{
            		System.out.println(file.getName());
    				LPInstance lpi = LPInstance.read(file);
    				printWriter.write(file.getName() + ","+lpi.getH().size()+","+lpi.getC().length+",");
    				
    				long[] duration = new long[3];
    				long[] simplexDuration = new long[3];
    				for(int i=0;i<repetitions;i++){
    					LPSolver[] solvers = new LPSolver[3];
        				solvers[0] = new SimplexApache(lpi);
        				solvers[1] = new SampLP(lpi);
        				solvers[2] = new IterSampLP(lpi);
        				for(int j=0;j<3;j++){
        					LPSolver solver = solvers[j];
	    					long start = System.nanoTime();
	    					double[] x = solver.solve();
	    					duration[j] += System.nanoTime()-start;
	    					simplexDuration[j] += solver.getSimplexDuration();
	    					System.out.println(j+": "+Arrays.toString(x)+", "+((long) (solver.getSimplexDuration())));
        				}
    				}
    				for(int j=0;j<3;j++){
    					duration[j] /= repetitions*1e3;
    					simplexDuration[j] /= repetitions*1e3;
    				}
    				printWriter.write(duration[0]+","+simplexDuration[0]+",");
    				printWriter.write(duration[1]+","+simplexDuration[1]+",");
    				printWriter.write(duration[2]+","+simplexDuration[2]+"\n");
	                printWriter.flush();				
    			}
    			catch(OutOfMemoryError e){ 
	                printWriter.write(file.getName() + "," + "heapSpace" + "\n");
	                printWriter.flush();	
    			}
            	catch(IllegalArgumentException e){
	                printWriter.write(file.getName() + "," + "tooLarge" + "\n");
	                printWriter.flush();			
            	}
            	catch(StackOverflowError e){
	                printWriter.write(file.getName() + "," + "StackOverflow" + "\n");
	                printWriter.flush();			
            	}
            	catch(Exception e){
	                printWriter.write(file.getName() + "," + "someOtherError" + "\n");
	                printWriter.flush();			
            	}

            }
        }
        printWriter.close();
	}
	
	public static void generateInstances(){
		for(int d = 2;d<10;d++){
			int n = 10* ((int) Math.pow(d, 3));
			for(int i=0;i<4;i++){
				LPInstance lps = LPInstance.generate(n, d);
				try {
					new SimplexGurobi(lps).solve();
					lps.save(new File("lpinstances/n"+n+"_d"+d+"_"+i+".txt"));
				} catch(Exception e){i--;}
			}
		}
	}
	
	public static void wiki(){
		// Wikipedia example
		double[] A1 = {3.0,2.0,1.0};
		double b1 = 10;
		Constraint h1 = new Constraint(b1,A1);
		
		double[] A2 = {2.0,5.0,3.0};
		double b2 = 15;
		Constraint h2 = new Constraint(b2,A2);
		
		ArrayList<Constraint> H = new ArrayList<Constraint>();
		//for(int i = 0 ; i<50; i++){
		//	H.add(new Constraint(b2,A2));
		//}
		
		H.add(h1); H.add(h2);
		
		double[] c = {-2,-3,-4};
		
	}

}
