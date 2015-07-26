package Jython_programs;
import org.python.core.PyInstance;
import org.python.util.PythonInterpreter;
/**
 * interpreter class is designed to create a jython interpreter
 * This class can then create jyhton classes via createClass function
 * class methods may then be invoked via the clas.invoke method
 * @author kaspar
 *
 */
public class interpreter
{
   PythonInterpreter interpreter = null;
 
   public interpreter()
   {
      PythonInterpreter.initialize(System.getProperties(),
                                   System.getProperties(), new String[0]);
      this.interpreter = new PythonInterpreter();
   }
   /**
    * excefile executes the python file
    * @param fileName the filepath to the python file
    */
   public void execfile( final String fileName )
   {
      this.interpreter.execfile(fileName);
   }
   /**
    * Create class creates a jython class that may then be used in java
    * @param className the python class name
    * @param opts the options used in the class creation (eg data)
    * @return returns a jython class
    */
   PyInstance createClass( final String className, final String opts )
   {
      return (PyInstance) this.interpreter.eval(className + "(" + opts + ")");
   }
 
}
