package powder_toolkit;
import java.io.File;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import DataAnalysis.LoadedDataObject;
import DataAnalysis.Loader;
import DataAnalysis.MyDataHolder;


public class LoadTab{
	
	Boolean myrange = false;
	public static String filepath;
	public static GridData griddata = new GridData();
	private Text textboxtext;
	private MyDataHolder holder;
	private static Text sampletext;
	LoadTab(MyDataHolder holder){
		this.holder = holder;
	
	}
	

	public Composite create(CTabFolder folder,Composite parent,Display display,final List LoadedData){// composite allows me to use more then one item in my tab folder
        
		Composite composite = new Composite(folder, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        layout.marginWidth = 25;
        layout.marginHeight = 25;
        composite.setLayout(layout);
        
        Label readview = new Label(composite, SWT.NONE);
        readview.setText("Read view");
        
        // fill grid data
        griddata = new GridData(GridData.CENTER);
		griddata.horizontalSpan = 3;
		readview.setLayoutData(griddata);
		// first label sample name
		Label samplename = new Label(composite, SWT.NONE);
		samplename.setText("Sample Name:");
		// textbox
		sampletext = new Text(composite,SWT.BORDER);
		sampletext.setText(" ");
		griddata = new GridData(150,15);
		griddata.horizontalSpan = 2;
		sampletext.setLayoutData(griddata);
		
		
		// label filepath
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText("File path:");
		// filepath textbox
		final Text filetext = new Text(composite, SWT.BORDER);
		filetext.setText(" ");
		griddata = new GridData(150,15);
		filetext.setLayoutData(griddata);
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
		rangebox.setText("Range");
		
		final Text lower = new Text(composite,SWT.BORDER);
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
        griddata = new GridData(300,30);
        griddata.horizontalSpan = 3;
        griddata.minimumWidth = 40;
        load.setLayoutData(griddata);
        // output textbox
        textboxtext  = new Text(composite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
        griddata = new GridData(SWT.FILL, SWT.FILL, true, true);
        griddata.horizontalSpan = 3;
        textboxtext.setLayoutData(griddata);
        
       
		
		
		// load button function
        folder.setSelection(0);
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
					LoadedData.add(sampletext.getText());
					IndexTab indextab = new IndexTab(holder);
					indextab.setData(sampletext.getText());
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
        composite.pack();
        return composite;
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
	}


	
	}
