package org.oliot.tracktrace.backend;

import java.sql.Timestamp;
import java.util.Date;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class BackendDb {
	

	
	public void createTables(Session session){
		session.execute(getStatmentctObjectTrackTrace()); //general
		session.execute(getStatmentctLocationByObject()); //q1
		session.execute(getStatmentctObjectsByLocation()); //q2
		session.execute(getStatmentcteventTimeByObject()); // q3 & q4
		session.execute(getStatmentctSartTimeByLocation()); // q5 & q6
		session.execute(getStatmentctEndTimeByLocation()); // q5 & q6
		session.execute(getStatmentctChildByParentId()); // Aggregation event 
		session.execute(getStatmentctTransformedFromTo()); // Transformation
		session.execute(getStatmentctTransformedToFrom()); // Transformation
		
	}
	
	public void deleteTables(Session session){
		session.execute("DROP TABLE object_track_trace"); // general
		session.execute("DROP TABLE location_by_object"); // q1
		session.execute("DROP TABLE objects_by_location"); // q2
		session.execute("DROP TABLE event_time_by_object"); // q3 & q4
		session.execute("DROP TABLE start_time_by_location"); // q5 & q6
		session.execute("DROP TABLE end_time_by_location"); // q5 & q6
		session.execute("DROP TABLE childEPC_by_parentId"); // Aggregation event
		session.execute("DROP TABLE transformed_from_to"); // Transformation 
		session.execute("DROP TABLE transformed_to_from"); // Transformation
			
	}
	
	public void truncateTables(Session session){
		session.execute("TRUNCATE object_track_trace"); // general
		session.execute("TRUNCATE location_by_object"); // q1
		session.execute("TRUNCATE objects_by_location"); // q2
		session.execute("TRUNCATE event_time_by_object"); // q3 & q4
		session.execute("TRUNCATE start_time_by_location"); // q5 & q6
		session.execute("TRUNCATE end_time_by_location"); // q5 & q6
		session.execute("TRUNCATE childEPC_by_parentId"); // Aggregation event
		session.execute("TRUNCATE transformed_from_to"); // Transformation 
		session.execute("TRUNCATE transformed_to_from"); // Transformation
			
	}
	
//----------------------- </ insert into tables > --------------------------------//
	public void insertIntoObjectTrackTrace(Session session, String epcId,String bizLocation, Timestamp eventTime, String bizStep ){// general "object_track_trace"); 
		String statement="INSERT INTO object_track_trace "
				+ "(epc_id ,biz_location ,event_time ,biz_step ) "
				+ "VALUES (?,?,?,?);";
		
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(epcId,	bizLocation,eventTime,bizStep));
		
	} 
	
	public void insertIntoLocationByObject(Session session, String epcId, String bizLocation, Timestamp eventTime, String bizStep){ // q1 "location_by_object");
		String statement="INSERT INTO location_by_object "
				+ "(epc_id, biz_location,event_time,biz_step )"
				+ "VALUES (?,?,?,?)";
				
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(epcId, bizLocation, eventTime,bizStep ));
	}
	
	public void insertIntoObjectsByLocation(Session session, String bizLocation,int year, String epcId, Timestamp eventTime, String bizStep){ // q2  "objects_by_location");
		String statement="INSERT INTO objects_by_location "
				+ "(biz_location,year, epc_id, event_time, biz_step)"
				+ "VALUES (?,?,?,?,?)";
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(bizLocation,year, epcId, eventTime, bizStep));
	}
	
	public void insertIntoEventTimeByObjects(Session session, String epcId, Timestamp eventTime, String bizLocation, String bizStep ){// q3 & q4 "event_time_by_object"); 
		String statement="INSERT INTO event_time_by_object"
				+ "(epc_id, event_time, biz_location,  biz_step)"
				+ "VALUES (?,?,?,?)";
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(epcId,eventTime, bizLocation, bizStep));
	}
	
	public void insertIntoStartTimeByLocation(Session session, String bizLocation,int year, Timestamp startTime, String epcId,Timestamp endTime, String bizStep){ // q5 & q6   "start_time_by_location");
		String statement="INSERT INTO start_time_by_location "
				+ "(biz_location,year,start_time, epc_id, end_time, biz_step )"
				+ "VALUES (?,?,?,?,?,?)"; 
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(bizLocation, year, startTime,  epcId, endTime,bizStep));
	}
	
	
	public void insertIntoEndTimeByLocation(Session session, String bizLocation,int year, Timestamp endTime, String epcId, Timestamp  startTime, String bizStep){  // q5 & q6   "end_time_by_location");
		String statement="INSERT INTO end_time_by_location "
				+ "(biz_location,year,end_time, epc_id, start_time , biz_step)"
				+ "VALUES (?,?,?,?,?,?)";
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(bizLocation,year, endTime,  epcId,  startTime,  bizStep));
	}
	
	
	public void insertIntoChildEPCByParents(Session session, String parentId, String epcId){ // Aggregation event   "childEPC_by_parentId");
		String statement="INSERT INTO childEPC_by_parentId"
				+ " (parent_id,epc_id)"
				+ " VALUES (?,?);";
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(parentId,epcId));
	}
	
	public void insertIntoTransformedFromTo(Session session, String epcIdFrom, String epcIdTo, Timestamp eventTime, String bizLocation, String transformationId, String despostion){ // Transformation  "transformed_from_to"); 
		String statement="INSERT INTO transformed_from_to "
				+ "(epc_id_from, epc_id_to , event_time, biz_location, transformation_id, despostion)"
				+ "VALUES (?,?,?,?,?,?)";
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(epcIdFrom,  epcIdTo,  eventTime,  bizLocation,  transformationId,  despostion));
	}
	
	public void insertIntoTransformedToFrom(Session session, String epcIdTo, String epcIdFrom, Timestamp eventTime, String bizLocation, String transformationId, String despostion){ // Transformation "transformed_to_from");
		String statement="INSERT INTO transformed_to_from "
				+ "( epc_id_to,epc_id_from , event_time, biz_location, transformation_id, despostion)"
				+ "VALUES (?,?,?,?,?,?)";
		PreparedStatement prStatement= session.prepare(statement);
		BoundStatement boundStatement=new BoundStatement(prStatement);
		session.execute(boundStatement.bind(epcIdTo, epcIdFrom, eventTime,  bizLocation, transformationId, despostion));
	}

	
