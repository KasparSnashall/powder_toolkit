package uk.ac.diamond.powder_toolkit.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * A widget designed to be used for error handling Currently very basic but this
 * needs improving and implementing throughout program
 * 
 * @author sfz19839
 *
 */
public class ErrorWidget   {

	public ErrorWidget(Exception e) {
		MessageBox message = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
		message.setMessage(e.getMessage());
		message.setText("Error");
		message.open();
		
	}

}
