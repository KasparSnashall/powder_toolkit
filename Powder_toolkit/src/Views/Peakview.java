package Views;

import java.util.Arrays;
import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.io.ILoaderService;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetFactory;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;
import org.eclipse.dawnsci.plotting.api.IPlottingSystem;
import org.eclipse.dawnsci.plotting.api.PlotType;
import org.eclipse.dawnsci.plotting.api.PlottingFactory;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import swing2swt.layout.BoxLayout;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Button;


public class Peakview extends ViewPart {

	public static final String ID = "Views.Peakview"; //$NON-NLS-1$
	protected ILoaderService  service;
	protected static IPlottingSystem plotting;
	
	public Peakview() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		IDataset x = new DoubleDataset(new double[] {1,2,3,4,1,1,2,3,1,2,3,1,23,1,1});
		
		
		try {
			plotting = PlottingFactory.createPlottingSystem();
			parent.setLayout(new FillLayout(SWT.HORIZONTAL));
			Composite composite = new Composite(parent, SWT.NONE);
			GridLayout gl_composite = new GridLayout(9, false);
			gl_composite.marginWidth = 25;
			gl_composite.marginHeight = 25;
			composite.setLayout(gl_composite);
			
			
			Button btnNewButton = new Button(composite, SWT.NONE);
			btnNewButton.setText("New Button");
			
			Composite composite_1 = new Composite(composite, SWT.NONE);
			composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
			GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, true, true, 9, 4);
			gd_composite_1.heightHint = 372;
			gd_composite_1.widthHint = 524;
			composite_1.setLayoutData(gd_composite_1);
			
			plotting.createPlotPart(composite_1, "Plot",getViewSite().getActionBars(), PlotType.XY, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
	
	
	public static void createMyplot(IDataset data){
		Dataset   indices = DatasetFactory.createRange(data.getSize(), Dataset.INT32);
		plotting.createPlot1D(indices, Arrays.asList(data), null);	
	}
	
	@Override
	public void setFocus() {
		// Set the focus
	}
}
