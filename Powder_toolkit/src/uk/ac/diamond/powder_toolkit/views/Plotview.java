package uk.ac.diamond.powder_toolkit.views;

import java.util.Arrays;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetFactory;
import org.eclipse.dawnsci.plotting.api.IPlottingSystem;
import org.eclipse.dawnsci.plotting.api.PlotType;
import org.eclipse.dawnsci.plotting.api.PlottingFactory;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import uk.ac.diamond.powder_toolkit.dataAnalysis.ServiceLoader;

public class Plotview extends ViewPart {

	public static final String ID = "Views.Plotview"; //$NON-NLS-1$
	protected static IPlottingSystem plotting;
	public Plotview() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		try{
//		plotting = PlottingFactory.createPlottingSystem();
		plotting = ServiceLoader.getPlottingService().createPlottingSystem();
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		plotting.createPlotPart(parent, "Plot",getViewSite().getActionBars(), PlotType.XY, this);
		}catch(Exception e){
		
		}
	}
	
	public static void createMyplot(List<IDataset> data){
		plotting.clear();
		plotting.reset();
		IDataset intensity = null;
		IDataset x = null;
		for(IDataset mydata: data){
			if(mydata.getName().equals("Intensity"))
				 intensity = mydata;
			else if(mydata.getName().equals("D_space")){
				x = DatasetFactory.createRange(mydata.getSize(), Dataset.INT32);
				}
			else{
				x = mydata;}
		}
		try{
		plotting.createPlot1D(x,Arrays.asList(intensity), "New plot", null);	
		}
		catch(Exception e){}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class clazz) {
		if (plotting.getAdapter(clazz)!=null) return plotting.getAdapter(clazz);
		return super.getAdapter(clazz);
	}

}
