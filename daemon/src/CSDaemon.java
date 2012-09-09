import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;

public class CSDaemon extends Thread{

	public static final String[] daemons = {"pvfs2", "ipvsadm", "traffic"};
	public static final String[] secComms = {"java Pvfs2Logger", "java LvmLogger", "java TrafficLogger"};
	public static final String[] minComms = {"echo 'none'", "java MinLvmLogger", "java MinStorageLogger"};
	public static final String dbAddr = "10.12.17.201";
	public static final String dbUsername = "root"; 
	public static final String dbPassword = "swmaestro";
	private boolean[] runningDaemons;
	private long repeatTime;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean runningDaemons[] = new boolean[daemons.length];
		long repeatTime = 3000;
		
		for(int i = 0 ; i < runningDaemons.length; i++)
			runningDaemons[i] = false;
		
		if (args.length < 1 ) 
			System.exit(1);
		
		for(int i = 0; i < args.length; i++)
			for(int j = 0; j < daemons.length; j++)
				if(args[i].equals(daemons[j]))
				{
					runningDaemons[j] = true;
					System.out.println(daemons[j]);
				}
		
		new CSDaemon(runningDaemons, 300000).start();
		
		long ago = Calendar.getInstance().getTimeInMillis();
		while(true)
		{
			try 
			{
				Thread.sleep(repeatTime - ( Calendar.getInstance().getTimeInMillis() - ago ));
				ago = Calendar.getInstance().getTimeInMillis();
				for(int i = 0; i < runningDaemons.length; i++)
					if(runningDaemons[i])
					{
						new Logger(secComms[i]).start();
						System.out.println("sec run: " + daemons[i]);
					}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public CSDaemon(boolean[] runningDaemons, long repeatTime)
	{
		this.runningDaemons = runningDaemons;
		this.repeatTime = repeatTime;
	}
	
	public void run()
	{
		long ago = Calendar.getInstance().getTimeInMillis();
		while(true)
		{
			try 
			{
				Thread.sleep(repeatTime - ( Calendar.getInstance().getTimeInMillis() - ago ));
				ago = Calendar.getInstance().getTimeInMillis();
				for(int i = 0; i < runningDaemons.length; i++)
					if(runningDaemons[i])
					{
						new Logger(minComms[i]).start();
						System.out.println(daemons[i]);
					}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	public static Connection getDBConnection(String dbName) throws SQLException
	{
		return DriverManager.getConnection
				("jdbc:mysql://"+CSDaemon.dbAddr+"/"+dbName, dbUsername, dbPassword);
	}
}
