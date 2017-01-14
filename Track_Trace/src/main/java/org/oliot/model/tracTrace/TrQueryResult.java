package org.oliot.model.tracTrace;

import java.util.List;

public class TrQueryResult {
	
	List<TrackObject> trackObjects;
	ObjectList objectList;
	LocationList locationList;
	List<TransformedObject> transformedObjects;
	
	public List<TrackObject> getTrackObjects() {
		return trackObjects;
	}
	public void setTrackObjects(List<TrackObject> trackObjects) {
		this.trackObjects = trackObjects;
	}
	public ObjectList getObjectList() {
		return objectList;
	}
	public void setObjectList(ObjectList objectList) {
		this.objectList = objectList;
	}
	public LocationList getLocationList() {
		return locationList;
	}
	public void setLocationList(LocationList locationList) {
		this.locationList = locationList;
	}
	public List<TransformedObject> getTransformedObjects() {
		return transformedObjects;
	}
	public void setTransformedObjects(List<TransformedObject> transformedObjects) {
		this.transformedObjects = transformedObjects;
	}
	
	

}
