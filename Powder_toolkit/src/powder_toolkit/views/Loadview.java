package powder_toolkit.views;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.diamond.scisoft.analysis.io.DatLoader;
import uk.ac.diamond.scisoft.analysis.io.LoaderFactory;

import org.eclipse.swt.widgets.Combo;

import powder_toolkit.dataAnalysis.Loader;
import powder_toolkit.dataAnalysis.MyDataHolder;

@SuppressWarnings("deprecation")
public class Loadview extends ViewPart {
	public Loadview() {
	}
	
	public static final String ID = "Views.Loadview"; //$NON-NLS-1$
	static Boolean myrange = false;
	public static String filepath;
	public static GridData griddata = new GridData();
	private static GridData griddata_3;
	private static GridData griddata_2;
	private static GridData griddata_1;
	private static Text sampletext;
	private Table table;
	private static Map<String,TableEditor> editors = new HashMap<String,TableEditor>(); // I know its back to front this doesnt matter
	private static Map<Integer,String> comboList =  new HashMap<Integer,String>(); // list of combo data
	private static List<Integer> columnnumbers = new ArrayList<Integer>(); // list of column numbers needed?
	private static int checkboxnumber = 0;
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final Shell shell = new Shell();
		composite.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
        GridLayout layout = new GridLayout(7,false);
        layout.marginWidth = 25;
        layout.marginHeight = 25;
        composite.setLayout(layout);
		// label filepath
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText("File path:");
		// filepath textbox
		final Text filetext = new Text(composite, SWT.BORDER);
		filetext.setText(" ");
		griddata_2 = new GridData(150,15);
		griddata_2.horizontalSpan = 2;
		griddata_2.horizontalAlignment = SWT.CENTER;
		filetext.setLayoutData(griddata_2);
		// browse buutton
		Button browse = new Button(composite, SWT.PUSH);
		griddata_2 = new GridData();
		griddata_2.horizontalSpan = 3;
		griddata_2.horizontalAlignment = SWT.LEFT;
		browse.setText("Open");
		browse.setToolTipText("open a data file");
		browse.setLayoutData(griddata_2);
		
		
		new Label(composite, SWT.NONE);
		
		// first label sample name
		Label samplename = new Label(composite, SWT.NONE);
		samplename.setText("Sample Name:");
		
		// textbox
		sampletext = new Text(composite,SWT.BORDER);
		sampletext.setToolTipText("Enter a custom data name, try not to include spaces");
		sampletext.setText(" ");
		griddata_1 = new GridData(150,15);
		griddata_1.horizontalSpan = 4;
		griddata_1.horizontalAlignment = SWT.LEFT;
		sampletext.setLayoutData(griddata_1);

