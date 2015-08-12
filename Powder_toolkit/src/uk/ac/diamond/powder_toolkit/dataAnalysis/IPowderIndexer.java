package uk.ac.diamond.powder_toolkit.dataAnalysis;

import java.util.List;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.swt.widgets.TableItem;

/**
 * Interface designed to be a wrapper for indexing algorithms
 * 
 * @author sfz19839
 *
 */
public interface IPowderIndexer {

	/**
	 * 
	 * @return returns a map of keywords and values used in the indexing program
	 */
	public Map<String, String> getKeywords();

	/**
	 * 
	 * @param keywords
	 *            Map<string,string> set of keywords to be added to the indexing
	 *            program
	 * 
	 */
	public void setKeywords(Map<String, String> keywords);

	/**
	 * Adds keyowrds to the keyword map
	 * 
	 * @param key
	 *            of indexing program keywords
	 * @param value
	 *            a string usually in a numeric form the value of keyword key
	 * 
	 */
	public void addKeyword(String key, String value);

	/**
	 * 
	 * @return the standard values of the indexing programs keywords for example
	 *         is x has a normal value 4 it will return 4
	 */
	public String[] getStandard_values();

	/**
	 * 
	 * @return Returns the list of standard keys
	 */
	public String[] getKeys();

	/**
	 * 
	 * @return the filepath used for creating a data file or running a data file
	 */
	public String getFilepath();

	/**
	 * 
	 * @param filepath
	 *            sets the filepath used to create dat or run dat files
	 * 
	 */
	public void setFilepath(String filepath);

	/**
	 * 
	 * @return returns the set of datasets used in the program
	 *
	 */
	public List<IDataset> getData();

	/**
	 * 
	 * @param data
	 *            the dataset used in the program usually just a single array
	 *            with greater then 20 points
	 * 
	 */
	public void setData(List<IDataset> data);

	/**
	 * 
	 * @return gets the title used to create the files
	 *
	 */
	public String getTitle();

	/**
	 * 
	 * @param title
	 *            is the string used in file creation and in some programs
	 *            operation
	 * 
	 */
	public void setTitle(String mytitle);

	/**
	 * 
	 * @return gets the clean output List of List of doubles that go
	 *         a,b,c,alpha,beta,gamma all indexing programs should return
	 *         something similar to this for cleaned output to work, future
	 *         developer may consider changing this.
	 * 
	 */
	public List<List<Double>> read_output();

	/**
	 * writes the input file, uses title file path and data
	 */
	public void write_input();

	/**
	 * Run runs the indexing program
	 * 
	 * @return a string list raw output
	 */
	public List<String> Run();

	/**
	 * Resets the keywords map
	 */
	public void resetKeywords();

	/**
	 * Optional function to get tooltips for properties widget
	 * 
	 * @param itemname
	 *            the name of the variable
	 * @return a string tool tip for that variable
	 */
	public String getTooltip(String itemname);

}
