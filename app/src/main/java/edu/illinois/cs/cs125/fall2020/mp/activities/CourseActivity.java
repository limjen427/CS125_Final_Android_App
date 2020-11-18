package edu.illinois.cs.cs125.fall2020.mp.activities;

import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.illinois.cs.cs125.fall2020.mp.R;
import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
import edu.illinois.cs.cs125.fall2020.mp.models.Course;
import edu.illinois.cs.cs125.fall2020.mp.databinding.ActivityCourseBinding;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import edu.illinois.cs.cs125.fall2020.mp.network.Client;


/**
 * Course Activity class.
 */
public class CourseActivity extends AppCompatActivity implements Client.CourseClientCallbacks {
  private static final String TAG = CourseActivity.class.getSimpleName();
  // Binding to the layout in activity_main.xml
  private ActivityCourseBinding binding;
  private ObjectMapper mapper = new ObjectMapper();
  @NonNull private String courseTitle;
  @NonNull private String courseDescription;

  /**
   * Call when this cativity is created.
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(@NonNull final Bundle savedInstanceState) {
    //Log.i(TAG, "Course Activity Started");
    super.onCreate(savedInstanceState);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // Bind to the layout in activity_main.xml
    binding = DataBindingUtil.setContentView(this, R.layout.activity_course);
    Intent intent = getIntent();
    String courseString = intent.getStringExtra("COURSE");
    try {
      Summary course = mapper.readValue(courseString, Course.class);
      CourseableApplication application = (CourseableApplication) getApplication();
      application.getCourseClient().getCourse(course, this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }
  /**
   * Callback called when the client has retrieved the list of courses for this component to display.
   *
   * @param summary the summary that was retrieved
   * @param course the course that was retrieved
   */
  @Override
  public void courseResponse(final Summary summary, final Course course) {
    courseTitle = summary.getEntire();
    courseDescription = course.getDescription();
    binding.title.setText(courseTitle);
    binding.description.setText(courseDescription);
  }

}
