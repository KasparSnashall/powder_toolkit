package uk.ac.diamond.powder_toolkit.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
/**
 * A widget designed to be used for error handling
 * Currently very basic but will improve this
 * @author sfz19839
 *
 */
public class ErrorWidget  {
	
	public ErrorWidget(Exception e){
		Shell myshell = new Shell();
		myshell.setText("Error");
		Composite composite = new Composite(myshell,SWT.FILL);
		GridLayout layout = new GridLayout(1,false);
	    layout.marginWidth = 25;
	    layout.marginHeight = 25;
	    composite.setLayout(layout);
		
		Label myerror = new Label(composite,SWT.NONE);
		myerror.setText(e.getMessage());
		GridData grid = new GridData(SWT.FILL,SWT.FILL,true,true,1,1);
		myerror.setLayoutData(grid);
		composite.pack();
		myshell.setSize(200, 100);
		Monitor primary = Display.getCurrent().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = myshell.getBounds();
	    
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	    myshell.setLocation(x, y);
		myshell.open();
		
	}

}
