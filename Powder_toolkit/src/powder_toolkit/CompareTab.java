package powder_toolkit;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import DataAnalysis.Comparator;
import DataAnalysis.LoadedDataObject;
import DataAnalysis.MyDataHolder;


public class CompareTab {
	
	private GridData griddata;
	private static MyDataHolder holder;
	private static Text Data1; // data text box 1
	private static Text Data2; // data text box 2
	private static Combo combo1; // drop down 1
	private static Combo combo2; // drop down 2
	private static Combo types; // drop down with comparison types
	private static Text output; // output text box
	
	CompareTab(MyDataHolder holder){
		CompareTab.holder = holder;
		
	}
	
	public Composite create(CTabFolder folder,Shell shell,Display display){// composite allows me to use more then one item in my tab folder
        Composite composite = new Composite(folder, SWT.NONE);
        griddata = new GridData(SWT.FILL, SWT.FILL, true, false); // must figure these variables out
        griddata.horizontalSpan = 2;
        composite.setLayoutData(griddata);
        composite.setLayout(new GridLayout(2, false)); // two columns
        // addition of new items
        Label header = new Label(composite, SWT.NONE);
        header.setText("Compare View");
        header.setLayoutData(griddata);
        
        
        Label data1label = new Label(composite, SWT.NONE);
        data1label.setText("Data 1");
        data1label.setLayoutData(griddata);
        
        Data1 = new Text(composite, SWT.BORDER);
        griddata = new GridData();
        griddata.horizontalSpan = 1;
        Data1.setLayoutData(griddata);
        combo1 = new Combo(composite,SWT.NONE);
        combo1.setLayoutData(griddata);
        
        Label data2label = new Label(composite, SWT.NONE);
        data2label.setText("Data 2");
        griddata = new GridData();
        griddata.horizontalSpan = 2;
        data2label.setLayoutData(griddata);
        
        Data2 = new Text(composite, SWT.BORDER);
        griddata = new GridData();
        griddata.horizontalSpan = 1;
        Data2.setLayoutData(griddata);
        
        combo2 = new Combo(composite,SWT.NONE);
        combo2.setLayoutData(griddata);
        
        // add in listeners
        getdatalistener(Data1,combo1);
        getdatalistener(Data2,combo2);
        
        types = new Combo(composite,SWT.NONE);
        griddata = new GridData();
        griddata.horizontalSpan = 1;
        types.setLayoutData(griddata);
        types.add("Both");
        types.add("Fractional");
        types.add("Binary");
        
        Button compare = new Button(composite, SWT.PUSH);
        compare.setText("Compare");
        griddata = new GridData();
        griddata.horizontalSpan = 1;
        compare.setLayoutData(griddata);
        comparefunction(compare);
        
        output = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        griddata = new GridData(SWT.FILL, SWT.FILL, true, true);
        griddata.horizontalSpan = 2;
        griddata.minimumHeight = 200;
        output.setLayoutData(griddata);
        
        return composite;}

	
	public static void getdatalistener(final Text text,final Combo combo){
	ModifyListener modifyListener = new ModifyListener(){
		      public void modifyText(ModifyEvent event) {
		        // Get the widget whose text was modified
		    	  combo.removeAll();
		        try{
		        	LoadedDataObject loaded = holder.getData(text.getText());
		        	List<IDataset> datalist = loaded.data;
		        	for(int i = 0; i < datalist.size(); i++){
		        		System.out.println(datalist.get(i));
		        		combo.add(datalist.get(i).getName());
		        		System.out.println(combo.getItem(i));
		        	}
		        }catch(Exception e){
		        	System.out.println(e.getMessage());
		        	
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
}
