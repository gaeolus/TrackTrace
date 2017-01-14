package org.oliot.model.tracTrace;

import java.util.Date;

public class TransformedObject {
	String epc_id_To;
	String epc_id_From;
	String biz_location;
	Date event_time;
	String transformationId;
	String despositionId;
	public String getEpc_id_To() {
		return epc_id_To;
	}
	public void setEpc_id_To(String epc_id_To) {
		this.epc_id_To = epc_id_To;
	}
	public String getEpc_id_From() {
		return epc_id_From;
	}
	public void setEpc_id_From(String epc_id_From) {
		this.epc_id_From = epc_id_From;
	}
	public String getBiz_location() {
		return biz_location;
	}
	public void setBiz_location(String biz_location) {
		this.biz_location = biz_location;
	}
	public Date getEvent_time() {
		return event_time;
	}
	public void setEvent_time(Date event_time) {
		this.event_time = event_time;
	}
	public String getTransformationId() {
		return transformationId;
	}
	public void setTransformationId(String transformationId) {
		this.transformationId = transformationId;
	}
	public String getDespositionId() {
		return despositionId;
	}
	public void setDespositionId(String despositionId) {
		this.despositionId = despositionId;
	}
	
	
	

}
