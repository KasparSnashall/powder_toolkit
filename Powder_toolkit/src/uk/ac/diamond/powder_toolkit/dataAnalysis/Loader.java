package uk.ac.diamond.powder_toolkit.dataAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.dawnsci.analysis.dataset.impl.Maths;

import uk.ac.diamond.scisoft.analysis.io.DatLoader;
import uk.ac.diamond.scisoft.analysis.io.LoaderFactory;
import uk.ac.diamond.scisoft.analysis.io.NexusHDF5Loader;

/**
 * Loader Class designed to load in the data easily, it uses loader factory and
 * a makes basic decisions about the files (is it a .nxs) for example
 * 
 * @author sfz19839
 *
 */
public class Loader {

	public static double Upper;
	public static double Lower;
	public static boolean range = false;
	private static int x1;
	private static int x2;
	public static IDataHolder dh;

	/**
	 * sets the upper bound for the range
	 * 
	 * @param upper
	 *            double
	 */
	public void setUpper(double upper) {
		Upper = upper;
	}

	/**
	 * Sets the lower bound for the range
	 * 
	 * @param lower
	 *            double
	 */
	public void setLower(double lower) {
		Lower = lower;
	}

	/**
	 * Sets the range of the data
	 * 
	 * @param range
	 *            boolean range flag
	 */
	public void setRange(boolean range) {
		Loader.range = range;
	}

	/**
	 * Allows opening of data
	 */
	@SuppressWarnings("deprecation")
	public static List<IDataset> openData(String filepath) {
		try {
			String[] myextension = filepath.split("\\.");
			String extension = myextension[myextension.length - 1];
			if (filepath.contains(".nxs") | filepath.contains("nexus")) {
				LoaderFactory.registerLoader(extension, NexusHDF5Loader.class);
				dh = LoaderFactory.getData(filepath);
				List<IDataset> mydata = new ArrayList<IDataset>();

				for (int i = 0; i < dh.size(); i++) {
					ILazyDataset lazydataset = dh.getLazyDataset(i);
					final IDataset dataset = lazydataset.getSlice().squeeze();
					mydata.add(dataset);
				}

				return mydata;
			} else {
				List<IDataset> mydata = new ArrayList<IDataset>();
				LoaderFactory.registerLoader(extension, DatLoader.class);
				dh = LoaderFactory.getData(filepath);
				for (int i = 0; i < dh.size(); i++) {
					IDataset dataset = dh.getDataset(i);
					System.out.println(dataset);
					mydata.add(dataset);
				}
				return mydata;
			}

		} catch (Exception e) {

		}
		return null;

	}

	/**
	 * Load the data into a data object
	 */
	public static List<IDataset> setData(List<IDataset> data,
			List<String> names, String flag, List<Integer> colnumbers) {
		try {
			List<IDataset> dataholder = new ArrayList<IDataset>(); // the list
																	// of
																	// datasets
			System.out.println(names);
			System.out.println(dataholder);
			for (int i = 0; i < colnumbers.size(); i++) {
				IDataset column = data.get(colnumbers.get(i));
				column.setName(names.get(i));
				dataholder.add(column);
			}

			if (range) {
				dataholder = RangeData(names, dataholder);
				return dataholder;
			} else {
				System.out.println(names);
				System.out.println(dataholder);
				return dataholder;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Error in load");
		}
		return null;
	}

	/**
	 * Applies a range to the data
	 * 
	 * @param names
	 *            the names of the columns
	 * @param dataholder
	 *            the list of data sets to be ranged
	 * @return a sliced data set
	 */
	private static List<IDataset> RangeData(List<String> names,
			List<IDataset> dataholder) {
		for (IDataset col : dataholder) {
			if (!col.getName().equals("Intensity")) {
				x1 = Maths.abs(Maths.subtract(col, Lower)).argMin();
				x2 = Maths.abs(Maths.subtract(col, Upper)).argMin();
			}
		}
		List<IDataset> rangeddataholder = new ArrayList<IDataset>();
		for (IDataset column : dataholder) {
			if (x2 > x1) {
				IDataset ranged = column.getSliceView(new Slice(x1, x2));
				ranged.setName(column.getName());
				rangeddataholder.add(ranged);
			} else {
				IDataset ranged = column.getSliceView(new Slice(x2, x1));
				ranged.setName(column.getName());
				rangeddataholder.add(ranged);
			}
		}

		return rangeddataholder;

	}

}
