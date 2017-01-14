package org.oliot.model.tracTrace;

import java.util.Date;

public class TrackObject {

	String epc_id;
	String biz_location;
	Date event_time;
	String biz_step;
	public String getEpc_id() {
		return epc_id;
	}
	public void setEpc_id(String epc_id) {
		this.epc_id = epc_id;
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
	public String getBiz_step() {
		return biz_step;
	}
	public void setBiz_step(String biz_step) {
		this.biz_step = biz_step;
	}
	
	
	
}
