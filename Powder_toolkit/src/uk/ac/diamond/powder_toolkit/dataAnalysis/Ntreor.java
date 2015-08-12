package uk.ac.diamond.powder_toolkit.dataAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.diamond.powder_toolkit.widgets.ErrorWidget;

/**
 * Ntreor indexing wrapper, all new indexing programs should have structure
 * similar to this
 * 
 * @author kaspar
 *
 */
public class Ntreor extends AbstractPowderIndexer implements IPowderIndexer {

	// keys
	public static String[] keys = { "WAVE", "KH", "KK", "KL", "KS", "THH",
			"THL", "THK", "THS", "OH1", "OK1", "OL1", "OS1", "OH2", "OK2",
			"OL2", "OS2", "OH3", "OK3", "OL3", "OS3", "MH1", "MK1", "ML1",
			"MS1", "MH2", "MK2", "ML2", "MS2", "MH3", "MK3", "ML3", "MS3",
			"MH4", "MK4", "ML4", "MS4", "MONOSET", "MONOGRAM", "MONO", "SHORT",
			"USE", "IQ", "LIST", "SELECT", "MERIT", "NIX", "IDIV", "VOL",
			"CEM", "D1", "SSQTL", "D2", "CHOICE", "DENS", "EDENS", "MOLW",
			"TRIC" };
	// values
	public static String[] standard_values = { "1.5405981", "4", "4", "4", "6",
			"4", "4", "4", "4", "2", "2", "2", "3", "2", "2", "2", "4", "2",
			"2", "2", "4", "2", "2", "2", "2", "2", "2", "2", "3", "2", "2",
			"2", "2", "2", "2", "2", "2", "0", "1", "0", "1", "19", "16", "0",
			"0", "10", "1", "1", "2000", "25", "0.002", "0.05", "0.0004", "0",
			"0", "0", "0", "0" };
	private IDataset data1;
	// tool tips
	private static String[] tooltips = {
			"WAVE 1.5405981 WAVE LENGTH IN ANG. AS A RULE ONE SHOULD NEVER CHANGE WAVE FROM 1.5405981.",
			"KH MAX H FOR CUBIC BASE LINE.",
			"KK  MAX K FOR CUBIC BASE LINE.",
			"KL MAX L FOR CUBIC BASE LINE.",
			"KS MAX H+K+L FOR THIS LINE.",
			"THH  MAX H FOR TETRAGONAL AND HEXAGONAL BASE LINES.",
			"THK MAX K FOR TETRAGONAL AND HEXAGONAL BASE LINES.",
			"THL MAX L FOR TETRAGONAL AND HEXAGONAL BASE LINES.",
			"THS MAX H+K+L FOR THESE LINES.",
			"OH1 MAX H FOR THE FIRST ORTHORHOMBIC BASE LINE.",
			"OK1 MAX K FOR THE FIRST ORTHORHOMBIC BASE LINE.",
			"OL1 MAX L FOR THE FIRST ORTHORHOMBIC BASE LINE.",
			"OS1  MAX H+K+L FOR THIS LINE.",
			" OH2  MAX H FOR THE SECOND ORTHORHOMBIC BASE LINE.",
			"OK2  MAX K FOR THE SECOND ORTHORHOMBIC BASE LINE.",
			"OL2 MAX L FOR THE SECOND ORTHORHOMBIC BASE LINE.",
			"OS2 MAX H+K+L FOR THIS LINE.",
			"OH3  MAX H FOR THE THIRD ORTHORHOMBIC BASE LINE.",
			"OK3  MAX K FOR THE THIRD ORTHORHOMBIC BASE LINE.",
			"OL3   MAX L FOR THE THIRD ORTHORHOMBIC BASE LINE.",
			"OS3 MAX H+K+L FOR THIS LINE.",
			" MH1 MAX ABS(H) FOR THE FIRST MONOCLINIC BASE LINE.",
			"MK1  MAX K FOR THE FIRST MONOCLINIC BASE LINE.",
			"ML1 MAX L FOR THE FIRST MONOCLINIC BASE LINE.",
			" MS1 MAX ABS(H)+K+L FOR THIS LINE.",
			"MH2 MAX ABS(H) FOR THE SECOND MONOCLINIC BASE LINE.",
			"MK2 MAX K FOR THE SECOND MONOCLINIC BASE LINE.",
			"ML2 MAX L FOR THE SECOND MONOCLINIC BASE LINE.",
			"MS2 MAX ABS(H)+K+L FOR THIS LINE.",
			"MH3 MAX ABS(H) FOR THE THIRD MONOCLINIC BASE LINE.",
			"MK3 MAX K FOR THE THIRD MONOCLINIC BASE LINE.",
			"ML3 MAX L FOR THE THIRD MONOCLINIC BASE LINE.",
			"MS3 MAX ABS(H)+K+L FOR THIS LINE.",
			"MH4 MAX ABS(H) FOR THE FOURTH MONOCLINIC BASE LINE.",
			"MK4 MAX K FOR THE FOURTH MONOCLINIC BASE LINE.",
			"ML4 MAX L FOR THE FOURTH MONOCLINIC BASE LINE.",
			"MS4 MAX ABS(H)+K+L FOR THIS LINE.",
			"MONOSET THIS PARAMETER MAKES IT POSSIBLE TO USE MORE THAN 3 SETS OF BASE LINES IN THE MONOCLINIC TRIALS.\nIF MONOSET IS:\nGREATER THAN 3 THE BASE LINE SET (1,3,4,5) WILL BE USED\nGREATER THAN 4 THE BASE LINE SET (1,2,3,6) WILL BE USED\nGREATER THAN 5 THE BASE LINE SET (2,3,4,5) WILL BE USED\nGREATER THAN 6 THE BASE LINE SET (1,2,3,7) WILL BE USED\nTHUS MAX 7 BASE LINE SETS CAN BE USED.",
			"MONOGAM THE BEST 5 TRIAL PARAMETER SETS STORED\n(SEE PARAMETER 'IQ') FOR EACH BASE LINE SET WILL BE\nREFINED BEFORE NEXT BASE LINE SET IS TRIED.\n IF MONOGAM=0 ALL BASE LINE SETS ARE TRIED BEFORE\nANY REFINEMENT IS PERFORMED.",
			"MONO MAX BETA ANGLE ALLOWED IN A MONOCLINIC CELL.",
			"SHORT SHORT AXIS TEST. ",
			"USE -OR EQ. TO THE NUMBER OF INPUT LINES IF THERE ARE LESS THAN 19 LINES",
			"IQ THE NUMBER OF INDEXABLE LINES REQUIRED IN THE TRIAL-\nINDEXING PROCEDURE IF THE CELL SHOULD BE STORED FOR\n EV. LEAST-SQUARES REFINEMENT.\nTHESE RECIPROCAL CELL PARAMETERS ARE PRINTED IF THE\nPARAMETER   LIST=1",
			"LIST SEE. ABOVE.",
			"IF 'SELECT' IS NON ZERO THE ORTHORHOMBIC BASE LINES ARE(SELECT,1,2) (SELECT,1,3) AND (SELECT,2,3)",
			"MERIT FIGURE OF MERIT REQUIRED AS STOP LIMIT.",
			"NIX IF A CELL AFTER LEAST SQUARES REFINEMENT HAS A FIGURE OF MERIT EQ. TO OR GREATER THAN THE PARAMETER  'MERIT' \nAND THE NUMBER OF NOT INDEXABLE LINES AMONG THE 'USE'\n FIRST LINES ARE LESS THAN OR EQ. TO 'NIX' THE CALCULATIONS ARE STOPPED.",
			"IDIV THE 7 FIRST LINES ARE ADJUSTED BY (EVENTUALLY OCCURRING) HIGHER ORDER LINES. IF IDIV=0  NO CORRECTIONS. \nUSUALLY THE DEFAULT VALUE 1 IS O.K. THERE ARE \nEXEPTIONS, HOWEVER. IF INDEXING IS NOT SUCCESSFUL IT IS RECOMMENDED TO TRY IDIV=0.",
			"VOL MAX CELL VOLUME (IN ANGSTROEM**3)",
			"CEM MAX CELL EDGE (IN ANGSTROEM)",
			"D1 DEFINED AS FOR PROGRAM PIRUM. (WERNER P.E. ARKIV KEMI 31(1969)513-516)",
			"SSQTL DEFINED AS FOR PROGRAM PIRUM.",
			"D2 DEFINED AS FOR PROGRAM PIRUM.",
			"CHOICE INDICATOR DEFINING 'SQ' ON CARD SET TWO. In the current program this is automatically chosen see java docs",
			"DENS DENSITY NOT USED.",
			"EDENS NOT USED UNLESS DENS EQ. NON ZERO.EDENS EQ. MAX. DEVIATION IN THE PARAMETER DENS.",
			"MOLW NOT USED UNLESS DENS ( AND EDENS ) EQ. NON ZERO  MOL. WEIGHT IN A.U. (OBS CRYSTAL WATER INCLUDED)",
			"TRIC NO TRICLINIC TEST." };

