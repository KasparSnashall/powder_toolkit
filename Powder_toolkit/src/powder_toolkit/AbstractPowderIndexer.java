package powder_toolkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;

public abstract class Execution {
	
	
	public static Map<String,String> standard_dict = new HashMap<String, String>(); // have a blank dictionary
	public static Map<String,String> keywords = new HashMap<String,String>(); 
	public static String filepath = "";
	public static Dataset data = new DoubleDataset();
	public static final String title = "";
	//public abstract void write_input();
	public abstract void read_output();

}
