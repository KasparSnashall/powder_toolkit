package powder_toolkit;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;

public interface IPowderIndexer {
	
	
	public Map<String, String> getKeywords();
	/**
	 * 
	 * @param keywords
	 * Map<string,string> set of keywords to be added to the indexing program
	 */
	public void setKeywords(Map<String, String> keywords);
	/**
	 * 
	 * @param key
	 * the key a string
	 * @param value
	 * a string usually in a numeric form
	 */
	public void addKeyword(String key,String value);
	/**
	 * Adds keywords to the keyword map
	 * @return
	 */
	public String[] getStandard_values();
	/**
	 * Returns the list of standard values defined in each program
	 * @return
	 */
	public String[] getKeys();
	/**
	 * Returns the key of the standard values
	 * @return
	 */
	public String getFilepath();
	/**
	 * 
	 * @param filepath
	 * The path to the run directory/ data
	 */
	public void setFilepath(String filepath);
	/**
	 * 
	 * @return
	 * Returns the run directory/data
	 */
	public Dataset getData();
	/**
	 * 
	 * @param data
	 * the dataset used in all programs usually just a single array with greater then 20 points
	 */
	public void setData(Dataset data);
	/**
	 * 
	 * @return
	 * Returns the data used in the indeing program
	 */
	public String getTitle();
	/**
	 * 
	 * @param mytitle
	 * Some programs have options for a title this is used only in that case
	 */
	public void setTitle(String mytitle);
	/**
	 * 
	 * @return
	 * Sets the title used in some indexing programs
	 */
	public List<String> read_output();
	/**
	 * Reads the output of the programs usually by parsing an output file
	 */
	public void write_input();
	/**
	 * Writes an input file for the indexing program
	 * @return
	 */
	public List<String> Run();
	/**
	 * Runs the indexing programs, using a runtime routine,
	 *  usually by passing it filenames
	 */
	public void resetKeywords();
	/**
	 * Resets the keywords map
	 */
}
