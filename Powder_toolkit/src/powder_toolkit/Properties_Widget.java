package powder_toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.python.core.PyObject;
import org.python.core.PyString;



public class Properties_Widget {
	
	public static PyObject MyClass; // the python class
	public static String myname = ""; // the title
	public static String myfilepath = ""; // the filepath to be used
	
	public Table create(CTabFolder indexfolder, String filepath, String classname, String options){
		
		// get the current display and create colours
		Display display = Display.getCurrent();
		final Color grey = display.getSystemColor(SWT.COLOR_GRAY);
		final Color white = display.getSystemColor(SWT.COLOR_WHITE); 
		// set the global variables
		myname = classname;
		myfilepath = filepath;
		
		// create the table for the program
		final Table table = new Table(indexfolder, SWT.CHECK | SWT.MULTI | SWT.V_SCROLL); // table to be returned
		table.setHeaderVisible(true); // make sure headers are seen
		table.setToolTipText("Options for the data");
		GridData griddata = new GridData();
		griddata.grabExcessHorizontalSpace = true;
		griddata.horizontalSpan = 2;
		table.setLayoutData(griddata);
		
		// create interpreter
		interpreter ie = new interpreter(); // call my interpreter
		ie.execfile(myfilepath); // my file
		MyClass = ie.createClass(myname,options);// invoke my class as a pyobject
		
		
		PyObject mydict = MyClass.invoke("get_standard_dict"); // calls the run function
        String mydicti = mydict.toString();// make into string
        String replaced = mydicti.replace("{", ""); // replace the curly brackets
        String replaced2 = replaced.replace("}", ""); // replace the second curly bracket
        String replaced3 = replaced2.replaceAll("'", ""); //replace the "'"
        String[] pairs = replaced3.split(","); // split to list of key value pairs
        
        
       // create a list of keys and values
        ArrayList<String>variables = new ArrayList<String>();
        ArrayList<String>values = new ArrayList<String>();
        
        for (int i=0;i<pairs.length;i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            variables.add(keyValue[0]); // append lists with values and keys
            values.add(keyValue[1]);
        }
        
        // list of column titles
        String[] titles = {"Enable","Variable","Value","New Value"};
        // set column titles
    	for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
    	      TableColumn column = new TableColumn(table, SWT.NULL);
    	      column.setText(titles[loopIndex]);
    	      column.setWidth(100);
    	    }
    	// create the table with 4 columns and set the items
    	for (int loopIndex = 0; loopIndex < variables.size(); loopIndex++) {
    	      final TableItem item = new TableItem(table, SWT.NULL);
    	      item.setBackground(grey); // background colour
    	      item.setText(1,variables.get(loopIndex)); // the key
    	      item.setText(2,values.get(loopIndex)); // the value
    	      item.setText(3, ""); // blank column for new value
    	      
    	}
    	// create a table editor
    	final TableEditor editor = new TableEditor(table);
    	//The editor must have the same size as the cell and must
    	//not be any smaller than 50 pixels.
    	editor.horizontalAlignment = SWT.LEFT; // alignment
    	editor.grabHorizontal = true;
    	editor.minimumWidth = 50;
    	
    	// table editor function
    	table.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			// Clean up any previous editor control
    			Control oldEditor = editor.getEditor();
    			if (oldEditor != null) oldEditor.dispose();
    	
    			// Identify the selected row
    			TableItem item = (TableItem)e.item;
    			if (item == null) return;
    	
    			// The control that will be the editor must be a child of the Table
    			Text newEditor = new Text(table, SWT.BORDER);
    			newEditor.setText(item.getText(3)); // get the current text
    			newEditor.addModifyListener(new ModifyListener() {
    				@Override
    			public void modifyText(ModifyEvent me) { // if a change is detected set the text
    					Text text = (Text)editor.getEditor(); 
    					editor.getItem().setText(3, text.getText());
    				}
    			});
    			newEditor.selectAll(); // sets its for all table items
    			newEditor.setFocus();
    			editor.setEditor(newEditor, item, 3);
    		}
    	});
    	
    	// table checklist function
    	table.addSelectionListener(new SelectionAdapter(){
    		
    		public void widgetSelected(SelectionEvent event){
    			TableItem item = (TableItem)event.item;
    			if (item == null) return;
    			if(event.detail == SWT.CHECK) {
                    //Now what should I do here to get Whether it is a checked event or Unchecked event.
    				if (item.getChecked() == true){
  			  	      
  				  	  item.setBackground(white); // set background colour
  			  	      }
  			  	      else{
  			  	    	  item.setBackground(grey);
  			  	    	  
  			  	      }
                }
    		}
    		
    	});

    	
    	// pack the table
    	for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
    	      table.getColumn(loopIndex).pack();
    	    }
    	
    // return the table
	return table;  
	}
	public void write_dat(PyObject data){
		MyClass.invoke("set_data", data);
		MyClass.invoke("write_input");
	}
	public void reset_keywords(){
		// use the class to reset keywords
		MyClass.invoke("reset_keywords");
		System.out.println("keywords reset");
	}
	public void set_keywords(String key, String value){	
		// set keywords function, must be pystrings
		PyObject mykey = new PyString(key);
		PyObject myvalue = new PyString(value);
		PyObject[] mylist = new PyObject[]{mykey,myvalue};
		MyClass.invoke("set_keywords",mylist); // run the set function		
	}
	public String get_Name(){
		// get the title
		return myname;
	}
	public void set_filepath(String filepath){
		// set filepath function
		PyString options = new PyString(filepath);
		MyClass.invoke("set_filepath",options);
	}
	public void set_title(String name){
		// set title function
		PyString options = new PyString(name);
		MyClass.invoke("set_title",options);
	}
	
	public String run(){
		// run function
		PyObject output = MyClass.invoke("run");
		return output.toString();
	}

		
}
