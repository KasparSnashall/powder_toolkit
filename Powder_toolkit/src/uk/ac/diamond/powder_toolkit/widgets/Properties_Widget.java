package uk.ac.diamond.powder_toolkit.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import uk.ac.diamond.powder_toolkit.dataAnalysis.IPowderIndexer;

/**
 * Creates properties widgets for the indexing view, that can be used to input
 * parameters to indexing algorithms
 * 
 * @author sfz19839
 *
 */
public class Properties_Widget {

	/**
	 * Creates a properties widget (complex table)
	 * 
	 * @param indexfolder
	 *            the parent folder
	 * @param myindexer
	 *            a IPOwderIndexer including all the relevant functions
	 * @return returns a table with editors
	 */
	public Table create(CTabFolder indexfolder, final IPowderIndexer myindexer) {

		// get the current display and create colours
		Display display = Display.getCurrent();
		final Color grey = display.getSystemColor(SWT.COLOR_GRAY);
		final Color white = display.getSystemColor(SWT.COLOR_WHITE);
		// set the global variables

		// create the table for the program
		final Table table = new Table(indexfolder, SWT.CHECK | SWT.MULTI
				| SWT.V_SCROLL); // table to be returned
		table.setHeaderVisible(true); // make sure headers are seen
		table.setToolTipText("Options for the data");
		GridData griddata = new GridData();
		griddata.grabExcessHorizontalSpace = true;
		griddata.horizontalSpan = 2;
		table.setLayoutData(griddata);

		// create a list of keys and values
		String[] variables = myindexer.getKeys();
		String[] values = myindexer.getStandard_values();

		// list of column titles
		String[] titles = { "Enable", "Variable", "Value", "New Value" };
		// set column titles
		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[loopIndex]);
			column.setWidth(100);
		}
		// create the table with 4 columns and set the items
		for (int loopIndex = 0; loopIndex < variables.length; loopIndex++) {
			final TableItem item = new TableItem(table, SWT.NULL);
			item.setBackground(grey); // background colour
			item.setText(1, variables[loopIndex]); // the key
			item.setText(2, values[loopIndex]); // the value
			item.setText(3, ""); // blank column for new value

		}
		// create a table editor
		final TableEditor editor = new TableEditor(table);
		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT; // alignment
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

		// table editor function
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Clean up any previous editor control
				Control oldEditor = editor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();

				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;

				// The control that will be the editor must be a child of the
				// Table
				Text newEditor = new Text(table, SWT.BORDER);
				newEditor.setText(item.getText(3)); // get the current text
				newEditor.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent me) { // if a change is
																// detected set
																// the text
						Text text = (Text) editor.getEditor();
						editor.getItem().setText(3, text.getText());
					}
				});
				newEditor.selectAll(); // sets its for all table items
				newEditor.setFocus();
				editor.setEditor(newEditor, item, 3);
			}
		});

		// table checklist function
		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				TableItem item = (TableItem) event.item;
				if (item == null)
					return;
				if (event.detail == SWT.CHECK) {
					// Now what should I do here to get Whether it is a checked
					// event or Unchecked event.
					if (item.getChecked() == true) {

						item.setBackground(white); // set background colour
					} else {
						item.setBackground(grey);

					}
				}
			}

		});
		// optional tooltip setter, hence try and catch
		try {
			table.addListener(SWT.MouseHover, new Listener() {
				@Override
				public void handleEvent(Event event) {
					Point pt = new Point(event.x, event.y);
					TableItem item = table.getItem(pt);

					if (item == null) {
						return;
					}

					for (int i = 0; i < 3; i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							String itemname = item.getText(1);
							String tooltip = myindexer.getTooltip(itemname);
							table.setToolTipText(tooltip);
						}
					}
				}

			});

		} catch (Exception e) {
			// do nothing if not correct
		}

		// pack the table
		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}

		// return the table
		return table;
	}
}
