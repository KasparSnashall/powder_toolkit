package powder_toolkit;
import java.io.File;

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


public class LoadTab{
	Boolean myrange = false;
	public static Dataset data;
	public static String filepath;
	
	
	
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
				System.out.println("called"); // check
				try{
					/*
					textboxtext.setText(""); // clear text
					String myfilepath = filetext.getText(); // get filepath
					filepath = myfilepath; // set the general filepath
					String finalfilepath = "'"+myfilepath+"'"; // add filepath string to opts
					String myrangestring = "'"+myrange.toString()+"'"; // bool myrange to string 
					String top = "'"+upper.getText()+"'"; // upper limit
					String bottom = "'"+lower.getText()+"'"; // lowerlimit
					String opts = finalfilepath+","+myrangestring+","+top+","+bottom; // collect the options
					PyInstance Loader = ie.createClass("Loader",opts);// invoke my class as a pyobject
					data = Loader.invoke("load"); // calls the run function
					String datastring = data.toString(); // always returns pyobject so can be written to string
					textboxtext.append(sampletext.getText()+"\n"); // get the name of the data print to textbox
					textboxtext.append(datastring+"\n"); // print the data
					*/
					// normally this will send to the peaks tab
					new IndexTab().setMytitle(sampletext.getText());
					new IndexTab().setMydata(data);
					new IndexTab().setMyfilepath(filepath);
					System.out.println(filepath);
					System.out.println(data);
					
					
					
					
					}catch(Exception z){
						textboxtext.append("system error please retry with different values");}	
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub}
			}});
        composite.pack();
        return composite;
		}
	
	}
