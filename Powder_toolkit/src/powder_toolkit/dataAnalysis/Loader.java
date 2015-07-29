package powder_toolkit.dataAnalysis;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.dawnsci.analysis.dataset.impl.Maths;

import uk.ac.diamond.scisoft.analysis.io.DatLoader;
import uk.ac.diamond.scisoft.analysis.io.LoaderFactory;

public class Loader {
	
	
	private static String Filepath;
	public static double Upper;
	public static double Lower;
	public static boolean range;
	private static int x1;
	private static int x2;
	

	public void setUpper(double upper) {
		Upper = upper;
		}

	public void setLower(double lower) {
		Lower = lower;
		}

	public void setRange(boolean range) {
		Loader.range = range;
		}
	
	@SuppressWarnings("deprecation")
	public List<IDataset> Load_data(String filepath,List<String> names,String flag,List<Integer> colnumbers) {
			try{
			Filepath = filepath;
			LoaderFactory.registerLoader(flag, DatLoader.class);
			IDataHolder dh = LoaderFactory.getData(Filepath);
			List<IDataset> dataholder = new ArrayList<IDataset>(); // the list of datasets
		
			
			
			
			for(int i = 0; i < colnumbers.size(); i ++){
				IDataset column = dh.getDataset(colnumbers.get(i));
				column.setName(names.get(i));
				dataholder.add(column);}
			if(range){
				dataholder = RangeData(names, dataholder);
				return dataholder;
				}
				
			return dataholder;
			}
			catch(Exception e){System.out.println(e.getMessage());}
			return null;
			}
	
	
	private static List<IDataset> RangeData(List<String> names, List<IDataset> dataholder){
		
	
	for(IDataset col : dataholder){
				if(!col.getName().equals("Intensity")){
					x1 = Maths.abs(Maths.subtract(col, Lower)).argMin();
					x2 = Maths.abs(Maths.subtract(col, Upper)).argMin();
					}
			}
	List<IDataset> rangeddataholder = new ArrayList<IDataset>();
	for(IDataset column : dataholder){
	if(x2 > x1){
		IDataset ranged = column.getSliceView(new Slice(x1,x2));
		ranged.setName(column.getName());
		rangeddataholder.add(ranged);
		}
	else{
		IDataset ranged = column.getSliceView(new Slice(x2,x1));
		ranged.setName(column.getName());
		rangeddataholder.add(ranged);
		}
	}
	
	return rangeddataholder;
		
	}
	
}
	

