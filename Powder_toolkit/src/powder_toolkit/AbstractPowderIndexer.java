package powder_toolkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;

public abstract class AbstractPowderIndexer implements IPowderIndexer {
	
	
	public static Map<String,String> keywords = new HashMap<String,String>(); 
	public static String filepath = "";
	public static Dataset data = new DoubleDataset();
	public static String title = "";
	public static String[] keys; // set of standard keys in
	public static String[] standard_values;
	
	public  String[] getKeys() {
		return keys;
	}
	public  String[] getStandard_values() {
		return standard_values;
	}
	public void addKeyword(String key, String value){
		keywords.put(key, value);
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
	public void setTitle(String mytitle){
		title = mytitle;
	}
	public void resetKeywords(){
		keywords = new HashMap<String,String>();
	}
	
	

}
