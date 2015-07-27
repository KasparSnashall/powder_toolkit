package Views;

import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import DataAnalysis.Comparator;
import DataAnalysis.LoadedDataObject;
import DataAnalysis.MyDataHolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;

public class Compareview extends ViewPart {

	public static final String ID = "Views.Compareview"; //$NON-NLS-1$
	private static GridData griddata;
	private static GridData compgriddata;
	private static MyDataHolder holder = LoadedDataview.holder;
	private static Text Data1; // data text box 1
	private static Text Data2; // data text box 2
	private static Combo combo1; // drop down 1
	private static Combo combo2; // drop down 2
	private static Combo types; // drop down with comparison types
	private static Text output; // output text box
	private static Text tolerance;
	
	public Compareview() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
        GridLayout gl_composite = new GridLayout(1, false);
        gl_composite.marginBottom = 20;
        gl_composite.marginRight = 20;
        gl_composite.marginLeft = 20;
        gl_composite.marginHeight = 20;
        gl_composite.marginTop = 20;
        gl_composite.marginWidth = 20;
        composite.setLayout(gl_composite);
        
        Group grpCompare = new Group(composite, SWT.NONE);
        GridLayout gl_grpCompare = new GridLayout(3, false);
        gl_grpCompare.marginWidth = 15;
        gl_grpCompare.marginHeight = 25;
        grpCompare.setLayout(gl_grpCompare);
        GridData gd_grpCompare = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_grpCompare.widthHint = 408;
        gd_grpCompare.heightHint = 519;
        grpCompare.setLayoutData(gd_grpCompare);
        grpCompare.setText("Compare");
        
        
        Label data1label = new Label(grpCompare, SWT.NONE);
        data1label.setText("Data 1");
        
        Data1 = new Text(grpCompare, SWT.BORDER);
        
        // add in listeners
       
        combo1 = new Combo(grpCompare,SWT.NONE);
        GridData gd_combo1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_combo1.widthHint = 222;
        combo1.setLayoutData(gd_combo1);
        getdatalistener(Data1,combo1);
        Label data2label = new Label(grpCompare, SWT.NONE);
        data2label.setText("Data 2");
        
        Data2 = new Text(grpCompare, SWT.BORDER);
       
        combo2 = new Combo(grpCompare,SWT.NONE);
        GridData gd_combo2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_combo2.widthHint = 216;
        combo2.setLayoutData(gd_combo2);
        getdatalistener(Data2,combo2);
        Label lblTolerance = new Label(grpCompare, SWT.NONE);
        lblTolerance.setText("Tolerance");
        tolerance = new Text(grpCompare,SWT.BORDER);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        
        Label lblType = new Label(grpCompare, SWT.NONE);
        lblType.setText("Type");
        
        types = new Combo(grpCompare,SWT.NONE);
        GridData gd_types = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_types.widthHint = 223;
        types.setLayoutData(gd_types);
        types.setText("Both");
        types.add("Both");
        types.add("Fractional");
        types.add("Binary");
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        
        Button compare = new Button(grpCompare, SWT.PUSH);
        compare.setText("Compare");
        comparefunction(compare);
        new Label(grpCompare, SWT.NONE);
        new Label(grpCompare, SWT.NONE);
        
        output = new Text(grpCompare, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        GridData gd_output = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_output.widthHint = 218;
        gd_output.heightHint = 93;
        output.setLayoutData(gd_output);
        createActions();
		initializeToolBar();
		initializeMenu();
        
        }

	
	public static void getdatalistener(final Text text,final Combo combo){
	ModifyListener modifyListener = new ModifyListener(){
		      public void modifyText(ModifyEvent event) {
		        // Get the widget whose text was modified
		    	  combo.removeAll();
		        try{
		        	System.out.println(text.getText());
		        	LoadedDataObject loaded = holder.getData(text.getText());
		        	System.out.println(loaded);
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
				System.out.println(name1);
				IDataset data1 = holder.getDataSet(name1, column1);
				IDataset data2 = holder.getDataSet(name2, column2);
				String choice = types.getText();
				
				System.out.println(data1);
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
