package powder_toolkit.views;

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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import powder_toolkit.dataAnalysis.Comparator;
import powder_toolkit.dataAnalysis.LoadedDataObject;
import powder_toolkit.dataAnalysis.MyDataHolder;
import powder_toolkit.jython_programs.CSD_cellsearch;
import powder_toolkit.widgets.ErrorWidget;

import org.eclipse.swt.widgets.Table;

public class Compareview extends ViewPart {

	public static final String ID = "Views.Compareview"; //$NON-NLS-1$
	private static MyDataHolder holder = LoadedDataview.holder;
	private static Text Data1; // data text box 1
	private static Text Data2; // data text box 2
	private static Combo combo1; // drop down 1
	private static Combo combo2; // drop down 2
	private static Combo types; // drop down with comparison types
	private static Text output; // output text box
	private static Text tolerance;
	private static Text txtAlength;
	private static Text txtBlength;
	private static Text txtClength;
	private static Text txtAlpha;
	private static Text txtBeta;
	private static Text txtGamma;
	private static Text txtNumresults;
	private static Table search_out;
	
	public Compareview() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		FillLayout fillLayout = (FillLayout) parent.getLayout();
		fillLayout.spacing = 10;
		fillLayout.marginWidth = 15;
		fillLayout.marginHeight = 15;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
        composite.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        Group grpCompare = new Group(composite, SWT.NONE);
        GridLayout gl_grpCompare = new GridLayout(3, false);
        gl_grpCompare.marginWidth = 15;
        gl_grpCompare.marginHeight = 25;
        grpCompare.setLayout(gl_grpCompare);
        grpCompare.setText("Compare");
        
        
        Label data1label = new Label(grpCompare, SWT.NONE);
        data1label.setText("Data 1");
        
        Data1 = new Text(grpCompare, SWT.BORDER);
        
        // add in listeners
       
        combo1 = new Combo(grpCompare,SWT.NONE);
        GridData gd_combo1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_combo1.widthHint = 222;
        combo1.setLayoutData(gd_combo1);
        getdatalistener(Data1,combo1);
        Label data2label = new Label(grpCompare, SWT.NONE);
        data2label.setText("Data 2");
        
        Data2 = new Text(grpCompare, SWT.BORDER);
       
        combo2 = new Combo(grpCompare,SWT.NONE);
        GridData gd_combo2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_combo2.widthHint = 216;
        combo2.setLayoutData(gd_combo2);
        getdatalistener(Data2,combo2);
        Label lblTolerance = new Label(grpCompare, SWT.NONE);
        lblTolerance.setText("Tolerance");
        tolerance = new Text(grpCompare,SWT.BORDER);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        
        Label lblType = new Label(grpCompare, SWT.NONE);
        lblType.setText("Type");
        
