package powder_toolkit;
import java.io.IOException;

import javax.script.ScriptException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import DataAnalysis.MyDataHolder;



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
        CTabFolder folder = new CTabFolder(shell, SWT.TOP |SWT.BORDER); // create a tab set
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true,1, 2);
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
        
       
        new Label(shell,SWT.NONE).setText("Loaded Data");;
        final List LoadedData = new List(shell, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP |SWT.RIGHT| SWT.BORDER);
        GridData data2 = new GridData(SWT.FILL, SWT.FILL, true, false,1, 1);
        data2.minimumWidth = 200;
        LoadedData.setLayoutData(data2);
        
      
        
       
        
        
        MyDataHolder holder = new MyDataHolder();
        // in all cases I am making a composite controller to each tab
        // add in the load tab (this will allow reading of data)
        final LoadTab loadtab = new LoadTab(holder);
        cTabItem1.setControl(loadtab.create(folder,shell,display,LoadedData));
        //add in the find peaks tab
        FindPeaks findtab =  new FindPeaks();
        cTabItem2.setControl(findtab.create(folder,shell,display));
        //add in index tab
        final IndexTab indextab = new IndexTab(holder);
        cTabItem3.setControl(indextab.create(folder,shell,display));
        
        CompareTab comparetab = new CompareTab();
        cTabItem4.setControl(comparetab.create(folder,shell,display));
        
        SearchTab searchtab = new SearchTab();
        cTabItem5.setControl(searchtab.create(folder,shell,display));
        
        // create Loaded Data listener
        LoadedData.addMouseListener(new MouseListener()
    	{
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// append data
				try{
				String name = LoadedData.getSelection()[0];
				System.out.println(name);
				loadtab.setData(name);
				indextab.setData(name);
				
				}catch(Exception e){ System.out.println(e.getMessage());}
				//TODO add in the rest of set Data functions
			}
			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
        
        
        
        // pack and load shell
        folder.pack();
        shell.pack();
        shell.setSize(700, 800);
        shell.open();
        while (!shell.isDisposed()) {
          if (!display.readAndDispatch()) {
            display.sleep();
          }
        }
        display.dispose();}
    }
 

