package powder_toolkit.views;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import powder_toolkit.dataAnalysis.LoadedDataObject;
import powder_toolkit.dataAnalysis.MyDataHolder;


/**
 * LoadedDataView
 * view to hold and share data
 * @author sfz19839
 *
 */
public class LoadedDataview extends ViewPart {

	public static final String ID = "Views.LoadedDataview"; //$NON-NLS-1$
	public static MyDataHolder holder = new MyDataHolder(); // the data holder
	private static List datalist;
	private static List cellList;
	public LoadedDataview() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		FillLayout fl_parent = new FillLayout(SWT.HORIZONTAL);
		fl_parent.spacing = 25;
		fl_parent.marginWidth = 15;
		fl_parent.marginHeight = 25;
		parent.setLayout(fl_parent);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		
		Label lblLoadedData = new Label(container, SWT.HORIZONTAL | SWT.CENTER);
		lblLoadedData.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		lblLoadedData.setText("Loaded Data");
		
		datalist = new List(container, SWT.BORDER);	
		datalist.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		datalist.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent arg0) {
			}
		});
		Button btnClear = new Button(container, SWT.NONE);
		btnClear.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// remove the selected from the list
				int[] myitems = datalist.getSelectionIndices();
				for(int i = 0; i < myitems.length;i++){
					datalist.remove(i);
					holder.delData(datalist.getItem(i));}
			}
		});
		
		btnClear.setText("Clear");
		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				datalist.removeAll();
				holder.delAllData();
			}
		});
		btnClearAll.setText("Clear All");
				new Label(container, SWT.NONE);
				new Label(container, SWT.NONE);
		
				Label label = new Label(container, SWT.CENTER);
				label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
				label.setText("Cell Data");
		
		cellList= new List(container, SWT.BORDER);
		cellList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		cellList.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent arg0) {
				
			}
		});
		
		Button btnClearCell = new Button(container, SWT.NONE);
		btnClearCell.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearCell.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// remove the selected from the list
				int[] myitems = cellList.getSelectionIndices();
				for(int i = 0; i < myitems.length;i++){
					cellList.remove(i);}
			}
		});
		btnClearCell.setText("Clear Cell");
		
		Button btnClearAll_1 = new Button(container, SWT.NONE);
		btnClearAll_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearAll_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cellList.removeAll();	
			}
		});
		btnClearAll_1.setText("Clear All");

		createActions();
		initializeToolBar();
		initializeMenu();
	}
	
	public static void addData(String name,String flag, java.util.List<IDataset> data, String filepath) throws Exception{
		for(LoadedDataObject loads : holder.getDatalist()){
			if(loads.name.equals(name)){
				throw new Exception("No two datasets may have the same name, try clearing the data beforehand");
			}
			
		}
		holder.addData(name, flag, data, filepath);
		datalist.add(name);
		//TODO add tooltip for list item
		//datalist.addMouseTrackListener(listener);
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
