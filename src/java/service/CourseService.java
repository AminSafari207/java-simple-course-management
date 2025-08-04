package service;

import exception.CourseAlreadyExistsException;
import exception.CourseNotFoundException;
import model.Course;
import repository.CourseRepository;
import utils.ValidationUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CourseService {
    private final CourseRepository courseRepository;

    private final List<String> validUpdateKeys = List.of("title", "department", "credits");

    public CourseService() {
        courseRepository = new CourseRepository();
    }

    public void registerCourses(List<Course> courseList) {
        ValidationUtils.validateCollection(courseList, "courseList");

        for (Course course: courseList) {
            ValidationUtils.validateNotNull(course, "course");
            if (course.isIdSet()) throw new CourseAlreadyExistsException(course.getTitle(), course.getId());
        }

        try {
            courseRepository.create(courseList);
        } catch (SQLException e) {
            throw new RuntimeException("Registering courses failed.", e);
        }
    }

    public void updateCourse(int courseId, Map<String, Object> updateMap) {
        ValidationUtils.validateId(courseId);
        ValidationUtils.validateMap(updateMap, "updateMap");

        for (String key: updateMap.keySet()) {
            if (!validUpdateKeys.contains(key)) throw new IllegalArgumentException("Key '" + key + "' is not valid.");
        }

        try {
            courseRepository.update(courseId, updateMap);
        } catch (SQLException e) {
            throw new RuntimeException("Updating course failed.", e);
        }
    }

    public void removeCourse(int courseId) {
        ValidationUtils.validateId(courseId);

        try {
            courseRepository.delete(courseId);
        } catch (SQLException e) {
            throw new RuntimeException("Removing course failed.", e);
        }
    }

    public Course findCourseById(int courseId) {
        ValidationUtils.validateId(courseId);

        try {
            return courseRepository.findById(courseId);
        } catch (SQLException e) {
            throw new RuntimeException("Finding course by id failed.", e);
        }
    }

    public List<Course> findAllCourses() {
        try {
            return courseRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Finding all courses failed.", e);
        }
    }
}
