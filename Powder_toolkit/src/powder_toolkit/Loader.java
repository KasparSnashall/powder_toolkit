package powder_toolkit;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;

import uk.ac.diamond.scisoft.analysis.io.DatLoader;
import uk.ac.diamond.scisoft.analysis.io.LoaderFactory;

public class Loader {
	
	
	public static String Filepath;
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
	
	public List<IDataset> Load_data(String filepath) {
		Filepath = filepath;
		if(filepath.contains(".hkl")){
			return Loadhkl();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static List<IDataset> Loadhkl() {
		try {
			LoaderFactory.registerLoader(".hkl", DatLoader.class);
			IDataHolder dh = LoaderFactory.getData(Filepath);

			IDataset dataset = dh.getDataset(3); // d_space
			IDataset dataset2 = dh.getDataset(4); // f square
			List<IDataset> dataholder = new ArrayList<IDataset>();
			
			if(range){
				dataset = dh.getDataset(3).getSliceView(new Slice(Lower,Upper));
				dataset.setName("D_space");
				dataset2 = dh.getDataset(4).getSliceView(new Slice(Lower,Upper));
				dataset2.setName("Intensity");
				dataholder.add(dataset2); // add in desired datasets
				dataholder.add(dataset);	
			}
			else{
				dataset.setName("D_space");
				dataset2.setName("Intensity");
				dataholder.add(dataset2); // add in desired datasets
				dataholder.add(dataset);	
				
			}
			return dataholder;
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
		
	}
	

	}
	

