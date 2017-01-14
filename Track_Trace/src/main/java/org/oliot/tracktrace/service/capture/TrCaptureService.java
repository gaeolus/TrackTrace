package org.oliot.tracktrace.service.capture;

import java.util.List;
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

import javax.xml.bind.JAXBElement;

import org.oliot.epcis.configuration.Configuration;
import org.oliot.model.epcis.ActionType;
import org.oliot.model.epcis.AggregationEventType;
import org.oliot.model.epcis.EPCISDocumentType;
import org.oliot.model.epcis.EPCISMasterDataDocumentType;
import org.oliot.model.epcis.EventListType;
import org.oliot.model.epcis.ObjectEventType;
import org.oliot.model.epcis.QuantityEventType;
import org.oliot.model.epcis.SensorEventType;
import org.oliot.model.epcis.TransactionEventType;
import org.oliot.model.epcis.TransformationEventType;
import org.oliot.model.epcis.VocabularyListType;
import org.oliot.model.epcis.VocabularyType;
import org.oliot.tdt.SimplePureIdentityFilter;
import org.oliot.tracktrace.backend.BackCaptureEnd;

public class TrCaptureService {
	
	public void capture(AggregationEventType event) {

		// General Exception Handling
		// M7
		String timeZone = event.getEventTimeZoneOffset();
		if (!isCorrectTimeZone(timeZone)) {
			Configuration.logger.error("Req. M7 Error");
			return;
		}


		// Mandatory Field: Action
		if (event.getAction() == null) {
			Configuration.logger
					.error("Aggregation Event should have 'Action' field ");
			return;
		}
		// M13
		if (event.getAction() == ActionType.ADD
				|| event.getAction() == ActionType.DELETE) {
			if (event.getParentID() == null) {
				Configuration.logger.error("Req. M13 Error");
				return;
			}
		}
		// M10
		String parentID = event.getParentID();
		if (parentID != null) {

			if (SimplePureIdentityFilter.isPureIdentity(parentID) == false) {
				Configuration.logger.error("Req. M10 Error");
				return;
			}
		}
		
		BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
		backCaptureEnd.sotore(event);

		Configuration.logger.info("AggregationEventType stored");
		/* 
		if (Configuration.backend.equals("MongoDB")) {
			
		}
		else if(Configuration.backend.equals("MySQL")){
			MysqlCaptureUtil m = new MysqlCaptureUtil();
			m.capture(event);
		}
		*/
	}

	public void capture(ObjectEventType event) {

		// General Exception Handling
		// M7
		String timeZone = event.getEventTimeZoneOffset();
		if (!isCorrectTimeZone(timeZone)) {
			Configuration.logger.error("Req. M7 Error");
			return;
		}
		BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
		backCaptureEnd.sotore(event);
		Configuration.logger.info("ObjectEventType stored");
		/* 
		if (Configuration.backend.equals("MongoDB")) {
			
		}else if(Configuration.backend.equals("MySQL")){
			MysqlCaptureUtil m = new MysqlCaptureUtil();
			m.capture(event);
		}
		*/
	}

	public void capture(QuantityEventType event) {

		// General Exception Handling
		// M7
		String timeZone = event.getEventTimeZoneOffset();
		if (!isCorrectTimeZone(timeZone)) {
			Configuration.logger.error("Req. M7 Error");
			return;
		}
		BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
		backCaptureEnd.sotore(event);
		Configuration.logger.info("QuantityEventType stored");
		/* 
		if (Configuration.backend.equals("MongoDB")) {
			
		}else if(Configuration.backend.equals("MySQL")){
			MysqlCaptureUtil m = new MysqlCaptureUtil();
			m.capture(event);
		}
		 */
	}

	public void capture(TransactionEventType event) {

		// General Exception Handling
		// M7
		String timeZone = event.getEventTimeZoneOffset();
		if (!isCorrectTimeZone(timeZone)) {
			Configuration.logger.error("Req. M7 Error");
			return;
		}

		// M14
		String parentID = event.getParentID();
		if (parentID != null) {

			if (SimplePureIdentityFilter.isPureIdentity(parentID) == false) {
				Configuration.logger.error("Req. M14 Error");
				return;
			}
		}
		BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
		backCaptureEnd.sotore(event);
		Configuration.logger.info("TransactionEventType stored");
		/* 
		if (Configuration.backend.equals("MongoDB")) {
			
		}	
		else if(Configuration.backend.equals("MySQL")){
			MysqlCaptureUtil m = new MysqlCaptureUtil();
			m.capture(event);
		}
		 */
	}

