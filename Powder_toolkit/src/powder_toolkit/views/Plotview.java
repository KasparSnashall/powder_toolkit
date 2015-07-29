package powder_toolkit.views;

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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

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
		plotting = PlottingFactory.createPlottingSystem();
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		createActions();
		initializeToolBar();
		initializeMenu();
		plotting.createPlotPart(parent, "Plot",getViewSite().getActionBars(), PlotType.XY, null);
		}catch(Exception e){
		
		}
	}
	
	public static void createMyplot(List<IDataset> data){
		
		plotting.createPlot1D(data.get(0), Arrays.asList(data.get(1)), "New plot", null);	
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
