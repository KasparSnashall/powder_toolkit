package Views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.View;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import powder_toolkit.IndexTab;
import uk.ac.diamond.scisoft.analysis.io.DatLoader;
import uk.ac.diamond.scisoft.analysis.io.LoaderFactory;
import DataAnalysis.LoadedDataObject;
import DataAnalysis.Loader;
import DataAnalysis.MyDataHolder;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

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
	private MyDataHolder holder;
	private static Text sampletext;
	private Table table;
	private Table table_1;
	
	public void setholder(MyDataHolder holder){
		
	}
	
	

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
        GridLayout layout = new GridLayout(4,false);
        layout.marginWidth = 25;
        layout.marginHeight = 25;
        composite.setLayout(layout);
		// first label sample name
		Label samplename = new Label(composite, SWT.NONE);
		samplename.setText("Sample Name:");
		// textbox
		sampletext = new Text(composite,SWT.BORDER);
		sampletext.setText(" ");
		griddata_1 = new GridData(150,15);
		griddata_1.horizontalAlignment = SWT.CENTER;
		sampletext.setLayoutData(griddata_1);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		
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
		browse.setText("Browse...");
		// browse function
		final Shell shell = new Shell();
		new Label(composite, SWT.NONE);
		
		
		
		
        // check box
		final Button rangebox = new Button(composite, SWT.CHECK);
		rangebox.setText("Range");
		
		final Text lower = new Text(composite,SWT.BORDER);
		lower.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lower.setText("lower");
		lower.setEnabled(false);
		// upper and lower limit text boxes
		final Text upper = new Text(composite, SWT.BORDER);
		upper.setText("upper");
		upper.setEnabled(false);
		// rangebox function
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
        new Label(composite, SWT.NONE);
        // load button
        Button load = new Button(composite,SWT.PUSH);
        load.setText("Load");
        griddata_3 = new GridData(300,30);
        griddata_3.horizontalAlignment = SWT.LEFT;
        griddata_3.horizontalSpan = 3;
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
					
					java.util.List<IDataset> data = loader.Load_data(filepath);
					
					String flag = filepath.split("\\.")[1];
					holder.addData(sampletext.getText(), flag, data,filepath);
					//LoadedData.add(sampletext.getText());
					//IndexTab indextab = new IndexTab(holder);
					//indextab.setData(sampletext.getText());
					setData(sampletext.getText());

					}
					
					catch(Exception z){
						//textboxtext.append(z.toString());
						System.out.println(z.getStackTrace().toString());
						}	
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub}
			}});
        composite.pack();
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        
        Label lblDataTable = new Label(composite, SWT.NONE);
        lblDataTable.setText("Data Table");
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        
        final TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION );
        table = tableViewer.getTable();
        GridData gd_table_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        gd_table_2.heightHint = 201;
        gd_table_2.widthHint = 412;
        table.setLayoutData(gd_table_2);
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
							
							table = tableViewer.getTable();
							table.removeAll(); // refresh
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
							System.out.println(myextension.length);
							String extension = myextension[myextension.length-1];
							LoaderFactory.registerLoader(extension, DatLoader.class);
							IDataHolder dh = LoaderFactory.getData(filepath);
							System.out.println(dh.getList());
							
							for(int i =0; i < dh.size();i++){
								IDataset dataset = dh.getDataset(i);
								System.out.println(dataset.getName());
								TableColumn column = new TableColumn(table, SWT.NULL);				
								column.setText(dataset.getName());
								column.pack();}
							
							
							final TableItem item = new TableItem(table, SWT.NULL);
							
							for(int i =0; i < dh.size();i++){
								TableEditor editor = new TableEditor(table);
						        Combo combo = new Combo(table,SWT.BORDER);
						        combo.setSize(15, 50);
						        combo.setText("CCombo");
						        combo.add("item 1");
						        combo.add("item 2");
						        editor.grabHorizontal = true;
						        editor.setEditor(combo, item,i);
							}
							for(int i =0; i < dh.size();i++){
								IDataset dataset = dh.getDataset(i);
								if(i == 0){
									for(int j = 0; j < dataset.getSize();j++){
										final TableItem item1 = new TableItem(table, SWT.NULL);
										item1.setText(String.valueOf(dataset.getDouble(j)));
										System.out.println(dataset.getDouble(j));	
									}
									}
								else{
									for(int j = 0; j < dataset.getSize(); j++){
										TableItem item1 = table.getItem(j+1);
										item1.setText(i, String.valueOf(dataset.getDouble(j)));
										System.out.println(dataset.getDouble(j));
										
									}
								
								}
								
							}
							table.pack();
							table.layout();
							//tableViewer.re
							tableViewer.refresh();
							//composite.layout();
							
					        
								
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println(e.getMessage());
						}
			        	  
			        	 }
			          else
			        	  filetext.setText("Not a file");
				}
			}
		});
       
		}
	


	public void setData(String name) {
		// append the textbox
		try{
		LoadedDataObject mydata = holder.getData(name);
		//textboxtext.setText("");
		sampletext.setText(name);
		for(int i = 0; i < mydata.data.size();i++){
			IDataset mytext = mydata.data.get(i);
			
		}
		}
		catch(Exception e){System.out.println(e.getMessage());}
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
