import hotelapp.HotelData;
import org.junit.Assert;
import org.junit.Test;

public class myTest {

    @Test(timeout = TestUtils.TIMEOUT)
    public void testGetRating() {
        // Adds one hotel and three reviews for it. Makes sure the reviews are
        // sorted correctly (by date, and if the dates are equal, by username
        String testName = "testThreeReviewsSameHotel";
        HotelData hdata = new HotelData();
        hdata.addHotel("25622", "Hilton San Francisco Union Square", "San Francisco", "CA", "55 Cyril Magnin St", 37.78,
                -122.4);

        hdata.addReview("25622", "23d756a64672vr2gwegyhqw4", 5, "Great deal", "Loved the neighborhood, very lively",
                true, "2014-09-05T05:00:45", "Chris");
        hdata.addReview("25622", "92rlnlvnabuwbf256jsf20fj", 4, "Overpriced", "Good location, but very expensive", true,
                "2014-09-05T05:00:45", "Alicia");
        hdata.addReview("25622", "57b717a44751ca0b791823b2", 4, "Room too small",
                "Great location, but the room is too small", true, "2015-03-04T10:10:16", "Xiaofeng");

        StringBuilder sb = new StringBuilder();
        // most recent review first; if date is the same, should be sorted alphabetically
        sb.append("Hilton San Francisco Union Square: 25622" + System.lineSeparator() + "55 Cyril Magnin St" + System.lineSeparator() + "San Francisco, CA"+ System.lineSeparator());
        sb.append("--------------------" + System.lineSeparator() );

        sb.append("Review by Xiaofeng on Wed Mar 04 10:10:16 PST 2015" + System.lineSeparator() + "Rating: 4" + System.lineSeparator() + "Room too small" + System.lineSeparator() + "Great location, but the room is too small" + System.lineSeparator());
        sb.append("--------------------" + System.lineSeparator() );

        sb.append("Review by Alicia on Fri Sep 05 05:00:45 PDT 2014" + System.lineSeparator() + "Rating: 4" + System.lineSeparator() + "Overpriced" + System.lineSeparator() + "Good location, but very expensive" + System.lineSeparator());
        sb.append("--------------------" + System.lineSeparator());
        sb.append("Review by Chris on Fri Sep 05 05:00:45 PDT 2014" + System.lineSeparator() + "Rating: 5" + System.lineSeparator() + "Great deal"+ System.lineSeparator() + "Loved the neighborhood, very lively" + System.lineSeparator());
        String expected = sb.toString();

        Assert.assertEquals(String.format("%n" + "Test Case: %s%n", testName), expected.trim(), hdata.toString("25622").trim());
        System.out.println(hdata.getRating("25622"));
    }
}