//----------------------- </ insert into tables > --------------------------------//
	

//----------------------- < select from tables > --------------------------------//
	public ResultSet selectFromObjectTrackTrace(Session session, String epcId){
		String statment = "SELECT * FROM object_track_trace WHERE epc_id='"+epcId+"'";
		ResultSet results = session.execute(statment);
		return results;
		
	}
	
	public ResultSet selectFromLocationByObject(Session session, String epcId){
		String statment = "SELECT * FROM location_by_object WHERE epc_id='"+epcId+"'";
		ResultSet results = session.execute(statment);
		return results;
	}
	   //Q2
	public ResultSet selectFromObjectsByLocation(Session session, String bizLocation,int year){
		String statment = "SELECT epc_id FROM objects_by_location WHERE biz_location='"+bizLocation+"' AND year="+year;
		ResultSet results = session.execute(statment);
		return results;
	}
	// q3 
	public ResultSet selectFromEventTimeByObjects(Session session,String epcId, Timestamp startTime){
		String statment = "SELECT biz_location FROM event_time_by_object WHERE epc_id='"+epcId+"' AND event_time<='"+startTime+"' LIMIT 1";
		ResultSet results = session.execute(statment);
		return results;
	}
	// q4
	public ResultSet selectFromEventTimeByObjects(Session session,String epcId, Timestamp startTime,Timestamp endTime){
			String statment = "SELECT biz_location FROM event_time_by_object WHERE epc_id='"+epcId+"' AND event_time>='"+startTime+"'AND event_time<='"+endTime+"'";
			ResultSet results = session.execute(statment);
			return results;
	}
	public ResultSet selectFromSartTimeByLocation(Session session,String bizLocation,int year, Timestamp startTime){
		String statment = "SELECT * FROM start_time_by_location WHERE biz_location='"+bizLocation+"' AND year="+year+" AND start_time <='"+startTime+"'";
		ResultSet results = session.execute(statment);
			
		return results;
		
	}
	public ResultSet selectFromSartTimeByLocation(Session session,String bizLocation, int year,Timestamp givenStartTime, Timestamp givenEndTime){
		String statment = "SELECT * FROM start_time_by_location WHERE biz_location='"+bizLocation+"' AND year="+year+" AND start_time >='"+givenStartTime+"' AND start_time<'"+givenEndTime+"'";
		ResultSet results = session.execute(statment);
			
		return results;
		
	}
	public ResultSet selectFromEndTimeByLocation(Session session,String bizLocation, int year, Timestamp givenStartTime, Timestamp givenEndTime){
		String statment = "SELECT * FROM end_time_by_location WHERE biz_location='"+bizLocation+"' AND year="+year+" AND end_time >='"+givenStartTime+"' AND end_time<'"+givenEndTime+"'";
		ResultSet results = session.execute(statment);
			
		return results;
		
	}
	
	public ResultSet selectFromTransformedToFrom(Session session,String epcIdTo){
		String statment = "SELECT * FROM transformed_to_from WHERE epc_id_to='"+epcIdTo+"'";
		ResultSet results = session.execute(statment);
			
		return results;
		
	}
	
	public ResultSet selectFromTransformedFromTo(Session session,String epcIdFrom){
		String statment = "SELECT * FROM transformed_from_to WHERE epc_id_from='"+epcIdFrom+"'";
		ResultSet results = session.execute(statment);
			
		return results;
		
	}
	
	
//----------------------- </ select from tables > --------------------------------//

	
//----------------------- < Delete from tables > --------------------------------//

	public void deleteFromObjectsByLocation(Session session, String bizLocation,int year, String epcId){
		session.execute("DELETE FROM objects_by_location WHERE biz_location='"+bizLocation+"' AND year="+year+" AND epc_id='"+epcId+"'");		
	}
	
	public void deleteFromChildEPCByParents(Session session, String parentId, String epcId){
		String statment = "DELETE FROM childEPC_by_parentId WHERE parent_id='"+parentId+"' epc_id='"+epcId+"'";
		session.execute(statment);
	}
