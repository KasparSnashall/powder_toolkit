package DataAnalysis;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;

import uk.ac.diamond.scisoft.analysis.io.DatLoader;
import uk.ac.diamond.scisoft.analysis.io.LoaderFactory;

public class Loader {
	
	
	private static String Filepath;
	public static int Upper;
	public static int Lower;
	public static boolean range;
	

	public void setUpper(int upper) {
		Upper = upper;
		}

	public void setLower(int lower) {
		Lower = lower;
		}

	public void setRange(boolean range) {
		Loader.range = range;
		}
	
	public List<IDataset> Load_data(String filepath,List<String> names,String flag,List<Integer> colnumbers) {
			try{
			Filepath = filepath;
			LoaderFactory.registerLoader(flag, DatLoader.class);
			IDataHolder dh = LoaderFactory.getData(Filepath);
			List<IDataset> dataholder = new ArrayList<IDataset>(); // the list of datasets
			for(int i = 0; i < colnumbers.size(); i ++){
				
				IDataset column = dh.getDataset(colnumbers.get(i));
				column.setName(names.get(i));
				if(range){
					IDataset ranged = column.getSliceView(new Slice(Lower,Upper));
					dataholder.add(ranged);	
				}
				else{
					dataholder.add(column); // add in desired datasets	
				}
				
			}
			return dataholder;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
			return null;
		}
	
}
	

