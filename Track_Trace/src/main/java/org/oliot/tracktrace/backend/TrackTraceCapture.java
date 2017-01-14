package org.oliot.tracktrace.backend;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.oliot.model.epcis.TransformationEventType;
import org.oliot.model.tracTrace.EPCISEventModel;

public class TrackTraceCapture {

	public void transform(String message)throws IOException, JAXBException, InterruptedException{
		
		JAXBContext jAXBContext = JAXBContext.newInstance(EPCISEventModel.class);
		Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();

		Source source = new StreamSource(new StringReader(message));
		EPCISEventModel ePCISEvent = JAXB.unmarshal(source, EPCISEventModel.class);
		
		if(ePCISEvent.getEventType().equals("TransformationEventType"))
		{
		System.out.println("TransformationEventType");
		TransformationEventType TransformationEvent=ePCISEvent.getTransformationEventType();
		System.out.println(TransformationEvent.getEventTime());
		}
		else{
			System.out.println("AggregationEvent");	
		}
	}
}
