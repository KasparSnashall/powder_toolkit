package powder_toolkit;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import DataAnalysis.IPowderIndexer;
import DataAnalysis.LoadedDataObject;
import DataAnalysis.MyDataHolder;
import DataAnalysis.Ntreor;


public class IndexTab {
	
	GridData griddata;
	public static Text textbox; // loaded_data output
	public static List<IDataset> data; // data to be used
	public static String filepath = ""; // filepath (if loaded data this will be the loaded path)
	public static String title = "";
    // colours used in the properties widget
	private final Color grey = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
	private final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	private final Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private MyDataHolder holder;
	
	IndexTab(MyDataHolder holder){
		this.holder = holder;
	}
	
	public Composite create(CTabFolder folder,Composite parent,Display display){// composite allows me to use more then one item in my tab folder
		// new master composite
        Composite composite = new Composite(folder, SWT.LEFT);
        GridLayout gridlayout = new GridLayout(3, false);
        gridlayout.marginWidth = 10; // margins
        gridlayout.marginHeight = 10; // margins
        composite.setLayout(gridlayout); // three columns composite
        
        // the index view
        Label Indexview  = new Label(composite,SWT.NONE);
        Indexview.setText("Index View");
        griddata = new GridData(SWT.FILL, SWT.FILL, true, false); 
        griddata.horizontalSpan = 3; // 3 columns wide 
        Indexview.setLayoutData(griddata);
        
        // the load radio button
        final Button loadButton = new Button(composite, SWT.RADIO);
        loadButton.setText("Loaded data");
        loadButton.setSelection(true);
        
        // the load data textbox
        textbox = new Text(composite,SWT.BORDER);
        textbox.setText(title);
        griddata = new GridData(150,20); 
        griddata.horizontalSpan = 2;
        textbox.setLayoutData(griddata);
        
        // from file radio button
        final Button peaksButton = new Button(composite, SWT.RADIO);
        peaksButton.setText("Existing .dat");
        
        // text box for the filepath
        final Text filepathbox =  new Text(composite,SWT.BORDER);
        griddata = new GridData(150,20); 
        filepathbox.setLayoutData(griddata);
        
        // browse button
        final Button browse = new Button(composite, SWT.PUSH);
        browse.setAlignment(SWT.LEFT);
        browse.setText("Browse");
        filepathbox.setEnabled(false);
        browse.setEnabled(false);
        
        // properties widget tabs
        final CTabFolder indexfolder = new CTabFolder(composite, SWT.TOP | SWT.BORDER); // create a tab set
        griddata = new GridData(SWT.FILL,SWT.FILL,false, true, 2, 2);
        griddata.grabExcessHorizontalSpace = true;
        griddata.minimumHeight = 300;
        indexfolder.setLayoutData(griddata);
        
        // indeing programs list
        final Table indexingprogs = new Table(composite,SWT.CHECK | SWT.SCROLL_LINE |SWT.BORDER); // list of programs
        griddata = new GridData(SWT.FILL,SWT.FILL,false, false, 1, 2);
        griddata.minimumHeight = 200;
        indexingprogs.setLayoutData(griddata);
        new TableColumn(indexingprogs,SWT.NULL).setText("Programs");
        indexingprogs.getColumn(0).pack();
        indexingprogs.setHeaderVisible(true); // make sure headers are seen
        
        // list of programs available
        String[] myprogslist = new String[]{"Ntreor","McMaille"};
        // add the list to the programs table
        for (int loopIndex = 0; loopIndex < myprogslist.length; loopIndex++) {
  	      TableItem item = new TableItem(indexingprogs, SWT.NULL);
  	      item.setText(0,myprogslist[loopIndex]);}
        
        
        // add variable button
        Button addvariable = new Button(composite,SWT.NONE);
        addvariable.setText("Add variables");
        
        // reset button
        Button Reset = new Button(composite,SWT.NONE);
        Reset.setText("Reset");
        
        Button Save = new Button(composite,SWT.NONE);
        griddata = new GridData(150,30);
        griddata.horizontalAlignment = SWT.RIGHT;
        Save.setLayoutData(griddata);
        Save.setText("Save");
        
        // Add the ntreor tab, at time of writing this is the only one available
        CTabItem cTabItem1 = new CTabItem(indexfolder, SWT.NONE);
        cTabItem1.setText("Ntreor");        
        // get the properties widget
        Properties_Widget NtreorTable = new Properties_Widget();
        IPowderIndexer Ntreor = new Ntreor();
        Table Ntreor_table = NtreorTable.create(indexfolder, Ntreor); // folder indexer
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
        griddata = new GridData(150,50);
        griddata.horizontalAlignment = SWT.RIGHT; // align the button right
        griddata.horizontalSpan = 3;
        run.setLayoutData(griddata);
        
        // ******************** output tabs *************************************//
        final CTabFolder outputfolder = new CTabFolder(composite, SWT.TOP |SWT.BORDER); // create a tab set
        griddata = new GridData(SWT.FILL,SWT.FILL,false, true, 3, 1);
        griddata.grabExcessHorizontalSpace = true;
        griddata.minimumHeight = 200;
        outputfolder.setLayoutData(griddata);
        // output tab 1
		CTabItem rawOutTab = new CTabItem(outputfolder, SWT.NONE);
        rawOutTab.setText("Raw data");
        // output tab 2
        CTabItem cleanOutTab = new CTabItem(outputfolder,SWT.NONE);
        cleanOutTab.setText("Cleaned Data");
        final Text rawoutput = new Text(outputfolder,SWT.BORDER | SWT.MULTI | SWT.WRAP |SWT.V_SCROLL);
        final Text cleanoutput = new Text(outputfolder,SWT.BORDER | SWT.MULTI | SWT.WRAP |SWT.V_SCROLL);
        griddata = new GridData(SWT.FILL,SWT.FILL,false, true, 3, 1);
        griddata.minimumHeight = 200;
        griddata.horizontalSpan = 3;
        cleanoutput.setLayoutData(griddata);
        rawoutput.setLayoutData(griddata);
        rawOutTab.setControl(rawoutput);
        cleanOutTab.setControl(cleanoutput);
        outputfolder.setSelection(rawOutTab);
        
        
        
        //*************************** functions *************************************//
        // browse selection function
        final Shell shell = new Shell();
        browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String filepath = new FileDialog(shell).open();
				if (filepath != null) {
						// if a filepath is input
			          File file = new File(filepath);
			          if (file.isFile()){
			        	  // check is actually a file and not just directory or stub
			        	  filepathbox.setText(filepath);
			        	  }
			          else
			        	  filepathbox.setText("Not a file");
				}
			}
		});
        // add variable button function
        addvariable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (indexfolder.getEnabled() == false){
					rawoutput.append("Prebuilt files may not have variables added \n");
				}
				else{
				for (int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++){
					// go through the list of programs,tables and programs list (check boxes)
					IPowderIndexer myprog = indexer_list.get(loopIndex); // get the program
					Table mytable = table_list.get(loopIndex); // get the table
					TableItem ischecked = indexingprogs.getItem(loopIndex); // make sure the program is selected
					if (ischecked.getChecked() == true){ // is it checked?
				try{
				for (int loopIndex1 = 0; loopIndex1 < mytable.getItems().length; loopIndex1++) {
						  // go through table get checked values
				  	      TableItem myitem = mytable.getItem(loopIndex1);
				  	      if (myitem.getChecked() == true){
				  	      String value = myitem.getText(3);
				  	      String key = myitem.getText(1);
				  	    
				  	      
				  	      if (value == ""){ // make sure value is not null
				  	    	rawoutput.append("Value for" + key + " Not defined\n");
				  	    	myitem.setBackground(red); // make background red if it is
				  	    	}
				  	      else {
				  	    	
				  	    	myitem.setBackground(green); // make green
				  	    	myprog.addKeyword(key, value); // use the program to set keywords
				  	    	rawoutput.append(key + " " + value+"\n"); // print the values added
				  	    	
				  	      }
					  	  }}}
				
				catch (Exception e) {System.out.print(e.getMessage());}
			}
			}
			}
			}
		});
        
        
        // reset button function
        Reset.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent event) {
        		for (int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++){
					IPowderIndexer myprog = indexer_list.get(loopIndex);
					Table mytable = table_list.get(loopIndex);
					 myprog.resetKeywords();
					//myprog.reset_keywords(); // reset the keywords in the program
					for (int loopIndex1 = 0; loopIndex1 < mytable.getItems().length; loopIndex1++) {
				  	      TableItem myitem = mytable.getItem(loopIndex1); // go through each table and set the item text to ""
				  	      myitem.setText(3,"");
				  	      myitem.setChecked(false); // unckeck the boxes
				  	      myitem.setBackground(grey); // clear the background
				  	      rawoutput.setText("");
				  	      cleanoutput.setText("");    
					}
					}
        	}
        });
        Save.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent event) {
        			if (loadButton.getSelection()){
        			try{
        				
                		for(int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++){
                		IPowderIndexer myprog = indexer_list.get(loopIndex); // get the program
                		TableItem myitem = indexingprogs.getItem(loopIndex); // get checked?
                		if (myitem.getChecked() == true){
                		//output.append(myprog.get_Name()+" Saving \n"); // running...
                		File myfile = new File(filepath); // check if file
                		String mynewfilepath  = myfile.getParent().toString(); // get the parent directory
                		myprog.setTitle(title); // set the filename (file.end , handled in the python script )  
                		myprog.setFilepath(mynewfilepath);
                		myprog.setData(data); // make this more robust
                		myprog.write_input();
                		rawoutput.append("File saved");
                		}}}
                		catch(Exception e){
                			rawoutput.append(e.getMessage());
                			
                		}	
        		}
        	}
        });
        // run button function
        run.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent event) {
        		if (loadButton.getSelection()){
        			
        			try{
        				// *************************New dat file***********************
                		for(int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++){
                		IPowderIndexer myprog = indexer_list.get(loopIndex); // get the program
                		TableItem myitem = indexingprogs.getItem(loopIndex); // get checked?
                		if (myitem.getChecked()){
                		//output.append(myprog.get_Name()+" Running \n"); // running...
                		File myfile = new File(filepath); // check if file
                		String mynewfilepath  = myfile.getParent().toString(); // get the parent directory
                		String mybase = "/scratch/clean_workpsace/powder_toolkit/Powder_toolkit"; // current base directory
                		Path base = Paths.get(mybase); // current module path will make this automatic
                		Path myfilepath = Paths.get(mynewfilepath); 
                		Path relativepath = base.relativize(myfilepath); // relative path of runfile (Ntreor requires this)
                		if(title.contains(".")){
                			title = title.split("\\.")[0];
                			System.out.println(title);
                		}
                		myprog.setTitle(title); // set the filename (file.end , handled in the python script )  
                		myprog.setFilepath(relativepath.toString()+"/");
                		myprog.setData(data);
                		myprog.write_input();
                		
                		
                		List<String> newoutput = myprog.Run(); // the output
                		List<String> cleanout = myprog.read_output();
                		//holder.addCellData(cleanout);
                		
                		
                		for(int i = 0; i < newoutput.size();i++){
                			rawoutput.append(newoutput.get(i)+"\n"); // print output	
                		}
                		int cellnum = 1;
                		try{
                			for(int i = 0; i < cleanout.size();i++, cellnum ++){
                			cleanoutput.append("Cell Number " + String.valueOf(cellnum)+"\n");
                			cleanoutput.append(cleanout.get(i)+"\n");
                			cleanoutput.append("\n");
                			}
                			
                		}catch(Exception e){
                			
                		}
                		
                		
                		
                		}}}
                		catch(Exception e){
                			rawoutput.append(" ");
                			
                		}	
        			
        		}
        		if(peaksButton.getSelection()){
        			if (filepath.length() < 3){
        				rawoutput.append("No input file selected \n");	
        			}
        			else{
        		try{
        		// **************************Existing dat file ********************************
        		boolean runflag = false; 
        		for(int loopIndex = 0; loopIndex < indexer_list.size(); loopIndex++){
        		IPowderIndexer myprog = indexer_list.get(loopIndex); // get the program
        		TableItem myitem = indexingprogs.getItem(loopIndex); // get checked?
        		
        		
        		if (myitem.getChecked()){
        		runflag = true;
        		
        		//output.append(myprog.get_Name()+" Running \n"); // running...
        		File myfile = new File(filepath); // check if file
        		String mynewfilepath  = myfile.getParent().toString(); // get the parent directory
        		String mybase = "/scratch/clean_workpsace/powder_toolkit/Powder_toolkit"; // current base directory
        		//TODO mybase more generic
        		Path base = Paths.get(mybase); // current module path will make this automatic
        		Path myfilepath = Paths.get(mynewfilepath); 
        		Path relativepath = base.relativize(myfilepath); // relative path of runfile (Ntreor requires this)
        		System.out.print(relativepath.toString()); // dev check
        		myprog.setFilepath(relativepath.toString()+"/"); // pass the relative filepath to the prog
        		myprog.setTitle(myfile.getName().split("\\.")[0]); // set the filename (file.end , handled in the python script ) 
        		System.out.println(myfile.getName());
        		List<String> newoutput = myprog.Run(); // the output
        		List<String> cleanout = myprog.read_output();
        		
        		//holder.addCellData(cleanout);
        		
        		for(int i = 0; i < newoutput.size();i++){
        			rawoutput.append(newoutput.get(i)+"\n"); // print output
        		}
        		int cellnum = 1;
        		try{
        			for(int i = 0; i < cleanout.size();i++, cellnum ++){
        			cleanoutput.append("Cell Number " + String.valueOf(cellnum)+"\n");
        			cleanoutput.append(cleanout.get(i)+"\n");
        			cleanoutput.append("\n");
        			}
        		}catch(Exception e){System.out.println(e.getMessage());
        			
        		}
        		}
        		}
        		if (!runflag){
        			rawoutput.append("No program selected \n");
        			
        		}
        		}
        		catch(Exception e){
        			rawoutput.append(e.getMessage());
        			
        		}
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
        
        // peaks button function changes the filepath to the loaded data's filepath
        peaksButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				filepathbox.setEnabled(true);
		        browse.setEnabled(true);
		        textbox.setEnabled(false);
		        
				}
			
		});
        return composite;
        }
	
	// ***************** getters and setters **********************//


	public void setData(String name) {
		try{
		LoadedDataObject mydata = holder.getData(name);
		data = mydata.data;
		title = mydata.name;
		filepath = mydata.filepath;
		textbox.setText(title);
		}catch(Exception e){System.out.println(e.getMessage());}
	}
		
	

}
