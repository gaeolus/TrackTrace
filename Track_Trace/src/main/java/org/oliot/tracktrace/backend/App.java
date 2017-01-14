package org.oliot.tracktrace.backend;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	//CassndraDeo cd=new CassndraDeo();
    	Cluster cluster;
    	Session session;
    	cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("tracktrace");
		BackendDb backDb=new BackendDb();
		//backDb.createTables(session);
		backDb.truncateTables(session);
		//backDb.deleteTables(session);
		//BackendDb backendDb=new BackendDb();
		//Date time=new Date();
		//Calendar cal=Calendar.getInstance();
		//cal.add(Calendar.YEAR, 50);
		//Date time3= cal.getTime();
		//long x=time.getTime();
		//Timestamp startTimestamp=new Timestamp(x);
		//Timestamp endTimestamp=new Timestamp(time3.getTime());
	 
		//backendDb.insertIntoObjectTrackTrace(session);
		//backendDb.insertIntoChildEPCByParents(session);s
		//backDb.insertIntoLocationByObject(session, "epcId 1", "bizLocation 1", time, "bizStep");
		//backDb.insertIntoObjectsByLocation(session, "bizLocation 1",1987, "epcId 1", startTimestamp, "bizStep");
		//backendDb.deleteFromObjectsByLocation(session, "bizLocation 1", "epcId 1");
		
		//backendDb.insertIntoStartTimeByLocation(session, "bizLocation", timestamp, "epcId", timestamp, "bizStep");
		//ResultSet results =backendDb.selectFromSartTimeByLocation(session, "bizLocation",startTimestamp);
		
		//for (Row row : results) {
			//System.out.format( row.getString("biz_location"), "  ",row.getTimestamp("event_time"));
			//Date time2=row.getTimestamp("event_time");
			//System.out.println(row.getString("biz_location"));
			//System.out.println(row.getTimestamp("start_time"));
			//System.out.println(row.getString("epc_id"));
			//System.out.println(row.getTimestamp("end_time"));
			//System.out.println(row.getString("biz_step"));
			//System.out.println("here");
			//startTimestamp=new Timestamp(row.getTimestamp("start_time").getTime());
			//backendDb.insertIntoStartTimeByLocation(session, "bizLocation", startTimestamp, "epcId", endTimestamp, "bizStep");
			//}
		
		
		session.close();
		cluster.close();
		
		/*
		cd.insertIntousers(session);
		ResultSet results = cd.selectFromUsers(session);
		for (Row row : results) {
			System.out.format("%s %d\n", row.getString("firstname"), row.getInt("age"));
			}
    	*/
        System.out.println( "END APP" );
    }
}
