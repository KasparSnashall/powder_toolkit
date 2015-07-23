package Views;

import java.io.File;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import powder_toolkit.IndexTab;
import DataAnalysis.LoadedDataObject;
import DataAnalysis.Loader;
import DataAnalysis.MyDataHolder;
import org.eclipse.wb.swt.SWTResourceManager;

public class Loadview extends ViewPart {

	public static final String ID = "Views.Loadview"; //$NON-NLS-1$
	Boolean myrange = false;
	public static String filepath;
	public static GridData griddata = new GridData();
	private static GridData griddata_3;
	private static GridData griddata_2;
	private static GridData griddata_1;
	private Text textboxtext;
	private MyDataHolder holder;
	private static Text sampletext;
	public void setholder(MyDataHolder holder){
		this.holder = holder;
	}
	
	public Loadview() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
        GridLayout layout = new GridLayout(3,false);
        layout.marginWidth = 25;
        layout.marginHeight = 25;
        composite.setLayout(layout);
        
        Label readview = new Label(composite, SWT.NONE);
        readview.setText("Read view");
        
        // fill grid data
        griddata = new GridData(GridData.CENTER);
        griddata.horizontalAlignment = SWT.CENTER;
		readview.setLayoutData(griddata);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		// first label sample name
		Label samplename = new Label(composite, SWT.NONE);
		samplename.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		samplename.setText("Sample Name:");
		// textbox
		sampletext = new Text(composite,SWT.BORDER);
		sampletext.setText(" ");
		griddata_1 = new GridData(150,15);
		griddata_1.horizontalAlignment = SWT.CENTER;
		sampletext.setLayoutData(griddata_1);
		new Label(composite, SWT.NONE);
		
		
		// label filepath
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
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
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String filepath = new FileDialog(shell).open();
				if (filepath != null) {
						// if a filepath is input
			          File file = new File(filepath);
			          if (file.isFile()){
			        	  // check is actually a file and not just directory
			        	  filetext.setText(filepath);
			        	  sampletext.setText(file.getName());}
			          else
			        	  filetext.setText("Not a file");
				}
			}
		});
		
        // check box
		final Button rangebox = new Button(composite, SWT.CHECK);
		rangebox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
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
					
					textboxtext.setText(""); // clear text
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
						textboxtext.append(z.toString());
						System.out.println(z.getStackTrace().toString());
						}	
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub}
			}});
        // output textbox
        textboxtext  = new Text(composite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
        griddata = new GridData(SWT.FILL, SWT.FILL, true, true);
        griddata.horizontalSpan = 3;
        textboxtext.setLayoutData(griddata);
        composite.pack();

		}


	public void setData(String name) {
		// append the textbox
		try{
		LoadedDataObject mydata = holder.getData(name);
		textboxtext.setText("");
		sampletext.setText(name);
		for(int i = 0; i < mydata.data.size();i++){
			IDataset mytext = mydata.data.get(i);
			textboxtext.append(mytext+"\n");
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
