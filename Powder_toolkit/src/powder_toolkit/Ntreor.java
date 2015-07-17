package powder_toolkit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;

public class Ntreor extends Execution {
	
	//keys
	public String[] keys = {"KH","KK","KL","KS","THH","THL","THK","THS","OH1","OK1","OL1","OS1","OH2","OK2","OL2","OS2","OH3","OK3","OL3","OS3","MH1","MK1","ML1",
            "MS1","MH2","MK2","ML2","MS2","MH3","MK3","ML3","MS3","MH4","MK4","ML4","MS4",
            "MONOSET","MONOGRAM","MONO","SHORT","USE","IQ","LIST","SELECT","MERIT",
            "NIX","IDIV","WAVE","VOL","CEM","D1","SSQTL","D2",
            "CHOICE","DENS","EDENS","MOLW","TRIC"};
	//values
	public String[] standard_values = {"4","4","4","6","4","4","4","4","2","2","2","3","2","2","2","4","2","2",
			"2","4","2","2","2","2","2","2","2","3","2","2","2","2","2","2","2","2","0","1","0","1",
			"19","16","0","0","10","1","1","1.5405981","2000","25","0.002","0.05","0.0004",
            "0","0","0","0","0"};

	public static void write_input() {
		// create a dataset
		Dataset data1 = data.getSliceView(new Slice(0,20));
		if(title.contains(".")){
			String mytitle = title.split(".")[0];
		}
		
		try {
			File file = new File("myfile.txt");
			FileWriter write = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void read_output() {
		// TODO Auto-generated method stub
		
	}

	public static List<String> Run(){
		List<String> output = new ArrayList<String>();
		try{
			Runtime rt = Runtime.getRuntime(); // exe
			Process process = rt.exec("/scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/ntreor"); // loaction
			DataOutputStream out = new DataOutputStream(process.getOutputStream()); // exe input
		    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// exe writter
		    BufferedReader input = new BufferedReader( new InputStreamReader(process.getInputStream()));// exe output
		    
		    String line = null; // output
		    bw.write("N\n"); // general info?
		    bw.write("../../../runfiles/treor90.dat"+"\n"); // dat location
            bw.write("../../../runfiles/a.imp"+"\n"); // new imp location
            bw.write("../../../runfiles/b.con"+"\n"); // condensed location
            bw.write("../../../runfiles/c.short"+"\n"); // short location
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
	public static void main(String[] args){
		//List<String> run = Run();
		//System.out.println(run);
		write_input();
	}
	
	
	////////////getters and setters ///////
	public static Map<String, String> getStandard_dict() {
		return standard_dict;
	}
	public static void setStandard_dict(Map<String, String> standard_dict) {
		Execution.standard_dict = standard_dict;
	}
	public static Map<String, String> getKeywords() {
		return keywords;
	}
	public static void setKeywords(Map<String, String> keywords) {
		Execution.keywords = keywords;
	}
	public static String getFilepath() {
		return filepath;
	}
	public static void setFilepath(String filepath) {
		Execution.filepath = filepath;
	}
	public static Dataset getData() {
		return data;
	}
	public static void setData(Dataset data) {
		Execution.data = data;
	}
	public static String getTitle() {
		return title;
	}

}
