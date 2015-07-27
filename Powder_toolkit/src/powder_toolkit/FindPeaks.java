package powder_toolkit;
import org.eclipse.dawnsci.analysis.api.io.ILoaderService;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;




public class FindPeaks {
	

	
	public Composite create(CTabFolder folder,Composite parent,Display display){// composite allows me to use more then one item in my tab folder
        Composite composite = new Composite(folder, SWT.NONE);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false); // must figure these variables out
        gridData.horizontalSpan = 2;
        composite.setLayoutData(gridData);
        composite.setLayout(new GridLayout(2, false)); // two columns
        // addition of new items
        new Label(composite, SWT.NONE).setText("To be added");
        // make buttons in a row
        //IPlottingService pservice = new IPlottingService();
        
        
        
		
        // Later when the part is created, create a plotting system on it.
        //system.createPlotPart(...);
        // Later when the part is disposed
       // system.dispose();

        
		
        return composite;
		}
}