        types = new Combo(grpCompare,SWT.NONE);
        GridData gd_types = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_types.widthHint = 223;
        types.setLayoutData(gd_types);
        types.setText("Both");
        types.add("Both");
        types.add("Fractional");
        types.add("Binary");
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        
        Button compare = new Button(grpCompare, SWT.PUSH);
        compare.setText("Compare");
        
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        
        output = new Text(grpCompare, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        GridData gd_output = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_output.widthHint = 218;
        gd_output.heightHint = 93;
        output.setLayoutData(gd_output);
        
        Group grpSearch = new Group(parent, SWT.NONE);
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
        GridData gd_txtAlength = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_txtAlength.widthHint = 62;
        txtAlength.setText("10");
        txtAlength.setLayoutData(gd_txtAlength);
        
        Label lblB = new Label(grpSearch, SWT.NONE);
        lblB.setText("B");
        
        txtBlength = new Text(grpSearch, SWT.BORDER);
        txtBlength.setText("10");
        txtBlength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblC = new Label(grpSearch, SWT.NONE);
        lblC.setText("C");
        
        txtClength = new Text(grpSearch, SWT.BORDER);
        txtClength.setText("10");
        txtClength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblAlpha_1 = new Label(grpSearch, SWT.NONE);
        lblAlpha_1.setText("Alpha");
        
        
        txtAlpha = new Text(grpSearch, SWT.BORDER);
        txtAlpha.setText("90");
        txtAlpha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblBeta = new Label(grpSearch, SWT.NONE);
        lblBeta.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblBeta.setText("Beta");
        
        txtBeta = new Text(grpSearch, SWT.BORDER);
        txtBeta.setText("90");
        txtBeta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblGamma = new Label(grpSearch, SWT.NONE);
        lblGamma.setText("Gamma");
        
        txtGamma = new Text(grpSearch, SWT.BORDER);
        txtGamma.setText("90");
        txtGamma.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        
        Label lblNumresults = new Label(grpSearch, SWT.NONE);
        lblNumresults.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
        lblNumresults.setText("Number of results");
        
        txtNumresults = new Text(grpSearch, SWT.BORDER);
        txtNumresults.setText("1");
        GridData gd_txtNumresults = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_txtNumresults.widthHint = 84;
        txtNumresults.setLayoutData(gd_txtNumresults);
        
        
        /////////// functions //////////////
        comparefunction(compare);
        new Label(grpSearch, SWT.NONE);
        
        Button btnSearch = new Button(grpSearch, SWT.NONE);
        btnSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnSearch.setText("Search");
        searchfunction(btnSearch);
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        new Label(grpSearch, SWT.NONE);
        
        Button btnSaveCells = new Button(grpSearch, SWT.NONE);
        btnSaveCells.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		for(TableItem item : search_out.getItems()){
        			if(item.getChecked()){
        				String myitem = item.getText();
        				String[] replacelist = new String[]{"CellLengths(a",",","b=","c=","aplha=","beta=","gamma=","CellAngles(alpha=","=",")"}; // remove the junk
        				for(String s : replacelist){
        					myitem = myitem.replace(s, "");
        				}
        				// create a new dataobject for each cell
        				String[] mylist = myitem.split(" ");
        				IDataset celllengths = new DoubleDataset(new double[]{Double.valueOf(mylist[1]),Double.valueOf(mylist[2]),Double.valueOf(mylist[3])}); // cell lengths
        				celllengths.setName("Cell Lengths");
        				IDataset cellangles = new DoubleDataset(new double[]{Double.valueOf(mylist[4]),Double.valueOf(mylist[5]),Double.valueOf(mylist[6])}); // cell angles
        				cellangles.setName("Cell angles");
        				IDataset centering =  new StringDataset(new String[]{mylist[7]});
        				centering.setName("Cell centering");
        				List<IDataset> data = new ArrayList<IDataset>();
        				data.add(celllengths);
        				data.add(cellangles);
        				data.add(centering);
        				try {
							LoadedDataview.addCell(mylist[0], data); // name, datalist
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							new ErrorWidget(e1);
						}
        				
        				
        			}
        		}
        		
        	}
        });
        btnSaveCells.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnSaveCells.setText("Save Cells");
        
      
        search_out = new Table(grpSearch, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
        search_out.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
        search_out.setHeaderVisible(true);
        search_out.setLinesVisible(true);
        search_out.setToolTipText("The first n matching results");
        TableColumn column = new TableColumn(search_out, SWT.NULL);
        column.setText("Output");
        column.pack();
        }
	
	public static void searchfunction(Button search){
		
		SelectionAdapter searchfunc = new SelectionAdapter() {
		      
			public void widgetSelected(SelectionEvent event) {
				try{
					double a = Double.valueOf(txtAlength.getText());
					double b = Double.valueOf(txtBlength.getText());
					double c = Double.valueOf(txtClength.getText());
					
					double alpha = Double.valueOf(txtAlpha.getText());
					double beta = Double.valueOf(txtBeta.getText());
					double gamma = Double.valueOf(txtGamma.getText());
					List<Double> lengths = new ArrayList<Double>();
					lengths.add(a);lengths.add(b);lengths.add(c);
					
					List<Double> angles = new ArrayList<Double>();
					angles.add(alpha);angles.add(beta);angles.add(gamma);
					CSD_cellsearch mysearch = new CSD_cellsearch();
					mysearch.setCell_angles(angles);
					mysearch.setCell_lengths(lengths);
					mysearch.set_hits(Integer.valueOf(txtNumresults.getText()));
					
					List<String> result = CSD_cellsearch.search();
					search_out.removeAll();
					TableColumn[] columns = search_out.getColumns();
					for( TableColumn tc : columns ) {  
						tc.dispose() ;}
			        TableColumn column = new TableColumn(search_out, SWT.NULL);
			        column.setText("Output");
			       
					
					for(String r : result){
						final TableItem item1 = new TableItem(search_out, SWT.NULL);
						item1.setText(r);
						
					}
					 column.pack();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		        
		        }
		};
		search.addSelectionListener(searchfunc);
		
	}
	
	public static void getdatalistener(final Text text,final Combo combo){
	ModifyListener modifyListener = new ModifyListener(){
		      public void modifyText(ModifyEvent event) {
		        // Get the widget whose text was modified
		    	  combo.removeAll();
		        try{	
		        	LoadedDataObject loaded = holder.getData(text.getText());
		        	List<IDataset> datalist = loaded.data;
		        	for(int i = 0; i < datalist.size(); i++){
		        		combo.add(datalist.get(i).getName());
		        	}
		        }catch(Exception e){
		        	new ErrorWidget(e);
		        	
		        }
		      }
	};
	text.addModifyListener(modifyListener);
	}
	
	public static void comparefunction(final Button compare){
		SelectionAdapter comparefunc = new SelectionAdapter() {
		      
			public void widgetSelected(SelectionEvent event) {
				try{
				String name1 = Data1.getText();
				String name2 = Data2.getText();
				String column1 = combo1.getItem(combo1.getSelectionIndex());
				String column2 = combo2.getItem(combo2.getSelectionIndex());
				
				IDataset data1 = holder.getDataSet(name1, column1);
				IDataset data2 = holder.getDataSet(name2, column2);
				String choice = types.getText();

				Comparator.setData1(data1);
				Comparator.setData2(data2);
				
				Double result = Comparator.Compare(choice);
				output.append(String.valueOf(result)+"\n");
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
}
