package powder_toolkit;

import java.util.Map;

import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;

public interface IPowderIndexer {
	public Map<String, String> getStandard_dict();
	/**
	 * 
	 * @param standard_dict
	 * 
	 */
	public void setStandard_dict(Map<String, String> standard_dict);
	public Map<String, String> getKeywords();
	public void setKeywords(Map<String, String> keywords);
	public String getFilepath();
	public void setFilepath(String filepath);
	public Dataset getData();
	public void setData(Dataset data);
	public String getTitle();
	public void read_output();
	public void write_input();
}
