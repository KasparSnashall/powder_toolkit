package uk.ac.diamond.powder_toolkit.dataAnalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetFactory;
import org.eclipse.dawnsci.analysis.dataset.impl.Maths;

/**
 * Comparator is a class designed to compare arrays, it is a conversion from an
 * original python script and its main use should be the comparison of large
 * datasets eg Intensity's
 * 
 * @author sfz19839
 *
 */
public class Comparator {

	public static IDataset data1;
	public static IDataset data2;
	public static boolean optimise = false; // optimise boolean
	public static int upper = 7; // upper range limit
	public static int lower = 0; // lower range limit
	public static boolean range_flag = false; // range flag
	public static double tolerance = 20.0; // tolerance percentage
	public static List<Double> weights = new ArrayList<Double>(); // the list of
																	// weights
	public static List<Integer> weight_lower = new ArrayList<Integer>(); // weight
																			// lower
																			// limit
	public static List<Integer> weight_upper = new ArrayList<Integer>(); // weight
																			// upper
																			// limit

	/**
	 * Binary comparison compares arrays by asserting if the data points lie
	 * within an percentage tolerance of each other
	 * 
	 * @return A double (percentage match)
	 * @throws Exception
	 *             various exceptions to make sure comparison is accurate
	 */
	private static Double binary() throws Exception {
		IDataset priv_data1 = data1; // private variables
		IDataset priv_data2 = data2;

		if (priv_data1.getSize() == 0 | priv_data2.getSize() == 0) { // check is
																		// not
																		// null
			throw new Exception("Data size must not be zero");
		}

		if (priv_data1.getSize() != priv_data2.getSize()) {
			// if data is not same size data1 is considered priority
			int mysize = priv_data1.getSize();
			priv_data2 = priv_data2.getSliceView(new Slice(0, mysize));
		}
		if (range_flag) {
			// amend range
			priv_data1 = slice_range(priv_data1);
			priv_data2 = slice_range(priv_data2);
		}

		if (optimise) { // optimiser also known as apply shift
			priv_data2 = optimiser(priv_data1, priv_data2);
		}

		final List<Double> counts = new ArrayList<Double>(); // counts list
		for (int i = 0; i < priv_data1.getSize(); i++) {
			double a = java.lang.Math.abs(priv_data1.getDouble(i)); // element a
			double b = java.lang.Math.abs(priv_data2.getDouble(i)); // element b
			Double tol = tolerance * priv_data1.getDouble(i) / 100.0; // percentage
																		// tolerance
																		// tol

			if (a - tol <= b | b <= a + tol) { // is it in range?
				counts.add(1.0);
			} // positive
			else if (a == b) {// odd case
				counts.add(1.0);
			} else {
				System.out.println(a);
				System.out.println(b);
				counts.add(0.0); // negative
			}
		}

		// weighted
		if (weights.size() != 0) {
			Double mypercent = weighted_average(counts);
			return mypercent;
		} else {
			// non weighted
			double sum = 0;
			for (int i = 0; i < counts.size(); i++)
				sum += counts.get(i);
			double percent = 100.0 * sum / counts.size();
			return percent;
		}
	}

	/**
	 * Weighted average applied a weighted average to an array of percentages
	 * this enables the weighting of data to rely more heavily on a selected
	 * range
	 * 
	 * @param percents
	 * @return an average weighted percentage
	 */
	private static double weighted_average(List<Double> percents) {
		List<Double> kilos = new ArrayList<Double>();
		// populates the kilos list with 1.0s
		for (int i = 0; i < percents.size(); i++)
			kilos.add(1.0);

		for (int i = 0; i < weights.size(); i++) {
			int b = weight_upper.get(i) + 1; // upper limit
			int a = weight_lower.get(i); // lower limit
			double c = weights.get(i); // weight
			IDataset myrange = DatasetFactory.createRange(a, b, 1,
					Dataset.FLOAT64);
			for (int j = 0; j < myrange.getSize(); j++) {
				int indexnum = myrange.getInt(j);
				kilos.set(indexnum, c); // created the weighted list greater
										// then 1.0
			}
		}

		List<Double> grammes = new ArrayList<Double>();
		for (int i = 0; i < kilos.size(); i++) {
			double weight = kilos.get(i);
			double value = percents.get(i);
			double milligramme = weight * value;
			grammes.add(milligramme); // weights*counts list
		}
		double sum = 0;
		for (int i = 0; i < grammes.size(); i++)
			sum += grammes.get(i); // the sum
		double weightsum = 0; // the denominator

		for (int i = 0; i < kilos.size(); i++)
			weightsum += kilos.get(i);

		double percent = 100 * sum / weightsum; // 100*sum/denominator
		return percent;
	}

