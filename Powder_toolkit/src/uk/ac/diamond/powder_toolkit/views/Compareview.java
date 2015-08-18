package uk.ac.diamond.powder_toolkit.views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.StringDataset;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.diamond.powder_toolkit.dataAnalysis.Comparator;
import uk.ac.diamond.powder_toolkit.dataAnalysis.LoadedDataObject;
import uk.ac.diamond.powder_toolkit.dataAnalysis.MyDataHolder;
import uk.ac.diamond.powder_toolkit.jython_programs.CSD_cellsearch;
import uk.ac.diamond.powder_toolkit.widgets.ErrorWidget;

/**
 * The comparison view of the perspective
 * 
 * @author sfz19839
 *
 */
public class Compareview extends ViewPart {

	public static final String ID = "Views.Compareview"; //$NON-NLS-1$
	private static MyDataHolder holder = LoadedDataview.holder; // the data
																// holder
	private static Text Data1; // data text box 1
	private static Text Data2; // data text box 2
	private static Combo combo1; // drop down 1
	private static Combo combo2; // drop down 2
	private static Combo types; // drop down with comparison types
	private static Text output; // output text box
	private static Text tolerance; // the tolerance text box
	private static Text txtAlength; // the a length
	private static Text txtBlength; // b length
	private static Text txtClength; // c length
	private static Text txtAlpha; // alpha angle
	private static Text txtBeta; // beta angle
	private static Text txtGamma; // gamma angle
	private static Text txtNumresults; // number of results to be displayed
	private static Table search_out; // the output tab;e
	private Text txtLower; // the lower limit of weights
	private Text txtUpper; // the upper limit of weights
	private Text txtValue; // the weight value
	private Scale scale; // the scaler for the weight value
	private static Button btnAddWeight; // add weights to the comparison button
	private static Button btnClearWeights; // remove all weights from comparison
											// button
	private static Button compare;
	public Compareview() {
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		FillLayout fillLayout = (FillLayout) parent.getLayout();
		fillLayout.spacing = 10;
		fillLayout.marginWidth = 15;
		fillLayout.marginHeight = 15;
		// comparison group
		Group grpCompare = new Group(parent, SWT.NONE);
		grpCompare.setToolTipText("Compare datasets");
		GridLayout gl_grpCompare = new GridLayout(3, false);
		gl_grpCompare.marginRight = 15;
		gl_grpCompare.marginLeft = 15;
		gl_grpCompare.marginWidth = 15;
		gl_grpCompare.marginHeight = 25;
		grpCompare.setLayout(gl_grpCompare);
		grpCompare.setText("Compare");
		// data labels
		Label data1label = new Label(grpCompare, SWT.NONE);
		data1label.setText("Data 1");

		Data1 = new Text(grpCompare, SWT.BORDER);
		Data1.setToolTipText("The name of the data to be used in the comparison");
		Data1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		// add in listeners
		combo1 = new Combo(grpCompare, SWT.NONE);
		combo1.setToolTipText("The dataset to be used in comparison");
		GridData gd_combo1 = new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1);
		gd_combo1.widthHint = 222;
		combo1.setLayoutData(gd_combo1);
		
		getdatalistener(Data1, combo1);
		Label data2label = new Label(grpCompare, SWT.NONE);
		data2label.setText("Data 2");

