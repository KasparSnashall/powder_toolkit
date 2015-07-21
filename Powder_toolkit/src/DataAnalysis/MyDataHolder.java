package DataAnalysis;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

public class MyDataHolder {
	
	private List<LoadedDataObject> datalist;

	public List<LoadedDataObject> getDatalist() {
		return datalist;
	}
	
	public void addData(String name, String flag, List<IDataset> data){
		datalist.add(new LoadedDataObject(name,flag,data));
	}
	
	public void delData(String name){
		for(int i=0; i < datalist.size();i++){
			LoadedDataObject loaded = datalist.get(i);
			if(loaded.name == name){
				datalist.remove(i);
			} 
		}
	}
	public LoadedDataObject getData(String name){
		for(int i=0; i < datalist.size();i++){
			LoadedDataObject loaded = datalist.get(i);
			if(loaded.name == name){
				return loaded;
			}
		}
		return null;
	}
	
}
