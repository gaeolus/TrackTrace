package org.oliot.tracktrace.backend;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oliot.epcis.configuration.Configuration;
import org.oliot.model.epcis.AggregationEventType;
import org.oliot.model.epcis.EPC;
import org.oliot.model.epcis.EPCListType;
import org.oliot.model.epcis.ObjectEventType;
import org.oliot.model.epcis.QuantityEventType;
import org.oliot.model.epcis.SensorEventType;
import org.oliot.model.epcis.TransactionEventType;
import org.oliot.model.epcis.TransformationEventType;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Copyright (C) 2015 KAIST RESL
 *
 * This project is part of Oliot (oliot.org), 
 * implements track and race using 

 * 
 *
 * @author Yalew Kidane, MSc student
 * 
 *         Korea Advanced Institute of Science and Technology (KAIST)
 * 
 *         Real-time Embedded System Laboratory(RESL)
 * 
 *         yalewkidane@kaist.ac.kr
 */
public class BackCaptureEnd {
	Cluster cluster;
	Session session;
	public void sotore(ObjectEventType event){
		
	try{
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("tracktrace");
		Timestamp eventTime=new Timestamp(0) ;
		int yearNew=2000;
		String bizLocation= "bizLocation";
		String bizStep="bizLocation";
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.YEAR, 50);
		Date largeTime= cal.getTime();
		if(event.getEventTime()!=null){
			eventTime=new Timestamp(event.getEventTime().toGregorianCalendar().getTimeInMillis());
			yearNew=event.getEventTime().toGregorianCalendar().get(Calendar.YEAR);
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
					Calendar tempcal=Calendar.getInstance();
					tempcal.setTime(eventDateOld);
					yearOld=tempcal.get(Calendar.YEAR);
					
					eventTimeOld=new Timestamp(eventDateOld.getTime());
					bizStepOld=row.getString("biz_step");
					backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
					backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
				}
				backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
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
		Configuration.logger.info("objectEvent saved");
	}catch(Exception e){
		Configuration.logger.error("TrackTrace Keyspace not found");
		Configuration.logger.error(e);
	}

	}
	
	public void sotore(AggregationEventType event) {
		
		try{
			//System.out.println("here..... 1");
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");
			BackendDb backendDb=new BackendDb();
			Timestamp eventTime=new Timestamp(0) ;
			int yearNew=2000;
			String bizLocation= "bizLocation";
			String bizStep="bizLocation";
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.YEAR, 50);
			Date largeTime= cal.getTime();
			if(event.getEventTime()!=null){
				eventTime=new Timestamp(event.getEventTime().toGregorianCalendar().getTimeInMillis());
				yearNew=event.getEventTime().toGregorianCalendar().get(Calendar.YEAR);
			}
			 
			if(event.getBizLocation()!=null){
				if(event.getBizLocation().getId()!=null && !event.getBizLocation().getId().isEmpty())
					bizLocation= event.getBizLocation().getId();
			} 
			if(event.getBizStep()!=null && !event.getBizStep().isEmpty())
				bizStep=event.getBizStep();
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
						Calendar tempcal=Calendar.getInstance();
						tempcal.setTime(eventDateOld);
						yearOld=tempcal.get(Calendar.YEAR);
						eventTimeOld=new Timestamp(eventDateOld.getTime());
						bizStepOld=row.getString("biz_step");
						backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
						}
					backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
					
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
					Calendar tempcal=Calendar.getInstance();
					tempcal.setTime(eventDateOld);
					yearOld=tempcal.get(Calendar.YEAR);
					eventTimeOld=new Timestamp(eventDateOld.getTime());
					bizStepOld=row.getString("biz_step");
					backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
					}
				backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
				
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
			Configuration.logger.info("AggregationEventType saved");
		}catch(Exception e){
			Configuration.logger.error("TrackTrace Keyspace not found");
			Configuration.logger.error(e);
		}


	}
	
	public void sotore(QuantityEventType event) {
		
		try{
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");	
		
		}catch(Exception e){
			Configuration.logger.error("TrackTrace Keyspace not found");
		}

	}
	
	public void sotore(TransactionEventType event) {
		
		try{
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");
			Timestamp eventTime=new Timestamp(0) ;
			int yearNew=2000;
			String bizLocation= "bizLocation";
			String bizStep="bizLocation";
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.YEAR, 50);
			Date largeTime= cal.getTime();
			if(event.getEventTime()!=null){
				eventTime=new Timestamp(event.getEventTime().toGregorianCalendar().getTimeInMillis());
				yearNew=event.getEventTime().toGregorianCalendar().get(Calendar.YEAR);
			}
			 
			if(event.getBizLocation()!=null){
				if(event.getBizLocation().getId()!=null && !event.getBizLocation().getId().isEmpty())
					bizLocation= event.getBizLocation().getId();
			} 
			if(event.getBizStep()!=null && !event.getBizStep().isEmpty())
				bizStep=event.getBizStep();
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
						Calendar tempcal=Calendar.getInstance();
						tempcal.setTime(eventDateOld);
						yearOld=tempcal.get(Calendar.YEAR);
						eventTimeOld=new Timestamp(eventDateOld.getTime());
						bizStepOld=row.getString("biz_step");
						backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
						}
					backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
					
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
					Calendar tempcal=Calendar.getInstance();
					tempcal.setTime(eventDateOld);
					yearOld=tempcal.get(Calendar.YEAR);
					eventTimeOld=new Timestamp(eventDateOld.getTime());
					bizStepOld=row.getString("biz_step");
					backendDb.deleteFromObjectsByLocation(session, bizLocationOld,yearOld, epcId);
					}
				backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, epcId, eventTime, bizStep);
				
				//q1
				backendDb.insertIntoLocationByObject(session, epcId, bizLocation, eventTime, bizStep);
				//q3 && q4
				backendDb.insertIntoEventTimeByObjects(session, epcId, eventTime, bizLocation, bizStep);
				//q5 && q6
				if(bizLocationOld !="unUpdated"){
					backendDb.insertIntoStartTimeByLocation(session, bizLocationOld, yearOld,eventTimeOld, epcId, eventTime, bizStepOld);
					backendDb.insertIntoEndTimeByLocation(session, bizLocationOld,yearNew, eventTime, epcId, eventTimeOld, bizStepOld);
					backendDb.insertIntoStartTimeByLocation(session, bizLocation, yearNew,eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
				}else{
					backendDb.insertIntoStartTimeByLocation(session, bizLocation, yearNew,eventTime, epcId, new Timestamp(largeTime.getTime()), bizStep);
				}
			}
			
			session.close();
			cluster.close();
			Configuration.logger.info("TransactionEventType saved");
		}catch(Exception e){
			Configuration.logger.error("TrackTrace Keyspace not found");
			Configuration.logger.error(e);
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
					int yearNew=1987;
					Calendar cal=Calendar.getInstance();
					cal.add(Calendar.YEAR, 50);
					Date largeTime= cal.getTime();
					if(event.getEventTime()!=null){
						eventTime=new Timestamp(event.getEventTime().toGregorianCalendar().getTimeInMillis());
						yearNew=event.getEventTime().toGregorianCalendar().get(Calendar.YEAR);
					}
					 
					String transformationId="";
					String despostion="";
					String bizLocation= "";
					String bizStep="";
					if(event.getTransformationID()!=null && !event.getTransformationID().isEmpty())
						transformationId=event.getTransformationID() ;
					if(event.getDisposition()!=null && !event.getDisposition().isEmpty())
						despostion=event.getDisposition();
					if(event.getBizLocation()!=null){
						if(event.getBizLocation().getId()!=null && !event.getBizLocation().getId().isEmpty())
							bizLocation= event.getBizLocation().getId();
					}
					if(event.getBizStep()!=null && !event.getBizStep().isEmpty())
						bizStep=event.getBizStep();
					BackendDb backendDb=new BackendDb();
					for(int i=0; i<inputEpcList.size();i++){
						for(int j=0; j<outputEpcList.size(); j++){
							String inputEPC=inputEpcList.get(i).getValue();
							String outputEpc=outputEpcList.get(j).getValue();
							try{
								backendDb.insertIntoTransformedFromTo(session, inputEPC, outputEpc, eventTime, bizLocation, transformationId, despostion);
								backendDb.insertIntoTransformedToFrom(session, outputEpc, inputEPC, eventTime, bizLocation, transformationId, despostion);
								ResultSet epcIdFromResults=backendDb.selectFromTransformedToFrom(session, inputEPC);
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
					for(int x=0; x<outputEpcList.size();x++){
						String outputEpc=outputEpcList.get(x).getValue();
						//q0
						backendDb.insertIntoObjectTrackTrace(session, outputEpc, bizLocation, eventTime, bizStep);
						//q2
						backendDb.insertIntoObjectsByLocation(session, bizLocation,yearNew, outputEpc, eventTime, bizStep);
						//q1
						backendDb.insertIntoLocationByObject(session, outputEpc, bizLocation, eventTime, bizStep);
						//q3 && q4
						backendDb.insertIntoEventTimeByObjects(session, outputEpc, eventTime, bizLocation, bizStep);
						//q5 && q6	
						backendDb.insertIntoStartTimeByLocation(session, bizLocation,yearNew, eventTime, outputEpc, new Timestamp(largeTime.getTime()), bizStep);

					}
				}
			}
			session.close();
			cluster.close();
			Configuration.logger.error("TransformationEvent saved");
		}catch(Exception e){
			Configuration.logger.error("TrackTrace Keyspace not found");
			Configuration.logger.error(e);
		}

	}
	
	public void sotore(SensorEventType event) {
		
		try{
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("tracktrace");	
		
		}catch(Exception e){
			Configuration.logger.error("TrackTrace Keyspace not found");
		}

	}
	
}
