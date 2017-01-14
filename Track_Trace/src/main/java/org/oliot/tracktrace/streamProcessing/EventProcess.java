package org.oliot.tracktrace.streamProcessing;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oliot.epcis.configuration.Configuration;
import org.oliot.model.epcis.AggregationEventType;
import org.oliot.model.epcis.EPC;
import org.oliot.model.epcis.ObjectEventType;
import org.oliot.model.epcis.QuantityEventType;
import org.oliot.model.epcis.SensorEventType;
import org.oliot.model.epcis.TransactionEventType;
import org.oliot.model.epcis.TransformationEventType;
import org.oliot.tracktrace.backend.BackendDb;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class EventProcess extends BaseBasicBolt {
  //For holding words and counts
    Map<String, Integer> counts = new HashMap<String, Integer>();

    //execute is called to process tuples
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
    	System.out.println("After the message is recived on EventProcess");
    	Object event=tuple.getValue(0);
		if (event instanceof ObjectEventType) {
			sotore((ObjectEventType) event);
		} else if (event instanceof AggregationEventType) {
			System.out.println("Before AggregationEventType stored");
			sotore((AggregationEventType) event);
			System.out.println("After AggregationEventType stored");
		} else if (event instanceof TransactionEventType) {
			sotore((TransactionEventType) event);
		} else if (event instanceof TransformationEventType) {
			sotore((TransformationEventType) event);
		} else if (event instanceof QuantityEventType) {
			sotore((QuantityEventType) event);
		} else if (event instanceof SensorEventType) {
			sotore((SensorEventType) event);
		}
    	
//      //Get the word contents from the tuple
//      String word = tuple.getString(0);
//      //Have we counted any already?
//      Integer count = counts.get(word);
//      if (count == null)
//        count = 0;
//      //Increment the count and store it
//      count++;
//      counts.put(word, count);
//      //Emit the word and the current count
//      collector.emit(new Values(word, count));
    }

    //Declare that we will emit a tuple containing two fields; word and count
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("object"));
    }
    
    
    Cluster cluster;
	Session session;
	public void sotore(ObjectEventType event){
		
	try{
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("tracktrace");
		Timestamp eventTime=new Timestamp(0) ;
		int yearNew=1987;
		String bizLocation= "bizLocation";
		String bizStep="bizLocation";
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.YEAR, 50);
		Date largeTime= cal.getTime();
		if(event.getEventTime()!=null){
			eventTime=new Timestamp(event.getEventTime().toGregorianCalendar().getTimeInMillis());
			yearNew=event.getEventTime().toGregorianCalendar().getTime().getYear();
		}if(event.getBizLocation()!=null){
			if(event.getBizLocation().getId()!=null && !event.getBizLocation().getId().isEmpty())
				bizLocation= event.getBizLocation().getId();
		} 
		if(event.getBizStep()!=null && !event.getBizStep().isEmpty())
			bizStep=event.getDisposition();
		if(event.getEpcList()!=null){
			List<EPC> epcList=event.getEpcList().getEpc();//.getInputEPCList().getEpc();
			for(int i=0; i<epcList.size();i++){
				String epcId=epcList.get(i).getValue();
				String bizLocationOld="unUpdated";
				String bizStepOld="bizStepOld";
				Timestamp eventTimeOld=new Timestamp(0) ;
				int yearOld=1987;
				BackendDb backendDb=new BackendDb();
				//q0
				backendDb.insertIntoObjectTrackTrace(session, epcId, bizLocation, eventTime, bizStep);
				//q2
				ResultSet epcIdFromResults=backendDb.selectFromLocationByObject(session, epcId);
				for (Row row : epcIdFromResults) {
					bizLocationOld=row.getString("biz_location");
					Date eventDateOld=row.getTimestamp("event_time");
					yearOld=eventDateOld.getYear();
					eventTimeOld=new Timestamp(eventDateOld.getTime());
					bizStepOld=row.getString("biz_step");
					backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
					backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
				}
				//q1
				backendDb.insertIntoLocationByObject(session, epcId, bizLocation, eventTime, bizStep);
				//q3 && q4
				backendDb.insertIntoEventTimeByObjects(session, epcId, eventTime, bizLocation, bizStep);
				//q5 && q6
				if(bizLocationOld !="unUpdated"){
					backendDb.insertIntoStartTimeByLocation(session, bizLocationOld,yearOld, eventTimeOld, epcId, eventTime, bizStepOld);
					backendDb.insertIntoEndTimeByLocation(session, bizLocationOld,yearNew, eventTime, epcId, eventTimeOld, bizStepOld);
					backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
				}else{
					backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
				}
			}
		}
		
		session.close();
		cluster.close();
		System.out.println("objectEvent saved");
		//Configuration.logger.info("objectEvent saved");
	}catch(Exception e){
		System.out.println("TrackTrace Keyspace not found");
		System.out.println(e);
		//Configuration.logger.error("TrackTrace Keyspace not found");
		//Configuration.logger.error(e);
	}

	}
	
	public void sotore(AggregationEventType event) {
		
		try{
			System.out.println("inside agregation event store");
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");
			BackendDb backendDb=new BackendDb();
			Timestamp eventTime=new Timestamp(0) ;
			int yearNew=1987;
			String bizLocation= "bizLocation";
			String bizStep="bizLocation";
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.YEAR, 50);
			Date largeTime= cal.getTime();
			if(event.getEventTime()!=null){
				eventTime=new Timestamp(event.getEventTime().toGregorianCalendar().getTimeInMillis());
				yearNew=event.getEventTime().toGregorianCalendar().getTime().getYear();
			}
			if(event.getBizLocation()!=null){
				if(event.getBizLocation().getId()!=null && !event.getBizLocation().getId().isEmpty())
					bizLocation= event.getBizLocation().getId();
			} 
			if(event.getBizStep()!=null && !event.getBizStep().isEmpty())
				bizStep=event.getDisposition();
			if(event.getChildEPCs()!=null){
				//System.out.println("here..... 2");
				List<EPC> epcList=event.getChildEPCs().getEpc();//.getInputEPCList().getEpc();
				for(int i=0; i<epcList.size();i++){
					String epcId=epcList.get(i).getValue();
					String bizLocationOld="unUpdated";
					String bizStepOld="bizStepOld";
					Timestamp eventTimeOld=new Timestamp(0) ;
					int yearOld=1987;
					//q0
					backendDb.insertIntoObjectTrackTrace(session, epcId, bizLocation, eventTime, bizStep);
					//q2
					ResultSet epcIdFromResults=backendDb.selectFromLocationByObject(session, epcId);
					for (Row row : epcIdFromResults) {
						bizLocationOld=row.getString("biz_location");
						Date eventDateOld=row.getTimestamp("event_time");
						yearOld=eventDateOld.getYear();
						eventTimeOld=new Timestamp(eventDateOld.getTime());
						bizStepOld=row.getString("biz_step");
						backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
						backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
					}
					//q1
					backendDb.insertIntoLocationByObject(session, epcId, bizLocation, eventTime, bizStep);
					//q3 && q4
					backendDb.insertIntoEventTimeByObjects(session, epcId, eventTime, bizLocation, bizStep);
					//q5 && q6
					if(bizLocationOld !="unUpdated"){
						backendDb.insertIntoStartTimeByLocation(session, bizLocationOld,yearOld, eventTimeOld, epcId, eventTime, bizStepOld);
						backendDb.insertIntoEndTimeByLocation(session, bizLocationOld,yearNew, eventTime, epcId, eventTimeOld, bizStepOld);
						backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
					}else{
						backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
					}
				}
				
			}
			if(event.getParentID() !=null){
				//System.out.println("here..... 3");
				String epcId=event.getParentID();
				String bizLocationOld="unUpdated";
				String bizStepOld="bizStepOld";
				Timestamp eventTimeOld=new Timestamp(0) ;
				int yearOld=1987;
				//q0
				backendDb.insertIntoObjectTrackTrace(session, epcId, bizLocation, eventTime, bizStep);
				//q2
				ResultSet epcIdFromResults=backendDb.selectFromLocationByObject(session, epcId);
				for (Row row : epcIdFromResults) {
					bizLocationOld=row.getString("biz_location");
					Date eventDateOld=row.getTimestamp("event_time");
					yearOld=eventDateOld.getYear();
					eventTimeOld=new Timestamp(eventDateOld.getTime());
					bizStepOld=row.getString("biz_step");
					backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
					backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
				}
				//q1
				backendDb.insertIntoLocationByObject(session, epcId, bizLocation, eventTime, bizStep);
				//q3 && q4
				backendDb.insertIntoEventTimeByObjects(session, epcId, eventTime, bizLocation, bizStep);
				//q5 && q6
				if(bizLocationOld !="unUpdated"){
					backendDb.insertIntoStartTimeByLocation(session, bizLocationOld, yearOld,eventTimeOld, epcId, eventTime, bizStepOld);
					backendDb.insertIntoEndTimeByLocation(session, bizLocationOld,yearNew, eventTime, epcId, eventTimeOld, bizStepOld);
					backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
				}else{
					backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
				}
			}
			
			if(event.getAction().equals("ADD")){
				if(event.getParentID() !=null){
					String parentId=event.getParentID();
					if(event.getChildEPCs()!=null){
						List<EPC> epcList=event.getChildEPCs().getEpc();//.getInputEPCList().getEpc();
						for(int i=0; i<epcList.size();i++){
							String epcId=epcList.get(i).getValue();
							backendDb.insertIntoChildEPCByParents(session, parentId, epcId);	
						}
						}
				}
			}else if(event.getAction().equals("DELETE")){
				if(event.getParentID() !=null){
					String parentId=event.getParentID();
					if(event.getChildEPCs()!=null){
						List<EPC> epcList=event.getChildEPCs().getEpc();//.getInputEPCList().getEpc();
						for(int i=0; i<epcList.size();i++){
							String epcId=epcList.get(i).getValue();
							backendDb.deleteFromChildEPCByParents(session, parentId, epcId);
						}
							
						}
				}
			}
			
			session.close();
			cluster.close();
			System.out.println("[*] Even processing AggregationEventType stored");
			//Configuration.logger.info("AggregationEventType saved");
		}catch(Exception e){
			System.out.println("TrackTrace Keyspace not found");
			System.out.println(e);
			//Configuration.logger.error("TrackTrace Keyspace not found");
			//Configuration.logger.error(e);
		}


	}
	
	public void sotore(QuantityEventType event) {
		
		try{
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");	
		
		}catch(Exception e){
			System.out.println("TrackTrace Keyspace not found");
			//Configuration.logger.error("TrackTrace Keyspace not found");
		}

	}
	
	public void sotore(TransactionEventType event) {
		
		try{
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");
			Timestamp eventTime=new Timestamp(0) ;
			int yearNew=1987;
			String bizLocation= "bizLocation";
			String bizStep="bizLocation";
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.YEAR, 50);
			Date largeTime= cal.getTime();
			if(event.getEventTime()!=null){
				eventTime=new Timestamp(event.getEventTime().toGregorianCalendar().getTimeInMillis());
				yearNew=event.getEventTime().toGregorianCalendar().getTime().getYear();
			}
			if(event.getBizLocation()!=null){
				if(event.getBizLocation().getId()!=null && !event.getBizLocation().getId().isEmpty())
					bizLocation= event.getBizLocation().getId();
			} 
			if(event.getBizStep()!=null && !event.getBizStep().isEmpty())
				bizStep=event.getDisposition();
			if(event.getEpcList()!=null){
				List<EPC> epcList=event.getEpcList().getEpc();//.getInputEPCList().getEpc();
				for(int i=0; i<epcList.size();i++){
					String epcId=epcList.get(i).getValue();
					String bizLocationOld="unUpdated";
					String bizStepOld="bizStepOld";
					Timestamp eventTimeOld=new Timestamp(0) ;
					int yearOld=1987;
					BackendDb backendDb=new BackendDb();
					//q0
					backendDb.insertIntoObjectTrackTrace(session, epcId, bizLocation, eventTime, bizStep);
					//q2
					ResultSet epcIdFromResults=backendDb.selectFromLocationByObject(session, epcId);
					for (Row row : epcIdFromResults) {
						bizLocationOld=row.getString("biz_location");
						Date eventDateOld=row.getTimestamp("event_time");
						yearOld=eventDateOld.getYear();
						eventTimeOld=new Timestamp(eventDateOld.getTime());
						bizStepOld=row.getString("biz_step");
						backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
						backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
					}
					//q1
					backendDb.insertIntoLocationByObject(session, epcId, bizLocation, eventTime, bizStep);
					//q3 && q4
					backendDb.insertIntoEventTimeByObjects(session, epcId, eventTime, bizLocation, bizStep);
					//q5 && q6
					if(bizLocationOld !="unUpdated"){
						backendDb.insertIntoStartTimeByLocation(session, bizLocationOld,yearOld, eventTimeOld, epcId, eventTime, bizStepOld);
						backendDb.insertIntoEndTimeByLocation(session, bizLocationOld,yearNew, eventTime, epcId, eventTimeOld, bizStepOld);
						backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
					}else{
						backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
					}
				}
				
			}
			if(event.getParentID() !=null){
				String epcId=event.getParentID();
				String bizLocationOld="unUpdated";
				String bizStepOld="bizStepOld";
				Timestamp eventTimeOld=new Timestamp(0) ;
				int yearOld=1987;
				BackendDb backendDb=new BackendDb();
				//q0
				backendDb.insertIntoObjectTrackTrace(session, epcId, bizLocation, eventTime, bizStep);
				//q2
				ResultSet epcIdFromResults=backendDb.selectFromLocationByObject(session, epcId);
				for (Row row : epcIdFromResults) {
					bizLocationOld=row.getString("biz_location");
					Date eventDateOld=row.getTimestamp("event_time");
					yearOld=eventDateOld.getYear();
					eventTimeOld=new Timestamp(eventDateOld.getTime());
					bizStepOld=row.getString("biz_step");
					backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
					backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
				}
				//q1
				backendDb.insertIntoLocationByObject(session, epcId, bizLocation, eventTime, bizStep);
				//q3 && q4
				backendDb.insertIntoEventTimeByObjects(session, epcId, eventTime, bizLocation, bizStep);
				//q5 && q6
				if(bizLocationOld !="unUpdated"){
					backendDb.insertIntoStartTimeByLocation(session, bizLocationOld,yearOld, eventTimeOld, epcId, eventTime, bizStepOld);
					backendDb.insertIntoEndTimeByLocation(session, bizLocationOld,yearNew, eventTime, epcId, eventTimeOld, bizStepOld);
					backendDb.insertIntoStartTimeByLocation(session, bizLocation, yearNew,eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
				}else{
					backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
				}
			}
			
			session.close();
			cluster.close();
			System.out.println("TransactionEventType saved");
			//Configuration.logger.info("TransactionEventType saved");
		}catch(Exception e){
			System.out.println("TrackTrace Keyspace not found");
			System.out.println(e);
			//Configuration.logger.error("TrackTrace Keyspace not found");
			//Configuration.logger.error(e);
		}

	}
	
	public void sotore(TransformationEventType event) {
		
		try{
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");	
			
			if(event.getInputEPCList() !=null){
				if(event.getOutputEPCList()!=null){
					List<EPC> inputEpcList=event.getInputEPCList().getEpc();
					List<EPC> outputEpcList=event.getOutputEPCList().getEpc();
					Timestamp eventTime=new Timestamp(0) ;
					if(event.getEventTime()!=null)
					 eventTime=new Timestamp(event.getEventTime().toGregorianCalendar().getTimeInMillis());
					
					String transformationId="";
					String despostion="";
					String bizLocation= "";
					if(event.getTransformationID()!=null && !event.getTransformationID().isEmpty())
						transformationId=event.getTransformationID() ;
					if(event.getDisposition()!=null && !event.getDisposition().isEmpty())
						despostion=event.getDisposition();
					if(event.getBizLocation()!=null){
						if(event.getBizLocation().getId()!=null && !event.getBizLocation().getId().isEmpty())
							bizLocation= event.getBizLocation().getId();
					}
					for(int i=0; i<inputEpcList.size();i++){
						for(int j=0; j<outputEpcList.size(); j++){
							String inputEPC=inputEpcList.get(i).getValue();
							String outputEpc=outputEpcList.get(j).getValue();
							try{
								BackendDb backendDb=new BackendDb();
								backendDb.insertIntoTransformedFromTo(session, inputEPC, outputEpc, eventTime, bizLocation, transformationId, despostion);
								backendDb.insertIntoTransformedToFrom(session, outputEpc, inputEPC, eventTime, bizLocation, transformationId, despostion);
								ResultSet epcIdFromResults=backendDb.selectFromTransformedToFrom(session, outputEpc);
								for (Row row : epcIdFromResults) {
									String epcIdFrom=row.getString("epc_id_from");	
									backendDb.insertIntoTransformedFromTo(session, epcIdFrom, outputEpc, eventTime, bizLocation, transformationId, despostion);
									backendDb.insertIntoTransformedToFrom(session, outputEpc, epcIdFrom, eventTime, bizLocation, transformationId, despostion);
								}
							}catch(Exception e){
								Configuration.logger.error(e);
							}
							
						}
					}
				}
			}
			session.close();
			cluster.close();
			System.out.println("TransformationEvent saved");
			//Configuration.logger.error("TransformationEvent saved");
		}catch(Exception e){
			System.out.println("TrackTrace Keyspace not found");
			System.out.println(e);
			//Configuration.logger.error("TrackTrace Keyspace not found");
			//Configuration.logger.error(e);
		}

	}
	
	public void sotore(SensorEventType event) {
		
		try{
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");	
		
		}catch(Exception e){
			System.out.println("TrackTrace Keyspace not found");
			//Configuration.logger.error("TrackTrace Keyspace not found");
		}

	}
}
