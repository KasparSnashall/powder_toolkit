package powder_toolkit.views;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.jface.action.Separator;

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
		parent.setLayout(new FormLayout());
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(null);
		FormData fd_container = new FormData();
		fd_container.top = new FormAttachment(0, 21);
		fd_container.left = new FormAttachment(0, 10);
		fd_container.bottom = new FormAttachment(100, -29);
		fd_container.right = new FormAttachment(100, -10);
		container.setLayoutData(fd_container);
		
		datalist = new List(container, SWT.BORDER);	
		datalist.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent arg0) {
			}
		});
		datalist.setBounds(23, 32, 236, 245);
		
		Label lblLoadedData = new Label(container, SWT.NONE);
		lblLoadedData.setBounds(99, 9, 93, 17);
		lblLoadedData.setText("Loaded Data");		
		Button btnClear = new Button(container, SWT.NONE);
		btnClear.setBounds(23, 295, 88, 29);
		
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
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				datalist.removeAll();
				holder.delAllData();
			}
		});
		btnClearAll.setBounds(171, 295, 88, 29);
		btnClearAll.setText("Clear All");
		
		cellList= new List(container, SWT.BORDER);
		cellList.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent arg0) {
				
			}
		});
		cellList.setBounds(23, 371, 236, 177);
		
		Button btnClearCell = new Button(container, SWT.NONE);
		btnClearCell.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// remove the selected from the list
				int[] myitems = cellList.getSelectionIndices();
				for(int i = 0; i < myitems.length;i++){
					cellList.remove(i);}
			}
		});
		btnClearCell.setBounds(23, 554, 88, 29);
		btnClearCell.setText("Clear Cell");
		
		Button btnClearAll_1 = new Button(container, SWT.NONE);
		btnClearAll_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cellList.removeAll();	
			}
		});
		btnClearAll_1.setBounds(171, 554, 88, 29);
		btnClearAll_1.setText("Clear All");

		Label label = new Label(container, SWT.NONE);
		label.setBounds(111, 339, 60, 17);
		label.setText("Cell Data");
		
		Label label_1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(0, 330, 282, 2);

		createActions();
		initializeToolBar();
		initializeMenu();
	}
	
	public static void addData(String name,String flag, java.util.List<IDataset> data, String filepath){
		holder.addData(name, flag, data, filepath);
		datalist.add(name);
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
