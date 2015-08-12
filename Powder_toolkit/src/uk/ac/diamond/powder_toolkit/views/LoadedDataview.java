package uk.ac.diamond.powder_toolkit.views;

import java.util.ArrayList;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import org.eclipse.swt.widgets.Control;
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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

/**
 * LoadedDataView view to hold and share data with other views
 * 
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
	 * 
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
		lblLoadedData.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 2, 1));
		lblLoadedData.setText("Loaded Data");

		datalist = new Table(container, SWT.BORDER);
		datalist.setHeaderVisible(true);
		datalist.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));
		datalist.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent arg0) {
			}
		});
		Button btnClear = new Button(container, SWT.NONE);
		btnClear.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// remove the selected from the list
				int[] myitems = datalist.getSelectionIndices();
				for (int i = 0; i < myitems.length; i++) {
					holder.delData(datalist.getItem(i).getText());
					datalist.remove(i);
				}
			}
		});

		btnClear.setText("Clear");
		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < datalist.getItemCount(); i++) {
					holder.delData(datalist.getItem(i).getText());
					datalist.remove(i);
				}
			}
		});
		btnClearAll.setText("Clear All");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label label = new Label(container, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false,
				2, 1));
		label.setText("Cell Data");

		cellList = new Table(container, SWT.BORDER | SWT.V_SCROLL);
		cellList.setHeaderVisible(true);
		cellList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));
		cellList.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent arg0) {

			}
		});

		Button btnClearCell = new Button(container, SWT.NONE);
		btnClearCell.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnClearCell.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// remove the selected from the list
				int[] myitems = cellList.getSelectionIndices();
				for (int i = 0; i < myitems.length; i++) {
					String name = cellList.getItem(i).getText();
					holder.delData(cellList.getItem(i).getText());
					cellList.remove(i);
				}
			}
		});
		btnClearCell.setText("Clear Cell");

		Button btnClearAll_1 = new Button(container, SWT.NONE);
		btnClearAll_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnClearAll_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < cellList.getItemCount(); i++) {
					holder.delData(cellList.getItem(i).getText());
					cellList.remove(i);
				}
			}
		});
		btnClearAll_1.setText("Clear All");

		// tool tip functionality
		/**
		 * The Tool tip
		 */
		datalist.addListener(SWT.MouseHover, new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TableItem item = datalist.getItem(pt);
				if (item == null)
					return;
				for (int i = 0; i < 1; i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(pt)) {
						datalist.setToolTipText(item.getText()
								+ "\n"
								+ holder.getData(item.getText()).data
										.toString());
					}
				}
			}
		});
		/**
		 * Retrieves data and shows plot, sets data in index view
		 */
		datalist.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				String dataname = datalist.getSelection()[0].getText();
				java.util.List<IDataset> mydata = holder.getData(dataname).data; // Retrieve
																					// the
																					// data
				Plotview.createMyplot(mydata);
				Indexview.setData(dataname);

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		/**
		 * The tool tip
		 */
		cellList.addListener(SWT.MouseHover, new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TableItem item = cellList.getItem(pt);
				if (item == null)
					return;
				for (int i = 0; i < 1; i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(pt)) {
						java.util.List<Double> angles = new ArrayList<Double>();
						java.util.List<Double> lengths = new ArrayList<Double>();
						String primitive = "";
						for (int i1 = 0; i1 < 3; i1++) {
							angles.add(holder.getData(item.getText()).data.get(
									1).getDouble(i1));
							lengths.add(holder.getData(item.getText()).data
									.get(0).getDouble(i1));
						}
						try {
							// not all cells have a cell centring
							primitive = holder.getData(item.getText()).data
									.get(2).getString(0);
						} catch (Exception e) {

						}
						cellList.setToolTipText(item.getText() + "\n" + "a= "
								+ lengths.get(0) + "\n" + "b= "
								+ lengths.get(1) + "\n" + "c ="
								+ lengths.get(2) + "\n" + "\u03B1" + "= "
								+ angles.get(0) + "\n" + "\u03B2" + "= "
								+ angles.get(1) + "\n" + "\u03B3" + "= "
								+ angles.get(2) + "\n" + primitive);

					}
				}
			}
		});

		/**
		 * Double click options
		 */
		cellList.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				String dataname = cellList.getSelection()[0].getText();
				java.util.List<IDataset> mydata = holder.getData(dataname).data; // Retrieve
																					// the
																					// data
				Compareview.setsearchCell(mydata);

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		// data list drag and drop
		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		DragSource source = new DragSource(datalist, operations);

		// Provide data in Text format
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		source.setTransfer(types);

		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				// Only start the drag if there is actually text in the
				// label - this text will be what is dropped on the target.
				if (event.toString().length() == 0) {
					event.doit = false;
				}
			}

			public void dragSetData(DragSourceEvent event) {
				// Provide the data of the requested type.
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = datalist.getSelection()[0].getText();
				}
			}

			public void dragFinished(DragSourceEvent event) {
				// If a move operation has been performed, remove the data
				// from the source
				if (event.detail == DND.DROP_MOVE)
					;
			}
		});

		// cell list drag and drop
		DragSource source2 = new DragSource(cellList, operations);
		// Provide data in Text format
		Transfer[] types2 = new Transfer[] { TextTransfer.getInstance() };
		source2.setTransfer(types2);
		source2.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				// Only start the drag if there is actually text in the
				// label - this text will be what is dropped on the target.
				if (event.toString().length() == 0) {
					event.doit = false;
				}
			}

			public void dragSetData(DragSourceEvent event) {
				// Provide the data of the requested type.
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = cellList.getSelection()[0].getText();
				}
			}

			public void dragFinished(DragSourceEvent event) {
				// If a move operation has been performed, remove the data
				// from the source
				if (event.detail == DND.DROP_MOVE)
					;
			}
		});
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Adds data to the data holder
	 * 
	 * @param name
	 *            the name of the dataobject
	 * @param flag
	 *            the file extension
	 * @param data
	 *            the List of IDatasets
	 * @param filepath
	 *            the filepath to the file
	 * @throws Exception
	 *             if data name is the same, throw no two datasets may be the
	 *             same
	 */
	public static void addData(String name, String flag,
			java.util.List<IDataset> data, String filepath) throws Exception {
		for (LoadedDataObject loads : holder.getDatalist()) {
			if (loads.name.equals(name)) {
				throw new Exception(
						"No two datasets may have the same name, try clearing the data beforehand");
			}

		}
		holder.addData(name, flag, data, filepath);
		TableItem myitem = new TableItem(datalist, SWT.NONE);
		myitem.setText(name);

	}

	/**
	 * Adds cell data to the holder
	 * 
	 * @param name
	 *            the name of the data
	 * @param data
	 *            the list is IDatasets
	 * @throws Exception
	 *             no two datasets may have the smae name
	 */
	public static void addCell(String name, java.util.List<IDataset> data)
			throws Exception {
		for (LoadedDataObject loads : holder.getDatalist()) {
			if (loads.name.equals(name)) {
				throw new Exception(
						"No two datasets may have the same name, try clearing the data beforehand");
			}
		}
		holder.addData(name, "cell", data, null);
		TableItem myitem = new TableItem(cellList, SWT.NONE);
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
