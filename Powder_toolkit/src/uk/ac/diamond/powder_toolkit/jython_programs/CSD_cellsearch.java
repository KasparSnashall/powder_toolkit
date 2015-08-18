package uk.ac.diamond.powder_toolkit.jython_programs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Jython wrapper for a cell search python program, runs by creating and clling
 * a bash script
 * 
 * @author sfz19839
 *
 */
public class CSD_cellsearch {

	private static List<Double> cell_lengths = new ArrayList<Double>();
	private static List<Double> cell_angles = new ArrayList<Double>();
	private static int hits = 10;

	public static List<String> search() {
		try {
			PrintWriter writer = new PrintWriter(
					"/scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/csd.sh",
					"UTF-8");

			writer.write("source /dls_sw/apps/ccdc/scripts/ccdc-api.sh\n");
			writer.write("python /scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/CSD_cell_search.py ");
			// write in the lengths and angles
			for (double length : cell_lengths) {
				writer.write(length + " ");
			}
			for (double angle : cell_angles) {
				writer.write(angle + " ");
			}
			writer.write("primitive ");
			writer.write(String.valueOf(hits));
			writer.close();
		} catch (Exception e) {

		}

		final String cmd = " /scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/csd.sh";
		// TODO make this more permentant
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
				{myoutput.add(line);}
			}
			return myoutput;

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;

	}

	public void setCell_lengths(List<Double> cell_lengths) {
		try {
			if (cell_lengths.size() != 3) {
				throw new Exception("lengths not correct");
			}
			CSD_cellsearch.cell_lengths = cell_lengths;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setCell_angles(List<Double> cell_angles) {
		try {
			if (cell_angles.size() != 3) {
				throw new Exception("angles not correct");
			}
			CSD_cellsearch.cell_angles = cell_angles;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void set_hits(int hits) {
		CSD_cellsearch.hits = hits;

	}

}
