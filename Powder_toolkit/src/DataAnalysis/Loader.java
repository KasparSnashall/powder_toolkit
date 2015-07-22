package DataAnalysis;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;

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
			else if(filepath.contains(".xy") | filepath.contains(".xye")){}
			return LoadXY();
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
				Slice mySlice = new Slice(Lower,Upper); 
				dataset = dh.getDataset(3).getSliceView(mySlice);
				dataset.setName("D_space");
				dataset2 = dh.getDataset(4).getSliceView(mySlice);
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
	
	@SuppressWarnings("deprecation")
	public static List<IDataset> LoadXY(){
		try{
			LoaderFactory.registerLoader(".xye", DatLoader.class);
			IDataHolder dh = LoaderFactory.getData(Filepath);
			IDataset column1 = dh.getDataset(0);
			column1.setName("Theta");
			IDataset column2 = dh.getDataset(1);
			column2.setName("Intensity");
			List<IDataset> dataholder = new ArrayList<IDataset>();
			if(range){
				IDataset ranged1 = column1.getSliceView(new Slice(Lower,Upper));
				IDataset ranged2 = column2.getSliceView(new Slice(Lower,Upper));
				dataholder.add(ranged1); // add in desired datasets
				dataholder.add(ranged2);	
			}
			else{
				dataholder.add(column1); // add in desired datasets
				dataholder.add(column2);	
				
			}
			return dataholder;

			}
		catch(Exception e){
			System.out.println(e.getMessage());
			}
		return null;
		}

	}
	

