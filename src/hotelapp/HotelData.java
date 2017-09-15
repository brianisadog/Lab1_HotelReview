package hotelapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Class HotelData - a data structure that stores information about hotels and
 * hotel reviews. Allows to quickly lookup a Hotel given the hotel id. (use TreeMap).
 * Allows to efficiently find hotel reviews for a given hotelID (use a TreeMap,
 * where for each hotelId, the value is a TreeSet). Reviews for a
 * given hotel id are sorted by date from most recent to oldest;
 * if the dates are the same, the reviews are sorted by user nickname,
 * and the user nicknames are the same, by the reviewId.
 */
public class HotelData {

	private Map<String, Hotel> hotelMap;
	private TreeSet<Review> reviewSet;
	private Map<String, TreeSet> reviewMap;

	/**
	 * Default constructor.
	 */
	public HotelData() {
		hotelMap = new TreeMap<>();
		reviewSet = new TreeSet<>();
		reviewMap = new TreeMap<>();
	}

	/**
	 * Create a Hotel given the parameters, and add it to the appropriate data
	 * structure.
	 * 
	 * @param hotelId
	 *            - the id of the hotel
	 * @param hotelName
	 *            - the name of the hotel
	 * @param city
	 *            - the city where the hotel is located
	 * @param state
	 *            - the state where the hotel is located.
	 * @param streetAddress
	 *            - the building number and the street
	 * @param lat latitude
	 * @param lon longitude
	 */
	public void addHotel(String hotelId, String hotelName, String city, String state, String streetAddress, double lat, double lon) {
		Address address = new Address(city, state, streetAddress, lat, lon);
		Hotel newHotel = new Hotel(hotelId, hotelName, address);
		hotelMap.put(hotelId, newHotel);
	}

