package org.oliot.tracktrace.service.query;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXB;

import org.json.JSONArray;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.model.tracTrace.LocationList;
import org.oliot.model.tracTrace.ObjectList;
import org.oliot.model.tracTrace.TrQueryResult;
import org.oliot.model.tracTrace.TrackObject;
import org.oliot.model.tracTrace.TransformedObject;
import org.oliot.tracktrace.backend.BackendDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

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

@Controller
public class RESTLikeQueryService implements ServletContextAware {

	@Autowired
	ServletContext servletContext;

	@Autowired
	private HttpServletRequest request;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;

	}

	Cluster cluster;
	Session session;

	@RequestMapping(value = "/object", method = RequestMethod.GET)
	@ResponseBody
	public String getTrackObjectList(
			@RequestParam(required = false) String epc_id,
			@RequestParam(required = false) String biz_locaion,
			@RequestParam(required = false) String event_time,
			@RequestParam(required = false) String biz_step,
			@RequestParam(required = false) String start_time,
			@RequestParam(required = false) String end_time
			) throws ParseException{
		TrQueryResult trQueryResult=new TrQueryResult();
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("tracktrace");
		
		
		//q0
		if((epc_id !=null)&&(biz_locaion ==null)&&(event_time ==null)
				&&(biz_step ==null)&&(start_time ==null)&&(end_time ==null)){
			Configuration.logger.info("Executing Query 0");
			trQueryResult=getQueryZeroResult(session, epc_id);	
		}
		//q2
		if((epc_id ==null)&&(biz_locaion !=null)&&(event_time ==null)
				&&(biz_step ==null)&&(start_time ==null)&&(end_time ==null)){
			Configuration.logger.info("Executing Query 2");
			trQueryResult=getQueryTwoResult(session, biz_locaion);
		}
		//q5
		if((epc_id ==null)&&(biz_locaion !=null)&&(event_time !=null)
				&&(biz_step ==null)&&(start_time ==null)&&(end_time ==null)){
			Configuration.logger.info("Executing Query 5");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			GregorianCalendar gregorianCalendar=new GregorianCalendar();
			gregorianCalendar.setTime(sdf.parse(event_time));
			int year=gregorianCalendar.getTime().getYear();
			long eventTime=gregorianCalendar.getTimeInMillis();
			trQueryResult=getQueryTFiveResult(session, biz_locaion,year, new Timestamp(eventTime));
			
		}
		//q6
		if((epc_id ==null)&&(biz_locaion !=null)&&(event_time ==null)
				&&(biz_step ==null)&&(start_time !=null)&&(end_time !=null)){
			Configuration.logger.info("Executing Query 6");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			GregorianCalendar gregorianCalendar=new GregorianCalendar();
			gregorianCalendar.setTime(sdf.parse(start_time));
			long startTime=gregorianCalendar.getTimeInMillis();
			int startyear=gregorianCalendar.getTime().getYear();
			gregorianCalendar.setTime(sdf.parse(end_time));
			long endTime=gregorianCalendar.getTimeInMillis();
			int endYear=gregorianCalendar.getTime().getYear();
			trQueryResult=getQueryTSixResult(session, biz_locaion,startyear,endYear, new Timestamp(startTime),new Timestamp(endTime));
			
		}
		
		session.close();
		cluster.close();
		
		StringWriter sw=new StringWriter();
		JAXB.marshal(trQueryResult, sw);
		return sw.toString();
	}
	//q0
	TrQueryResult getQueryZeroResult(Session session,String epcId){
		TrQueryResult trQueryResult=new TrQueryResult();
		//List<TrackObject> TrackObjectList=
		trQueryResult.setTrackObjects(new ArrayList<TrackObject>());//.getTrackObjects()=new ArrayList<TrackObject>();
		BackendDb backendDb=new BackendDb();
		ResultSet results =backendDb.selectFromObjectTrackTrace(session,epcId);
		
		for (Row row : results) {
			TrackObject trackObject=new TrackObject();
			trackObject.setEpc_id(epcId);
			trackObject.setBiz_location(row.getString("biz_location"));
			trackObject.setEvent_time(row.getTimestamp("event_time"));
			trackObject.setBiz_step(row.getString("biz_step"));
			trQueryResult.getTrackObjects().add(trackObject);
		}
		return trQueryResult;
	}
	//q2
		TrQueryResult getQueryTwoResult(Session session,String bizLocation){
			TrQueryResult trQueryResult=new TrQueryResult();
			ObjectList objectList=new ObjectList();
			objectList.setEpc(new ArrayList<String>());
			trQueryResult.setObjectList(objectList);
			Date date=new Date();
			int year=date.getYear();
			BackendDb backendDb=new BackendDb();
			for(int tempYear=year-0; tempYear<=year;tempYear++){
				ResultSet results =backendDb.selectFromObjectsByLocation(session, bizLocation,tempYear);
				for (Row row : results) {
					trQueryResult.getObjectList().getEpc().add(row.getString("epc_id"));
				}
			}
			
			return trQueryResult;
		}
		//q5
		TrQueryResult getQueryTFiveResult(Session session,String bizLocation,int year, Timestamp eventTime){
			TrQueryResult trQueryResult=new TrQueryResult();
			ObjectList objectList=new ObjectList();
			objectList.setEpc(new ArrayList<String>());
			trQueryResult.setObjectList(objectList);
			Date  givenDate= new Date(eventTime.getTime());
			BackendDb backendDb=new BackendDb();
			for(int tempYear=year-0; tempYear<=year;tempYear++){
				ResultSet results =backendDb.selectFromSartTimeByLocation(session, bizLocation,tempYear,eventTime);
				for (Row row : results) {
					Date  endDate=row.getTimestamp("end_time");//end_time
					if(givenDate.compareTo(endDate)<0)
					trQueryResult.getObjectList().getEpc().add(row.getString("epc_id"));
				}
			}
			
			return trQueryResult;
			
		}
		//q6
		TrQueryResult getQueryTSixResult(Session session,String bizLocation,int startYear, int endYear,Timestamp givenStartTime, Timestamp givenEndTime){
			TrQueryResult trQueryResult=new TrQueryResult();
			
			ObjectList objectList=new ObjectList();
			objectList.setEpc(new ArrayList<String>());
			trQueryResult.setObjectList(objectList);
			Date  givenEndDate= new Date(givenEndTime.getTime());
			BackendDb backendDb=new BackendDb();
			
			for(int tempYear=startYear-0; tempYear<=endYear;tempYear++){
				ResultSet results =backendDb.selectFromSartTimeByLocation(session, bizLocation,tempYear,givenStartTime);
				for (Row row : results) {
					Date  endDate=row.getTimestamp("end_time");//end_time
					if(givenEndDate.compareTo(endDate)<0)
					trQueryResult.getObjectList().getEpc().add(row.getString("epc_id"));
				}
				//start_time >=t1 and start_time<t1
				results =backendDb.selectFromSartTimeByLocation(session, bizLocation,tempYear,givenStartTime,givenEndTime);
				for (Row row : results) {
					trQueryResult.getObjectList().getEpc().add(row.getString("epc_id"));
				}
				//end_time >=t1 and end_time<t1
				results =backendDb.selectFromEndTimeByLocation(session, bizLocation,tempYear,givenStartTime,givenEndTime);
				for (Row row : results) {
					trQueryResult.getObjectList().getEpc().add(row.getString("epc_id"));
				}
			}
			
			
			
			return trQueryResult;
			
		}
	
	@RequestMapping(value = "/location", method = RequestMethod.GET)
	@ResponseBody
	public String getTrackLocationList(
			@RequestParam(required = false) String epc_id,
			@RequestParam(required = false) String biz_locaion,
			@RequestParam(required = false) String event_time,
			@RequestParam(required = false) String biz_step,
			@RequestParam(required = false) String start_time,
			@RequestParam(required = false) String end_time
			) throws ParseException{
		TrQueryResult trQueryResult=new TrQueryResult();
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("tracktrace");
		
		
		//q1-implemented here
		if((epc_id !=null)&&(biz_locaion ==null)&&(event_time ==null)
						&&(biz_step ==null)&&(start_time ==null)&&(end_time ==null)){
			Configuration.logger.info("Executing Query 1");
			trQueryResult=getQueryOneResult(session, epc_id);	
			}
		//q3 -implemented here
		if((epc_id !=null)&&(biz_locaion ==null)&&(event_time !=null)
				&&(biz_step ==null)&&(start_time ==null)&&(end_time ==null)){
			Configuration.logger.info("Executing Query 3");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			GregorianCalendar gregorianCalendar=new GregorianCalendar();
			gregorianCalendar.setTime(sdf.parse(event_time));
			long eventTime=gregorianCalendar.getTimeInMillis();
			trQueryResult=getQueryThreeResult(session, epc_id, new Timestamp(eventTime));
			
		}
		//q4
		if((epc_id !=null)&&(biz_locaion ==null)&&(event_time ==null)
				&&(biz_step ==null)&&(start_time !=null)&&(end_time !=null)){
			Configuration.logger.info("Executing Query 4");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			GregorianCalendar gregorianCalendar=new GregorianCalendar();
			gregorianCalendar.setTime(sdf.parse(start_time));
			long startTime=gregorianCalendar.getTimeInMillis();
			gregorianCalendar.setTime(sdf.parse(end_time));
			long endTime=gregorianCalendar.getTimeInMillis();
			trQueryResult=getQueryFoureResult(session, epc_id, new Timestamp(startTime),new Timestamp(endTime));
			
		}
		
		session.close();
		cluster.close();
		
		StringWriter sw=new StringWriter();
		JAXB.marshal(trQueryResult, sw);
		return sw.toString();
	}
	//q1
	TrQueryResult getQueryOneResult(Session session,String epcId){
		TrQueryResult trQueryResult=new TrQueryResult();
		//List<TrackObject> TrackObjectList=
		//trQueryResult.setTrackObjects(new ArrayList<TrackObject>());
		LocationList locationlist=new LocationList();
		locationlist.setBizLocation(new ArrayList<String>());
		trQueryResult.setLocationList(locationlist);
		//trQueryResult.setLocationList(new ArrayList<String>());
		BackendDb backendDb=new BackendDb();
		ResultSet results =backendDb.selectFromLocationByObject(session,epcId);
		for (Row row : results) {
			trQueryResult.getLocationList().getBizLocation().add(row.getString("biz_location"));
		}
		return trQueryResult;
	}
	TrQueryResult getQueryThreeResult(Session session,String epcId,Timestamp startTime){
		TrQueryResult trQueryResult=new TrQueryResult();
		//List<TrackObject> TrackObjectList=
		//trQueryResult.setTrackObjects(new ArrayList<TrackObject>());
		LocationList locationlist=new LocationList();
		locationlist.setBizLocation(new ArrayList<String>());
		trQueryResult.setLocationList(locationlist);
		//trQueryResult.setLocationList(new ArrayList<String>());
		BackendDb backendDb=new BackendDb();
		ResultSet results =backendDb.selectFromEventTimeByObjects(session, epcId, startTime);
		for (Row row : results) {
			trQueryResult.getLocationList().getBizLocation().add(row.getString("biz_location"));
		}
		return trQueryResult;
	}
	TrQueryResult getQueryFoureResult(Session session,String epcId,Timestamp startTime,Timestamp endTime ){
		TrQueryResult trQueryResult=new TrQueryResult();
		//List<TrackObject> TrackObjectList=
		//trQueryResult.setTrackObjects(new ArrayList<TrackObject>());
		LocationList locationlist=new LocationList();
		locationlist.setBizLocation(new ArrayList<String>());
		trQueryResult.setLocationList(locationlist);
		//trQueryResult.setLocationList(new ArrayList<String>());
		BackendDb backendDb=new BackendDb();
		ResultSet results =backendDb.selectFromEventTimeByObjects(session, epcId, startTime,endTime);
		for (Row row : results) {
			trQueryResult.getLocationList().getBizLocation().add(row.getString("biz_location"));
		}
		return trQueryResult;
	}
	
	@RequestMapping(value = "/TransformedFrom", method = RequestMethod.GET)
	@ResponseBody
	public String getTransformationFrom(
			@RequestParam(required = false) String epc_id){
		TrQueryResult trQueryResult=new TrQueryResult();
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("tracktrace");
		trQueryResult=getQueryTransformedFromToResult(session,epc_id);
		
		
		session.close();
		cluster.close();
		
		StringWriter sw=new StringWriter();
		JAXB.marshal(trQueryResult, sw);
		return sw.toString();
	}
	
	//TransformedFromToResult
	TrQueryResult getQueryTransformedFromToResult(Session session,String epcIdFrom){
		TrQueryResult trQueryResult=new TrQueryResult();
		trQueryResult.setTransformedObjects(new ArrayList<TransformedObject>());
		
		BackendDb backendDb=new BackendDb();
		ResultSet results =backendDb.selectFromTransformedFromTo(session,epcIdFrom);
		
		for (Row row : results) {
			TransformedObject transformedObject=new TransformedObject();
			transformedObject.setEpc_id_From(row.getString("epc_id_from"));
			transformedObject.setEpc_id_To(row.getString("epc_id_to"));
			transformedObject.setEvent_time(row.getTimestamp("event_time"));
			transformedObject.setBiz_location(row.getString("biz_location"));
			transformedObject.setTransformationId(row.getString("transformation_id"));
			transformedObject.setDespositionId(row.getString("despostion"));
			trQueryResult.getTransformedObjects().add(transformedObject);
			
		}
		return trQueryResult;
	}
	
	
	@RequestMapping(value = "/TransformedTo", method = RequestMethod.GET)
	@ResponseBody
	public String getTransformationTo(
			@RequestParam(required = false) String epc_id){
		TrQueryResult trQueryResult=new TrQueryResult();
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("tracktrace");
		trQueryResult=getQueryTransformedToFromResult(session,epc_id);
		
		
		session.close();
		cluster.close();
		
		StringWriter sw=new StringWriter();
		JAXB.marshal(trQueryResult, sw);
		return sw.toString();
	}
	
	//TransformedFromToResult

		TrQueryResult getQueryTransformedToFromResult(Session session,String epcIdTo){
			TrQueryResult trQueryResult=new TrQueryResult();
			trQueryResult.setTransformedObjects(new ArrayList<TransformedObject>());
			
			BackendDb backendDb=new BackendDb();
			ResultSet results =backendDb.selectFromTransformedToFrom(session,epcIdTo);
			
			for (Row row : results) {
				TransformedObject transformedObject=new TransformedObject();
				transformedObject.setEpc_id_From(row.getString("epc_id_from"));
				transformedObject.setEpc_id_To(row.getString("epc_id_to"));
				transformedObject.setEvent_time(row.getTimestamp("event_time"));
				transformedObject.setBiz_location(row.getString("biz_location"));
				transformedObject.setTransformationId(row.getString("transformation_id"));
				transformedObject.setDespositionId(row.getString("despostion"));
				trQueryResult.getTransformedObjects().add(transformedObject);
				
			}
			return trQueryResult;
		}
	
	
}
