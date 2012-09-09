import java.io.StringReader;
import java.sql.*;
import java.util.Scanner;
import java.util.Vector;

public class Pvfs2Logger extends Logger {
	
	public static void main(String[] args) 
	{
		try {
			new Pvfs2Logger().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Pvfs2Logger() throws Exception {
		super("/usr/local/bin/pvfs2-statfs -m /mnt/storage-server1");
	}

	@Override
	protected void logging(StringReader res) throws Exception{
		
		Vector<Long> hddUse = new Vector<Long>();
		Vector<Long> hddTotal = new Vector<Long>();
		Vector<String> hostName = new Vector<String>();
		
		Scanner in = new Scanner(res);
		while(in.hasNext() && !in.nextLine().equals("I/O server statistics:"));
		
		while(in.hasNext())
		{
			while(in.hasNext() && !in.next().equals("server:"));
			if(!in.hasNext())	break;
			hostName.add(in.next().replaceAll("\\:3334", "").replaceAll("tcp\\://", ""));
			
			//while(in.hasNext() && !in.next().equals("bytes") && !in.next().equals("available"));
			while(in.hasNext() && !in.next().equals("bytes"));
			while(in.hasNext() && !in.next().equals("bytes"));
			while(in.hasNext() && !in.next().equals("bytes"));
			in.next();in.next();
			long available = in.nextLong();
			
			while(in.hasNext() && !in.next().equals(":"));
			long total = in.nextLong();
			hddUse.add(total - available);
			hddTotal.add(total);
		}
		in.close();
		
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection conn = CSDaemon.getDBConnection("csstate");
		Statement stm = conn.createStatement();
		
		for(int i = 0; i < hostName.size(); i++)
		{
			stm.executeUpdate("update storage_state set hdd_use='" + hddUse.elementAt(i)
					+"' where hostname='" + hostName.elementAt(i) + "'");
			stm.executeUpdate("update storage_state set hdd_total='" + hddTotal.elementAt(i)
					+"' where hostname='" + hostName.elementAt(i) + "'");
		}
		
		stm.close();
		conn.close();
	}

}
