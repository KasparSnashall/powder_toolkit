package uk.ac.diamond.powder_toolkit.views;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import uk.ac.diamond.powder_toolkit.dataAnalysis.IPowderIndexer;
import uk.ac.diamond.powder_toolkit.dataAnalysis.LoadedDataObject;
import uk.ac.diamond.powder_toolkit.dataAnalysis.MyDataHolder;
import uk.ac.diamond.powder_toolkit.dataAnalysis.Ntreor;
import uk.ac.diamond.powder_toolkit.widgets.ErrorWidget;
import uk.ac.diamond.powder_toolkit.widgets.Properties_Widget;

import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.CoolBar;

public class Indexview extends ViewPart {
	public Indexview() {
	}

	public static final String ID = "Views.Indexview"; //$NON-NLS-1$
	GridData griddata;
	private GridData griddata_4;
	private GridData griddata_3;
	private GridData griddata_2;
	private GridData griddata_1;
	public static Text textbox; // loaded_data output
	public static List<IDataset> data; // data to be used
	public static String filepath = ""; // filepath (if loaded data this will be
										// the loaded path)
	public static String title = "";
	// colours used in the properties widget
	private final Color grey = Display.getCurrent().getSystemColor(
			SWT.COLOR_GRAY);
	private final Color green = Display.getCurrent().getSystemColor(
			SWT.COLOR_GREEN);
	private final Color red = Display.getCurrent()
			.getSystemColor(SWT.COLOR_RED);
	private static MyDataHolder holder = LoadedDataview.holder;
	private static Table cleanoutput;
	private static Text rawoutput;
	private static Button btnSaveSelected;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);

		createActions();
		initializeToolBar();
		initializeMenu();
		GridLayout gridlayout = new GridLayout(5, false);
		gridlayout.marginWidth = 25; // margins
		gridlayout.marginHeight = 25; // margins
		composite.setLayout(gridlayout); // three columns composite

		// the index view
		Label Indexview = new Label(composite, SWT.NONE);
		Indexview.setText("Index View");
		griddata = new GridData(SWT.FILL, SWT.FILL, true, false);
		griddata.horizontalSpan = 5; // 3 columns wide
		Indexview.setLayoutData(griddata);

		// the load radio button
		final Button loadButton = new Button(composite, SWT.RADIO);
		loadButton.setText("Loaded data");
		loadButton.setSelection(true);
		loadButton.setToolTipText("Select loaded peaks to be indexed");

		// the load data textbox
		textbox = new Text(composite, SWT.BORDER);
		textbox.setText(title);
		griddata = new GridData(150, 20);
		griddata.horizontalSpan = 4;
		textbox.setLayoutData(griddata);

		// from file radio button
		final Button peaksButton = new Button(composite, SWT.RADIO);
		peaksButton.setText("Existing input file");
		peaksButton.setToolTipText("Select a pre-existing input file");

		// text box for the filepath
		final Text filepathbox = new Text(composite, SWT.BORDER);
		griddata = new GridData(150, 20);
		filepathbox.setLayoutData(griddata);

		// browse button
		final Button browse = new Button(composite, SWT.PUSH);
		GridData gd_browse = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_browse.heightHint = 32;
		gd_browse.widthHint = 101;
		browse.setLayoutData(gd_browse);
		browse.setAlignment(SWT.LEFT);
		browse.setText("Browse...");
		browse.setEnabled(false);
		browse.setToolTipText("Browse for input files");
		final Shell shell = new Shell();
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String filepath = new FileDialog(shell).open();
				if (filepath != null) {
					// if a filepath is input
					File file = new File(filepath);
					if (file.isFile()) {
						// check is actually a file and not just directory or
						// stub
						filepathbox.setText(filepath);
						title = file.getName().split("\\.")[0];
					} else
						filepathbox.setText("Not a file");
				}
			}
		});
		filepathbox.setEnabled(false);

		// indeing programs list
		final Table indexingprogs = new Table(composite, SWT.CHECK
				| SWT.SCROLL_LINE | SWT.BORDER); // list of programs
		griddata_2 = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2);
		griddata_2.heightHint = 181;
		griddata_2.minimumHeight = 200;
		indexingprogs.setLayoutData(griddata_2);
		new TableColumn(indexingprogs, SWT.NULL).setText("Programs");
		indexingprogs.getColumn(0).pack();
		indexingprogs.setHeaderVisible(true);
		indexingprogs.setToolTipText("Select the indexing algorithm to be used");
		// properties widget tabs
		final CTabFolder indexfolder = new CTabFolder(composite, SWT.TOP
				| SWT.BORDER); // create a tab set
		griddata_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		griddata_1.heightHint = 161;
		griddata_1.minimumHeight = 200;
		indexfolder.setLayoutData(griddata_1);

		// list of programs available
		String[] myprogslist = new String[] { "Ntreor", "McMaille" };
		// add the list to the programs table
		for (int loopIndex = 0; loopIndex < myprogslist.length; loopIndex++) {
			TableItem item = new TableItem(indexingprogs, SWT.NULL);
			item.setText(0, myprogslist[loopIndex]);
		}

		// add variable button
		Button addvariable = new Button(composite, SWT.NONE);
		GridData gd_addvariable = new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1);
		gd_addvariable.widthHint = 146;
		gd_addvariable.heightHint = 30;
		addvariable.setLayoutData(gd_addvariable);
		addvariable.setText("Add variables");
		addvariable.setToolTipText("Add selected variables to the indexing program (loaded peaks only)");

		// reset button
		Button Reset = new Button(composite, SWT.NONE);
		GridData gd_Reset = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1,
				1);
		gd_Reset.heightHint = 30;
		gd_Reset.widthHint = 100;
		Reset.setLayoutData(gd_Reset);
		Reset.setText("Reset");
		Reset.setToolTipText("Reset the current screen including all tables and vaiables");
		Button Save = new Button(composite, SWT.NONE);
		griddata_4 = new GridData(150, 50);
		griddata_4.verticalAlignment = SWT.FILL;
		griddata_4.horizontalAlignment = SWT.RIGHT;
		Save.setLayoutData(griddata_4);
		Save.setText("Save");
		Save.setToolTipText("Create and save input files with current settings");
		// Add the ntreor tab, at time of writing this is the only one available
		CTabItem cTabItem1 = new CTabItem(indexfolder, SWT.NONE);
		cTabItem1.setText("Ntreor");
		// get the properties widget
		Properties_Widget NtreorTable = new Properties_Widget();
		IPowderIndexer Ntreor = new Ntreor();
		Table Ntreor_table = NtreorTable.create(indexfolder, Ntreor); // folder
																		// indexer
		cTabItem1.setControl(Ntreor_table);
		indexfolder.setSelection(cTabItem1); // default selection

		// add in second tab currently a stub
		CTabItem cTabItem2 = new CTabItem(indexfolder, SWT.BORDER);
		cTabItem2.setText("McMaille");

		// make a list of the current properties widgets
		final List<IPowderIndexer> indexer_list = new ArrayList<IPowderIndexer>();
		indexer_list.add(Ntreor);
		// make a list of the properties widgets tables
		final List<Table> table_list = new ArrayList<Table>();
		table_list.add(Ntreor_table);

		// run button
		Button run = new Button(composite, SWT.NONE);
		run.setText("Save and Run");
		run.setToolTipText("Create save and run input files based on current settings");
		griddata_3 = new GridData(151, 30);
		griddata_3.horizontalSpan = 2;
		griddata_3.verticalAlignment = SWT.FILL;
		griddata_3.horizontalAlignment = SWT.RIGHT;
		run.setLayoutData(griddata_3);
		// run button function
		final CTabFolder outputfolder = new CTabFolder(composite, SWT.TOP
				| SWT.BORDER); // create a tab set
		griddata = new GridData(SWT.FILL, SWT.FILL, false, true, 5, 1);
		griddata.grabExcessHorizontalSpace = true;
		griddata.minimumHeight = 200;
		outputfolder.setLayoutData(griddata);
		// output tab 1
		CTabItem rawOutTab = new CTabItem(outputfolder, SWT.NONE);
		rawOutTab.setText("Raw data");
		rawOutTab.setToolTipText("Displays the raw output of the indexing program");
		// output tab 2
		CTabItem cleanOutTab = new CTabItem(outputfolder, SWT.NONE);
		cleanOutTab.setText("Cleaned Data");
		cleanOutTab.setToolTipText("Displays the cleaned output from the indexing program");
		rawoutput = new Text(outputfolder, SWT.BORDER | SWT.MULTI | SWT.WRAP
				| SWT.V_SCROLL);
		cleanoutput = new Table(outputfolder, SWT.CHECK | SWT.BORDER
				| SWT.V_SCROLL);
		cleanoutput.setLinesVisible(true);
		cleanoutput.setHeaderVisible(true);
		TableColumn Outcolumn1 = new TableColumn(cleanoutput, SWT.NONE);
		Outcolumn1.setText("Cell");

		Outcolumn1.pack();
		Outcolumn1.setWidth(200);
		TableColumn Outcolumn2 = new TableColumn(cleanoutput, SWT.NONE);
		Outcolumn2.setText("Cell values [a,b,c,Aplha,Beta,Gamma]");
		Outcolumn2.pack();
		griddata = new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1);
		griddata.minimumHeight = 200;
		griddata.horizontalSpan = 3;
		cleanoutput.setLayoutData(griddata);
		griddata = new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1);
		griddata.minimumHeight = 200;
		griddata.horizontalSpan = 3;
		rawoutput.setLayoutData(griddata);

		rawOutTab.setControl(rawoutput);
		cleanOutTab.setControl(cleanoutput);
		outputfolder.setSelection(rawOutTab);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		btnSaveSelected = new Button(composite, SWT.NONE);
		btnSaveSelected.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1));
		btnSaveSelected.setText("Save Selected");
		btnSaveSelected.setToolTipText("Save slected generated cells from the clean output tab");
		btnSaveSelected.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TableItem[] myitems = cleanoutput.getItems();
				for (TableItem item : myitems) {
					if (item.getChecked()) {
						ArrayList celldata = (ArrayList) item.getData();
						IDataset celllengths = new DoubleDataset(new double[] {
								(double) celldata.get(0),
								(double) celldata.get(1),
								(double) celldata.get(2) });
						celllengths.setName("Cell Lengths");
						IDataset cellangles = new DoubleDataset(new double[] {
								(double) celldata.get(3),
								(double) celldata.get(4),
								(double) celldata.get(5) });
						cellangles.setName("Cell Angles");
						List<IDataset> celldatalist = new ArrayList<IDataset>();
						celldatalist.add(cellangles);
						celldatalist.add(celllengths);
						try {
							LoadedDataview.addCell(item.getText(0),
									celldatalist);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							new ErrorWidget(e);
						}
					}
				}

			}

		});
		// create a table editor for the editing of cell names
		final TableEditor editor = new TableEditor(cleanoutput);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT; // alignment
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;
		cleanoutput.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Clean up any previous editor control
				Control oldEditor = editor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();

				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;

				// The control that will be the editor must be a child of the
				// Table
				Text newEditor = new Text(cleanoutput, SWT.BORDER);
				newEditor.setText(item.getText(0)); // get the current text
				newEditor.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent me) { // if a change is
																// detected set
																// the text
						Text text = (Text) editor.getEditor();
						editor.getItem().setText(0, text.getText());
					}
				});
				newEditor.selectAll(); // sets its for all table items
				newEditor.setFocus();
				editor.setEditor(newEditor, item, 0);
			}
		});

		Save.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (loadButton.getSelection()) {
					try {

						for (int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++) {
							IPowderIndexer myprog = indexer_list.get(loopIndex); // get
																					// the
																					// program
							TableItem myitem = indexingprogs.getItem(loopIndex); // get
																					// checked?
							if (myitem.getChecked() == true) {
								// output.append(myprog.get_Name()+" Saving \n");
								// // running...
								File myfile = new File(filepath); // check if
																	// file
								String mynewfilepath = myfile.getParent()
										.toString(); // get the parent directory
								myprog.setTitle(title); // set the filename
														// (file.end , handled
														// in the python script
														// )
								myprog.setFilepath(mynewfilepath);
								myprog.setData(data); // make this more robust
								myprog.write_input();
								rawoutput.append("File saved");
							}
						}
					} catch (Exception e) {
						rawoutput.append(e.getMessage());

					}
				}
			}
		});

		run.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					for (int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++) {
						IPowderIndexer myprog = indexer_list.get(loopIndex); // get
																				// the
																				// program
						TableItem myitem = indexingprogs.getItem(loopIndex); // get
																				// checked?
						if (myitem.getChecked()) {
							// output.append(myprog.get_Name()+" Running \n");
							// // running...
							File myfile = new File(filepath); // check if file
							String mynewfilepath = myfile.getParent()
									.toString(); // get the parent directory
							String mybase = System.getProperty("user.dir"); // current
																			// base
																			// directory
							Path base = Paths.get(mybase); // current module
															// path will make
															// this automatic
							Path myfilepath = Paths.get(mynewfilepath);
							Path relativepath = base.relativize(myfilepath); // relative
																				// path
																				// of
																				// runfile
																				// (Ntreor
																				// requires
																				// this)
							if (title.equals("")) {

								title = myfile.getName().split("\\.")[0];
							}
							if (title.contains(".")) {
								title = title.split("\\.")[0];
							}

							myprog.setTitle(title); // set the filename
													// (file.end , handled in
													// the python script )
							myprog.setFilepath(relativepath.toString() + "/");
							myprog.setData(data);
							if (loadButton.getSelection()) {
								myprog.write_input();
							}

							List<String> newoutput = myprog.Run(); // the output
							List<List<Double>> cleanout = myprog.read_output();
							// holder.addCellData(cleanout);

							for (int i = 0; i < newoutput.size(); i++) {
								rawoutput.append(newoutput.get(i) + "\n"); // print
																			// output
							}
							int cellnum = 1;
							for (int i = 0; i < cleanout.size(); i++, cellnum++) {
								TableItem cleanitem = new TableItem(
										cleanoutput, SWT.NONE);
								cleanitem.setText(0,
										myitem.getText() + " Cell Number "
												+ String.valueOf(cellnum));
								cleanitem.setText(1,
										String.valueOf(cleanout.get(i)));
								cleanitem.setData(cleanout.get(i));

							}
						}
					}
				} catch (Exception e) {
					new ErrorWidget(e);
				}
			}
		});
		// add variable button function
		addvariable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (indexfolder.getEnabled() == false) {
					rawoutput
							.append("Prebuilt files may not have variables added \n");
				} else {
					for (int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++) {
						// go through the list of programs,tables and programs
						// list (check boxes)
						IPowderIndexer myprog = indexer_list.get(loopIndex); // get
																				// the
																				// program
						Table mytable = table_list.get(loopIndex); // get the
																	// table
						TableItem ischecked = indexingprogs.getItem(loopIndex); // make
																				// sure
																				// the
																				// program
																				// is
																				// selected
						if (ischecked.getChecked() == true) { // is it checked?
							try {
								for (int loopIndex1 = 0; loopIndex1 < mytable
										.getItems().length; loopIndex1++) {
									// go through table get checked values
									TableItem myitem = mytable
											.getItem(loopIndex1);
									if (myitem.getChecked() == true) {
										String value = myitem.getText(3);
										String key = myitem.getText(1);

										if (value == "") { // make sure value is
															// not null
											rawoutput.append("Value for" + key
													+ " Not defined\n");
											myitem.setBackground(red); // make
																		// background
																		// red
																		// if it
																		// is
										} else {

											myitem.setBackground(green); // make
																			// green
											myprog.addKeyword(key, value); // use
																			// the
																			// program
																			// to
																			// set
																			// keywords
											rawoutput.append(key + " " + value
													+ "\n"); // print the values
																// added

										}
									}
								}
							}

							catch (Exception e) {
								new ErrorWidget(e);
							}
						}
					}
				}
			}
		});

		// reset button function
		Reset.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				for (int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++) {
					IPowderIndexer myprog = indexer_list.get(loopIndex);
					Table mytable = table_list.get(loopIndex);
					myprog.resetKeywords();
					// myprog.reset_keywords(); // reset the keywords in the
					// program
					for (int loopIndex1 = 0; loopIndex1 < mytable.getItems().length; loopIndex1++) {
						TableItem myitem = mytable.getItem(loopIndex1); // go
																		// through
																		// each
																		// table
																		// and
																		// set
																		// the
																		// item
																		// text
																		// to ""
						myitem.setText(3, "");
						myitem.setChecked(false); // unckeck the boxes
						myitem.setBackground(grey); // clear the background
						rawoutput.setText("");
						cleanoutput.removeAll();
					}
				}
			}
		});

		// load button function
		loadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				filepathbox.setEnabled(false);
				browse.setEnabled(false);
				textbox.setEnabled(true);

			}

		});

		// filepathbox function (changes String filepath to selected)
		filepathbox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent me) {
				filepath = filepathbox.getText();
			}
		});

		// peaks button function changes the filepath to the loaded data's
		// filepath
		peaksButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				filepathbox.setEnabled(true);
				browse.setEnabled(true);
				textbox.setEnabled(false);

			}

		});

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	// ***************** getters and setters **********************//

	public static void setData(String name) {
		try {
			LoadedDataObject mydata = holder.getData(name);
			data = mydata.data;
			title = mydata.name;
			filepath = mydata.filepath;
			textbox.setText(title);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
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
