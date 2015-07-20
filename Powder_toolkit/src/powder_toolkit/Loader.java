package powder_toolkit;
import java.io.File;
import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.dawnsci.analysis.api.io.ILoaderService;
import org.eclipse.dawnsci.analysis.api.monitor.IMonitor;

public class Loader {
	
	private static ILoaderService service;
	public static String filepath;
	
	public void Load_data(String filepath) {
		if(filepath.contains(".hkl")){
			Loadhkl();
		}
	}
		
		
	
	public static void Loadhkl() {
		
		final File loc  = new File("/scratch/clean_workpsace/powder_toolkit/Powder_toolkit/python_code/testdata/test1.hkl");
		System.out.println(loc.exists());
		System.out.println(loc.getAbsolutePath());
		try{
		final IDataHolder dh = service.getData(loc.getAbsolutePath(), new IMonitor.Stub());
		System.out.println(dh.getNames());
		}catch(Exception e){System.out.print(e.getMessage());}
		/*
		try{
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String line; // line variable
			while ((line = br.readLine()) != null) {
			   // process the line.
			   if (line.contains("TOTAL NUMBER OF LINES ="))
			   {
			       try {
			    	for(int i = 0; i<3;i++){
					line = br.readLine(); // grab the 3 lines after
					
					}
			    	
				} catch (IOException e) {
					e.printStackTrace();
				}
			   }
			}
			br.close();
		}
		catch(Exception e){}
	}*/
		
	}
	
}
