

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import cern.colt.Arrays;
import cern.colt.matrix.DoubleMatrix1D;

import com.joptimizer.optimizers.LPStandardConverter;
import com.joptimizer.util.MPSParser;

public class MPSReader {

	public static void main(String[] args){
		MPSParser m = new MPSParser();
		LPStandardConverter lps = new LPStandardConverter();
		try {			
			//file should be present in bin folder
			m.parse("mps_files/atlanta-ip.mps");
			lps.toStandardForm(m.getC(), m.getG(), m.getH(), m.getA(), m.getB(), m.getLb(), m.getUb());
			LPInstance lpi = transformToLPInstance(lps);
			double[] x = new SimplexSolver(lpi).solve();
			System.out.println(Arrays.toString(x));
			double[] y = new SampLP(lpi).solve();
			System.out.println(Arrays.toString(y));
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
