package powder_toolkit;
import java.io.IOException;
import javax.script.ScriptException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;



public class CSD {
	
    public static void main(String[] args) throws ScriptException, IOException{
    	Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("My Powder Diffraction toolkit");

        // create a new GridLayout with two columns
        // of different size
        GridLayout layout = new GridLayout(2, false);

        // set the layout to the shell
        shell.setLayout(layout);
        
        
        // creating initial tabs for operation selection
        CTabFolder folder = new CTabFolder(shell, SWT.TOP); // create a tab set
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true,2, 1);
        folder.setLayoutData(data);
        
        // add tabs to the layout
        // make these more descriptive
        CTabItem cTabItem1 = new CTabItem(folder, SWT.NONE);
        cTabItem1.setText("Load");
        CTabItem cTabItem2 = new CTabItem(folder, SWT.NONE);
        cTabItem2.setText("Find peaks");
        CTabItem cTabItem3 = new CTabItem(folder, SWT.NONE);
        cTabItem3.setText("Index");
        CTabItem cTabItem4 = new CTabItem(folder, SWT.NONE);
        cTabItem4.setText("Compare");
        CTabItem cTabItem5 = new CTabItem(folder, SWT.NONE);
        cTabItem5.setText("Search");
        
       
       
        // in all cases I am making a composite controller to each tab
        // add in the load tab (this will allow reading of data)
        LoadTab myloadtab = new LoadTab();
        cTabItem1.setControl(myloadtab.create(folder,shell,display));
        //add in the find peaks tab
        FindPeaks findtab =  new FindPeaks();
        cTabItem2.setControl(findtab.create(folder,shell,display));
        //add in index tab
        IndexTab indextab = new IndexTab();
        cTabItem3.setControl(indextab.create(folder,shell,display));
        
        CompareTab comparetab = new CompareTab();
        cTabItem4.setControl(comparetab.create(folder,shell,display));
        
        SearchTab searchtab = new SearchTab();
        cTabItem5.setControl(searchtab.create(folder,shell,display));
        
        // create the data holder
       
        
        
        // pack and load shell
        folder.pack();
        shell.pack();
        shell.setSize(600, 700);
        shell.open();
        while (!shell.isDisposed()) {
          if (!display.readAndDispatch()) {
            display.sleep();
          }
        }
        display.dispose();}
    }
 