	public void capture(TransformationEventType event) {

		// General Exception Handling
		// M7
		String timeZone = event.getEventTimeZoneOffset();
		if (!isCorrectTimeZone(timeZone)) {
			Configuration.logger.error("Req. M7 Error");
			return;
		}
		BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
		backCaptureEnd.sotore(event);
		Configuration.logger.info("TransformationEventType stored");
		/* 
		if (Configuration.backend.equals("MongoDB")) {
			
		}
		else if(Configuration.backend.equals("MySQL")){
			MysqlCaptureUtil m = new MysqlCaptureUtil();
			m.capture(event);
		}
		 */
	}

	public void capture(SensorEventType event) {

		// General Exception Handling
		// M7
		String timeZone = event.getEventTimeZoneOffset();
		if (!isCorrectTimeZone(timeZone)) {
			Configuration.logger.error("Req. M7 Error");
			return;
		}
		BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
		backCaptureEnd.sotore(event);
		Configuration.logger.info("SensorEventType stored");
		
		/*
		if (Configuration.backend.equals("MongoDB")) {
			
		}
		else if(Configuration.backend.equals("MySQL")){
			MysqlCaptureUtil m = new MysqlCaptureUtil();
			m.capture(event);
		}
		 */
	}

	public void capture(VocabularyType vocabulary) {
		System.out.println("VocabularyType");
		/* 
		if (Configuration.backend.equals("MongoDB")) {
			
		}
		else if(Configuration.backend.equals("MySQL")){
			MysqlCaptureUtil m = new MysqlCaptureUtil();
			m.capture(vocabulary);
		}
		 */
	}

	
	public void capture(EPCISDocumentType epcisDocument) {
		if (epcisDocument.getEPCISBody() == null) {
			Configuration.logger.info(" There is no DocumentBody ");
			return;
		}
		if (epcisDocument.getEPCISBody().getEventList() == null) {
			Configuration.logger.info(" There is no EventList ");
			return;
		}
		EventListType eventListType = epcisDocument.getEPCISBody()
				.getEventList();
		List<Object> eventList = eventListType
				.getObjectEventOrAggregationEventOrQuantityEvent();

		

		for (int i = 0; i < eventList.size(); i++) {
			JAXBElement eventElement = (JAXBElement) eventList.get(i);
			Object event = eventElement.getValue();
			if (event instanceof ObjectEventType) {
				capture((ObjectEventType) event);
			} else if (event instanceof AggregationEventType) {
				capture((AggregationEventType) event);
			} else if (event instanceof TransactionEventType) {
				capture((TransactionEventType) event);
			} else if (event instanceof TransformationEventType) {
				capture((TransformationEventType) event);
			} else if (event instanceof QuantityEventType) {
				capture((QuantityEventType) event);
			} else if (event instanceof SensorEventType) {
				capture((SensorEventType) event);
			}
		}
	}

	public boolean isCorrectTimeZone(String timeZone) {

		boolean isMatch = timeZone
				.matches("^(?:Z|[+-](?:2[0-3]|[01][0-9]):[0-5][0-9])$");

		return isMatch;
	}

	
	public void capture(EPCISMasterDataDocumentType epcisMasterDataDocument) {

		if (epcisMasterDataDocument.getEPCISBody() == null) {
			Configuration.logger.info(" There is no DocumentBody ");
			return;
		}

		if (epcisMasterDataDocument.getEPCISBody().getVocabularyList() == null) {
			Configuration.logger.info(" There is no Vocabulary List ");
			return;
		}

		VocabularyListType vocabularyListType = epcisMasterDataDocument
				.getEPCISBody().getVocabularyList();

		List<VocabularyType> vocabularyTypeList = vocabularyListType
				.getVocabulary();

		for (int i = 0; i < vocabularyTypeList.size(); i++) {
			VocabularyType vocabulary = vocabularyTypeList.get(i);
			capture(vocabulary);
		}
	}
}
