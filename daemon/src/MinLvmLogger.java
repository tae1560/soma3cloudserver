import java.io.File;
import java.io.StringReader;
import java.sql.*;
import java.util.Scanner;

public class MinLvmLogger extends Logger {

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
			new MinLvmLogger().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MinLvmLogger() throws Exception {
		super("echo 'Lvm state storing...'");
	}

	@Override
	protected void logging(StringReader res)  throws Exception{

		Class.forName("com.mysql.jdbc.Driver");
		
		Connection statisticsConn = CSDaemon.getDBConnection("csstatistics");
		Connection stateConn = CSDaemon.getDBConnection("csstate");
		Statement statisticsStm = statisticsConn.createStatement();
		Statement stateStm = stateConn.createStatement();
		
		ResultSet rs = stateStm.executeQuery("select * from lvm_state");
		
		while(rs.next())
		{
			statisticsStm.executeUpdate("insert into lvm_states values(default" 
				+ ", default," + "'" + rs.getString("hostname") + "','" 
				+ rs.getLong("active_conn") + "','" + rs.getLong("inact_conn") + "')");
		}
		
		rs.close();
		statisticsStm.close();
		stateStm.close();
		statisticsConn.close();
		stateConn.close();
	}

}
