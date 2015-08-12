package uk.ac.diamond.powder_toolkit.dataAnalysis;

import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

/**
 * Loaded Data object is an object used to store all data in the program
 * 
 * @author sfz19839
 *
 */
public class LoadedDataObject {

	public String name;
	public String flag;
	public List<IDataset> data;
	public String filepath;

	public String getName() {
		return name;
	}

	public String getFlag() {
		return flag;
	}

	public List<IDataset> getData() {
		return data;
	}

	LoadedDataObject(String name, String flag, List<IDataset> data,
			String filepath) {
		this.name = name;
		this.flag = flag;
		this.data = data;
		this.filepath = filepath;
	}

}