	/**
	 * Writes
	 */
	@Override
	public void write_input() {
		// data check
		for (int i = 0; i < data.size(); i++) {
			IDataset mydataset = data.get(i);
			System.out.println(mydataset.getName());
			// Accommodate for ntreor choices this is automatic
			if (mydataset.getName().contains("D_space")) {
				data1 = mydataset.getSliceView(new Slice(0, 20));
				keywords.put("CHOICE", "4");
			} else if (mydataset.getName().contains("Theta")) {
				data1 = mydataset.getSliceView(new Slice(0, 20));
				keywords.put("CHOICE", "2");
			} else if (mydataset.getName().contains("Two Theta")) {
				data1 = mydataset.getSliceView(new Slice(0, 20));
				keywords.put("CHOICE", "3");
			}
		}

		if (data1 == null) {
			try {
				throw new Exception("No data submitted");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String mytitle = title;
		if (mytitle != null && mytitle.contains(".")) {
			String[] mytitlelist = mytitle.split("\\.");
			mytitle = mytitlelist[0];
		}
		try {
			PrintWriter writer = new PrintWriter(filepath + title + ".dat",
					"UTF-8");
			writer.println(mytitle);
			// write in the data
			for (int i = 0; i < data1.getSize(); i++) {
				double d = data1.getDouble(i);
				String d1 = String.valueOf(d);
				writer.println(d1);
			}
			// write in the key words
			for (Entry<String, String> entry : keywords.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				writer.println(key + " = " + value + ",");
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
	public List<List<Double>> read_output() {
		List<List<Double>> output = new ArrayList<List<Double>>(); // my output
																	// string
																	// array
		try {
			String outputfile = filepath + title + ".short";
			BufferedReader br = new BufferedReader(new FileReader(outputfile));
			String line; // line variable

			while ((line = br.readLine()) != null) {
				// process the line.
				if (line.contains("The following cell has been selected for refinement by PIRUM:")) {
					try {
						String newline = "";
						for (int i = 0; i < 3; i++) {
							line = br.readLine(); // grab the 3 lines after

							newline += line;
						}
						System.out.println(newline);
						String[] out1 = newline.split("\\s+");
						List<Double> numeric_out = new ArrayList<Double>();
						for (String s : out1) {
							try {
								numeric_out.add(Double.parseDouble(s));
							} catch (NumberFormatException e) {
								// A string which doesn't represent a number
							}
						}
						output.add(numeric_out);// add the list of doubles
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return output; // cleaned output
	}

	@Override
	public List<String> Run() {
		List<String> output = new ArrayList<String>();
		try {
			Runtime rt = Runtime.getRuntime(); // exe
			// TODO make this a relative filepath
			Process process = rt
					.exec("/scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/ntreor"); // loaction
			DataOutputStream out = new DataOutputStream(
					process.getOutputStream()); // exe input
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// exe
																				// writter
			BufferedReader input = new BufferedReader(new InputStreamReader(
					process.getInputStream()));// exe output
			String line = null; // output
			bw.write("N\n"); // general info?
			bw.write(filepath + title + ".dat" + "\n"); // dat location
			bw.write(filepath + title + ".imp" + "\n"); // new imp location
			bw.write(filepath + title + ".con" + "\n"); // condensed location
			bw.write(filepath + title + ".short" + "\n"); // short location
			bw.write("0\n"); // theta shift
			bw.write("N\n"); // stop after 1 iteration?
			bw.flush(); // flush the output
			out.close(); // close the output
			while ((line = input.readLine()) != null) {
				// System.out.println(line);
				output.add(line);
			}

			int exitVal = process.waitFor();
			if (exitVal != 0) {
				throw new Exception("Ntreor exited with Error code "
						+ String.valueOf(exitVal));
			}

		} catch (Exception e) {
			new ErrorWidget(e);
		}
		return output;
	}

	// overide the previous
	public String[] getKeys() {
		return keys;

	}

	// overide
	public String[] getStandard_values() {
		return standard_values;
	}

	@Override
	public String getTooltip(String itemname) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], tooltips[i]);
		}
		String tool = map.get(itemname);
		return tool;
	}

}
