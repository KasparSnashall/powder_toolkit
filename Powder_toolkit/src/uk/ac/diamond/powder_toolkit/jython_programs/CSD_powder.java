package uk.ac.diamond.powder_toolkit.jython_programs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSD_powder {
	public static String name; // the chemical name
	public static String option; // the option
	
	
	
	public static List<String> search_powder() {
		try {
			PrintWriter writer = new PrintWriter(
					"/scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/csdpowder.sh", // make new file
					"UTF-8");

			writer.write("source /dls_sw/apps/ccdc/scripts/ccdc-api.sh\n"); // call the ccdc module
			writer.write("python /scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/CSD_powder.py ");
			// write in the name and option of the search
			writer.write(name+" ");
			writer.write(option);
			writer.close();
		} catch (Exception e) {

		}

		final String cmd = " /scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/csd.sh";
		// TODO this is not suitable for long
		Process process;
		try {
			
			process = Runtime.getRuntime().exec("sh" + cmd);
			System.out.println(process.toString());
			List<String> myoutput = new ArrayList<String>();
			// BufferedWriter bw = new BufferedWriter(new
			// OutputStreamWriter(out));// exe writter
			BufferedReader output = new BufferedReader(new InputStreamReader(
					process.getInputStream()));// exe output
			String line;
			while ((line = output.readLine()) != null) {
				if(!line.contains("conda"))
					// read in lines the are of form i j
				{myoutput.add(line);}
			}
			return myoutput;

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;

	}
	
	

}
