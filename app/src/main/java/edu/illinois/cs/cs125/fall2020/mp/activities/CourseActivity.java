package edu.illinois.cs.cs125.fall2020.mp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.illinois.cs.cs125.fall2020.mp.R;
import edu.illinois.cs.cs125.fall2020.mp.models.Course;
import edu.illinois.cs.cs125.fall2020.mp.databinding.ActivityCourseBinding;


//import edu.illinois.cs.cs125.fall2020.mp.R;

/**
 * Course Activity class.
 */
public class CourseActivity extends AppCompatActivity {
  private static final String TAG = CourseActivity.class.getSimpleName();
  // Binding to the layout in activity_main.xml
  private ActivityCourseBinding binding;
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Call when this cativity is created.
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(@NonNull final Bundle savedInstanceState) {
    Log.i(TAG, "Course Activity Started");
    super.onCreate(savedInstanceState);
    // Bind to the layout in activity_main.xml
    binding = DataBindingUtil.setContentView(this, R.layout.activity_course);
    try {
      Intent intent = getIntent();
      String courseString = intent.getStringExtra("COURSE");
      Course myCourse = mapper.readValue(courseString, Course.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    //setContentView(R.layout.activity_course);
  }

}