//----------------------- </ Delete from tables > --------------------------------//
	
//----------------------- < Update tables > --------------------------------//
	
	public void updateStartTimeByLocation(Session session, String bizLocation, Timestamp startTime, String epcId,Timestamp endTime, String bizStep){
		 String statment = "UPDATE start_time_by_location SET end_time='"+endTime+"' WHERE biz_location='"+ bizLocation+"' AND start_time='"+startTime+"' AND epc_id='"+ epcId+"'";
		 session.execute(statment);
	 }
	
//----------------------- </ Update tables > --------------------------------//
	
	// ---------------------- Statements ---------------------------//
	
//----------------------- < create table > --------------------------------//
	String getStatmentctObjectTrackTrace(){  // general
		String statement="CREATE TABLE object_track_trace("
							+ "epc_id text,"
							+ "biz_location text,"
							+ "event_time timestamp,"
							+ "biz_step text,"
							+ "PRIMARY KEY ((epc_id), biz_location, event_time))";
		
		return statement;
	}
		
	String getStatmentctLocationByObject(){ // q1
		String statement="CREATE TABLE location_by_object("
							+ "epc_id text,"
							+ "biz_location text,"
							+ "event_time timestamp,"
							+ "biz_step text,"
							+ "PRIMARY KEY (epc_id))";
		
		return statement;
	}
	
	String getStatmentctObjectsByLocation (){ // q2
		String statement="CREATE TABLE objects_by_location("
							+ "biz_location text,"
							+ "year int,"
							+ "epc_id text,"
							+ "event_time timestamp,"
							+ "biz_step text,"
							+ "PRIMARY KEY ((biz_location,year), epc_id))";
		
		return statement;
	}
	
	String getStatmentcteventTimeByObject (){ // q3
		String statement="CREATE TABLE event_time_by_object("
							+ "epc_id text,"			
							+ "event_time timestamp,"
							+ "biz_location text,"
							+ "biz_step text,"
							+ "PRIMARY KEY ((epc_id), event_time))"
							+ "WITH CLUSTERING ORDER BY ( event_time DESC)";
		
		return statement;
	}
	String getStatmentctSartTimeByLocation (){ // q5 & q6
		String statement="CREATE TABLE start_time_by_location("
							+ "biz_location text,"		
							+ "year int,"
							+ "start_time timestamp,"
							+ "epc_id text,"	
							+ "end_time timestamp,"
							+ "biz_step text,"
							+ "PRIMARY KEY ((biz_location,year), start_time,epc_id))"
							+ "WITH CLUSTERING ORDER BY ( start_time DESC)";
		
		return statement;
	}
	String getStatmentctEndTimeByLocation (){ // // q5 & q6
		String statement="CREATE TABLE end_time_by_location("
							+ "biz_location text,"		
							+ "year int,"
							+ "end_time timestamp,"
							+ "epc_id text,"	
							+ "start_time timestamp,"
							+ "biz_step text,"
							+ "PRIMARY KEY ((biz_location,year), end_time,epc_id))"
							+ "WITH CLUSTERING ORDER BY ( end_time DESC)";
		
		return statement;
	}
	
	String getStatmentctChildByParentId (){ // Aggregation 
		String statement="CREATE TABLE childEPC_by_parentId("
							+ "parent_id text,"
							+ "epc_id text,"
							+ "PRIMARY KEY ((parent_id), epc_id))";
		
		return statement;
	}
	
	String getStatmentctTransformedFromTo (){ // Transformation 
		String statement="CREATE TABLE transformed_from_to("
							+ "epc_id_from text,"
							+ "epc_id_to text,"
							+ "event_time timestamp,"
							+ "biz_location text,"
							+ "transformation_id text,"
							+ "despostion text,"
							+ "PRIMARY KEY ((epc_id_from), epc_id_to))";

		return statement;
	}
	String getStatmentctTransformedToFrom (){ // Transformation
		String statement="CREATE TABLE transformed_to_from("
							+ "epc_id_to text,"
							+ "epc_id_from text,"
							+ "event_time timestamp,"
							+ "biz_location text,"
							+ "transformation_id text,"
							+ "despostion text,"
							+ "PRIMARY KEY ((epc_id_to), epc_id_from))";
		
		return statement;
	}

//----------------------- </ create table > --------------------------------//
	
	
	// Connect to the cluster and keyspace "demo"
	void insertIntousers(Session session){
						
		// Insert one record into the users table
			session.execute("INSERT INTO users (lastname, age, city, email, firstname) "
						+ "VALUES ('Jones', 35, 'Austin', 'bob@example.com', 'Bob')");
						
		}
			
	ResultSet selectFromUsers(Session session){
			ResultSet results = session.execute("SELECT * FROM users WHERE lastname='Jones'");
				
			return results;
			
		}
		
}
