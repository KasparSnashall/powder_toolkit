package uk.ac.diamond.powder_toolkit.jython_programs;

import org.python.core.PyInstance;
import org.python.pydev.jython.PythonInterpreterWrapper;

/**
 * interpreter class is designed to create a jython interpreter This class can
 * then create jyhton classes via createClass function class methods may then be
 * invoked via the clas.invoke method This class is currently unused
 * 
 * @author sfz19839
 *
 */
public class Interpreter {

	private PythonInterpreterWrapper interpreter = null;

	// PythonInterpreter interpreter = null;

	public Interpreter() {

		// interpreter.initialize(System.getProperties(),System.getProperties(),
		// new String[0]);
		this.interpreter = new PythonInterpreterWrapper();
	}

	/**
	 * excefile executes the python file
	 * 
	 * @param fileName
	 *            the filepath to the python file
	 */
	public void execfile(final String fileName) {
		this.interpreter.execfile(fileName);
	}

	/**
	 * Create class creates a jython class that may then be used in java
	 * 
	 * @param className
	 *            the python class name
	 * @param opts
	 *            the options used in the class creation (eg data)
	 * @return returns a jython class
	 */
	PyInstance createClass(final String className, final String opts) {
		return (PyInstance) this.interpreter.eval(className + "(" + opts + ")");
	}

}
