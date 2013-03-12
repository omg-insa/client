package fr.insalyon.pyp.entities;

public class EventEnitity {
	public String id;
	public double lon;
	public double lat;
	public Boolean isCheckedIn;
	
	public Boolean getIsCheckedIn() {
		return isCheckedIn;
	}
	public void setIsCheckedIn(Boolean isCheckedIn) {
		this.isCheckedIn = isCheckedIn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public EventEnitity(String id, double lon, double lat) {
		this.id = id;
		this.lon = lon;
		this.lat = lat;
		isCheckedIn = false;
	}
	
	
}
