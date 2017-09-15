package hotelapp;

/** The class that represents the address of a hotel in USA. Stores the following data about the address:
 * city, state, street address, latitude and longitude. 
 */
public class Address {
	private String streetAddress;
	private String city;
	private String state;
	private double latitude;
	private double longitude;

	/** 
	 * Constructor that takes city, state, streetAddress, latitude and longitude
	 */
	public Address(String city, String state, String streetAddress, double lat, double lon) {
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.latitude = lat;
		this.longitude = lon;
	}

	public String getStreetAddress() {
		return this.streetAddress;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}

    /** Return the string representing the address in the following format:
	 * street address on the first line,
	 * city, state on the second line. Example:
	 17 Green st.
	 San Francisco, CA
	 * @return string representing the address of the hotel
	 */
	public String toString() {
		return this.getStreetAddress() + System.lineSeparator() + this.getCity() + ", " + getState();
	}
}

