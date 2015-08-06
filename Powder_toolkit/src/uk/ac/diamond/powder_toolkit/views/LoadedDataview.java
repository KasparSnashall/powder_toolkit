package uk.ac.diamond.powder_toolkit.views;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.diamond.powder_toolkit.dataAnalysis.LoadedDataObject;
import uk.ac.diamond.powder_toolkit.dataAnalysis.MyDataHolder;


/**
 * LoadedDataView
 * view to hold and share data with other views
 * @author sfz19839
 *
 */
public class LoadedDataview extends ViewPart {

	public static final String ID = "Views.LoadedDataview"; //$NON-NLS-1$
	public static MyDataHolder holder = new MyDataHolder(); // the data holder
	private static Table datalist;
	private static Table cellList;
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
		
		datalist = new Table(container, SWT.BORDER);
		datalist.setHeaderVisible(true);
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
					holder.delData(datalist.getItem(i).getText());
					datalist.remove(i);}
			}
		});
		
		btnClear.setText("Clear");
		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearAll.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent e) {				
				for(int i = 0; i < datalist.getItemCount();i++){
					holder.delData(datalist.getItem(i).getText());
					datalist.remove(i);}
			}
		});
		btnClearAll.setText("Clear All");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label label = new Label(container, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		label.setText("Cell Data");
		
		cellList= new Table(container, SWT.BORDER | SWT.V_SCROLL);
		cellList.setHeaderVisible(true);
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
					String name = cellList.getItem(i).getText();
					holder.delData(cellList.getItem(i).getText());
					cellList.remove(i);}
			}
		});
		btnClearCell.setText("Clear Cell");
		
		Button btnClearAll_1 = new Button(container, SWT.NONE);
		btnClearAll_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearAll_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int i = 0; i < cellList.getItemCount();i++){
					holder.delData(cellList.getItem(i).getText());
					cellList.remove(i);}
			}
		});
		btnClearAll_1.setText("Clear All");
		
		
		// tool tip functionality
		datalist.addListener(SWT.MouseHover, new Listener() {
		      public void handleEvent(Event event) {
		        Point pt = new Point(event.x, event.y);
		        TableItem item = datalist.getItem(pt);
		        if (item == null)
		          return;
		        for (int i = 0; i < 1; i++) {
		          Rectangle rect = item.getBounds(i);
		          if (rect.contains(pt)) {
		          datalist.setToolTipText(item.getText() +"\n" +holder.getData(item.getText()).data.toString()); 
		          }
		        }
		      }
		    });
		
		cellList.addListener(SWT.MouseHover, new Listener() {
		      public void handleEvent(Event event) {
		        Point pt = new Point(event.x, event.y);
		        TableItem item = cellList.getItem(pt);
		        if (item == null)
		          return;
		        for (int i = 0; i < 1; i++) {
		          Rectangle rect = item.getBounds(i);
		          if (rect.contains(pt)) {
		        	  String angles = "";
		        	  String lengths = "";
		        	  String primitive = "";
		        	  for(int i1 = 0 ; i1 < 3; i1++){
		        		  angles += " "+holder.getData(item.getText()).data.get(1).getDouble(i1);
		        		  lengths += " "+holder.getData(item.getText()).data.get(0).getDouble(i1);
		        	  }
		        	  primitive = holder.getData(item.getText()).data.get(2).getString(0);
			          cellList.setToolTipText(item.getText() +"\n" +"(a,b,c) "+lengths +"\n" +"(A,B,C)"+angles+"\n"+primitive);
		          }
		        }
		      }
		    });
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
		TableItem myitem = new TableItem(datalist,SWT.NONE);
		myitem.setText(name);	
	}
	
	public static void addCell(String name,java.util.List<IDataset> data) throws Exception{
		for(LoadedDataObject loads : holder.getDatalist()){
			if(loads.name.equals(name)){
				throw new Exception("No two datasets may have the same name, try clearing the data beforehand");
			}	
		}
		holder.addData(name, "cell", data, null);
		TableItem myitem = new TableItem(cellList,SWT.NONE);
		myitem.setText(name);
		
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
