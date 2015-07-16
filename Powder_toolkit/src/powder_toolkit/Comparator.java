package powder_toolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	public static boolean optimise = true; // optimise boolean
	public static int upper = 7; // upper range limit
	public static int lower = 0; // lower range limit
	public static double tolerance = 5.0; // tolerance percentage
	public static List<Double> weights = new ArrayList<Double>();
	public static List<Integer> weight_lower = new ArrayList<Integer>();
	public static List<Integer> weight_upper = new ArrayList<Integer>();
	
	
	
	
	public static Double binary() throws Exception{
		
		if (data1.getSize() == 0 | data2.getSize() == 0){ // check is not null
			throw new Exception("Data size must not be zero");}
		
		if (data1.getSize() != data2.getSize()){
			// if data is not same size data1 is considered priority
			int mysize = data1.getSize();
			data2 = data2.getSliceView(new Slice(0,mysize));
		}
		
		if(optimise){ // optimiser also known as apply shift
			data2 = optimiser();	
		}
		
		final List<Integer> counts = new ArrayList<Integer>(); // counts list
		for(int i = 0; i < data1.getSize(); i++){
			double a = data1.getDouble(i); // element a
			double b = data2.getDouble(i); // element b
			Double tol = tolerance*data1.getDouble(i)/100.0; // percentage tolerance tol
			if(a - tol <= b && b <= a+tol){ // is it in range?
				counts.add(1);} // positive
			else{
				counts.add(0); // negative 
			}
		}
		
		// weighted
		if (weights.size() != 0){
			Double percent = weighted_average(counts);
			return percent;
		}
		else{
			// non weighted
			double sum = 0;
			for(int i = 0; i < counts.size(); i++)
			    sum += counts.get(i);
			double percent = 100.0*sum/counts.size();
			return percent;
			}	
	}
	
	private static double weighted_average(List<Integer> counts) {
		List<Double> kilos = new ArrayList<Double>();
		// populates the kilos list with 1.0s
		for(int i = 0; i < counts.size(); i++)
		    kilos.add(1.0);
		
		for(int i=0; i < weights.size(); i++){
			int b = weight_upper.get(i)+1; // upper limit
			int a = weight_lower.get(i); // lower limit
			double c = weights.get(i); // weight
			IDataset myrange = DatasetFactory.createRange(a, b, 1, Dataset.FLOAT64);
			for(int j=0;j < myrange.getSize();j++){
				int indexnum = myrange.getInt(j);
				kilos.set(indexnum,c); // created the weighted list
			}
		} 

		List<Double> grammes = new ArrayList<Double>();
		for(int i = 0; i < kilos.size(); i++){
			double weight = kilos.get(i); 
			double value = counts.get(i);
			double milligramme = weight*value;
			grammes.add(milligramme); // weights*counts list
		}
		double sum = 0;
		for(int i = 0; i < grammes.size(); i++)
		    sum += grammes.get(i); // the sum
		double weightsum = 0; // the denominator 
		
		for(int i = 0; i < kilos.size(); i++)
		    weightsum += kilos.get(i);
		
		double percent = 100*sum/weightsum; // 100*sum/denominator
		return percent;
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
	
	public static Dataset optimiser(){
		double step1 = -0.5;
		double step2 = 0.5;
		int stepnum = 300;
		double stepsize = (Math.abs(step1)+Math.abs(step2))/stepnum; // stepsize
		IDataset steps = DatasetFactory.createRange(step1, step2, stepsize, Dataset.FLOAT64);
		final List<Double> average_shifts = new ArrayList<Double>();
		
		for(int j=0;j < steps.getSize();j++){
			Double mystep = steps.getDouble(j);
			final List<Double> shifted = new ArrayList<Double>();
			final List<Double> delx = new ArrayList<Double>();
			Double sum = 0.0;
			for(int i = 0; i < data2.getSize();i++){
					
					Double myshift = data2.getDouble(i)+mystep;
					shifted.add(myshift);
					double deltax = data1.getDouble(i)-shifted.get(i);
					delx.add(Math.pow(deltax, 2));
					
					for(int k = 0; k < delx.size(); k++){
						sum += delx.get(k);
						}
					
					}
			average_shifts.add(sum);
			
			}
		for(int i=0;i < data2.getSize();i++){
			Double bestfit = data2.getDouble(i)+Collections.min(average_shifts);
			data2.set(bestfit, i);
		}
		System.out.println(Collections.min(average_shifts));
		System.out.println(data2.toString(true));
		return data2;
		// TODO
	}
	
	public static void main(String[] args) throws Exception{
		System.out.print("Comparator \n");
		Dataset newdata = slice_range(data1);
		binary();
	}
	
	

}
