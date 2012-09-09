import java.io.File;
import java.io.StringReader;
import java.sql.*;
import java.util.Scanner;
import java.util.Vector;

public class LvmLogger extends Logger {

	public static void main(String[] args) 
	{
		try {
			Scanner in = new Scanner(new File("/etc/ha.d/resource.d/node"));
			if(in.hasNext() && !in.next().equals("master"))
			{
				in.close();
				return;
			}
			in.close();
			new LvmLogger().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LvmLogger() throws Exception {
		super("/sbin/ipvsadm");
	}

	@Override
	protected void logging(StringReader res)  throws Exception{

		Vector<Integer> activeConn = new Vector<Integer>(), inactConn = new Vector<Integer>();
		Vector<String> hostName = new Vector<String>();
		
		Scanner in = new Scanner(res);
		while(in.hasNext() && !in.next().equals("rr"));
				
		while(in.hasNext())
		{
			in.next();
			hostName.add(in.next().replaceAll("\\:http", ""));
			in.next();in.next();
			activeConn.add(in.nextInt());
			inactConn.add(in.nextInt());
		}
		in.close();
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = CSDaemon.getDBConnection("csstate");
		Statement stm = conn.createStatement();
		
		for(int i = 0; i < hostName.size(); i++)
		{
			stm.executeUpdate("update lvm_state set active_conn='" + activeConn.elementAt(i)
					+"' where hostname='" + hostName.elementAt(i) + "'");
			stm.executeUpdate("update lvm_state set inact_conn='" + inactConn.elementAt(i)
					+"' where hostname='" + hostName.elementAt(i) + "'");
		}
		
		stm.close();
		conn.close();
	}

}
