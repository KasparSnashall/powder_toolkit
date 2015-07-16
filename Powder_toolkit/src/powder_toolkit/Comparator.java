package powder_toolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.dataset.impl.BooleanDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.Comparisons;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetFactory;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetUtils;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.FFT;
import org.eclipse.dawnsci.analysis.dataset.impl.LinearAlgebra;
import org.eclipse.dawnsci.analysis.dataset.impl.Maths;
import org.eclipse.dawnsci.analysis.dataset.impl.Random;


public class Comparator {
	
	public Dataset data;
	public boolean optimise = false;
	public float upper;
	public float lower;
	
	
	
	public float binary(){
		data = new DoubleDataset(new double[]{1,2,3,4,4,5,6,7,8,9,9,6,4,3});
		
		if (data.getSize() == 0){ // check size
			return 0;}
		return (float) 1;
		
	}
	
	public void fractional(){
		
	}
	
	public Dataset slice_range(Dataset dataset) throws Exception{
		if (lower > upper){
			 throw new Exception("Invalid range");
		}
		if (lower  < 0 ||upper < 0.0){
			throw new Exception("Range must be positive");
		}
		
		
		
		return data;
	}
	public static void main(String[] args){
		System.out.print("things");
		
	}
	
	

}
