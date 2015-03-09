import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class LPInstance {
	
	ArrayList<Constraint> H;
	double[] c;
	
	public LPInstance(ArrayList<Constraint> h, double[] c) {
		super();
		H = h;
		this.c = c;
	}
	public ArrayList<Constraint> getH() {
		return H;
	}
	public void setH(ArrayList<Constraint> h) {
		H = h;
	}
	public double[] getC() {
		return c;
	}
	public void setC(double[] c) {
		this.c = c;
	}
	
	public double value(double[] x){
		double sum = 0;
		for(int i=0;i<c.length;i++){
			sum += c[i] * x[i];
		}
		return sum;
	}
	
//  Unused	
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
	
	// Save LP Instance to file
	//Format:
	//constraints 20
	//variables 10
	// (foreach constraint)
		//a1 a2 a3 a4 ...
		//b
	// (foreach variable)
		//v1
		//v2
	
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

	public LPInstance read(File f){
		try{
		Scanner sc = new Scanner(f);
		sc.next(); //"constraints"
		int constraints = sc.nextInt();
		sc.nextLine();
		sc.next(); //"variables"
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
		return new LPInstance(H,c);
		} catch(Exception e){
			return null;
		}
	}
}
