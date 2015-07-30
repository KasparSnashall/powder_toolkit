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
		interpret.execfile("/scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/CSD_cell_search.py");
		try{
		MyClass = interpret.createClass("CSD_cell_search", "");	
		}catch(Exception e){System.out.println(e.getMessage());}
	}
	
	
	public static String search(){
		try{
		String result = MyClass.invoke("search").toString();
		return result;
		}catch(Exception e){
			System.out.println(e.getMessage());
			
		}
		return null;
	}
	
	

	public void setCell_lengths(List<Double> cell_lengths) {
		try{
		if(cell_lengths.size() != 3){
			throw new Exception("lengths not correct");
		}
		CSD_cellsearch.cell_lengths = cell_lengths;
		System.out.println(MyClass.invoke("Printme"));
		PyList mylist = new PyList();
		for(Double item : cell_lengths){
			mylist.add(item);
		}
		
		
		MyClass.invoke("set_angles",mylist);
		}
		catch(Exception e){System.out.println(e.getMessage());
		}
	}


	public void setCell_angles(List<Double> cell_angles) {
		try{
		if(cell_angles.size() != 3){
			throw new Exception("angles not correct");
		}
		CSD_cellsearch.cell_angles = cell_angles;
		PyList mylist = new PyList();
		for(Double item : cell_angles){
			mylist.add(item);
		}
		MyClass.invoke("set_angles",mylist);
		}catch(Exception e){System.out.println(e.getMessage());}
	}
	
	

}
