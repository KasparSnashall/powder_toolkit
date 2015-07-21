package DataAnalysis;
import java.util.List;
import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

public class LoadedDataObject {
	String name;
	String flag;
	List<IDataset> data;
	
	LoadedDataObject(String name, String flag,List<IDataset> data){
		this.name = name;
		this.flag = flag;
		this.data = data;
	}

}
