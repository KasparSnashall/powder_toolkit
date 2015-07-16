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
	
	public static Dataset data1  = new DoubleDataset(new double[]{1,2,3,4,4,5,6,7,8,9,9,6,4,3,2,1,2});
	public static Dataset data2 =  new DoubleDataset(new double[]{1,2,3,4,4,5,6,7,8,9,9,6,4,3,2,1,2});
	public static boolean optimise = false; // optimise boolean
	public static int upper = 7; // upper range limit
	public static int lower = 0; // lower range limit
	public static double tolerance = 5.0; // tolerance percentage
	public static boolean weights_flag = true;
	public static List<Double> weights = new ArrayList<Double>();
	public static List<Integer> weight_lower = new ArrayList<Integer>();
	public static List<Integer> weight_upper = new ArrayList<Integer>();
	
	
	
	
	public static Double binary() throws Exception{
		
		
		if (data1.getSize() == 0){ // check size
			throw new Exception("Data size is zero");}
		
		if(optimise == true){ // optimiser also known as apply shift
			optimiser();	
		}
		
		final List<Integer> counts = new ArrayList<Integer>(); // counts list
		for(int i = 0; i < data1.getSize(); i++){
			double a = data1.getDouble(i); // element a
			double b = data2.getDouble(i); // element b
			Double tol = tolerance*data1.getDouble(i)/100.0; // percentage tolerance tol
			if(a - tol <= b && b <= a+tol){
				counts.add(1);}
			else{
				counts.add(0);
			}
		}
		System.out.println(weights.toString());
		
		// weights
		if (weights_flag){
			Double percent = scale();
			return percent;
		}
		else{
			int i;
			double sum = 0;
			for(i = 0; i < counts.size(); i++)
			    sum += counts.get(i);
			double percent = 100.0*sum/counts.size();
			System.out.println(percent);
			return (double) 1;
		}
		
		
	}
	
	private static double scale() {
		
		weights.add(5.2);
		weight_lower.add(2);
		weight_upper.add(7);
		
		List<Double> kilos = new ArrayList<Double>();
		// populates the kilos list
		for(int i = 0; i < data1.getSize(); i++)
		    kilos.add(1.0);
		for(int i=0; i < weights.size(); i++){
			int a = weight_upper.get(i)+1;
			int b = weight_lower.get(i);
			double c = weights.get(i);
			IDataset myrange = DatasetFactory.createRange(b, a, 1, Dataset.FLOAT64);
			for(int j=0;j < myrange.getSize();j++){
				int indexnum = myrange.getInt(j);
				kilos.add(indexnum, c);
				
				
			}
		} 
		
		System.out.println(kilos.toString());
		return 1.0;
	}

	public static void fractional(){
		//TODO
	}
	
	public static Dataset slice_range(Dataset dataset) throws Exception{
		if (lower > upper){
			 throw new Exception("Invalid range");
		}
		if (lower  < 0 ||upper < 0){
			throw new Exception("Range must be positive");
		}
		if (lower  == upper){
			throw new Exception("Range must be at least 1 in length");
		}
		dataset = dataset.getSliceView(new Slice(lower,upper));
		return dataset;}
	
	public static void optimiser(){
		// TODO
	}
	
	
	public static void main(String[] args) throws Exception{
		System.out.print("Comparator \n");
		Dataset newdata = slice_range(data1);
		System.out.print(newdata.toString(true)+"\n");
		binary();
	}
	
	

}
