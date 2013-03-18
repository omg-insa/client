package eu.chitic.syp.entities;

public class EventEnitity {
	private String id;
	private double lon;
	private double lat;
	private String name;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private Boolean isCheckedIn;

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

	public EventEnitity(String id, double lon, double lat, String name,String description) {
		this.id = id;
		this.lon = lon;
		this.lat = lat;
		this.name = name;
		isCheckedIn = false;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
