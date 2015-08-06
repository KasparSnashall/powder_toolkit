package powder_toolkit.dataAnalysis;

import org.eclipse.dawnsci.plotting.api.IPlottingService;

public class ServiceLoader {
	
	private static IPlottingService     plottingService;

	/**
	 * Used by OSGI
	 */
	public ServiceLoader() {
		// do nothing
	}

	public static IPlottingService getPlottingService() {
		return plottingService;
	}
	public static void setPlottingService(IPlottingService ps) {
		plottingService = ps;
	}
	
}
