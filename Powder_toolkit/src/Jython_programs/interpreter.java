package Jython_programs;
import org.python.core.PyInstance;
import org.python.util.PythonInterpreter;
public class interpreter
{
   PythonInterpreter interpreter = null;
 
 
   public interpreter()
   {
      PythonInterpreter.initialize(System.getProperties(),
                                   System.getProperties(), new String[0]);
      this.interpreter = new PythonInterpreter();
   }
 
   public void execfile( final String fileName )
   {
      this.interpreter.execfile(fileName);
   }
 
   PyInstance createClass( final String className, final String opts )
   {
      return (PyInstance) this.interpreter.eval(className + "(" + opts + ")");
   }
 
}
