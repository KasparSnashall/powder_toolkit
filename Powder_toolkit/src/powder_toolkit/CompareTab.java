package powder_toolkit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class CompareTab {
	private GridData griddata;
	
	
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
        
        Text Data1 = new Text(composite,SWT.MULTI | SWT.BORDER);
        griddata = new GridData();
        griddata.horizontalSpan = 1;
        Data1.setLayoutData(griddata);
        Combo combo1 = new Combo(composite,SWT.NONE);
        combo1.setLayoutData(griddata);
        
        Label data2label = new Label(composite, SWT.NONE);
        data2label.setText("Data 2");
        griddata = new GridData();
        griddata.horizontalSpan = 2;
        data2label.setLayoutData(griddata);
        
        Text Data2 = new Text(composite,SWT.MULTI | SWT.BORDER);
        griddata = new GridData();
        griddata.horizontalSpan = 1;
        Data2.setLayoutData(griddata);
        Combo combo2 = new Combo(composite,SWT.NONE);
        combo2.setLayoutData(griddata);
        
        
        
        return composite;}

}
