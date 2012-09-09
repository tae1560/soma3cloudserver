import java.io.File;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Scanner;


public class TrafficLogger extends Logger {
	
	public static void main(String[] args) 
	{
		try {
			new TrafficLogger().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TrafficLogger() throws Exception {
		super("echo $HOSTNAME");
	}
	
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
		Thread.sleep(3000);
		t2 = getNetUsage("bond0");
		
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection conn = CSDaemon.getDBConnection("csstate");
		Statement stm = conn.createStatement();
		
		stm.executeUpdate("update storage_state set rx_use='" + (t2[0].subtract(t1[0]))
				+"' where hostname='" + hostName + "'");
		stm.executeUpdate("update storage_state set tx_use='" + (t2[1].subtract(t1[1]))
				+"' where hostname='" + hostName + "'");
		
		stm.close();
		conn.close();
	}
	
	
	private BigInteger[] getNetUsage(String device) throws Exception
	{
		BigInteger[] t = new BigInteger[2];
		Scanner in = new Scanner(new File("/proc/net/dev"));
		
		String tokenLine;
		while(in.hasNext())
		{
			tokenLine = in.nextLine();
			if ( tokenLine.indexOf(device) >= 0)
			{
				String[] token = tokenLine.split(":");
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
