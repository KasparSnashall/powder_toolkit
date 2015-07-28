package Views;

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
import DataAnalysis.Loader;
import DataAnalysis.MyDataHolder;

public class Loadview extends ViewPart {
	
	
	public Loadview() {
	}

	public static final String ID = "Views.Loadview"; //$NON-NLS-1$
	Boolean myrange = false;
	public static String filepath;
	public static GridData griddata = new GridData();
	private static GridData griddata_3;
	private static GridData griddata_2;
	private static GridData griddata_1;
	@SuppressWarnings("unused")
	private MyDataHolder holder;
	private static Text sampletext;
	private Table table;
	private static Map<String,TableEditor> editors = new HashMap<String,TableEditor>(); // I know its back to front this doesnt matter
	private static Map<Integer,String> comboList =  new HashMap<Integer,String>(); // list of combo data
	private static List<Integer> columnnumbers = new ArrayList<Integer>(); // list of column numbers needed?
	public void setholder(MyDataHolder holder){
		
	}
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final Shell shell = new Shell();
		composite.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
        GridLayout layout = new GridLayout(3,false);
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
		griddata_2.horizontalAlignment = SWT.CENTER;
		filetext.setLayoutData(griddata_2);
		// browse buutton
		Button browse = new Button(composite, SWT.PUSH);
		browse.setAlignment(SWT.LEFT);
		browse.setText("Open");
		browse.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
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
							GridData gd_table_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
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
						        combo.setText("Intensity");
						        combo.add("Intensity");
						        combo.add("Two theta");
						        combo.add("D_space");
						        combo.add("Theta");
						        editor.grabHorizontal = true;
						        editor.setEditor(combo, item,i);
						        item.setData("combo", combo); //Point to notice
						        combo.addSelectionListener(new SelectionAdapter() {
						            public void widgetSelected(SelectionEvent event) {
						            	comboList.put(k,combo.getText());
						              }
						            }
						          );     
							}
							
							

							final TableItem item2 = new TableItem(table, SWT.NONE);
							for(int i =0; i < dh.size();i++){
								final int k = i;
								TableEditor editor = new TableEditor(table);
						        final Button buttonb = new Button(table,SWT.CHECK);
						        String countnum = String.valueOf(i)+"B";
								editors.put(countnum,editor);
						        editor.grabHorizontal = true;
						        editor.setEditor(buttonb, item2,i);
						        buttonb.addSelectionListener(new SelectionAdapter() {
						        	public void widgetSelected(SelectionEvent event) {
						        		 if (buttonb.getSelection()){
						        			 columnnumbers.add(k);
						        		 }   
						        	     else{
						        	        columnnumbers.remove(columnnumbers.indexOf(k)); // remove value is unchecked	
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
		
		// first label sample name
		Label samplename = new Label(composite, SWT.NONE);
		samplename.setText("Sample Name:");
		// textbox
		sampletext = new Text(composite,SWT.BORDER);
		sampletext.setText(" ");
		griddata_1 = new GridData(150,15);
		griddata_1.horizontalAlignment = SWT.CENTER;
		sampletext.setLayoutData(griddata_1);
		// browse function
		
        // check box
		final Button rangebox = new Button(composite, SWT.CHECK);
		rangebox.setText("Range");
		// rangebox function
		
		
		final Text lower = new Text(composite,SWT.BORDER);
		lower.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lower.setText("lower");
		lower.setEnabled(false);
		// upper and lower limit text boxes
		final Text upper = new Text(composite, SWT.BORDER);
		upper.setText("upper");
		upper.setEnabled(false);

        composite.pack();
        // load button
        Button load = new Button(composite,SWT.PUSH);
        load.setText("Load");
        griddata_3 = new GridData(276,30);
        griddata_3.horizontalSpan = 2;
        griddata_3.horizontalAlignment = SWT.LEFT;
        griddata_3.minimumWidth = 40;
        load.setLayoutData(griddata_3);
        // load button function
        
        load.addSelectionListener(new SelectionListener(){
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		try{
        			
        			//textboxtext.setText(""); // clear text
        			String myfilepath = filetext.getText(); // get filepath
        			filepath = myfilepath; // set the general filepath
        			Loader loader = new Loader();
        			if (myrange){
        				// if ranged must be able to accept doubles or floats
        				loader.setRange(true);
        				String l = lower.getText();
        				String u = upper.getText();
        				loader.setUpper(Integer.valueOf(u));
        				loader.setLower(Integer.valueOf(l));
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
        			List<IDataset> data = loader.Load_data(myfilepath, names, flag, columnnumbers); // may change this
        			LoadedDataview.addData(sampletext.getText(), flag, data,filepath);
        			Indexview.setData(sampletext.getText());
        			Peakview.createMyplot(data.get(0));
        			}
        			
        			catch(Exception z){
        				
        				System.out.println(z.getMessage());
        				}	
        	}

        	@Override
        	public void widgetDefaultSelected(SelectionEvent e) {
        		// TODO Auto-generated method stub}
        	}});
        Label lblDataTable = new Label(composite, SWT.NONE);
        lblDataTable.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2));
        lblDataTable.setText("Data Table");

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
        
        table = new Table(composite,SWT.BORDER);
        GridData gd_table_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2);
        gd_table_2.heightHint = 201;
        gd_table_2.widthHint = 412;
        table.setLayoutData(gd_table_2);
       
       
		}
	

	@Override
	public void setFocus() {
		// Set the focus
	}
	
}
