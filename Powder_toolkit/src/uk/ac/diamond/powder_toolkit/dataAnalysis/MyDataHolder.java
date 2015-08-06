package uk.ac.diamond.powder_toolkit.dataAnalysis;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

public class MyDataHolder {
	
	public List<LoadedDataObject> datalist = new ArrayList<LoadedDataObject>(); // arraylist

	public List<LoadedDataObject> getDatalist() {
		return datalist;
	}
	
	public void addData(String name, String flag, List<IDataset> data,String filepath){
		datalist.add(new LoadedDataObject(name,flag,data,filepath));
	}
	
	public void delData(String name){
		for(int i=0; i < datalist.size();i++){
			LoadedDataObject loaded = datalist.get(i);
			if(loaded.name.equals(name)){
				datalist.remove(i);
			} 
		}
	}
	public LoadedDataObject getData(String name){
		for(int i=0; i < datalist.size();i++){
			LoadedDataObject loaded = datalist.get(i);
			if(loaded.name.equals(name)){
				return loaded;
			}
		}
		return null;
	}

	
	public IDataset getDataSet(String name,String columnname){
		for(int i=0; i < datalist.size();i++){
			LoadedDataObject loaded = datalist.get(i);
			if(loaded.name.equals(name)){
				List<IDataset> mydata = loaded.data;
				for(int j = 0; j< mydata.size();j++){
					if(mydata.get(j).getName().equals(columnname)){
						return mydata.get(j);
					}
				
				}
			} 
		
	}
		return null;
	}
	
	public void delAllData(){
		datalist = new ArrayList<LoadedDataObject>();	
	}
	
	
}
