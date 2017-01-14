package org.oliot.model.tracTrace;

import org.oliot.model.epcis.AggregationEventExtensionType;
import org.oliot.model.epcis.ObjectEventType;
import org.oliot.model.epcis.TransactionEventType;
import org.oliot.model.epcis.TransformationEventType;

public class EPCISEventModel {
	
	String  eventType;
	AggregationEventExtensionType ggregationEventExtensionType;
	ObjectEventType objectEventType;
	TransformationEventType transformationEventType;
	TransactionEventType transactionEventType;
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public AggregationEventExtensionType getGgregationEventExtensionType() {
		return ggregationEventExtensionType;
	}
	public void setGgregationEventExtensionType(AggregationEventExtensionType ggregationEventExtensionType) {
		this.ggregationEventExtensionType = ggregationEventExtensionType;
	}
	public ObjectEventType getObjectEventType() {
		return objectEventType;
	}
	public void setObjectEventType(ObjectEventType objectEventType) {
		this.objectEventType = objectEventType;
	}
	public TransformationEventType getTransformationEventType() {
		return transformationEventType;
	}
	public void setTransformationEventType(TransformationEventType transformationEventType) {
		this.transformationEventType = transformationEventType;
	}
	public TransactionEventType getTransactionEventType() {
		return transactionEventType;
	}
	public void setTransactionEventType(TransactionEventType transactionEventType) {
		this.transactionEventType = transactionEventType;
	}
	
	
	

}
