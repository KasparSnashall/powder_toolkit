package powder_toolkit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.osgi.framework.adaptor.FilePath;

public class Ntreor extends AbstractPowderIndexer {
	
	//keys
	public static String[] keys = {"KH","KK","KL","KS","THH","THL","THK","THS","OH1","OK1","OL1","OS1","OH2","OK2","OL2","OS2","OH3","OK3","OL3","OS3","MH1","MK1","ML1",
            "MS1","MH2","MK2","ML2","MS2","MH3","MK3","ML3","MS3","MH4","MK4","ML4","MS4",
            "MONOSET","MONOGRAM","MONO","SHORT","USE","IQ","LIST","SELECT","MERIT",
            "NIX","IDIV","WAVE","VOL","CEM","D1","SSQTL","D2",
            "CHOICE","DENS","EDENS","MOLW","TRIC"};
	//values
	public static String[] standard_values = {"4","4","4","6","4","4","4","4","2","2","2","3","2","2","2","4","2","2",
			"2","4","2","2","2","2","2","2","2","3","2","2","2","2","2","2","2","2","0","1","0","1",
			"19","16","0","0","10","1","1","1.5405981","2000","25","0.002","0.05","0.0004",
            "0","0","0","0","0"};

	public void write_input() {
		// create a dataset
		Dataset data1 = data.getSliceView(new Slice(0,20));
		String mytitle = title;
		if(mytitle != null && mytitle.contains(".")){
			String[] mytitlelist = mytitle.split("\\.");
			mytitle = mytitlelist[0];
		}
		try {
			PrintWriter writer = new PrintWriter("/scratch/runfiles/mytest.dat", "UTF-8");
			writer.println(mytitle);
			// write in the data
			for(int i =0; i< data1.getSize(); i++){
				double d = data1.getDouble(i);
				String d1 = String.valueOf(d);
				writer.println(d1);
			}
			// write in the key words
			for (Entry<String, String> entry : keywords.entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();
			    writer.println(key +" = "+value+",");
			}
			// finish file
			writer.println("END*");
			writer.println("0.00");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> read_output() {
		List<String> output = new ArrayList<String>(); // my output array
		try{
		BufferedReader br = new BufferedReader(new FileReader("/scratch/runfiles/treor90_output.imp"));
		String line; // line variable
		while ((line = br.readLine()) != null) {
		   // process the line.
		   if (line.contains("TOTAL NUMBER OF LINES ="))
		   {
		       try {
		    	for(int i = 0; i<3;i++){
				line = br.readLine(); // grab the 3 lines after
				output.add(line); // append the output
				}
		    	
			} catch (IOException e) {
				e.printStackTrace();
			}
		   }
		}
		br.close();
		}
		catch(Exception e){
			System.out.println(e);}
		
	return output; // cleaned output
	}

	public List<String> Run(){
		List<String> output = new ArrayList<String>();
		try{
			Runtime rt = Runtime.getRuntime(); // exe
			Process process = rt.exec("/scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/ntreor"); // loaction
			DataOutputStream out = new DataOutputStream(process.getOutputStream()); // exe input
		    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// exe writter
		    BufferedReader input = new BufferedReader( new InputStreamReader(process.getInputStream()));// exe output
		    
		    String line = null; // output
		    bw.write("N\n"); // general info?
		    bw.write(filepath+title+".dat"+"\n"); // dat location
            bw.write(filepath+title+".imp"+"\n"); // new imp location
            bw.write(filepath+title+".con"+"\n"); // condensed location
            bw.write(filepath+title+".short"+"\n"); // short location
            bw.write("0\n"); // theta shift
            bw.write("N\n"); // stop after 1 iteration?
            bw.flush(); // flush the output
            out.close(); // close the output
		    while((line=input.readLine()) != null) {
		            //System.out.println(line);
		            output.add(line);
		        }
		   
	    	int exitVal = process.waitFor();
        	System.out.println("Exited with error code "+exitVal);
			} 
		catch(Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			}
		return output;
	}

	// overide the previous
	public String[] getKeys(){
		return keys;
		
	}
	// overide
	public String[] getStandard_values(){
		return standard_values;
	}

}
