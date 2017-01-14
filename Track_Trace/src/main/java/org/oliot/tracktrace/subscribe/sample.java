package org.oliot.tracktrace.subscribe;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.oliot.model.epcis.TransformationEventType;
import org.oliot.model.tracTrace.AggregationEvent;
import org.oliot.model.tracTrace.EPCISEventModel;
import org.oliot.model.tracTrace.EventList;
import org.oliot.model.tracTrace.TransformationEvent;



public class sample {
	
	void transform ()throws IOException, JAXBException, InterruptedException{
	String TrEvent="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+ "<ns4:local xmlns:ns2=\"http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader\" "
			+ "xmlns:ns4=\"uri\" xmlns:ns3=\"axis.epcis.oliot.org\">"
			+ "    <eventType>TransformationEventType</eventType>"
			+ "    <transformationEventType>"
			+ "        <eventTime>2013-10-31T14:58:56.591Z</eventTime>"
			+ "        <eventTimeZoneOffset>+02:00</eventTimeZoneOffset>"
			+ "        <inputEPCList>"
			+ "            <epc>urn:epc:id:sgtin:4012345.011122.25</epc>"
			+ "            <epc>urn:epc:id:sgtin:4000001.065432.99886655</epc>"
			+ "        </inputEPCList>"
			+ "        <inputQuantityList>"
			+ "            <quantityElement>"
			+ "                <epcClass>urn:epc:class:lgtin:4012345.011111.4444</epcClass>"
			+ "                <quantity>10.0</quantity>"
			+ "                <uom>KGM</uom>"
			+ "            </quantityElement>"
			+ "            <quantityElement>"
			+ "                <epcClass>urn:epc:class:lgtin:0614141.077777.987</epcClass>"
			+ "                <quantity>30.0</quantity>"
			+ "            </quantityElement>"
			+ "            <quantityElement>"
			+ "                <epcClass>urn:epc:idpat:sgtin:4012345.066666.*</epcClass>"
			+ "                <quantity>220.0</quantity>"
			+ "            </quantityElement>"
			+ "        </inputQuantityList>"
			+ "        <outputEPCList>"
			+ "            <epc>urn:epc:id:sgtin:4012345.077889.25</epc>"
			+ "            <epc>urn:epc:id:sgtin:4012345.077889.26</epc>"
			+ "            <epc>urn:epc:id:sgtin:4012345.077889.27</epc>"
			+ "            <epc>urn:epc:id:sgtin:4012345.077889.28</epc>"
			+ "        </outputEPCList>"
			+ "        <transformationID>transformationID</transformationID>"
			+ "        <bizStep>urn:epcglobal:cbv:bizstep:transforming</bizStep>"
			+ "        <disposition>urn:epcglobal:cbv:disp:in_progress</disposition>"
			+ "        <readPoint>"
			+ "            <id>urn:epc:id:sgln:4012345.00001.0</id>"
			+ "        </readPoint>"
			+ "        <ilmd>"
			+ "            <extension>"
			+ "                <example:bestBeforeDate xmlns:example=\"http://ns.example.com/epcis\" xmlns:epcis=\"urn:epcglobal:epcis:xsd:1\">2014-12-10</example:bestBeforeDate>"
			+ "                <example:batch xmlns:example=\"http://ns.example.com/epcis\" "
			+ "xmlns:epcis=\"urn:epcglobal:epcis:xsd:1\">XYZ</example:batch>"
			+ "            </extension>"
			+ "        </ilmd>"
			+ "    </transformationEventType>"
			+ "</ns4:local>";
	
//	JAXBContext jc= JAXBContext.newInstance("org.oliot.model.tracTrace.TransformationEvent");
//	Unmarshaller um = jc.createUnmarshaller();
//	Source source = new StreamSource(new StringReader(TrEvent));
//	JAXBElement element= (JAXBElement) um.unmarshal(source);
//	
//	Object event = element.getValue();
//	save((TransformationEvent)event);
	//try{
	JAXBContext jAXBContext = JAXBContext.newInstance(EPCISEventModel.class);
	Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();

	Source source = new StreamSource(new StringReader(TrEvent));
	EPCISEventModel ePCISEvent = JAXB.unmarshal(source, EPCISEventModel.class);
	
	//JAXBElement element= (JAXBElement) object.getObjectEventOrAggregationEventOrQuantityEvent().get(0);
	//Object event = object.getValue();
	//save((TransformationEvent)event);
	if(ePCISEvent.getEventType().equals("TransformationEventType"))
	{
	System.out.println("TransformationEventType");
	TransformationEventType TransformationEvent=ePCISEvent.getTransformationEventType();
	System.out.println(TransformationEvent.getEventTime());
	}
	else{
		System.out.println("AggregationEvent");	
	}
	//System.out.println( object.getEventTime());
//	}catch (Exception e1){
//		try{
//			JAXBContext jAXBContext = JAXBContext.newInstance(TransformationEvent.class);
//			Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
//
//			Source source = new StreamSource(new StringReader(TrEvent));
//			TransformationEvent object = JAXB.unmarshal(source, TransformationEvent.class);
//			
//			//JAXBElement element= (JAXBElement) object;
//			//Object event = object.getValue();
//			//save((TransformationEvent)event);
//			System.out.println("TransformationEvent");
//			System.out.println( object.getEventTime());
//			
//		}catch (Exception e2){
//			
//		}
//	}

	}
	
	
	void save(TransformationEvent event){
		System.out.println("TransformationEvent");
		System.out.println(event.getEventTime());
	}
	
	void save(AggregationEvent event){
		System.out.println("AggregationEvent");
		
	}
}
