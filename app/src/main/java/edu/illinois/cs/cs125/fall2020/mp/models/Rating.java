package edu.illinois.cs.cs125.fall2020.mp.models;


/**
 * Rating class for storing client ratings of courses.
 */
public class Rating {
    /**
     * Rating indicating that the course has not been rated yet.
     */
  public static final double NOT_RATED = -1.0;
  private String id;
  private double rating;

  /**
   * Empty constructor.
   * Need to you serialized and desrialized
   */
  public Rating() {}

    /**
     * Create Rating constructor which has two parameter of setId and setRating.
     * @param setId the Id for this client
     * @param setRating the rating for this client about the courses
     */
  public Rating(final String setId, final double setRating) {
    id = setId;
    rating = setRating;
  }

    /**
     * Getter of client ID.
     * @return String client ID
     */
  public final String getId() {
    return id;
  }

    /**
     * Getter of client rating of courses.
     * @return double client rating
     */
  public final double getRating() {
    return rating;
  }


}
