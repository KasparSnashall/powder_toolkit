package Views;

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
import DataAnalysis.MyDataHolder;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.jface.action.Separator;


/**
 * LoadedDataView
 * view to hold and share data
 * @author sfz19839
 *
 */
public class LoadedDataview extends ViewPart {

	public static final String ID = "Views.LoadedDataview"; //$NON-NLS-1$
	public static MyDataHolder holder = new MyDataHolder();
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
		
		final List list = new List(container, SWT.BORDER);	
		list.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent arg0) {
			}
		});
		list.setBounds(23, 32, 236, 245);
		
		Label lblLoadedData = new Label(container, SWT.NONE);
		lblLoadedData.setBounds(99, 9, 93, 17);
		lblLoadedData.setText("Loaded Data");		
		Button btnClear = new Button(container, SWT.NONE);
		btnClear.setBounds(23, 295, 88, 29);
		
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// remove the selected from the list
				int[] myitems = list.getSelectionIndices();
				for(int i = 0; i < myitems.length;i++){
					list.remove(i);
					holder.delData(list.getItem(i));}
			}
		});
		
		btnClear.setText("Clear");
		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.removeAll();
			}
		});
		btnClearAll.setBounds(171, 295, 88, 29);
		btnClearAll.setText("Clear All");
		
		final List list_1 = new List(container, SWT.BORDER);
		list_1.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent arg0) {
			}
		});
		list_1.setBounds(23, 371, 236, 177);
		
		Button btnClearCell = new Button(container, SWT.NONE);
		btnClearCell.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// remove the selected from the list
				int[] myitems = list_1.getSelectionIndices();
				for(int i = 0; i < myitems.length;i++){
					list_1.remove(i);}
			}
		});
		btnClearCell.setBounds(23, 554, 88, 29);
		btnClearCell.setText("Clear Cell");
		
		Button btnClearAll_1 = new Button(container, SWT.NONE);
		btnClearAll_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list_1.removeAll();
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