	/**
	 * Fractional comparison compares arrays by getting relative fractions of
	 * elements It is not as accurate as binary but an an alternative test
	 * 
	 * @return a percentage average
	 * @throws Exception
	 */
	private static double fractional() throws Exception {
		IDataset priv_data1 = data1; // private data used
		IDataset priv_data2 = data2;

		if (priv_data1.getSize() == 0 | priv_data2.getSize() == 0) { // check is
																		// not
																		// null
			throw new Exception("Data size must not be zero");
		}

		if (priv_data1.getSize() != priv_data2.getSize()) {
			// if data is not same size data1 is considered priority
			int mysize = priv_data1.getSize();
			priv_data2 = priv_data2.getSliceView(new Slice(0, mysize));
		}
		if (range_flag) {
			priv_data1 = slice_range(priv_data1);
			priv_data2 = slice_range(priv_data2);
		}

		List<Double> percents = new ArrayList<Double>();
		for (int i = 0; i < priv_data1.getSize(); i++) {
			double a = java.lang.Math.abs(priv_data1.getDouble(i));
			double b = java.lang.Math.abs(priv_data2.getDouble(i));
			if (a >= 0.0 && b >= 0.0) {
				if (a == b) {
					percents.add(1.0);
				} else if (a > b) {
					double p = 1.0 * b / a;
					percents.add(p);
				} else if (b > a) {
					double p = 1.0 * a / b;
					percents.add(p);
				}
			} else if (a == 0.0 | b == 0.0) {
				if (a == 0 && b == 0) {
					double p = 1.0;
					percents.add(p);
				} else {
					System.out.println(a);
					System.out.println(b);
					double p = 0.0;
					percents.add(p);
				}
			} else {
				System.out.println(a);
				System.out.println(b);
				double p = 0.0;
				percents.add(p);
			}
		}
		if (weights.size() != 0) {
			double percent = weighted_average(percents);
			return percent;
		} else {
			double sum = 0;
			for (int i = 0; i < percents.size(); i++)
				sum += percents.get(i);

			double percent = 100 * sum / percents.size();
			return percent;
		}

	}

	/**
	 * slice_range slices a dataset into a selected x-axis range (for example
	 * theta values)
	 * 
	 * @param priv_data1
	 *            the dataset
	 * @return a IDataset of ranged data
	 * @throws Exception
	 */
	private static IDataset slice_range(IDataset priv_data1) throws Exception {
		if (lower > upper) {
			throw new Exception("Invalid range");
		}
		if (lower < 0 || upper < 0) {
			throw new Exception("Range must be positive");
		}
		if (lower == upper) {
			throw new Exception("Range must be at least 1 in length");
		}
		priv_data1 = priv_data1.getSliceView(new Slice(lower, upper));
		return priv_data1;
	}

	/**
	 * optimiser optimises binary comparison by applying a number of shifts over
	 * a range selecting the shift with the minimum squared difference thus
	 * optimising the data
	 * 
	 * @param priv_data1
	 *            the main dataset
	 * @param priv_data2
	 *            the dataset to be shifted
	 * @return a shifted dataset
	 */
	private static IDataset optimiser(IDataset priv_data1, IDataset priv_data2) {
		double step1 = -0.5;
		double step2 = 0.5;
		int stepnum = 300;
		double stepsize = (Math.abs(step1) + Math.abs(step2)) / stepnum; // stepsize
		IDataset steps = DatasetFactory.createRange(step1, step2, stepsize,
				Dataset.FLOAT64);
		final List<Double> average_shifts = new ArrayList<Double>();

		for (int j = 0; j < steps.getSize(); j++) {
			Double mystep = steps.getDouble(j);
			final List<Double> shifted = new ArrayList<Double>();
			final List<Double> delx = new ArrayList<Double>();
			Double sum = 0.0;
			for (int i = 0; i < priv_data2.getSize(); i++) {

				Double myshift = priv_data2.getDouble(i) + mystep;
				shifted.add(myshift);
				double deltax = priv_data1.getDouble(i) - shifted.get(i);
				delx.add(Math.pow(deltax, 2));

				for (int k = 0; k < delx.size(); k++) {
					sum += delx.get(k);
				}

			}
			average_shifts.add(sum);

		}
		for (int i = 0; i < priv_data2.getSize(); i++) {
			Double bestfit = priv_data2.getDouble(i)
					+ Collections.min(average_shifts);
			priv_data2.set(bestfit, i);
		}
		return priv_data2;
	}

	/**
	 * both runs both a binary and fractional comparison
	 * 
	 * @return a percentage average of the two comparisons
	 * @throws Exception
	 */
	private static Double both() throws Exception {
		// TODO Auto-generated method stub

		Double percent1 = binary();
		Double percent2 = fractional();
		Double average = 0.0;
		average = (percent1 + percent2) / 2;
		return average;
	}

	public static Double Compare(String option) throws Exception {
		if (option.equals("Binary")) {
			// binary comparison
			return binary();
		} else if (option.equals("Fractional")) {
			// fractional comparison
			return fractional();
		} else {
			// defailt is the average
			return both();
		}
	}

	// ////////getters and setters/////////////

	public static void setData1(IDataset data1) {
		Comparator.data1 = data1;
	}

	public static void setData2(IDataset data2) {
		Comparator.data2 = data2;
	}

	public static void setOptimise(boolean optimise) {
		Comparator.optimise = optimise;
	}

	public static void setUpper(int upper) {
		Comparator.upper = upper;
	}

	public static void setLower(int lower) {
		Comparator.lower = lower;
	}

	public static void setRange_flag(boolean range_flag) {
		Comparator.range_flag = range_flag;
	}

	public static void setTolerance(double tolerance) {
		Comparator.tolerance = tolerance;
	}

	public static void addWeights(Double weights) {
		Comparator.weights.add(weights);
	}

	public static void addWeight_lower(Integer weight_lower) {
		Comparator.weight_lower.add(weight_lower);
	}

	public static void addWeight_upper(Integer weight_upper) {
		Comparator.weight_upper.add(weight_upper);
	}

	/**
	 * Clears the weighted values
	 */
	public static void delWeights() {
		weights.clear();
		weight_lower.clear();
		weight_upper.clear();

	}

}