		Data2 = new Text(grpCompare, SWT.BORDER);
		Data2.setToolTipText("The name of the data to be used in the comparison");
		Data2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));

		combo2 = new Combo(grpCompare, SWT.NONE);
		combo2.setToolTipText("The dataset to be used in comparison");
		GridData gd_combo2 = new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1);
		gd_combo2.widthHint = 216;
		combo2.setLayoutData(gd_combo2);
		getdatalistener(Data2, combo2);
		Label lblTolerance = new Label(grpCompare, SWT.NONE);
		lblTolerance.setToolTipText("percentage tolerance in binary comparison");
		lblTolerance.setText("% Tolerance");
		tolerance = new Text(grpCompare, SWT.BORDER);
		GridData gd_tolerance = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_tolerance.widthHint = 66;
		tolerance.setLayoutData(gd_tolerance);
		tolerance.setText("5");
		Button btnOptimise = new Button(grpCompare, SWT.CHECK);
		btnOptimise.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnOptimise
				.setToolTipText("Apply a shift when performing binary comparison");
		btnOptimise.setText("Optimise");

		Group grpWeightOptions = new Group(grpCompare, SWT.NONE);
		grpWeightOptions
				.setToolTipText("Add weights to the data, note not used for cells");
		GridLayout gl_grpWeightOptions = new GridLayout(3, false);
		gl_grpWeightOptions.horizontalSpacing = 10;
		gl_grpWeightOptions.marginWidth = 10;
		gl_grpWeightOptions.marginHeight = 10;
		grpWeightOptions.setLayout(gl_grpWeightOptions);
		GridData gd_grpWeightOptions = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 3, 1);
		gd_grpWeightOptions.heightHint = 150;
		gd_grpWeightOptions.widthHint = 283;
		grpWeightOptions.setLayoutData(gd_grpWeightOptions);
		grpWeightOptions.setText("weight options");

		Label lblLowerX = new Label(grpWeightOptions, SWT.NONE);
		lblLowerX.setText("Lower X");
		lblLowerX.setToolTipText("The lower bound of weighting ");

		txtLower = new Text(grpWeightOptions, SWT.BORDER);
		txtLower.setText("lower");
		new Label(grpWeightOptions, SWT.NONE);

		Label lblUpperX = new Label(grpWeightOptions, SWT.NONE);
		lblUpperX.setText("Upper X");
		lblUpperX.setToolTipText("The Upper bound of weighting");

		txtUpper = new Text(grpWeightOptions, SWT.BORDER);
		txtUpper.setText("upper");
		new Label(grpWeightOptions, SWT.NONE);

		Label lblValue = new Label(grpWeightOptions, SWT.NONE);
		lblValue.setText("Value");
		lblValue.setToolTipText("The value of weight to be added");

		txtValue = new Text(grpWeightOptions, SWT.BORDER | SWT.CENTER);
		txtValue.setText("1");
		GridData gd_txtValue = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_txtValue.widthHint = 84;
		txtValue.setLayoutData(gd_txtValue);

		scale = new Scale(grpWeightOptions, SWT.NONE);
		scale.setMaximum(10);
		scale.setMinimum(1);
		scale.setIncrement(1);
		scale.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event arg0) {
				// TODO Auto-generated method stub
				int perspectiveValue = scale.getSelection();
				txtValue.setText(String.valueOf(perspectiveValue));
			}
		});
		GridData gd_scale = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_scale.widthHint = 114;
		scale.setLayoutData(gd_scale);
		new Label(grpWeightOptions, SWT.NONE);

		btnAddWeight = new Button(grpWeightOptions, SWT.NONE);
		btnAddWeight.setText("Add weight");
		btnAddWeight
				.setToolTipText("Add the slected weights to the comparison");
		btnAddWeight.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// add weights to the comparison
				Comparator.addWeights(Double.valueOf(txtValue.getText()));
				Comparator.addWeight_upper(Integer.valueOf(txtUpper.getText()));
				Comparator.addWeight_lower(Integer.valueOf(txtLower.getText()));
			}

		});

		btnClearWeights = new Button(grpWeightOptions, SWT.NONE);
		btnClearWeights.setToolTipText("Remove all weights from the program");
		btnClearWeights.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Comparator.delWeights();
			}
		});
		btnClearWeights.setText("Clear weights");

		Label lblType = new Label(grpCompare, SWT.NONE);
		lblType.setText("Type");

		types = new Combo(grpCompare, SWT.NONE);
		GridData gd_types = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_types.widthHint = 223;
		types.setLayoutData(gd_types);
		types.setText("Both");
		types.add("Both");
		types.add("Fractional");
		types.add("Binary");
		types.setToolTipText("The type of comparison\n fractional- ratio of data points \n binary - within a tolerance\n both - average of both comparison types");
		new Label(grpCompare, SWT.NONE);

		compare = new Button(grpCompare, SWT.PUSH);
		compare.setToolTipText("Compare the datasets selected");
		GridData gd_compare = new GridData(SWT.FILL, SWT.CENTER, false, false,
				2, 1);
		gd_compare.widthHint = 87;
		gd_compare.heightHint = 31;
		compare.setLayoutData(gd_compare);
		compare.setText("Compare");

		// ///////// functions //////////////
		comparefunction(compare);
		new Label(grpCompare, SWT.NONE);

		output = new Text(grpCompare, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		output.setFont(SWTResourceManager.getFont("Arial", 25, SWT.BOLD));
		GridData gd_output = new GridData(SWT.FILL, SWT.CENTER, false, false,
				2, 1);
		gd_output.widthHint = 180;
		gd_output.heightHint = 50;
		output.setLayoutData(gd_output);
		new Label(grpCompare, SWT.NONE);

		Group grpSearch = new Group(parent, SWT.NONE);
		grpSearch.setToolTipText("Search the CSD for matching cells");
		grpSearch.setText("Search");
		GridLayout gl_grpSearch = new GridLayout(6, false);
		gl_grpSearch.horizontalSpacing = 25;
		gl_grpSearch.verticalSpacing = 10;
		gl_grpSearch.marginWidth = 35;
		gl_grpSearch.marginHeight = 25;
		grpSearch.setLayout(gl_grpSearch);

		Label lblA = new Label(grpSearch, SWT.NONE);
		lblA.setText("A");

		txtAlength = new Text(grpSearch, SWT.BORDER);
		GridData gd_txtAlength = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_txtAlength.widthHint = 62;
		txtAlength.setText("10");
		txtAlength.setLayoutData(gd_txtAlength);

		Label lblB = new Label(grpSearch, SWT.NONE);
		lblB.setText("B");

		txtBlength = new Text(grpSearch, SWT.BORDER);
		txtBlength.setText("10");
		txtBlength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblC = new Label(grpSearch, SWT.NONE);
		lblC.setText("C");

		txtClength = new Text(grpSearch, SWT.BORDER);
		txtClength.setText("10");
		txtClength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblAlpha_1 = new Label(grpSearch, SWT.NONE);
		lblAlpha_1.setText("Alpha");

		txtAlpha = new Text(grpSearch, SWT.BORDER);
		txtAlpha.setText("90");
		txtAlpha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label lblBeta = new Label(grpSearch, SWT.NONE);
		lblBeta.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblBeta.setText("Beta");

		txtBeta = new Text(grpSearch, SWT.BORDER);
		txtBeta.setText("90");
		txtBeta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label lblGamma = new Label(grpSearch, SWT.NONE);
		lblGamma.setText("Gamma");

		txtGamma = new Text(grpSearch, SWT.BORDER);
		txtGamma.setText("90");
		txtGamma.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);

		Label lblNumresults = new Label(grpSearch, SWT.NONE);
		lblNumresults.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 3, 1));
		lblNumresults.setText("Number of results");

		txtNumresults = new Text(grpSearch, SWT.BORDER);
		txtNumresults.setText("10");
		GridData gd_txtNumresults = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_txtNumresults.widthHint = 84;
		txtNumresults.setLayoutData(gd_txtNumresults);
		new Label(grpSearch, SWT.NONE);

		Button btnSearch = new Button(grpSearch, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnSearch.setText("Search");
		searchfunction(btnSearch);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		// saves cells to the datalist
		Button btnSaveCells = new Button(grpSearch, SWT.NONE);
		btnSaveCells
				.setToolTipText("Save selected cells to the loaded data view");
		btnSaveCells.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (TableItem item : search_out.getItems()) {
					if (item.getChecked()) {
						String myitem = item.getText();
						String[] replacelist = new String[] { "CellLengths(a",
								",", "b=", "c=", "aplha=", "beta=", "gamma=",
								"CellAngles(alpha=", "=", ")" }; // remove the
																	// junk
						for (String s : replacelist) {
							myitem = myitem.replace(s, "");
						}
						// create a new dataobject for each cell
						String[] mylist = myitem.split(" ");
						IDataset celllengths = new DoubleDataset(new double[] {
								Double.valueOf(mylist[1]),
								Double.valueOf(mylist[2]),
								Double.valueOf(mylist[3]) }); // cell lengths
						celllengths.setName("Cell Lengths");
						IDataset cellangles = new DoubleDataset(new double[] {
								Double.valueOf(mylist[4]),
								Double.valueOf(mylist[5]),
								Double.valueOf(mylist[6]) }); // cell angles
						cellangles.setName("Cell angles");
						IDataset centering = new StringDataset(
								new String[] { mylist[7] });
						centering.setName("Cell centering");
						List<IDataset> data = new ArrayList<IDataset>();
						data.add(celllengths);
						data.add(cellangles);
						data.add(centering);
						try {
							LoadedDataview.addCell(mylist[0], data); // name,
																		// datalist
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							new ErrorWidget(e1);
						}

					}
				}

			}
		});
		btnSaveCells.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnSaveCells.setText("Save Cells");

		search_out = new Table(grpSearch, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.CHECK);
		search_out.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				6, 1));
		search_out.setHeaderVisible(true);
		search_out.setLinesVisible(true);
		search_out.setToolTipText("The first n matching results");
		TableColumn column = new TableColumn(search_out, SWT.NULL);
		column.setText("Output");
		column.pack();
	}

	/**
	 * Search function for the CSD
	 * 
	 * @param search
	 *            button
	 */
	public static void searchfunction(Button search) {

		SelectionAdapter searchfunc = new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				try {
					double a = Double.valueOf(txtAlength.getText());
					double b = Double.valueOf(txtBlength.getText());
					double c = Double.valueOf(txtClength.getText());

					double alpha = Double.valueOf(txtAlpha.getText());
					double beta = Double.valueOf(txtBeta.getText());
					double gamma = Double.valueOf(txtGamma.getText());
					List<Double> lengths = new ArrayList<Double>();
					lengths.add(a);
					lengths.add(b);
					lengths.add(c);

					List<Double> angles = new ArrayList<Double>();
					angles.add(alpha);
					angles.add(beta);
					angles.add(gamma);
					CSD_cellsearch mysearch = new CSD_cellsearch();
					mysearch.setCell_angles(angles);
					mysearch.setCell_lengths(lengths);
					mysearch.set_hits(Integer.valueOf(txtNumresults.getText()));

					List<String> result = CSD_cellsearch.search();
					search_out.removeAll();
					TableColumn[] columns = search_out.getColumns();
					for (TableColumn tc : columns) {
						tc.dispose();
					}
					TableColumn column = new TableColumn(search_out, SWT.NULL);
					column.setText("Output");

					for (String r : result) {
						final TableItem item1 = new TableItem(search_out,
								SWT.NULL);
						item1.setText(r);

					}
					column.pack(); // repack columns
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		search.addSelectionListener(searchfunc);

	}

	/**
	 * Sets the data when typing in the data1/data2 text boxes
	 * 
	 * @param text
	 *            the name of the LoadedDataobject
	 * @param combo
	 *            appends the data names to this combo
	 */
	public static void getdatalistener(final Text text, final Combo combo) {
		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				// Get the widget whose text was modified
				combo.removeAll();
				try {
					LoadedDataObject loaded = holder.getData(text.getText());
					List<IDataset> datalist = loaded.data;
					for (int i = 0; i < datalist.size(); i++) {
						combo.add(datalist.get(i).getName());
					}
				} catch (Exception e) {

				}
			}
		};
		text.addModifyListener(modifyListener);
	}

	/**
	 * Compares tow IDatasets
	 * 
	 * @param compare
	 */
	public static void comparefunction(final Button compare) {
		SelectionAdapter comparefunc = new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				try {
					String name1 = Data1.getText();
					String name2 = Data2.getText();
					String column1 = combo1.getItem(combo1.getSelectionIndex());
					String column2 = combo2.getItem(combo2.getSelectionIndex());

					IDataset data1 = holder.getDataSet(name1, column1);
					IDataset data2 = holder.getDataSet(name2, column2);
					String choice = types.getText();

					Comparator.setData1(data1);
					Comparator.setData2(data2);
					Comparator
							.setTolerance(Double.valueOf(tolerance.getText()));

					DecimalFormat df = new DecimalFormat("#.###");
					String result = df.format(Comparator.Compare(choice));
					output.setText(String.valueOf(result) + "%");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		compare.addSelectionListener(comparefunc);

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	/**
	 * Sets the cell for the search function
	 * 
	 * @param mydata
	 *            List of IDatasets of the cell data
	 */
	public static void setsearchCell(List<IDataset> mydata) {
		for (IDataset data : mydata) {
			if (data.getName().equals("Cell Lengths")) {
				txtAlength.setText(String.valueOf(data.getDouble(0)));
				txtBlength.setText(String.valueOf(data.getDouble(1)));
				txtClength.setText(String.valueOf(data.getDouble(2)));
			}
			if (data.getName().equals("Cell Angles")) {
				txtAlpha.setText(String.valueOf(data.getDouble(0)));
				txtBeta.setText(String.valueOf(data.getDouble(1)));
				txtGamma.setText(String.valueOf(data.getDouble(2)));
			}
		}

	}
}
