package edu.illinois.cs.cs125.fall2020.mp.models;

/**
 * Course class.
 */
public class Course extends Summary {
  /**
   * Create an empty Course constructor.
   */
  public Course() {}

  private String description;
  /**
   * Take the course description in this getter.
   * @return String Course Description
   */
  public final String getDescription() {
    return description;
  }
}
