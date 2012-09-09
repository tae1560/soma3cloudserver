import java.io.StringReader;
import java.util.*;

public class Logger extends Thread {
	
	protected String execomm;
	protected String commOut = "";
	
	public Logger(String execomm) throws Exception
	{
		this.execomm = execomm;
	}
	
	public void run()
	{	
		Runtime run = Runtime.getRuntime();
		Process ps;
		try {
			ps = run.exec(execomm);
			
			Scanner in = new Scanner(ps.getInputStream());
			while(in.hasNext())
				commOut += in.nextLine() + "\n";
			in.close();
			
			logging(new StringReader(commOut));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void logging(StringReader res) throws Exception
	{
		//System.out.println(res);
	}
}
