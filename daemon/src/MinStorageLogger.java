import java.io.File;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.*;
import java.util.Enumeration;
import java.util.Scanner;

public class MinStorageLogger extends Logger {
	
	public static void main(String[] args) 
	{
		try {
			new MinStorageLogger().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MinStorageLogger() throws Exception {
		super("echo 'Storage state storing...'");
	}

	@Override
	protected void logging(StringReader res) throws Exception
	{
		String hostName = null;
		Enumeration<InetAddress> inetAddresses 
				= NetworkInterface.getByName("bond0").getInetAddresses();
		while(inetAddresses.hasMoreElements()) 
	    {
	    	InetAddress ia = inetAddresses.nextElement();
	    	if (ia.getHostAddress() != null && ia.getHostAddress().indexOf(".") != -1) {
	    		hostName = ia.getHostAddress();
	    	}
	    }	
		
		BigInteger[] t1, t2;
		t1 = getNetUsage("bond0");
		Thread.sleep(300000);
		t2 = getNetUsage("bond0");	
		
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection statisticsConn = CSDaemon.getDBConnection("csstatistics");
		Connection stateConn = CSDaemon.getDBConnection("csstate");
		Statement statisticsStm = statisticsConn.createStatement();
		Statement stateStm = stateConn.createStatement();

		ResultSet rs = stateStm.executeQuery("select * from storage_state where hostname='"+ hostName +"'");
		
		while(rs.next())
		{
			statisticsStm.executeUpdate("insert into storage_states values(default" 
				+ ", default," + "'" + rs.getString("hostname") + "','" 
				+ (t2[0].subtract(t1[0])) + "','" + (t2[1].subtract(t1[1])) + "','"
				+ rs.getLong("hdd_use") + "','" + rs.getLong("user_space_use") + "','" + rs.getLong("hdd_total") +"')");
		}
		
		
		statisticsStm.close();
		stateStm.close();
		statisticsConn.close();
		stateConn.close();
	}
	
	private BigInteger[] getNetUsage(String device) throws Exception
	{
		BigInteger[] t = new BigInteger[2];
		Scanner in = new Scanner(new File("/proc/net/dev"));
		
		String targetLine;
		while(in.hasNext())
		{
			targetLine = in.nextLine();
			if ( targetLine.indexOf(device) >= 0)
			{
				String[] token = targetLine.split(":");
				Scanner tokenIn = new Scanner(token[1]);
				
				t[0] = tokenIn.nextBigInteger();
				
				for(int i = 0 ; i < 7; i++)
					tokenIn.next();
				
				t[1] = tokenIn.nextBigInteger();
				
				tokenIn.close();
				break;
			}
		}
		
		in.close();
		return t;
	}
}
