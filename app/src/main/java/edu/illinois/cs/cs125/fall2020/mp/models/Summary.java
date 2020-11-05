package edu.illinois.cs.cs125.fall2020.mp.models;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Model holding the course summary information shown in the course list.
 *
 * <p>You will need to complete this model for MP0.
 */
public class Summary implements SortedListAdapter.ViewModel {
  private String year;

  /**
   * Get the year for this Summary.
   *
   * @return the year for this Summary
   */
  public final String getYear() {
    return year;
  }

  private String semester;

  /**
   * Get the semester for this Summary.
   *
   * @return the semester for this Summary
   */
  public final String getSemester() {
    return semester;
  }

  private String department;

  /**
   * Get the department for this Summary.
   *
   * @return the department for this Summary
   */
  public final String getDepartment() {
    return department;
  }

  private String number;

  /**
   * Get the number for this Summary.
   *
   * @return the number for this Summary
   */
  public final String getNumber() {
    return number;
  }

  private String title;

  /**
   * Get the title for this Summary.
   *
   * @return the title for this Summary
   */
  public final String getTitle() {
    return title;
  }

  /**
   * Get the department + number + title.
   * @return the entire thing
   */
  public final String getEntire() {
    return  department + " " + number + ": " + title;
  }

  /**
   * Get the URL request path for this Summary.
   * @return the URL request path for this Summary
   */
  public final String getPath() {
    return year + "/" + semester + "/" + department + "/" + number;
  }

  /**
   * Create an empty Summary.
   */
  @SuppressWarnings({"unused", "RedundantSuppression"})
  public Summary() {}

  /**
   * Create a Summary with the provided fields.
   *
   * @param setYear       the year for this Summary
   * @param setSemester   the semester for this Summary
   * @param setDepartment the department for this Summary
   * @param setNumber     the number for this Summary
   * @param setTitle      the title for this Summary
   */
  public Summary(
      final String setYear,
      final String setSemester,
      final String setDepartment,
      final String setNumber,
      final String setTitle) {
    year = setYear;
    semester = setSemester;
    department = setDepartment;
    number = setNumber;
    title = setTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Summary)) {
      return false;
    }
    Summary course = (Summary) o;
    return Objects.equals(year, course.year)
        && Objects.equals(semester, course.semester)
        && Objects.equals(department, course.department)
        && Objects.equals(number, course.number);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(year, semester, department, number);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> boolean isSameModelAs(@NonNull final T model) {
    return equals(model);
  }

  /**
   * {@inheritDoc}
   * @param model
   * @return boolean
   */
  @Override
  public <T> boolean isContentTheSameAs(@NonNull final T model) {
    return equals(model);
  }

  /**
   * comparator courseModel.
   * @param courseModel1
   * @param courseModel2
   * @return Comparator<Summary>
   */
  public static final Comparator<Summary> COMPARATOR = (courseModel1, courseModel2) -> {
    return (courseModel1.getEntire().compareTo(courseModel2.getEntire()));
  };

  /**
   * filter the result.
   * @param courses
   * @param text
   * @return List<Summary>
   */
  public static List<Summary> filter(
      @NonNull final List<Summary> courses, @NonNull final String text) {
    //should filter the passed list of courses to only include those that
    //contain the passed String, ignoring case
    assert text != null : "text should not be null";
    assert courses != null : "courses should contain courses";
    List<Summary> result = new ArrayList<>();
    for (Summary course : courses) {
      if (course.getEntire().toUpperCase().contains(text.toUpperCase())) {
        result.add(course);
      }
    }
    return result;
  }
}
