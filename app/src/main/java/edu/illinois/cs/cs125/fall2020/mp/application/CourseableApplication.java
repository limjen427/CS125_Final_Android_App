package edu.illinois.cs.cs125.fall2020.mp.application;

import android.app.Application;
import android.content.Intent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import edu.illinois.cs.cs125.fall2020.mp.activities.CourseActivity;
import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.network.Client;
import edu.illinois.cs.cs125.fall2020.mp.network.Server;

/**
 * Application class for the Courseable app.
 *
 * <p>Starts the development server and creates the course API client.
 *
 * <p>You should not need to modify this file.
 */
public class CourseableApplication extends Application {
  /** Course API server port. */
  public static final int SERVER_PORT = 8888;
  /** Course API server URL. */
  public static final String SERVER_URL = "http://localhost:" + SERVER_PORT + "/";

  // Course API client created during application startup
  private Client client;

  private String clientId = UUID.randomUUID().toString();


  @Override
  public final void onCreate() {
    super.onCreate();
    client = Client.start();
    Server.start();
  }

  /**
   * Retrieve the course API client instance for this app.
   *
   * @return the course API client instance.
   */
  public final Client getCourseClient() {
    return client;
  }



//  private ObjectMapper mapper = new ObjectMapper();

//  /**
//   * Serialize UUID to Rating.
//   * @param id
//   */
//  public void onUUIDClicked(final UUID id) {
//    Intent startRating = new Intent(this, CourseActivity.class);
//    try {
//      String idString = mapper.writeValueAsString(id);
//      startRating.putExtra("UUID", idString);
//      startActivity(startRating);
//    } catch (JsonProcessingException e) {
//      e.printStackTrace();
//    }
//  }
}
