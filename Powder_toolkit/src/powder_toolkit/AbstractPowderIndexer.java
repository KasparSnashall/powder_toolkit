package powder_toolkit;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;

public abstract class AbstractPowderIndexer implements IPowderIndexer {
	
	
	public static Map<String,String> standard_dict = new HashMap<String, String>(); // have a blank dictionary
	public static Map<String,String> keywords = new HashMap<String,String>(); 
	public static String filepath = "";
	public static Dataset data = new DoubleDataset();
	public static final String title = "";
	public Map<String, String> getStandard_dict() {
		return standard_dict;
	}
	public void setStandard_dict(Map<String, String> standard_dict) {
		AbstractPowderIndexer.standard_dict = standard_dict;
	}
	public Map<String, String> getKeywords() {
		return keywords;
	}
	public void setKeywords(Map<String, String> keywords) {
		AbstractPowderIndexer.keywords = keywords;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		AbstractPowderIndexer.filepath = filepath;
	}
	public Dataset getData() {
		return data;
	}
	public void setData(Dataset data) {
		AbstractPowderIndexer.data = data;
	}
	public String getTitle() {
		return title;
	}
	

}
