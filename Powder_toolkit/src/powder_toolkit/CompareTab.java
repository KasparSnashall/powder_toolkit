package powder_toolkit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class CompareTab {
	public Composite create(CTabFolder folder,Shell shell,Display display){// composite allows me to use more then one item in my tab folder
        Composite composite = new Composite(folder, SWT.NONE);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false); // must figure these variables out
        gridData.horizontalSpan = 2;
        composite.setLayoutData(gridData);
        composite.setLayout(new GridLayout(2, false)); // two columns
        // addition of new items
        new Label(composite, SWT.NONE).setText("Compare View");

        return composite;}

}
