package powder_toolkit.dataAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;

import powder_toolkit.widgets.ErrorWidget;
/**
 * Ntreor indexing wrapper, all new indexing programs should have structure similar to this
 * @author kaspar
 *
 */
public class Ntreor extends AbstractPowderIndexer implements IPowderIndexer{
	
	//keys
	public static String[] keys = {"WAVE","KH","KK","KL","KS","THH","THL","THK","THS","OH1","OK1","OL1","OS1","OH2","OK2","OL2","OS2","OH3","OK3","OL3","OS3","MH1","MK1","ML1",
            "MS1","MH2","MK2","ML2","MS2","MH3","MK3","ML3","MS3","MH4","MK4","ML4","MS4",
            "MONOSET","MONOGRAM","MONO","SHORT","USE","IQ","LIST","SELECT","MERIT",
            "NIX","IDIV","VOL","CEM","D1","SSQTL","D2",
            "CHOICE","DENS","EDENS","MOLW","TRIC"};
	//values
	public static String[] standard_values = {"1.5405981","4","4","4","6","4","4","4","4","2","2","2","3","2","2","2","4","2","2",
			"2","4","2","2","2","2","2","2","2","3","2","2","2","2","2","2","2","2","0","1","0","1",
			"19","16","0","0","10","1","1","2000","25","0.002","0.05","0.0004",
            "0","0","0","0","0"};
	private IDataset data1;
	private List<String> celldata;
	//TODO add in cell data
	@Override
	public void write_input() {
		// data check
		for(int i= 0; i < data.size(); i++)
		{IDataset mydataset = data.get(i);
		System.out.println(mydataset.getName());
		if (mydataset.getName().contains("D_space")){
		data1 = mydataset.getSliceView(new Slice(0,20));}
		}
		if(data1 == null){
			try {
				throw new Exception("No data submitted");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String mytitle = title;
		if(mytitle != null && mytitle.contains(".")){
			String[] mytitlelist = mytitle.split("\\.");
			mytitle = mytitlelist[0];
		}
		try {
			PrintWriter writer = new PrintWriter(filepath+title+".dat", "UTF-8");
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
			writer.println(); // blank line
			writer.println("END*");
			writer.println("0.00");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<String> read_output() {
		List<String> output = new ArrayList<String>(); // my output array
		try{
		System.out.println(filepath);
		System.out.println(title);
		String outputfile = filepath+title+".short";
		System.out.println(outputfile);
		BufferedReader br = new BufferedReader(new FileReader(outputfile));
		String line; // line variable
		
		while ((line = br.readLine()) != null) {
		   // process the line.
		   if (line.contains("The following cell has been selected for refinement by PIRUM:"))
		   {
		    try {
		    String newline = "";
		    for(int i = 0; i<3;i++){
			line = br.readLine(); // grab the 3 lines after
			newline += line;
			
			}
		    output.add(newline); // append the output
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
	@Override
	public List<String> Run(){
		List<String> output = new ArrayList<String>();
		try{
			Runtime rt = Runtime.getRuntime(); // exe
			Process process = rt.exec("/scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/ntreor"); // loaction
			DataOutputStream out = new DataOutputStream(process.getOutputStream()); // exe input
		    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// exe writter
		    BufferedReader input = new BufferedReader( new InputStreamReader(process.getInputStream()));// exe output
		    System.out.println(filepath+title);
		    
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
	    	if(exitVal != 0){
	    		throw new Exception("Ntreor exited with Error code "+String.valueOf(exitVal));
	    	}
        	
			} 
		catch(Exception e) {
			new ErrorWidget(e);
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
