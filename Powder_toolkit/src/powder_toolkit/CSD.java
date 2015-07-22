package powder_toolkit;
import java.io.IOException;

import javax.script.ScriptException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import DataAnalysis.MyDataHolder;



public class CSD {
	
	private static LoadTab loadtab;
	private static IndexTab indextab;
	
	
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
        final CTabFolder folder = new CTabFolder(shell, SWT.TOP |SWT.BORDER); // create a tab set
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
        loadtab = new LoadTab(holder);
        cTabItem1.setControl(loadtab.create(folder,shell,display,LoadedData));
        //add in the find peaks tab
        FindPeaks findtab =  new FindPeaks();
        cTabItem2.setControl(findtab.create(folder,shell,display));
        //add in index tab
        indextab = new IndexTab(holder);
        cTabItem3.setControl(indextab.create(folder,shell,display));
        
        final CompareTab comparetab = new CompareTab(holder);
        cTabItem4.setControl(comparetab.create(folder,shell,display));
        
        SearchTab searchtab = new SearchTab();
        cTabItem5.setControl(searchtab.create(folder,shell,display));
        
        // functions
        clicker(LoadedData);
        setDragDrop(LoadedData);
        compareselect(folder,LoadedData);

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
    
    
    public static void clicker(final List LoadedData){
    	
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
    	
    }
    
    
    public static void setDragDrop(final List list) {
    	// Allows dragging and dropping of List items
    	// doesnt only works for compare tab
        Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
        int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
        final DragSource source = new DragSource(list, operations);
        source.setTransfer(types);
        source.addDragListener(new DragSourceListener() {
        	
          public void dragStart(DragSourceEvent event) {
            event.doit = (list.getSelection()[0].length() != 0);
          }

          public void dragSetData(DragSourceEvent event) {
            event.data = list.getSelection()[0];
          }

          public void dragFinished(DragSourceEvent event) {
           // do nothing
           }
        });

        DropTarget target = new DropTarget(list, operations);
        target.setTransfer(types);
        target.addDropListener(new DropTargetAdapter() {
          public void drop(DropTargetEvent event) {
            if (event.data == null) {
              event.detail = DND.DROP_NONE;
              return;
            }
            
          }
        });
      }
    
    public static void compareselect(final CTabFolder folder,final List LoadedData){
    	folder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(folder.getSelectionIndex() == 3){
					LoadedData.setDragDetect(true);
				}
				else{
					
					LoadedData.setDragDetect(false);
				}
			}
		});
    }
    }

    
 

