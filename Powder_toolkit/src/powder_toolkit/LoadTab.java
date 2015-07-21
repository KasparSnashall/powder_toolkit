package powder_toolkit;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.python.core.PyInstance;
import org.python.core.PyObject;
import DataAnalysis.Loader;
import Jython_programs.interpreter;


public class LoadTab{
	
	Boolean myrange = false;
	public static List<IDataset> data;
	public static String filepath;
	public Map <String,List<IDataset> > dataholder;
	
	



	public Composite create(CTabFolder folder,final Shell shell,Display display){// composite allows me to use more then one item in my tab folder
        Composite composite = new Composite(folder, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        layout.marginWidth = 25;
        layout.marginHeight = 25;
        composite.setLayout(layout);
        
        Label readview = new Label(composite, SWT.NONE);
        readview.setText("Read view");
        
        // fill grid data
        GridData griddata = new GridData(GridData.CENTER);
		griddata.horizontalSpan = 3;
		readview.setLayoutData(griddata);
		// first label sample name
		Label samplename = new Label(composite, SWT.NONE);
		samplename.setText("Sample Name:");
		// textbox
		final Text sampletext = new Text(composite,SWT.BORDER);
		sampletext.setText(" ");
		GridData griddata2 = new GridData(150,15);
		griddata2.horizontalSpan = 2;
		sampletext.setLayoutData(griddata2);
		
		
		// label filepath
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText("File path:");
		// filepath textbox
		final Text filetext = new Text(composite, SWT.BORDER);
		filetext.setText(" ");
		GridData griddata3 = new GridData(150,15);
		filetext.setLayoutData(griddata3);
		// browse buutton
		Button browse = new Button(composite, SWT.PUSH);
		browse.setAlignment(SWT.LEFT);
		browse.setText("Browse...");
		// browse function
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
        GridData griddata4 = new GridData(300,30);
        griddata4.horizontalSpan = 3;
        griddata4.minimumWidth = 40;
        load.setLayoutData(griddata4);
        new Label(composite,SWT.NONE).setText("Output");;
        // output textbox
        final Text textboxtext = new Text(composite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        GridData griddata5 = new GridData(SWT.FILL, SWT.FILL, true, true);
        griddata5.horizontalSpan = 3;
        textboxtext.setLayoutData(griddata5);
        
        final interpreter ie = new interpreter(); // call my interpreter
		ie.execfile("python_code/Loader.py"); // my file
		
		
		
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
					loader.setLower(Integer.valueOf(lower.getText()));
					loader.setUpper(Integer.valueOf(upper.getText()));
					loader.setRange(true);
					}
					
					data = loader.Load_data(filepath);
					// normally this will send to the peaks tab
					IndexTab indextab = new IndexTab();
					indextab.setMytitle(sampletext.getText());
					indextab.setMydata(data);
					indextab.setMyfilepath(filepath);
					// print out the data
					for(int i = 0; i < data.size();i++){
						IDataset mytext = data.get(i);
						textboxtext.append(mytext+"\n");
					}
					
					
					}
					
					catch(Exception z){
						textboxtext.append(z.toString());
						}	
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub}
			}});
        composite.pack();
        return composite;
		}
	
	}
