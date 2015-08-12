package uk.ac.diamond.powder_toolkit.dataAnalysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

/**
 * Abstract class used to create an indexing wrapper
 * 
 * @author kaspar
 *
 */
public abstract class AbstractPowderIndexer implements IPowderIndexer {

	public static Map<String, String> keywords = new HashMap<String, String>();
	public static String filepath = "";
	public static List<IDataset> data;
	public static String title = "";
	public static String[] keys; // set of standard keys in
	public static String[] standard_values;

	public String[] getKeys() {
		return keys;
	}

	public String[] getStandard_values() {
		return standard_values;
	}

	public void addKeyword(String key, String value) {
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

	public List<IDataset> getData() {
		return data;
	}

	public void setData(List<IDataset> data) {
		AbstractPowderIndexer.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String mytitle) {
		title = mytitle;
	}

	public void resetKeywords() {
		keywords = new HashMap<String, String>();
	}

}