        composite.pack();
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);  
          // check box
        final Button rangebox = new Button(composite, SWT.CHECK);
        rangebox.setText("X range");
        rangebox.setToolTipText("Apply a range, between upper and lower values");
     
        final Text lower = new Text(composite,SWT.BORDER);
        lower.setText("lower");
        lower.setEnabled(false);
        lower.setToolTipText("The lower x value");
        // upper and lower limit text boxes
        final Text upper = new Text(composite, SWT.BORDER);
        upper.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        upper.setText("upper");
        upper.setEnabled(false);
        upper.setToolTipText("The upper x value");
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        Label lblDataTable = new Label(composite, SWT.NONE);
        lblDataTable.setText("Data Table");
        Button load = new Button(composite,SWT.PUSH);
        load.setText("Load");
        load.setToolTipText("Load in your data");
        griddata_3 = new GridData(211,30);
        griddata_3.horizontalSpan = 4;
        griddata_3.horizontalAlignment = SWT.LEFT;
        griddata_3.minimumWidth = 40;
        load.setLayoutData(griddata_3);
        
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        
        table = new Table(composite,SWT.BORDER);
        table.setHeaderVisible(true);
        table.setToolTipText("Data table, opened data files will appear here");
        GridData gd_table_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 7, 4);
        gd_table_2.heightHint = 201;
        gd_table_2.widthHint = 412;
        gd_table_2.heightHint = 201;
        gd_table_2.widthHint = 412;
        table.setLayoutData(gd_table_2);
        
        
        LoadFunction(load, filetext, lower, upper);
        RangeBoxFunction(rangebox,lower,upper);
        BrowseFunction(browse, shell, filetext, composite, parent);
       
		}
	
	private void BrowseFunction(final Button browse,final Shell shell,final Text filetext, final Composite composite,final Composite parent){
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
					String filepath = new FileDialog(shell).open();
					if (filepath != null) {
							// if a filepath is input
				          File file = new File(filepath);
				          if (file.isFile()){
				        	  // check is actually a file and not just directory
				        	  filetext.setText(filepath);
				        	  sampletext.setText(file.getName());
				        	  
							try {
								try{
									for(TableEditor myeditor : editors.values()){
										myeditor.getEditor().dispose();}
								}catch(Exception e){}
								editors = new HashMap<String,TableEditor>(); 
								table.removeAll(); // refresh
								TableItem[] items = table.getItems();
								for (TableItem myitem : items){
									myitem.dispose();}
								TableColumn[] columns = table.getColumns();
								for( TableColumn tc : columns ) {  
									tc.dispose() ;}
								GridData gd_table_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 7, 4);
							    gd_table_2.heightHint = 201;
							    gd_table_2.widthHint = 510;
						        table.setLayoutData(gd_table_2);
						        table.setHeaderVisible(true);
						        table.setLinesVisible(true);
						       

								String[] myextension = filepath.split("\\.");
								String extension = myextension[myextension.length-1];
								LoaderFactory.registerLoader(extension, DatLoader.class);
								IDataHolder dh = LoaderFactory.getData(filepath);
								
								
								for(int i =0; i < dh.size();i++){
									IDataset dataset = dh.getDataset(i);
									TableColumn column = new TableColumn(table, SWT.NULL);				
									column.setText(dataset.getName());
									column.pack();}
								
								
								final TableItem item = new TableItem(table, SWT.NULL);
								
								
								for(int i =0; i < dh.size();i++){
									final int k = i;
									TableEditor editor = new TableEditor(table);
									String countnum = String.valueOf(i) + "C";
									editors.put(countnum,editor);

							        final CCombo combo = new CCombo(table,SWT.BORDER);
							        combo.setText("Select");
							        combo.add("Intensity");
							        combo.add("Two theta");
							        combo.add("D_space");
							        combo.add("Theta");
							        combo.setToolTipText("Select the name of the column");
							        editor.grabHorizontal = true;
							        combo.setEnabled(false);
							        editor.setEditor(combo, item,i);
							        item.setData("combo", combo); //Point to notice
							        combo.addSelectionListener(new SelectionAdapter() {
							            public void widgetSelected(SelectionEvent event) {
							            	if(combo.isEnabled()){
							            	comboList.put(k,combo.getText());}
							              }
							            	
							            }
							          );     
								}
								
								

								final TableItem item2 = new TableItem(table, SWT.NONE);
								for(int i =0; i < dh.size();i++){
									final int k = i;
									TableEditor editor = new TableEditor(table);
							        final Button buttonb = new Button(table,SWT.CHECK);
							        buttonb.setToolTipText("Enable column");
							        String countnum = String.valueOf(i);
									editors.put(countnum,editor);
							        editor.grabHorizontal = true;
							        editor.setEditor(buttonb, item2,i);
							        buttonb.addSelectionListener(new SelectionAdapter() {
							        	public void widgetSelected(SelectionEvent event) {
							        		 if (buttonb.getSelection()){
							        			 if(checkboxnumber == 0){
							        			 columnnumbers.add(k);
							        			 editors.get(k+"C").getEditor().setEnabled(true);
							        			 checkboxnumber += 1;
							        			 }
							        			 else if(checkboxnumber == 1){
							        				 columnnumbers.add(k);
								        			 editors.get(k+"C").getEditor().setEnabled(true);
								        			 checkboxnumber += 1;
							        			 }
							        			 else{
							        				 System.out.println("Too many boxes");
							        				 
							        			 }
							        		 }   
							        	     else{
							        	        columnnumbers.remove(columnnumbers.indexOf(k)); // remove value is unchecked
							        	        editors.get(k+"C").getEditor().setEnabled(false);
							        	        checkboxnumber -= 1;
							        	        }
							        	           
		
							              }
							        });
								}
								
								
								for(int i =0; i < dh.size();i++){
									IDataset dataset = dh.getDataset(i);
									if(i == 0){
										for(int j = 0; j < dataset.getSize();j++){
											final TableItem item1 = new TableItem(table, SWT.NULL);
											item1.setText(String.valueOf(dataset.getDouble(j)));
											
										}
										}
									else{
										for(int j = -1; j < dataset.getSize(); j++){
											TableItem item1 = table.getItem(j+2);
											item1.setText(i, String.valueOf(dataset.getDouble(j)));
											
										}
									
									}
									
								}
								TableColumn[] mycolumnlist = table.getColumns();
								for(TableColumn mycol : mycolumnlist){
									mycol.setWidth(150);
								}
								table.pack();
								table.layout();
								
								composite.layout();
								parent.layout();
						        
									
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								System.out.println(e.getMessage());
							}
				        	  
				        	 }
					}
				}
			});
	}
	
	private void RangeBoxFunction(final Button rangebox,final Text lower,final Text upper) {
		rangebox.addSelectionListener(new SelectionAdapter()
        {
        @Override
        public void widgetSelected(SelectionEvent e)
	        {
	        if (rangebox.getSelection())
		        {myrange = true;
		        upper.setEnabled(true);
		        lower.setEnabled(true);
		        }
	        else
		        {
		        myrange = false;
		        upper.setEnabled(false);
		        lower.setEnabled(false);
		        }
	        }
        });
		
	}

	private static void LoadFunction(final Button load,final Text filetext, final Text lower, final Text upper){
		SelectionListener mylistener  = new SelectionListener(){
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		try{String myfilepath = filetext.getText(); // get filepath
        			filepath = myfilepath; // set the general filepath
        			Loader loader = new Loader();
        			if (myrange){
        				// if ranged must be able to accept doubles or floats
        				loader.setRange(true);
						String l = lower.getText();
        				String u = upper.getText();
        				loader.setUpper(Double.valueOf(u));
        				loader.setLower(Double.valueOf(l));
        				}
        			else{
        				loader.setRange(false);
        			}
        			String flag = filepath.split("\\.")[1];
        			List<String> names =  new ArrayList<String>();
        			for(int j : columnnumbers){
        				String columnName = comboList.get(j);
        				names.add(columnName);
        			}
        			if(names.size() > 1){
	        			if(names.get(0).equals(names.get(1))){
	        				throw new Exception("Column names may not be the same");
        			}}
        			List<IDataset> data = loader.Load_data(myfilepath, names, flag, columnnumbers); // may change this
        			LoadedDataview.addData(sampletext.getText(), flag, data,filepath);
        			Indexview.setData(sampletext.getText());
        			
        			}
        			
        			catch(Exception z){
        				
        				System.out.println(z.getMessage());
        				}	
        	}
        	@Override
        	public void widgetDefaultSelected(SelectionEvent e) {
        		// TODO Auto-generated method stub}
        	}};
        	load.addSelectionListener(mylistener);
		
	}
	
	@Override
	public void setFocus() {
		// Set the focus
	}
	
}