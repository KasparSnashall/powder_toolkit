package uk.ac.diamond.powder_toolkit.dataAnalysis;

import org.eclipse.dawnsci.plotting.api.IPlottingService;

/**
 * OSGI services
 * 
 * @author sfz19839
 *
 */
public class ServiceLoader {

	private static IPlottingService plottingService;

	/**
	 * Used by OSGI
	 */
	public ServiceLoader() {
		// do nothing
	}

	/**
	 * the plotting service, allows use of plotting tools
	 * 
	 * @return
	 */
	public static IPlottingService getPlottingService() {
		return plottingService;
	}

	public static void setPlottingService(IPlottingService ps) {
		plottingService = ps;
	}

}
