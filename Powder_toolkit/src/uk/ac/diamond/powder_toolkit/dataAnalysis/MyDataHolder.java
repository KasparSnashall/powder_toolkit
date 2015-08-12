package uk.ac.diamond.powder_toolkit.dataAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

/**
 * Data holder is a list of dataobbjects used to hold all data in the program
 * 
 * @author sfz19839
 *
 */
public class MyDataHolder {

	public List<LoadedDataObject> datalist = new ArrayList<LoadedDataObject>(); // arraylist

	/**
	 * 
	 * @return the list of loaded data objects
	 */
	public List<LoadedDataObject> getDatalist() {
		return datalist;
	}

	/**
	 * Adds a loaded data object to the holder
	 * 
	 * @param name
	 *            string name of loadeddataobject
	 * @param flag
	 *            the file ending useful in previous version
	 * @param data
	 *            the list of Idatasets to be used
	 * @param filepath
	 *            the filepath of the original file, this is unused when a cell
	 *            in generated
	 */
	public void addData(String name, String flag, List<IDataset> data,
			String filepath) {
		datalist.add(new LoadedDataObject(name, flag, data, filepath));
	}

	/**
	 * Deletes a selected dataobject
	 * 
	 * @param name
	 *            the name of the data object
	 */
	public void delData(String name) {
		for (int i = 0; i < datalist.size(); i++) {
			LoadedDataObject loaded = datalist.get(i);
			if (loaded.name.equals(name)) {
				datalist.remove(i);
			}
		}
	}

	/**
	 * gets a single loadeddataobject based on the name
	 * 
	 * @param name
	 *            the name of the loaded data object
	 * @return a loaded data object
	 */
	public LoadedDataObject getData(String name) {
		for (int i = 0; i < datalist.size(); i++) {
			LoadedDataObject loaded = datalist.get(i);
			if (loaded.name.equals(name)) {
				return loaded;
			}
		}
		return null;
	}

	/**
	 * Returns a data set in a loadeddataobject
	 * 
	 * @param name
	 *            the name of the loadeddataobject
	 * @param columnname
	 *            the name of the IDataset
	 * @return IDataset
	 */
	public IDataset getDataSet(String name, String columnname) {
		for (int i = 0; i < datalist.size(); i++) {
			LoadedDataObject loaded = datalist.get(i);
			if (loaded.name.equals(name)) {
				List<IDataset> mydata = loaded.data;
				for (int j = 0; j < mydata.size(); j++) {
					if (mydata.get(j).getName().equals(columnname)) {
						return mydata.get(j);
					}

				}
			}

		}
		return null;
	}

	/**
	 * removes all data possibly unused
	 */
	public void delAllData() {
		datalist = new ArrayList<LoadedDataObject>();
	}

}
