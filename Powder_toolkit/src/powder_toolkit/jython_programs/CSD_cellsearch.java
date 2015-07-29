package powder_toolkit.jython_programs;

import java.util.ArrayList;
import java.util.List;

import org.python.core.PyInstance;
import org.python.core.PyList;
/**
 * Jython wrapper for a cell search python program
 * @author sfz19839
 *
 */
public class CSD_cellsearch {
	
	private static Interpreter interpret = new Interpreter();
	public static List<Double> cell_lengths = new ArrayList<Double>();
	public static List<Double> cell_angles = new ArrayList<Double>();
	

	public static PyInstance MyClass;
	
	public CSD_cellsearch(){
		interpret.execfile("/home/sfz19839/Desktop/CSD_cell_search.py");
		MyClass = interpret.createClass("CSD_cell_search", "");	
	}
	
	
	public static void search(){
		try{
		
		
		
		MyClass.invoke("search");
		}catch(Exception e){
			System.out.println(e.getMessage());
			
		}
	}
	
	

	public static void setCell_lengths(List<Double> cell_lengths) {
		try{
		if(cell_lengths.size() != 3){
			throw new Exception("lengths not correct");
		}
		CSD_cellsearch.cell_lengths = cell_lengths;
		PyList mylist = (PyList) cell_lengths;
		MyClass.invoke("set_angles",mylist);}
		catch(Exception e){System.out.println(e.getMessage());
		}
	}


	public static void setCell_angles(List<Double> cell_angles) {
		try{
		if(cell_angles.size() != 3){
			throw new Exception("angles not correct");
		}
		CSD_cellsearch.cell_angles = cell_angles;
		PyList mylist = (PyList) cell_angles;
		MyClass.invoke("set_angles",mylist);
		}catch(Exception e){System.out.println(e.getMessage());}
	}
	
	

}
