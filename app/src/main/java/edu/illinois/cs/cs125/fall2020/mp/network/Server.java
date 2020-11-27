package edu.illinois.cs.cs125.fall2020.mp.network;

import androidx.annotation.NonNull;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Development course API server.
 *
 * <p>Normally you would run this server on another machine, which the client would connect to over
 * the internet. For the sake of development, we're running the server right alongside the app on
 * the same device. However, all communication between the course API client and course API server
 * is still done using the HTTP protocol. Meaning that eventually it would be straightforward to
 * move this server to another machine where it could provide data for all course API clients.
 *
 * <p>You will need to add functionality to the server for MP1 and MP2.
 */
public final class Server extends Dispatcher {
  @SuppressWarnings({"unused", "RedundantSuppression"})
  private static final String TAG = Server.class.getSimpleName();

  private final Map<String, String> summaries = new HashMap<>();
  //summary/2020/fall

  private MockResponse getSummary(@NonNull final String path) {
    String[] parts = path.split("/");
    if (parts.length != 2) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    String summary = summaries.get(parts[0] + "_" + parts[1]);
    if (summary == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(summary);
  }

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private final Map<Summary, String> courses = new HashMap<>();
  private static final int FOUR = 4;
  //course/year/semester/department/number

  private MockResponse getCourse(@NonNull final String path) {
    //String path -> Summary
    String[] parts = path.split("/");
    if (parts.length != FOUR) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    Summary course1 = new Summary(parts[0], parts[1], parts[2], parts[3], "");

    String course = courses.getOrDefault(course1, null);
    if (course == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(course);
  }

  private boolean checkUUID(@NonNull final String uuid) {
    if (uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
      return true;
    }
    return false;
  }

  private final Map<Map<Summary, String>, Rating> ratings = new HashMap<>();

  private MockResponse getRating(@NonNull final RecordedRequest request) {
    String path = request.getPath().replaceFirst("/rating/", "");

    String[] uuidParts = path.split("\\?client=");
    if (uuidParts.length != 2) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    String[] urlParts = uuidParts[0].split("/");
    if (urlParts.length != FOUR) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    String ratingUuid = uuidParts[1];
    if (!checkUUID(ratingUuid)) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    Summary newCourse = new Summary(urlParts[0], urlParts[1], urlParts[2], urlParts[3], "");
    String course = courses.getOrDefault(newCourse, null);
    if (course == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }

    //Rating newRating = ratings.getOrDefault(courses, new Rating(ratingUuid, Rating.NOT_RATED));

//    if (!ratingUuid.equals(newRating.getId())) {
//      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
//    }

    String rating = "";
//    try {
//      rating = mapper.writeValueAsString(newRating);
//    } catch (JsonProcessingException e) {
//      e.printStackTrace();
//    }
    if (request.getMethod().equals("GET")) {
      //new Rating
      Rating newRating = ratings.getOrDefault(courses, new Rating(ratingUuid, Rating.NOT_RATED));
      //check newRating uuid .equals() request uuid
      if (!ratingUuid.equals(newRating.getId())) {
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
      }
      //deserialization
      try {
        rating = mapper.writeValueAsString(newRating);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(rating);
    } else if (request.getMethod().equals("POST")) {
      //get request info in string
      rating = request.getBody().readUtf8();
//      try {
//        Rating postRating = mapper.readValue(rating, Rating.class);
//        ratings.put(courses, postRating);
//      } catch (JsonProcessingException e) {
//        e.printStackTrace();
//      }
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_MOVED_TEMP).setHeader(
              "Location", "/rating/" + path
      );
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
  }

//  private String theString = "";
//  private MockResponse testPost(@NonNull final RecordedRequest request) {
//    if (request.getMethod().equals("GET")) {
//      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(theString);
//    } else if (request.getMethod().equals("POST")) {
//      theString = request.getBody().readUtf8();
//      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_MOVED_TEMP).setHeader(
//              "Location", "/test/"
//      );
//      //return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK);
//    }
//    System.out.println(request.getMethod());
//    System.out.println(request.getBody().readUtf8());
//    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
//  }


  @NonNull
  @Override
  public MockResponse dispatch(@NonNull final RecordedRequest request) {
    try {
      String path = request.getPath();
      if (path == null || request.getMethod() == null) {
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
      } else if (path.equals("/") && request.getMethod().equalsIgnoreCase("HEAD")) {
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK);
      } else if (path.startsWith("/summary/")) {
        return getSummary(path.replaceFirst("/summary/", ""));
      } else if (path.startsWith("/course/")) {
        return getCourse(path.replaceFirst("/course/", ""));
      } else if (path.startsWith("/rating/")) {
        return getRating(request);
        //return getRating(path.replaceFirst("rating", ""));
//      } else if (path.equals("/test/")) {
//        return testPost(request);
      }
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    } catch (Exception e) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }
  }

  private static boolean started = false;

  /**
   * Start the server if has not already been started.
   *
   * <p>We start the server in a new thread so that it operates separately from and does not
   * interfere with the rest of the app.
   */
  public static void start() {
    if (!started) {
      new Thread(Server::new).start();
      started = true;
    }
  }

  private final ObjectMapper mapper = new ObjectMapper();

  private Server() {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    loadSummary("2020", "fall");
    loadCourses("2020", "fall");

    try {
      MockWebServer server = new MockWebServer();
      server.setDispatcher(this);
      server.start(CourseableApplication.SERVER_PORT);

      String baseUrl = server.url("").toString();
      if (!CourseableApplication.SERVER_URL.equals(baseUrl)) {
        throw new IllegalStateException("Bad server URL: " + baseUrl);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @SuppressWarnings("SameParameterValue")
  private void loadSummary(@NonNull final String year, @NonNull final String semester) {
    String filename = "/" + year + "_" + semester + "_summary.json";
    String json =
        new Scanner(Server.class.getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
    summaries.put(year + "_" + semester, json);
  }

  @SuppressWarnings("SameParameterValue")
  private void loadCourses(@NonNull final String year, @NonNull final String semester) {
    String filename = "/" + year + "_" + semester + ".json";
    String json =
        new Scanner(Server.class.getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
    try {
      JsonNode nodes = mapper.readTree(json);
      for (Iterator<JsonNode> it = nodes.elements(); it.hasNext(); ) {
        JsonNode node = it.next();
        Summary course = mapper.readValue(node.toString(), Summary.class);
        courses.put(course, node.toPrettyString());
      }
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }
}