	/**
	 * Add a new hotel review. Add it to the map (to the TreeSet of reviews for a given key=hotelId).
	 * 
	 * @param hotelId
	 *            - the id of the hotel reviewed
	 * @param reviewId
	 *            - the id of the review
	 * @param rating
	 *            - integer rating 1-5.
	 * @param reviewTitle
	 *            - the title of the review
	 * @param review
	 *            - text of the review
	 * @param isRecom
	 *            - whether the user recommends it or not
	 * @param date
	 *            - date of the review in the format yyyy-MM-ddThh:mm:ss, e.g. "2016-06-29T17:50:37"
	 * @param username
	 *            - the nickname of the user writing the review.
	 * @return true if successful, false if unsuccessful because of invalid hotelId, invalid date
	 *         or rating. Needs to catch and handle the following exceptions:
	 *         ParseException if the date is invalid
	 *         InvalidRatingException if the rating is out of range.
	 */
	public boolean addReview(String hotelId, String reviewId, int rating, String reviewTitle, String review,
			boolean isRecom, String date, String username) {

		if (!hotelMap.containsKey(hotelId)) {
			System.out.println("Invalid hotelId");
			return false;
		}
		else {
			try {
				Review newReview = new Review(hotelId, reviewId, rating, reviewTitle, review, isRecom, date, username);
				reviewSet.add(newReview);
				reviewMap.put(hotelId, reviewSet);
				return true;
			} catch (java.text.ParseException e) {
				e.printStackTrace();
				return false;
			} catch (InvalidRatingException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Returns a string representing information about the hotel with the given
	 * id, including all the reviews for this hotel separated by --------------------
	 * Format of the string:
	 * HotelName: hotelId
	 * streetAddress
	 * city, state
	 * -------------------- 
	 * Review by username on date
	 * Rating: rating
	 * ReviewTitle
	 * ReviewText
	 * -------------------- 
	 * Review by username on date
	 * Rating: rating
	 * ReviewTitle
	 * ReviewText ...
	 * 
	 * @param hotelId
	 * @return - output string.
	 */
	public String toString(String hotelId) {
		StringBuilder sb = new StringBuilder();

		if (hotelMap.get(hotelId) != null) {
			sb.append(hotelMap.get(hotelId).toString());

			if (reviewMap.get(hotelId) != null) {
				Set<Review> reviews = reviewMap.get(hotelId);
				for (Review perReview : reviews) {
					sb.append("--------------------");
					sb.append(System.lineSeparator());
					sb.append(perReview.toString());
				}
			}
		}

		return sb.toString();
	}


	/**
	 * Return a list of hotel ids, in alphabetical order of hotelIds
	 * 
	 * @return
	 */
	public List<String> getHotels() {
		List<String> hotelIdList = new ArrayList<>();
		Set<String> hotelIdKeys = hotelMap.keySet();

		for (String key: hotelIdKeys) {
			hotelIdList.add(key);
		}

		return hotelIdList;
	}

	/**
	 * Return the average rating for the given hotelId.
	 * 
	 * @param hotelId-
	 *            the id of the hotel
	 * @return average rating or 0 if no ratings for the hotel
	 */
	public double getRating(String hotelId) {
		double rating = 0; //return 0 when there is no key hotelId

		if (reviewMap.get(hotelId) != null) {
			int sum = 0;
			Set<Review> reviews = reviewMap.get(hotelId);

			for (Review perReview : reviews) {
				sum += perReview.getRating();
			}

			rating = (double)sum / (double)reviews.size();
		}

		return rating;
	}


	/**
	 * Read the given json file with information about the hotels (check hotels.json to see the expected format)
	 * and load it into the appropriate data structure(s).
	 */
	/*
	Key	->	Value

	sr	->	JSONArray
	id	->	hotelId
	f	->	hotelName
	ci	->	city
	pr	->	state
	ad	->	streetAddress
	ll	->	{lat, lon}
	lat	->	lat
	lng	->	lon
	*/
	public void loadHotelInfo(String jsonFilename) {
		JSONParser parser = new JSONParser();

		try {
			JSONObject obj = (JSONObject)parser.parse(new FileReader(jsonFilename));
			JSONArray arr = (JSONArray)obj.get("sr");
			for (JSONObject res : (Iterable<JSONObject>) arr) {
				JSONObject ll = (JSONObject) res.get("ll"); //ll is another json object

				addHotel(res.get("id").toString(), res.get("f").toString()
						, res.get("ci").toString(), res.get("pr").toString(), res.get("ad").toString()
						, Double.parseDouble(ll.get("lat").toString()), Double.parseDouble(ll.get("lng").toString()));
			}
		}
		catch  (FileNotFoundException e) {
			System.out.println("Could not find file: " + jsonFilename);
		}
		catch (ParseException e) {
			System.out.println("Can not parse a given json file.");
		}
		catch (IOException e) {
			System.out.println("General IO Exception in readJSON");
		}
	}

	/**
	 * Find all review files in the given path (including in subfolders and subsubfolders etc),
	 * read them, parse them using JSONSimple library, and
	 * load review info to the TreeMap that contains a TreeSet of Review-s for each hotel id (you should
	 * have defined this instance variable above)
	 * @param path
	 */
	public void loadReviews(Path path) {

		try (DirectoryStream<Path> filesList = Files.newDirectoryStream(path)) {
			for (Path file: filesList) {
				if (file.toString().contains(".json")) {
					loadPerReview(file.toString()); //load .json file into Review TreeMap
				}
				else {
					loadReviews(file); //recursively find all .json files
				}
			}
		}
		catch (IOException e) {
			System.out.println("Could not print the contents of the following folder: " + path.toString());
		}

	}

	/**
	 * Save the string representation of the hotel data to the file specified by
	 * filename in the following format (see "expectedOutput" in the test folder):
	 * an empty line
	 * A line of 20 asterisks ********************
	 * on the next line information for each hotel, printed
	 * in the format described in the toString method of this class.
	 *
	 * The hotels in the file should be sorted by hotel ids
	 *
	 * @param filename
	 *            - Path specifying where to save the output.
	 */
	public void printToFile(Path filename) {
		if (hotelMap.size() > 0) {

			try (PrintWriter pw = new PrintWriter(filename.toString(), "UTF-8")) {
				StringBuilder sb = new StringBuilder();

				//get all the hotelId in hotelMap
				for (String key:getHotels()) {
					sb.append(System.lineSeparator());	//an empty line
					sb.append("********************");	//20 asterisks
					sb.append(System.lineSeparator());	//\n
					sb.append(toString(key));			//calling toString in this class
				}

				pw.println(sb.toString()); //write String into the file
				pw.flush();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * read the given .json file into the appropriate data structure(s).
	 * @param jsonFilename
	 */
	private void loadPerReview(String jsonFilename) {
		JSONParser parser = new JSONParser();

		try {
			JSONObject obj = (JSONObject)parser.parse(new FileReader(jsonFilename));

			//get to the review unit
			JSONObject reviewDetails = (JSONObject)obj.get("reviewDetails");
			JSONObject reviewCollection = (JSONObject)reviewDetails.get("reviewCollection");
			JSONArray reviewArray = (JSONArray)reviewCollection.get("review");

			//add each review into Review TreeMap
			for (JSONObject review : (Iterable<JSONObject>) reviewArray) {

				addReview(review.get("hotelId").toString(), review.get("reviewId").toString()				//String hotelId, String reviewId
						, Integer.parseInt(review.get("ratingOverall").toString())							//int rating
						, review.get("title").toString(), review.get("reviewText").toString()				//String reviewTitle, String review
						, (review.get("isRecommended").toString().toUpperCase().equals("NO") ? false : true)//boolean isRecom
						, review.get("reviewSubmissionTime").toString()										//String date
						, review.get("userNickname").toString());											//String username
			}

			reviewSet = new TreeSet<>(); //initialize the review set for each hotelId, one hotelId with one reviewSet
		}
		catch  (FileNotFoundException e) {
			System.out.println("Could not find file: " + jsonFilename);
		}
		catch (ParseException e) {
			System.out.println("Can not parse a given json file.");
		}
		catch (IOException e) {
			System.out.println("General IO Exception in readJSON");
		}
	}
}
