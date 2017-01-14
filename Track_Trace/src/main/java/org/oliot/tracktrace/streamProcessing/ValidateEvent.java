package org.oliot.tracktrace.streamProcessing;

import java.text.BreakIterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.oliot.epcis.configuration.Configuration;
import org.oliot.model.epcis.ActionType;
import org.oliot.model.epcis.AggregationEventType;
import org.oliot.model.epcis.EPCISDocumentType;
import org.oliot.model.epcis.EventListType;
import org.oliot.model.epcis.ObjectEventType;
import org.oliot.model.epcis.QuantityEventType;
import org.oliot.model.epcis.SensorEventType;
import org.oliot.model.epcis.TransactionEventType;
import org.oliot.model.epcis.TransformationEventType;
import org.oliot.model.epcis.VocabularyType;
import org.oliot.tdt.SimplePureIdentityFilter;
import org.oliot.tracktrace.backend.BackCaptureEnd;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ValidateEvent extends BaseBasicBolt {

	  //Execute is called to process tuples
	  @Override
	  public void execute(Tuple tuple, BasicOutputCollector collector) {
	    //Get the sentence content from the tuple
		System.out.println("Tuple is recived on validate event");
		EPCISDocumentType epcisDocument=(EPCISDocumentType) tuple.getValue(0);
		
		//	    String sentence = tuple.getString(0);
//	    //An iterator to get each word
//	    BreakIterator boundary=BreakIterator.getWordInstance();
//	    //Give the iterator the sentence
//	    boundary.setText(sentence);
//	    //Find the beginning first word
//	    int start=boundary.first();
//	    //Iterate over each word and emit it to the output stream
//	    for (int end=boundary.next(); end != BreakIterator.DONE; start=end, end=boundary.next()) {
//	      //get the word
//	      String word=sentence.substring(start,end);
//	      //If a word is whitespace characters, replace it with empty
//	      word=word.replaceAll("\\s+","");
//	      //if it's an actual word, emit it
//	      if (!word.equals("")) {
//	        collector.emit(new Values(word));
//	      }
//	    }
//	    
		
	    if (epcisDocument.getEPCISBody() == null) {
	    	System.out.println(" There is no DocumentBody ");
			//Configuration.logger.info(" There is no DocumentBody ");
			return;
		}
		if (epcisDocument.getEPCISBody().getEventList() == null) {
			System.out.println(" There is no EventList ");
			//Configuration.logger.info(" There is no EventList ");
			return;
		}
		System.out.println("Validate event get body");
		EventListType eventListType = epcisDocument.getEPCISBody()
				.getEventList();
		List<Object> eventList = eventListType
				.getObjectEventOrAggregationEventOrQuantityEvent();

		try{

		for (int i = 0; i < eventList.size(); i++) {
			JAXBElement eventElement = (JAXBElement) eventList.get(i);
			Object event = eventElement.getValue();
			if (event instanceof ObjectEventType) {
				collector.emit(new Values(capture((ObjectEventType) event)));
				System.out.println("After Validate event ObjectEventType emited");
			} else if (event instanceof AggregationEventType) {
				System.out.println("before Validate event AggregationEventType emited");
				collector.emit(new Values(capture((AggregationEventType) event)));
				System.out.println("After Validate event AggregationEventType emited");
			} else if (event instanceof TransactionEventType) {
				collector.emit(new Values(capture((TransactionEventType) event)));
				System.out.println("Validate event TransactionEventType emited");
			} else if (event instanceof TransformationEventType) {
				collector.emit(new Values(capture((TransformationEventType) event)));
				System.out.println("Validate event TransformationEventType emited");
			} else if (event instanceof QuantityEventType) {
				collector.emit(new Values(capture((QuantityEventType) event)));
				System.out.println("Validate event QuantityEventType emited");
			} else if (event instanceof SensorEventType) {
				collector.emit(new Values(capture((SensorEventType) event)));
				System.out.println("Validate event SensorEventType emited");
			}
		}
		}catch(Exception e){
			
			System.out.println(e);
			
		}
		
	    
	  }

	  //Declare that emitted tuples will contain a word field
	  @Override
	  public void declareOutputFields(OutputFieldsDeclarer declarer) {
	    declarer.declare(new Fields("Event"));
	  }
	  
	  
		public AggregationEventType capture(AggregationEventType event)throws Exception {

			// General Exception Handling
			// M7
			String timeZone = event.getEventTimeZoneOffset();
			if (!isCorrectTimeZone(timeZone)) {
				System.out.println("Req. M7 Error");
				//Configuration.logger.error("Req. M7 Error");
				throw new Exception();
			}


			// Mandatory Field: Action
			if (event.getAction() == null) {
				System.out.println("Aggregation Event should have 'Action' field ");
				//Configuration.logger.error("Aggregation Event should have 'Action' field ");
				throw new Exception();
			}
			// M13
			if (event.getAction() == ActionType.ADD
					|| event.getAction() == ActionType.DELETE) {
				if (event.getParentID() == null) {
					System.out.println("Req. M13 Error");
					//Configuration.logger.error("Req. M13 Error");
					throw new Exception();
				}
			}
			// M10
			String parentID = event.getParentID();
			if (parentID != null) {

				if (SimplePureIdentityFilter.isPureIdentity(parentID) == false) {
					System.out.println("Req. M10 Error");
					//Configuration.logger.error("Req. M10 Error");
					throw new Exception();
				}
			}
			
//			BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
//			backCaptureEnd.sotore(event);
			System.out.println("AggregationEventType Validated");
			//Configuration.logger.info("AggregationEventType stored");
			return event;
		}

		public ObjectEventType capture(ObjectEventType event)throws Exception {

			// General Exception Handling
			// M7
			String timeZone = event.getEventTimeZoneOffset();
			if (!isCorrectTimeZone(timeZone)) {
				System.out.println("Req. M7 Error");
				//Configuration.logger.error("Req. M7 Error");
				throw new Exception();
			}
			BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
			//backCaptureEnd.sotore(event);
			System.out.println("ObjectEventType Validated");
			//Configuration.logger.info("ObjectEventType stored");
			
			return event;
			
		}

		public QuantityEventType capture(QuantityEventType event)throws Exception {

			// General Exception Handling
			// M7
			String timeZone = event.getEventTimeZoneOffset();
			if (!isCorrectTimeZone(timeZone)) {
				//Configuration.logger.error("Req. M7 Error");
				throw new Exception();
			}
			//BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
			//backCaptureEnd.sotore(event);
			//Configuration.logger.info("QuantityEventType stored");
			return event;
		}

		public TransactionEventType capture(TransactionEventType event) throws Exception{

			// General Exception Handling
			// M7
			String timeZone = event.getEventTimeZoneOffset();
			if (!isCorrectTimeZone(timeZone)) {
				System.out.println("Req. M7 Error");
				//Configuration.logger.error("Req. M7 Error");
				throw new Exception();
			}

			// M14
			String parentID = event.getParentID();
			if (parentID != null) {

				if (SimplePureIdentityFilter.isPureIdentity(parentID) == false) {
					System.out.println("Req. M14 Error");
					//Configuration.logger.error("Req. M14 Error");
					throw new Exception();
				}
			}
			//BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
			//backCaptureEnd.sotore(event);
			System.out.println("TransactionEventType Validated");
			//Configuration.logger.info("TransactionEventType stored");
			return event;
			
		}

		public TransformationEventType capture(TransformationEventType event) throws Exception{

			// General Exception Handling
			// M7
			String timeZone = event.getEventTimeZoneOffset();
			if (!isCorrectTimeZone(timeZone)) {
				System.out.println("Req. M7 Error");
				//Configuration.logger.error("Req. M7 Error");
				throw new Exception();
			}
			//BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
			//backCaptureEnd.sotore(event);
			System.out.println("TransformationEventType Validated");
			//Configuration.logger.info("TransformationEventType stored");
			return event;
			
		}

		public SensorEventType capture(SensorEventType event) throws Exception{

			// General Exception Handling
			// M7
			String timeZone = event.getEventTimeZoneOffset();
			if (!isCorrectTimeZone(timeZone)) {
				System.out.println("Req. M7 Error");
				//Configuration.logger.error("Req. M7 Error");
				throw new Exception();
			}
			//BackCaptureEnd backCaptureEnd=new BackCaptureEnd();
			//backCaptureEnd.sotore(event);
			Configuration.logger.info("SensorEventType stored");
			
			return event;
		}

		public void capture(VocabularyType vocabulary) {
			System.out.println("VocabularyType");
			
		}
		
		public boolean isCorrectTimeZone(String timeZone) {

			boolean isMatch = timeZone
					.matches("^(?:Z|[+-](?:2[0-3]|[01][0-9]):[0-5][0-9])$");

			return isMatch;
		}
}
