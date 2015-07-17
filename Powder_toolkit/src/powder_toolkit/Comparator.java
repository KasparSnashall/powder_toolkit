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
	public static boolean optimise = false; // optimise boolean
	public static int upper = 7; // upper range limit
	public static int lower = 0; // lower range limit
	public static boolean range_flag = false; // range flag
	public static double tolerance = 5.0; // tolerance percentage
	public static List<Double> weights = new ArrayList<Double>(); // the list of weights
	public static List<Integer> weight_lower = new ArrayList<Integer>(); // weight lower limit
	public static List<Integer> weight_upper = new ArrayList<Integer>(); // weight upper limit
	
	
	
	
	private static Double binary() throws Exception{
		Dataset priv_data1 = data1; // private variables
		Dataset priv_data2 = data2;
		if (priv_data1.getSize() == 0 | priv_data2.getSize() == 0){ // check is not null
			throw new Exception("Data size must not be zero");}
		
		if (priv_data1.getSize() != priv_data2.getSize()){
			// if data is not same size data1 is considered priority
			int mysize = priv_data1.getSize();
			priv_data2 = priv_data2.getSliceView(new Slice(0,mysize));
		}
		if(range_flag){
			priv_data1 = slice_range(priv_data1);
			priv_data2 = slice_range(priv_data2);
		}
		
		if(optimise){ // optimiser also known as apply shift
			priv_data2 = optimiser(priv_data1,priv_data2);	
		}
		
		final List<Double> counts = new ArrayList<Double>(); // counts list
		for(int i = 0; i < priv_data1.getSize(); i++){
			double a = priv_data1.getDouble(i); // element a
			double b = priv_data2.getDouble(i); // element b
			Double tol = tolerance*priv_data1.getDouble(i)/100.0; // percentage tolerance tol
			if(a - tol <= b && b <= a+tol){ // is it in range?
				counts.add(1.0);} // positive
			else{
				counts.add(0.0); // negative 
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
	
	private static double weighted_average(List<Double> percents) {
		List<Double> kilos = new ArrayList<Double>();
		// populates the kilos list with 1.0s
		for(int i = 0; i < percents.size(); i++)
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
			double value = percents.get(i);
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

	private static double fractional() throws Exception{
		Dataset priv_data1 = data1;
		Dataset priv_data2 = data2;
		
		if (priv_data1.getSize() == 0 | priv_data2.getSize() == 0){ // check is not null
			throw new Exception("Data size must not be zero");}
		
		if (priv_data1.getSize() != priv_data2.getSize()){
			// if data is not same size data1 is considered priority
			int mysize = priv_data1.getSize();
			priv_data2 = priv_data2.getSliceView(new Slice(0,mysize));
		}
		if(range_flag){
			priv_data1 = slice_range(priv_data1);
			priv_data2 = slice_range(priv_data2);
		}
		
		List<Double> percents = new ArrayList<Double>();
		for(int i = 0; i < priv_data1.getSize(); i ++){
			double a = priv_data1.getDouble(i);
			double b = priv_data2.getDouble(i);
			if(a > 0.0 && b > 0.0){
				if(a == b){
					percents.add(100.0);
				}
				else if( a > b && a != b){
					double p = 100.0*a/b;
					percents.add(p);
				}
				else if (b > a && b!= a){
					double p = 100.0*b/a;
					percents.add(p);	
				}
			}
			else if(a == 0.0 | b == 0.0){
					if(a != b){
						double p = 0.0;
						percents.add(p);
					}
					else{
						double p = 100.0;
						percents.add(p);
					}
				}
			else{
				double p = 0.0;
				percents.add(p);}
			}
	if(weights.size() != 0){
		double percent = weighted_average(percents);
		return percent;
	}
	else{
		double sum = 0;
		for(int i = 0; i < percents.size(); i++)
		    sum += percents.get(i);
		System.out.println(percents);
		double percent = sum/percents.size();
		return percent;
	}
		
	}
	
	private static Dataset slice_range(Dataset dataset) throws Exception{
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
	
	
	private static Dataset optimiser(Dataset priv_data1,Dataset priv_data2){
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
			for(int i = 0; i < priv_data2.getSize();i++){
					
					Double myshift = priv_data2.getDouble(i)+mystep;
					shifted.add(myshift);
					double deltax = priv_data1.getDouble(i)-shifted.get(i);
					delx.add(Math.pow(deltax, 2));
					
					for(int k = 0; k < delx.size(); k++){
						sum += delx.get(k);
						}
					
					}
			average_shifts.add(sum);
			
			}
		for(int i=0;i < priv_data2.getSize();i++){
			Double bestfit = priv_data2.getDouble(i)+Collections.min(average_shifts);
			priv_data2.set(bestfit, i);
		}
		return priv_data2;
	}
	

	private static Double both() throws Exception {
		// TODO Auto-generated method stub
		Double percent1 = binary();
		Double percent2 = fractional();
		Double average = (percent1+percent2)/2;
		return average;
	}
	
	
	public static Double Compare(String option) throws Exception{
		if(option == "binary"){
			// binary comparison
			return binary();
		}
		else if(option == "fractional"){
			// fractional comparison
			return fractional();
		}
		else{
			// defailt is the average
			return both();
		}
	}

	public static Dataset getData1() {
		return data1;
	}

	public static void setData1(Dataset data1) {
		Comparator.data1 = data1;
	}

	public static Dataset getData2() {
		return data2;
	}

	public static void setData2(Dataset data2) {
		Comparator.data2 = data2;
	}

	public static boolean isOptimise() {
		return optimise;
	}

	public static void setOptimise(boolean optimise) {
		Comparator.optimise = optimise;
	}

	public static int getUpper() {
		return upper;
	}

	public static void setUpper(int upper) {
		Comparator.upper = upper;
	}

	public static int getLower() {
		return lower;
	}

	public static void setLower(int lower) {
		Comparator.lower = lower;
	}

	public static boolean isRange_flag() {
		return range_flag;
	}

	public static void setRange_flag(boolean range_flag) {
		Comparator.range_flag = range_flag;
	}

	public static double getTolerance() {
		return tolerance;
	}

	public static void setTolerance(double tolerance) {
		Comparator.tolerance = tolerance;
	}

	public static List<Double> getWeights() {
		return weights;
	}

	public static void setWeights(List<Double> weights) {
		Comparator.weights = weights;
	}

	public static List<Integer> getWeight_lower() {
		return weight_lower;
	}

	public static void setWeight_lower(List<Integer> weight_lower) {
		Comparator.weight_lower = weight_lower;
	}

	public static List<Integer> getWeight_upper() {
		return weight_upper;
	}

	public static void setWeight_upper(List<Integer> weight_upper) {
		Comparator.weight_upper = weight_upper;
	}

	

}
